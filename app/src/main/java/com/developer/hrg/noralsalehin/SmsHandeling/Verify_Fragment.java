package com.developer.hrg.noralsalehin.SmsHandeling;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.hrg.noralsalehin.Helps.ApiInterface;
import com.developer.hrg.noralsalehin.Helps.Apiclient;
import com.developer.hrg.noralsalehin.Helps.InternetCheck;
import com.developer.hrg.noralsalehin.Helps.SimpleResponse;
import com.developer.hrg.noralsalehin.Helps.UserInfo;
import com.developer.hrg.noralsalehin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Verify_Fragment extends Fragment {
 TextView tv_wrong , tv_lable ;
    EditText et_verify ;
    Button btn_verify ;
 UserInfo userInfo;
    ProgressDialog progress;
    public Verify_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo=new UserInfo(getActivity());
        progress=new ProgressDialog(getActivity());
        progress.setMessage("در حال ارسال..");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_verify_, container, false);
        et_verify=(EditText)view.findViewById(R.id.et_verify);
        tv_wrong=(TextView)view.findViewById(R.id.tv_wrong_number);
        tv_lable=(TextView)view.findViewById(R.id.tv_lable_verify);
        btn_verify=(Button)view.findViewById(R.id.btn_verify_code);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String number = userInfo.getMobileNumber();
        et_verify.requestFocus();
        tv_lable.setText("پیامک تایید کد هویت شما به شماره : " + number + " ارسال شد لطفا کد دریافت شده را در کادر زیر وارد نمایید");
        tv_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.deletMobileNumber();
                userInfo.set_isMobileSent(false);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_in,R.anim.left_out);
                fragmentTransaction.replace(R.id.smsContainer,Register_fragment.newInstance(number));
                fragmentTransaction.commit();


            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = et_verify.getText().toString();
                if (TextUtils.isEmpty(otp)) {
                    Toast.makeText(getActivity(), "لطفا کد مورد نظر را وارد نمایید", Toast.LENGTH_SHORT).show();
                   et_verify.requestFocus();
                }else if (!InternetCheck.isOnline(getActivity())) {
                    Toast.makeText(getActivity(), R.string.No_Internet, Toast.LENGTH_SHORT).show();
                }else {
                    progress.show();
                    ApiInterface apiInterface = Apiclient.getClient().create(ApiInterface.class);
                    Call<SimpleResponse> call = apiInterface.verify(number,otp);
                    call.enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    progress.cancel();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                boolean error = response.body().isError();
                                if (error) {
                                    Toast.makeText
                                            (getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    progress.cancel();
                                }else {
                                    Toast.makeText(getActivity(),response.body().getUser().getApikey() , Toast.LENGTH_SHORT).show();
                                    progress.cancel();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {

                        }
                    });
                }

            }
        });


    }
}

