package com.developer.hrg.noralsalehin.about;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.developer.hrg.noralsalehin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutNoorFragment extends Fragment {

    ImageView iv_back ;
    public AboutNoorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about_noor, container, false);
        iv_back=(ImageView)view.findViewById(R.id.iv_back_aNoor);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)  {
        super.onActivityCreated(savedInstanceState);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }
}
