package com.developer.hrg.noralsalehin.about;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.developer.hrg.noralsalehin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutProgramerFragment extends Fragment implements View.OnClickListener {
   ImageView iv_call , iv_telegram  , iv_email , iv_back ;


    public AboutProgramerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about_programer, container, false);
         iv_call=(ImageView)view.findViewById(R.id.iv_call_aProgramer);
        iv_telegram=(ImageView)view.findViewById(R.id.iv_telegram_aProgramer);
        iv_email=(ImageView)view.findViewById(R.id.iv_email_aPrgamer);
        iv_back=(ImageView)view.findViewById(R.id.iv_back_aProgramer);
        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TranslateAnimation animationLeft = new TranslateAnimation(300.0f, 0.0f, 0.0f, 0.0f);
        animationLeft.setDuration(1000);

        TranslateAnimation animationRight= new TranslateAnimation(-300.0f, 0.0f, 0.0f, 0.0f);
        animationRight.setDuration(1000);
        iv_back.setOnClickListener(this);
        iv_telegram.startAnimation(animationLeft);
        iv_call.startAnimation(animationRight);
        iv_call.setOnClickListener(this);
        iv_telegram.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        if (view==iv_back) {
            getFragmentManager().popBackStack();
        }else if (view==iv_call) {
            Intent intent  = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:09357273422"));
            startActivity(intent);
        } else if (view==iv_telegram) {
            try {
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/hrgdeveloper"));
                        telegram.setPackage("org.telegram.messenger");
                startActivity(telegram);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "تلگرام یافت نشد", Toast.LENGTH_LONG).show();
            }
        }
    }
}
