package com.developer.hrg.noralsalehin.SmsHandeling;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class Register_fragment extends Fragment  {

  EditText editText;
    Button btn_send ;
    UserInfo userInfo ;
    public Register_fragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo=new UserInfo(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register_fragment, container, false);
         editText=(EditText)view.findViewById(R.id.et_mobileNumber);
         btn_send=(Button)view.findViewById(R.id.btn_send_meeage);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText.requestFocus();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String number = editText.getText().toString();

                if (!InternetCheck.isOnline(getActivity())) {
                    Toast.makeText(getActivity(), R.string.No_Internet, Toast.LENGTH_SHORT).show();
                }else if (!number.matches("^(09)\\d{9}$"))
                           {
                               Toast.makeText(getActivity(), "شماره وارد شده صحیح نمیباشد", Toast.LENGTH_SHORT).show();
                }else{
                    ApiInterface apiInterface = Apiclient.getClient().create(ApiInterface.class);
                    Call<SimpleResponse> call = apiInterface.register(number);
                    call.enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            if (!response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                                }else {

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
