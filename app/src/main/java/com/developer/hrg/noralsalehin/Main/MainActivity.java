package com.developer.hrg.noralsalehin.Main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.developer.hrg.noralsalehin.Models.User;
import com.developer.hrg.noralsalehin.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
RecyclerView recyclerView ;
    User user;
    UserData userData ;
    ArrayList<Chanel> chanels = new ArrayList<>();
    GetChanelsAdapter adaptetChanels;
    UserInfo userInfo ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userData=new UserData(MainActivity.this);
        userInfo=new UserInfo(MainActivity.this);
        user=userData.getUser();
        recyclerView=(RecyclerView)findViewById(R.id.rv_main);
        adaptetChanels=new GetChanelsAdapter(MainActivity.this,chanels);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adaptetChanels);
        if (userInfo.get_IsFirstTimeMain()) {
            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        }
      if (InternetCheck.isOnline(MainActivity.this)) {
          getChanels();
      }else
          {

      }



    }


    public void getChanels() {
        ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
        Call<SimpleResponse> call = api.getAllChanels(user.getApikey());
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        Toast.makeText(MainActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {


                    chanels.addAll(response.body().getChanels());
                    adaptetChanels.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "خطلا در دریافت .", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
