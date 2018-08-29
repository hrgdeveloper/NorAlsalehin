package com.developer.hrg.noralsalehin.InsideChanel;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.TouchImageView;
import com.developer.hrg.noralsalehin.R;

import org.w3c.dom.Text;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_InsidePicture extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FILE = "file";
    private static final String TEXT = "text";

    // TODO: Rename and change types of parameters
    private String  file;
    private String text;
    FrameLayout frameLayout ;
    TouchImageView iv_pic   ;
    ImageView iv_back;
    TextView tv_text ;
    Toolbar toolbar ;
    boolean hide = false ;
    RelativeLayout relativeLayout_top ;

    public Fragment_InsidePicture() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Fragment_InsidePicture newInstance(String file, String text) {
        Fragment_InsidePicture fragment = new Fragment_InsidePicture();
        Bundle args = new Bundle();
        args.putString(FILE, file);
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            file = getArguments().getString(FILE);
            text = getArguments().getString(TEXT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inside_picture, container, false);

        iv_pic=(TouchImageView)view.findViewById(R.id.iv_picFragment);
        tv_text=(TextView)view.findViewById(R.id.tv_inside_picture_bottom);
        iv_back=(ImageView)view.findViewById(R.id.iv_back_insidepicture);
        relativeLayout_top=(RelativeLayout)view.findViewById(R.id.relative_inside_picture_top);


        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        iv_pic.setImageURI(Uri.fromFile(getFile(Config.Folders.IMAGES,file)));
        if (!TextUtils.isEmpty(text)) {
            tv_text.setText(text);
        }else {
            tv_text.setVisibility(View.GONE);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ((InsideActivity)getActivity()).onBackPressed();
            }
        });
        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hide) {
                    tv_text.setVisibility(View.INVISIBLE);
                    tv_text.animate().translationY(view.getHeight());
                    relativeLayout_top.setVisibility(View.INVISIBLE);
                    relativeLayout_top.animate().translationX(view.getHeight());
                }else {
                    tv_text.setVisibility(View.VISIBLE);
                    relativeLayout_top.setVisibility(View.VISIBLE);
                    tv_text.animate().translationY(0);
                    relativeLayout_top.animate().translationX(0);
                }
                hide=!hide;

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity()!=null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }


        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ((InsideActivity)getActivity()).onBackPressed();

                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public File getFile (String folderName, String filename) {
        File file = new File(Environment.getExternalStorageDirectory()+"/NoorAlSalehin/"+folderName,filename);
        return file ;
    }


}
