package com.developer.hrg.noralsalehin.SmsHandeling;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.developer.hrg.noralsalehin.Helps.UserInfo;
import com.developer.hrg.noralsalehin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Verify_Fragment extends Fragment {
 TextView tv_wrong , tv_lable ;
    EditText et_verify ;
 UserInfo userInfo;
    public Verify_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo=new UserInfo(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_verify_, container, false);
        et_verify=(EditText)view.findViewById(R.id.et_verify);
        tv_wrong=(TextView)view.findViewById(R.id.tv_wrong_number);
        tv_lable=(TextView)view.findViewById(R.id.tv_lable_verify);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String number = userInfo.getMobileNumber();
        tv_lable.setText("پیامک تایید کد هویت شما به شماره : " + number + " ارسال شد لطفا کد دریافت شده را در کادر زیر وارد نمایید");
        tv_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }
}

