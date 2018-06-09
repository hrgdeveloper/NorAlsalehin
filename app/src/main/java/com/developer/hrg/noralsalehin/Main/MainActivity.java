package com.developer.hrg.noralsalehin.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.developer.hrg.noralsalehin.InsideChanel.InsideActivity;
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
    boolean firstTimeLunchForBroadCast = true  ;
    boolean firstTimeLunchForOnResume = true  ;
    private BroadcastReceiver reciverChanelsTask;
    NetworkChangeReceiver networkChangeReceiver ;
    TextView tv_noChanel ;
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        defineView();
        defineClasees();
        user = userData.getUser();
        adaptetChanels = new GetChanelsAdapter(MainActivity.this, chanels, unreads);
        adaptetChanels.setMyClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adaptetChanels);

        if (userData.hasUnreadData() && userData.hasChanelsData()) {
            unreads.addAll(userData.getAllunReads());
            chanels.addAll(userData.getAllChanels());
            adaptetChanels.notifyDataSetChanged();
        }


     //   user = new User(3,"09357273422","9dab40ec77d921329df0264aa24d0043","2018-05-25 15:46:25");




        if (InternetCheck.isOnline(MainActivity.this)) {
            getChanels();

        }

//        else {
//            if (userData.hasChanelsData()) {
//                chanels.addAll(userData.getAllChanels());
//                adaptetChanels.notifyDataSetChanged();
//            }else {
//                tv_noChanel.setVisibility(View.VISIBLE);
//            }
//
//
//        }

        reciverChanelsTask=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NEW_CHANEL)) {
                      handleNewChanel(intent);

                }

            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(reciverChanelsTask,new IntentFilter(Config.PUSH_NEW_CHANEL));

        if (firstTimeLunchForOnResume) {
            firstTimeLunchForOnResume=false;
        }else {
            if (userData.hasUnreadData() && userData.hasChanelsData()) {
                unreads.clear();
                chanels.clear();
                unreads.addAll(userData.getAllunReads());
                chanels.addAll(userData.getAllChanels());
                adaptetChanels.notifyDataSetChanged();
            }
            if (InternetCheck.isOnline(MainActivity.this)) {
                getChanels();
            }
        }



    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkChangeReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(reciverChanelsTask);
        super.onPause();

    }

    public void defineView() {
        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);
        tv_noChanel=(TextView)findViewById(R.id.tv_main_noChanel);
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
    }
    public void defineClasees() {
        networkChangeReceiver = new NetworkChangeReceiver();
        userData = new UserData(MainActivity.this);
        userInfo = new UserInfo(MainActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reciverChanelsTask);
    }

    public void getChanels() {
        tv_toolbar.setText("در حال به روز رسانی");
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
                        tv_toolbar.setText(R.string.toolbar_text);
                      tv_noChanel.setVisibility(View.VISIBLE);
                    } else {
                        tv_noChanel.setVisibility(View.GONE);
                        tv_toolbar.setText( R.string.toolbar_text);
                        userData.deleteChanels();
                        chanels.clear();
                        chanels.addAll(response.body().getChanels());
                        userData.addChanels(chanels);
                        if (!userInfo.get_inUnreadFetched()) {
                            for (Chanel chanel : chanels) {
                                UnRead unRead = new UnRead(chanel.getChanel_id(), chanel.getCount(),0);
                                unreads.add(unRead);
                                userInfo.set_inUnreadFetched(true);

                            }
                            userData.addUnReads(unreads);
                        } else {
                            if (unreads.size() < chanels.size()) {
                                for (int i = unreads.size() ; i < chanels.size(); i++) {
                                    UnRead unRead = new UnRead(chanels.get(i).getChanel_id(), chanels.get(i).getCount(),0);
                                    userData.addUnread(unRead);
                               //     unreads.add(unRead);

                                }
                                unreads.clear();
                                unreads.addAll(userData.getAllunReads());
                            }


                       for (int i=0 ; i<unreads.size() ; i++) {
                            int sum = unreads.get(i).getCount()+unreads.get(i).getReadCount();
                           if (sum < chanels.get(i).getCount()) {
                               int ekhtelaf = chanels.get(i).getCount() - sum;
                                 userData.updateUnread(unreads.get(i).getCount()+ekhtelaf,chanels.get(i).getChanel_id());
                                  UnRead tempUnread = new UnRead(unreads.get(i).getChanel_id(),unreads.get(i).getCount()+ekhtelaf,unreads.get(i).getReadCount());
                               unreads.set(i,tempUnread);
                           }

                       }



                        }
                        adaptetChanels.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                tv_toolbar.setText(R.string.toolbar_text);
              Log.e(TAG,t.getMessage());
                if (userData.hasUnreadData() && userData.hasChanelsData()) {
                    unreads.addAll(userData.getAllunReads());
                    chanels.addAll(userData.getAllChanels());
                    adaptetChanels.notifyDataSetChanged();
                }else {
                    tv_noChanel.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    public void handleNewChanel(Intent intent) {
        Chanel chanel = (Chanel) intent.getParcelableExtra("chanel");
        chanels.add(chanel);
        UnRead unread = new UnRead(chanel.getChanel_id(),chanel.getCount(),0);
        unreads.add(unread);
        userData.addChanel(chanel);
        userData.addUnread(unread);
        adaptetChanels.notifyDataSetChanged();
    }

    @Override
    public void chanel_clicked(int position, View view) {
        Chanel chanel = chanels.get(position);
        Intent intent = new Intent(MainActivity.this, InsideActivity.class);
        intent.putExtra("chanel",chanel);
        startActivity(intent);
        userData.updateRead(unreads.get(position).getCount(), unreads.get(position).getReadCount(), unreads.get(position).getChanel_id());

    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (InternetCheck.isOnline(MainActivity.this)){
                if (firstTimeLunchForBroadCast) {
                    firstTimeLunchForBroadCast=false;
                }else {

                    if (userData.hasUnreadData() && userData.hasChanelsData()) {
                        unreads.clear();
                        chanels.clear();
                        unreads.addAll(userData.getAllunReads());
                        chanels.addAll(userData.getAllChanels());
                        adaptetChanels.notifyDataSetChanged();
                    }
                    getChanels();

                }

            }

        }


    }
}
