package com.developer.hrg.noralsalehin.Main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.developer.hrg.noralsalehin.Helps.ApiInterface;
import com.developer.hrg.noralsalehin.Helps.Apiclient;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.InternetCheck;
import com.developer.hrg.noralsalehin.Helps.MyAlert;
import com.developer.hrg.noralsalehin.Helps.SimpleResponse;
import com.developer.hrg.noralsalehin.Helps.UserData;
import com.developer.hrg.noralsalehin.Helps.UserInfo;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.developer.hrg.noralsalehin.Models.User;
import com.developer.hrg.noralsalehin.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GetChanelsAdapter.MyClickListener {
    RecyclerView recyclerView;
    User user;
    UserData userData;
    ArrayList<Chanel> chanels = new ArrayList<>();
    ArrayList<UnRead> unreads = new ArrayList<>();
    GetChanelsAdapter adaptetChanels;
    UserInfo userInfo;
    TextView tv_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);
        userData = new UserData(MainActivity.this);
        userInfo = new UserInfo(MainActivity.this);
        if (userData.getAllunReads() != null) {
            unreads.addAll(userData.getAllunReads());
        }

        user = userData.getUser();
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        adaptetChanels = new GetChanelsAdapter(MainActivity.this, chanels, unreads);
        adaptetChanels.setMyClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adaptetChanels);
        if (userInfo.get_IsFirstTimeMain()) {
            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        }
        if (InternetCheck.isOnline(MainActivity.this)) {

            getChanels();
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            chanels.addAll(userData.getAllChanels());
            for (Chanel temp : chanels) {
                stringBuffer.append(temp.getUpdated_at()==null ? "null \n" : temp.getUpdated_at()+ " \n" );
            }

            MyAlert.showAlert(MainActivity.this,"baresi" , stringBuffer.toString());

            adaptetChanels.notifyDataSetChanged();
        }


    }


    public void getChanels() {
        tv_toolbar.setText("در حال دریافت کانال ها ...");
        ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
        Call<SimpleResponse> call = api.getAllChanels(user.getApikey());
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        Toast.makeText(MainActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        tv_toolbar.setText(R.string.toolbar_text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.body().isError()) {
                        Toast.makeText(MainActivity.this, "هیچ کانالی ثبت نشده است", Toast.LENGTH_SHORT).show();
                    } else {
                        tv_toolbar.setText(R.string.toolbar_text);
                        userData.deleteChanels();
                        chanels.addAll(response.body().getChanels());
                        userData.addChanels(chanels);
                        if (!userInfo.get_inUnreadFetched()) {
                            for (Chanel chanel : chanels) {
                                UnRead unRead = new UnRead(chanel.getChanel_id(), chanel.getCount());
                                unreads.add(unRead);
                                userInfo.set_inUnreadFetched(true);

                            }
                            userData.addUnReads(unreads);
                        } else {
                            if (unreads.size() < chanels.size()) {
                                for (int i = unreads.size() ; i < chanels.size(); i++) {
                                    UnRead unRead = new UnRead(chanels.get(i).getChanel_id(), chanels.get(i).getCount());
                                    userData.addUnread(unRead);
                                    unreads.add(unRead);
                                }
                            }
                            adaptetChanels.notifyDataSetChanged();


                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                tv_toolbar.setText(R.string.toolbar_text);
                Toast.makeText(MainActivity.this, "خطلا در دریافت .", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void chanel_clicked(int position, View view) {

    }
}
