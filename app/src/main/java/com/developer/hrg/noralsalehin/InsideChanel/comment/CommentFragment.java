package com.developer.hrg.noralsalehin.InsideChanel.comment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.developer.hrg.noralsalehin.Helps.ApiInterface;
import com.developer.hrg.noralsalehin.Helps.Apiclient;

import com.developer.hrg.noralsalehin.Helps.MySnack;
import com.developer.hrg.noralsalehin.Helps.SimpleResponse;
import com.developer.hrg.noralsalehin.Helps.UserData;
import com.developer.hrg.noralsalehin.InsideChanel.InsideActivity;
import com.developer.hrg.noralsalehin.Models.Comment;
import com.developer.hrg.noralsalehin.Models.User;
import com.developer.hrg.noralsalehin.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment  {
  public static final String MESSAGE_ID = "message_id";
    public static final String CHANEL_ID = "chanel_id";
    Integer message_id = 0 , chanel_id ;
    TextView tv_likes , tv_comments;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btn_send ;
    EditText et_text ;
    ArrayList<Comment> comments = new ArrayList<>();
    Comment_adapter adater_comment;
    User user ;
    UserData userData;
    TextView tv_label ;
    CoordinatorLayout coordinatorLayout;

    public static CommentFragment getInstance(int message_id , int chanel_id) {
     CommentFragment commentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MESSAGE_ID,message_id);
        bundle.putInt(CHANEL_ID,chanel_id);
        commentFragment.setArguments(bundle);
        return commentFragment;

    }

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments()!=null) {
            message_id=getArguments().getInt(MESSAGE_ID);
            chanel_id=getArguments().getInt(CHANEL_ID);
        }
        adater_comment=new Comment_adapter(getActivity(),comments);
        userData=new UserData(getActivity());
        user=userData.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_comment, container, false);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar_comment);
        tv_likes=(TextView)view.findViewById(R.id.tv_comment_lieks);
        tv_comments=(TextView)view.findViewById(R.id.tv_comment_comments);
        recyclerView=(RecyclerView)view.findViewById(R.id.rv_comments);
        btn_send=(Button)view.findViewById(R.id.btn_comment_send);
        et_text=(EditText)view.findViewById(R.id.et_comment_text);
        tv_label=(TextView)view.findViewById(R.id.tv_comment_lable);
        coordinatorLayout=(CoordinatorLayout)view.findViewById(R.id.coordinate_comment);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);





        ((InsideActivity)getActivity()).setSupportActionBar(toolbar);

        ((InsideActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((InsideActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adater_comment);
        ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
        Call<SimpleResponse> call_comments = api.getAllComments(user.getApikey(),message_id);
        call_comments.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (!response.isSuccessful()) {

                }else  {
                    if (response.body().getComments()==null) {
                        tv_comments.setText(0+"");
                        tv_label.setText("هیچ نظری برای این پیام ثبت نشده . شما اولین باشید");

                    }else {
                        tv_comments.setText(response.body().getComments().size()+"");
                        comments.addAll(response.body().getComments());
                        adater_comment.notifyDataSetChanged();

                    }
                    tv_likes.setText(response.body().getLikes()+"");



                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.e("comments",t.getMessage());
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!InternetCheck.isOnline(getActivity())) {
//                    MySnack.showSnack(coordinatorLayout, getString(R.string.No_Internet));
//                }else {
                   String text = et_text.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        MySnack.showSnack(coordinatorLayout, "متن نظر نمیتواند خالی بماند");
                        et_text.requestFocus();
                    }else {
                        ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
                        Call<SimpleResponse> call_send_comment= api.sendComment(user.getApikey(),message_id,chanel_id,text);
                        call_send_comment.enqueue(new Callback<SimpleResponse>() {
                            @Override
                            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                if (!response.isSuccessful()) {



                                }else {
                                    if (response.body().isError()) {
                                        MySnack.showSnack(coordinatorLayout,response.body().getMessage());
                                    }else {
                                        MySnack.showSnack(coordinatorLayout,response.body().getMessage());
                                        et_text.setText("");
                                    }



                                }
                            }

                            @Override
                            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                                Log.e("comments",t.getMessage());
                            }
                        });
                    }
                }
         //   }
        });


    }
}
