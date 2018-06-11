package com.developer.hrg.noralsalehin.InsideChanel;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.MyApplication;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_insideToolbar extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    CircleImageView iv_thumb ;
    TextView tv_chanelName ,tv_tead , tv_description;

    FloatingActionButton fb_back;
    ImageView iv_pic ;
    Chanel chanel ;

    public Fragment_insideToolbar() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        chanel=((InsideActivity)getActivity()).getChanel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inside_toolbar, container, false);
         toolbar=(Toolbar)view.findViewById(R.id.toolbar_inside_toolbar);
        iv_thumb=(CircleImageView)view.findViewById(R.id.iv_inside_toolbar_thumb);
        tv_chanelName=(TextView)view.findViewById(R.id.tv_inside_toolbar_chanelName);
        tv_tead=(TextView)view.findViewById(R.id.tv_inside_toolbar_users);
        fb_back=(FloatingActionButton)view.findViewById(R.id.fb_back);
        iv_pic=(ImageView)view.findViewById(R.id.iv_inside_toolbar_pic);
        tv_description=(TextView)view.findViewById(R.id.tv_inside_toolbar_desc);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((InsideActivity)getActivity()).setSupportActionBar(toolbar);
        ((InsideActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((InsideActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        fb_back.setOnClickListener(this);
        Glide.with(getActivity()).load(Config.CHANEL_PIC_BASE_OFFLINE+chanel.getThumb())
                .apply(new RequestOptions().placeholder(R.drawable.broadcast).error(R.drawable.broadcast)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        ).into(iv_pic);

        Glide.with(getActivity()).load(Config.CHANEL_THUMB_BASE_OFFLINE+chanel.getThumb())
                .apply(new RequestOptions().placeholder(R.drawable.broadcast).error(R.drawable.broadcast)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(iv_thumb);
        tv_description.setText(chanel.getDescription());
        tv_chanelName.setText(chanel.getName());
        tv_tead.setText("تعداد کاربران : "+ MyApplication.getInstance().getUserInfo().getUserCount());


    }

    @Override
    public void onClick(View view) {
        if (view==fb_back) {
            ((InsideActivity)getActivity()).onBackPressed();

        }

    }
}
