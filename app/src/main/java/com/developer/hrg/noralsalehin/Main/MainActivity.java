package com.developer.hrg.noralsalehin.Main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.Models.Notify;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.developer.hrg.noralsalehin.Models.User;
import com.developer.hrg.noralsalehin.R;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    Toolbar toolbar;
    boolean firstTimeLunchForBroadCast = true  ;
    boolean firstTimeLunchForOnResume = true  ;
    private BroadcastReceiver reciverChanelsTask;
    NetworkChangeReceiver networkChangeReceiver ;
    TextView tv_noChanel ;
    DrawerLayout drawerLayout ;
    NavigationView navigationView ;
    ActionBarDrawerToggle drawerToggle;
    public static final int STORAGE_REQUEST =100;
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


        defineView();
        defineClasees();
        headerFunction();

        drawerToggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerToggle.syncState();


        adaptetChanels = new GetChanelsAdapter(MainActivity.this, chanels, unreads);
        adaptetChanels.setMyClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adaptetChanels);

        if (userData.hasUnreadData() && userData.hasChanelsData() && userData.hasNotifyData()) {
            unreads.addAll(userData.getAllunReads());
            chanels.addAll(userData.getAllChanels());
            adaptetChanels.notifyDataSetChanged();
        }


     //   user = new User(3,"09357273422","9dab40ec77d921329df0264aa24d0043","2018-05-25 15:46:25");




        if (InternetCheck.isOnline(MainActivity.this)) {
            getChanels();

        }


        reciverChanelsTask=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NEW_CHANEL)) {
                      handleNewChanel(intent);

                }else if (intent.getAction().equals(Config.PUSH_NEW_MESSAGE))
                {
                    Message message = intent.getParcelableExtra("message");

                     updateRows(message);
                }

            }
        };


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT>=23) {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_REQUEST);
        }else {

            creatFolders();
        }
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(reciverChanelsTask,new IntentFilter(Config.PUSH_NEW_CHANEL));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(reciverChanelsTask,new IntentFilter(Config.PUSH_NEW_MESSAGE));

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


    private void askForPermission(String permission, Integer requestCode) {
        if (requestCode==STORAGE_REQUEST){

            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                }
            } else {

                       creatFolders();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {


            case STORAGE_REQUEST:
                if(ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED ) {
                   creatFolders();

                }else {
                    Log.e("Premission","Storrage is not Granted");
                }


        }
    }

    public void creatFolders() {
        File mainfoldrs = new File(Environment.getExternalStorageDirectory(),"NoorAlSalehin");
        File images = new File(mainfoldrs,"Images");
        File autdios = new File(mainfoldrs,"Auidos");
        File videos = new File(mainfoldrs,"Videos");
        File doucmnets = new File(mainfoldrs,"Documnets");
        if (!mainfoldrs.exists()) {
            mainfoldrs.mkdir();
        }
        if (!images.exists()) {
            images.mkdir();
        }
        if (!autdios.exists()) {
            autdios.mkdir();
        }
        if (!videos.exists()) {
            videos.mkdir();
        }
        if (!doucmnets.exists()) {
            doucmnets.mkdir();
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkChangeReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(reciverChanelsTask);

        super.onPause();

    }

    public void updateRows(Message message) {
        for (Chanel chanel : chanels) {
            if (chanel.getChanel_id()==message.getChanel_id()) {
                // aval chaneli ke payam omade vasaho migirim
                int index = chanels.indexOf(chanel);

             //moshakhaste chanel ro ba tavajoh be message update mikonim
                chanel.setUsername(message.getAdmin_name());
                chanel.setLast_message(message.getMessage());
                chanel.setType(message.getType());
                chanel.setUpdated_at(message.getUpdated_at());
                chanel.setCount(chanel.getCount()+1);
                chanels.set(index,chanel);
                userData.updateChanel(chanel);

                 //tedade payam haye khonde nashode on chanel ro migirim
                int unreadCount = userData.getUnreadCount(message.getChanel_id());
                //yeki behesh ezafe mikonim  o to database va to list update mikoim
                unreadCount++;
                UnRead unRead = unreads.get(index);
                unRead.setCount(unreadCount);
                unreads.set(index,unRead);
                userData.updateUnread(unreadCount,message.getChanel_id());



                break;
            }
        }
        adaptetChanels.notifyDataSetChanged();
    }

    public void defineView() {
        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);
        tv_noChanel=(TextView)findViewById(R.id.tv_main_noChanel);
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView=(NavigationView)findViewById(R.id.navigationview);
        toolbar=(Toolbar)findViewById(R.id.toolbar_main);
    }
    public void defineClasees() {
        networkChangeReceiver = new NetworkChangeReceiver();
        userData = new UserData(MainActivity.this);
        userInfo = new UserInfo(MainActivity.this);
        user = userData.getUser();
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
//                        if (!userInfo.get_inUnreadFetched()) {
//                            for (Chanel chanel : chanels) {
//                                //meghdare akhare meghdare tedade payame khonde shode hast va vase avalin bar 0 mishe chon hich payami khone nashode
//                                UnRead unRead = new UnRead(chanel.getChanel_id(), chanel.getCount(),0);
//                                unreads.add(unRead);
//                                userInfo.set_inUnreadFetched(true);
//
//                            }
//                            userData.addUnReads(unreads);
                  //      } else {
                            if (unreads.size() < chanels.size()) {
                                for (int i = unreads.size() ; i < chanels.size(); i++) {
                                    UnRead unRead = new UnRead(chanels.get(i).getChanel_id(), chanels.get(i).getCount(),0);
                                    userData.addUnread(unRead);
                                    Notify notify = new Notify(chanels.get(i).getChanel_id(),1,1);
                                    userData.addNotify(notify);
                               //     unreads.add(unRead);

                                }
                                unreads.clear();
                                unreads.addAll(userData.getAllunReads());
                            }

                       // in qestmat vase ine ke agar moghei ke offline boodim payame jadidi ezafe shode check kone bebine chnata payam jadid ezaf shode

                       for (int i=0 ; i<unreads.size() ; i++) {
                            int sum = unreads.get(i).getCount()+unreads.get(i).getReadCount();
                           if (sum < chanels.get(i).getCount()) {
                               int ekhtelaf = chanels.get(i).getCount() - sum;
                                 userData.updateUnread(unreads.get(i).getCount()+ekhtelaf,chanels.get(i).getChanel_id());
                                  UnRead tempUnread = new UnRead(unreads.get(i).getChanel_id(),unreads.get(i).getCount()+ekhtelaf,unreads.get(i).getReadCount());
                               unreads.set(i,tempUnread);
                           }

                       }



          //              }
                        subscribeToAllChanels(chanels);
                        adaptetChanels.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                tv_toolbar.setText(R.string.toolbar_text);
              Log.e(TAG,t.getMessage()+" ");
                if (!userData.hasUnreadData() && !userData.hasChanelsData()) {
                    tv_noChanel.setVisibility(View.VISIBLE);
                }

            }
        });


    }



    public void subscribeToAllChanels(ArrayList<Chanel> chanels){
    for (Chanel chanel : chanels) {
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_CHANEL+chanel.getChanel_id());

    }
    }

    // az recivver akharin chanele ezafe shodaro migirim o be list ezafe mionim

    public void handleNewChanel(Intent intent) {
        Chanel chanel = (Chanel) intent.getParcelableExtra("chanel");
        chanels.add(chanel);
        // meghdare khande shodaro barabe ba 0 mizarim chon hanooz messagi khande nashode
        UnRead unread = new UnRead(chanel.getChanel_id(),chanel.getCount(),0);
        Notify notify = new Notify(chanel.getChanel_id(),1,1);
        unreads.add(unread);
        userData.addChanel(chanel);
        userData.addUnread(unread);
        userData.addNotify(notify);
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

    // vase zamani ke dobare connect mishe . check mikonim age activiry bare avaleshe run shode in code ejra nashe

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
    public void  headerFunction() {
        View view = navigationView.getHeaderView(0);
        String username = userData.getUser().getUsername();
        LinearLayout linearlayout = (LinearLayout)view.findViewById(R.id.linear_header);
        Button btn_send=(Button)view.findViewById(R.id.btn_header_sendusername);
        TextView tv_username = (TextView)view.findViewById(R.id.tv_header_username);
        TextView tv_number = (TextView)view.findViewById(R.id.tv_header_number);
    final       EditText et_username = (EditText)view.findViewById(R.id.et_header_username);
        tv_number.setText(user.getMobile());

        if (username.equalsIgnoreCase("e")) {
            linearlayout.setVisibility(View.VISIBLE);
            btn_send.setVisibility(View.VISIBLE);
            et_username.setVisibility(View.VISIBLE);
            tv_username.setVisibility(View.GONE);
           btn_send.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  if (!InternetCheck.isOnline(MainActivity.this)) {
                      Toast.makeText(getApplication(), "عدم دسترسی به اینترنت", Toast.LENGTH_SHORT).show();
                  }else {
                      final String typed_username = et_username.getText().toString();
                      if (TextUtils.isEmpty(typed_username)) {
                          Toast.makeText(getApplication(), "نام کاربری نمیتواند خالی بماند", Toast.LENGTH_SHORT).show();

                      }else if (typed_username.equalsIgnoreCase("e")) {
                          Toast.makeText(getApplication(), "این نام کاربری قابل انتخاب نیست", Toast.LENGTH_SHORT).show();
                      }else {
                          ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
                          Call<SimpleResponse> call = api.updateUsername(user.getApikey(),typed_username);
                          call.enqueue(new Callback<SimpleResponse>() {
                              @Override
                              public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                  if (!response.isSuccessful()) {
                                      try {
                                          JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                          String message = jsonObject.getString("message");
                                          Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      } catch (IOException e) {
                                          e.printStackTrace();
                                      }
                                  } else {
                                      if (response.body().isError()) {
                                          Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                      } else {
                                          userData.updateUsername(typed_username);
                                          user=userData.getUser();
                                          headerFunction();
                                      }
                                  }
                              }


                              @Override
                              public void onFailure(Call<SimpleResponse> call, Throwable t) {

                                  Toast.makeText(getApplicationContext(), "خطای برقراری ارتباط", Toast.LENGTH_SHORT).show();
                              }
                          });
                      }


                  }
               }
           });

        }else {
            et_username.setVisibility(View.GONE);
            linearlayout.setVisibility(View.INVISIBLE);
            btn_send.setVisibility(View.GONE);
            tv_username.setVisibility(View.VISIBLE);
            tv_username.setText(username);
        }



    }
}
