package com.developer.hrg.noralsalehin.SmsHandeling;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.developer.hrg.noralsalehin.Helps.ApiInterface;
import com.developer.hrg.noralsalehin.Helps.Apiclient;
import com.developer.hrg.noralsalehin.Helps.InternetCheck;
import com.developer.hrg.noralsalehin.Helps.MyAlert;
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
  private static final String NUMBER = "number";
  EditText editText;
    Button btn_send ;
    UserInfo userInfo ;
    ProgressDialog progress ;
    String wrongNumber=null;
    public Register_fragment() {

    }
    public static Register_fragment newInstance(String number)  {
        Register_fragment regist = new Register_fragment();
        Bundle bundle = new Bundle();
        bundle.putString(NUMBER,number);
        regist.setArguments(bundle);
        return regist;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            wrongNumber=getArguments().getString(NUMBER);

        }
        userInfo=new UserInfo(getActivity());
        progress=new ProgressDialog(getActivity());
        progress.setMessage("در حال ارسال");

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
        if (wrongNumber!=null) {
            editText.setText(wrongNumber);
        }
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String number = editText.getText().toString();

                if (!InternetCheck.isOnline(getActivity())) {
                    Toast.makeText(getActivity(), R.string.No_Internet, Toast.LENGTH_SHORT).show();
                }else if (!number.matches("^(09)\\d{9}$"))
                           {
                               Toast.makeText(getActivity(), "شماره وارد شده صحیح نمیباشد", Toast.LENGTH_SHORT).show();
                }else{
                    progress.show();
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

                                    FragmentTransaction transation = getFragmentManager().beginTransaction();
                                    transation.setCustomAnimations(R.anim.right_in,R.anim.left_out);
                                    transation.replace(R.id.smsContainer,new Verify_Fragment());
                                    userInfo.setMobileNumber(number);
                                    userInfo.set_isMobileSent(true);
                                    transation.commit();
                                    progress.cancel();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            MyAlert.showAlert(getActivity(),"خطا","خطای " +"\n" + t.getMessage() );
                            progress.cancel();
                        }
                    });
                }


            }
        });
    }
}
