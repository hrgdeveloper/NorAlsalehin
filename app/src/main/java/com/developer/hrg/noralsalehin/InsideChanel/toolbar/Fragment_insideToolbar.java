package com.developer.hrg.noralsalehin.InsideChanel.toolbar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.MyApplication;
import com.developer.hrg.noralsalehin.InsideChanel.InsideActivity;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_insideToolbar extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    CircleImageView iv_thumb ;
    TextView tv_chanelName ,tv_tead , tv_description , tv_notify_state;
    FloatingActionButton fb_back;
    ImageView iv_pic ;
    Chanel chanel ;
    Spinner spinner ;
    List<Integer> textSizes = Arrays.asList(14,18,22,26,30,34);
    ArrayAdapter<Integer> adapter_spinner ;
  int textSize ;

    public Fragment_insideToolbar() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        chanel=((InsideActivity)getActivity()).getChanel();
        adapter_spinner = new ArrayAdapter<Integer>(getActivity(),android.R.layout.simple_spinner_item,textSizes);
        adapter_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textSize=MyApplication.getInstance().getSettingPref().getTextSize();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inside_toolbar, container, false);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar_inside_toolbar);
        iv_thumb=(CircleImageView)view.findViewById(R.id.iv_inside_toolbar_thumb);
        tv_chanelName=(TextView)view.findViewById(R.id.tv_inside_toolbar_chanelName);
        tv_tead=(TextView)view.findViewById(R.id.tv_inside_toolbar_users);
        fb_back=(FloatingActionButton)view.findViewById(R.id.fb_back);
        iv_pic=(ImageView)view.findViewById(R.id.iv_inside_toolbar_pic);
        tv_notify_state=(TextView)view.findViewById(R.id.tv_notify_state);
        tv_description=(TextView)view.findViewById(R.id.tv_inside_toolbar_desc);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinner.setAdapter(adapter_spinner);

        int spinnerPosition = adapter_spinner.getPosition(textSize);
        spinner.setSelection(spinnerPosition);
        handleSpinnerClick();
        tv_notify_state.setOnClickListener(this);
        iv_pic.setOnClickListener(this);
        iv_thumb.setOnClickListener(this);

        ((InsideActivity)getActivity()).setSupportActionBar(toolbar);
        ((InsideActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((InsideActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        fb_back.setOnClickListener(this);
        Glide.with(getActivity()).load(Config.CHANEL_PIC_BASE_ONLINE_FINAL+chanel.getThumb())
                .apply(new RequestOptions().placeholder(R.drawable.broadcast).error(R.drawable.broadcast)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        ).into(iv_pic);

        Glide.with(getActivity()).load(Config.CHANEL_THUMB_BASE_ONLINE_FINAL+chanel.getThumb())
                .apply(new RequestOptions().placeholder(R.drawable.broadcast).error(R.drawable.broadcast)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(iv_thumb);
        tv_description.setText(chanel.getDescription());
        tv_chanelName.setText(chanel.getName());
        tv_tead.setText("تعداد کاربران : "+ MyApplication.getInstance().getUserInfo().getUserCount());
        // vaziat 1 yani notification roshane

          updateNotifyState();

    }


    public void updateNotifyState() {
        if (MyApplication.getInstance().getUserData().getChanelNotifyState(chanel.getChanel_id())==1)
        {
            tv_notify_state.setText("خاموش کردن");
            tv_notify_state.setTextColor(ContextCompat.getColor(getActivity(),R.color.notifyOff));
        }else {
            tv_notify_state.setText("روشن کردن");
            tv_notify_state.setTextColor(ContextCompat.getColor(getActivity(),R.color.notifyOn));

        }
    }

    @Override
    public void onClick(View view) {
        if (view==fb_back) {
            ((InsideActivity)getActivity()).onBackPressed();

        }else if (view==tv_notify_state) {
            int currentState = MyApplication.getInstance().getUserData().getChanelNotifyState(chanel.getChanel_id());
            Toast.makeText(getActivity(), currentState+ " ", Toast.LENGTH_SHORT).show();
            if (currentState==1) {
                currentState=0;
            }else {
                currentState = 1;
            }
            MyApplication.getInstance().getUserData().updateChanelnotifyState(chanel.getChanel_id(),currentState);

            int currentStateafterChange = MyApplication.getInstance().getUserData().getChanelNotifyState(chanel.getChanel_id());
            Toast.makeText(getActivity(), currentStateafterChange+ " ", Toast.LENGTH_SHORT).show();
            updateNotifyState();
        }else if (view==iv_pic) {

            openFragment(Profile_Fragment.getInstance(chanel.getChanel_id()));

        }else if (view==iv_thumb) {
            openFragment(Profile_Fragment.getInstance(chanel.getChanel_id()));
        }

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_inside, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    private void handleSpinnerClick() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                 int size = textSizes.get(position);
                 MyApplication.getInstance().getSettingPref().setTextSize(size);
                ((InsideActivity)getActivity()).getMessageAdapter().setTextSize(size);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
