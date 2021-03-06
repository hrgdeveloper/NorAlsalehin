package com.developer.hrg.noralsalehin.Main;

import android.Manifest;
import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.request.RequestOptions;
import com.developer.hrg.noralsalehin.Helps.ApiInterface;
import com.developer.hrg.noralsalehin.Helps.Apiclient;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.DownloadService;
import com.developer.hrg.noralsalehin.Helps.FireBaseActivity;
import com.developer.hrg.noralsalehin.Helps.ImageCompression;
import com.developer.hrg.noralsalehin.Helps.InternetCheck;

import com.developer.hrg.noralsalehin.Helps.MyApplication;
import com.developer.hrg.noralsalehin.Helps.MyProgress;
import com.developer.hrg.noralsalehin.Helps.Repetetive;
import com.developer.hrg.noralsalehin.Helps.SimpleResponse;
import com.developer.hrg.noralsalehin.Helps.UserData;
import com.developer.hrg.noralsalehin.Helps.UserInfo;
import com.developer.hrg.noralsalehin.InsideChanel.InsideActivity;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.Download;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.Models.Message_id;
import com.developer.hrg.noralsalehin.Models.Notify;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.developer.hrg.noralsalehin.Models.User;
import com.developer.hrg.noralsalehin.R;
import com.developer.hrg.noralsalehin.SmsHandeling.SmsActivity;
import com.developer.hrg.noralsalehin.about.AboutNoorFragment;
import com.developer.hrg.noralsalehin.about.AboutProgramerFragment;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements GetChanelsAdapter.MyClickListener {
    public final int GALLERY_REQUEST = 100;
    public final int RESULT_LOAD_IMG_Gallery = 101;
    CircleImageView iv_profile;
    File profile_file = null;
    RecyclerView recyclerView;
    User user;
    UserData userData;
    ArrayList<Chanel> chanels = new ArrayList<>();
    ArrayList<UnRead> unreads = new ArrayList<>();
    GetChanelsAdapter adaptetChanels;
    UserInfo userInfo;
    TextView tv_toolbar;
    Toolbar toolbar;
    boolean firstTimeLunchForBroadCast = true;
    boolean firstTimeLunchForOnResume = true;
    private BroadcastReceiver reciverChanelsTask;
    NetworkChangeReceiver networkChangeReceiver;
    NetworkChangeReceiverForDownloads networkChangeReciverFordownlaod;
    TextView tv_noChanel;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ImageCompression imageCompression;
    public static final int STORAGE_REQUEST = 102;
    public static final int READ_STORAGE = 108;

    private String TAG = MainActivity.class.getSimpleName();
    int count ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineClasees();
        if (userData.hasDownloadsData()) {
            userData.deleteDownloas();
        }
        if (userData.hasDownloadsBACKData()) {
            userData.deleteDownloasBack();
        }
        if (MyApplication.getInstance().getSettingPref().getDeletedCount()==-1) {
            ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
            Call<SimpleResponse> call_count = api.getDeletedCount();
            call_count.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (!response.isSuccessful()) {

                    } else {

                        MyApplication.getInstance().getSettingPref().setDeletedCount(response.body().getCount());

                        }

                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    Log.e("failer","failer");
                }
            });
        }

        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        defineView();

        // vase download ha ye reciver joda dar nazar migirim ke vaghti activity baste shod in reciver qat nashe o faqat
        //vaqti app close mishe reciver ham bahash qat mishe

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReciverFordownlaod, filter);
        headerFunction();
        // vase pak kardane download haye temp
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
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

        getChanels();
        checkFiireBaseState();
        reciverChanelsTask = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NEW_CHANEL)) {
                    handleNewChanel(intent);

                } else if (intent.getAction().equals(Config.PUSH_NEW_MESSAGE)) {
                    Message message = intent.getParcelableExtra("message");

                    updateRows(message);
                }

            }
        };
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked())
                    item.setChecked(false);
                else item.setChecked(true);
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {

                    case R.id.mu_about:
                     openFragmeentAbout(new AboutNoorFragment());
                        return true;
                    case R.id.mu_developer:
                        openFragmeentAbout(new AboutProgramerFragment());
                        return true;
                    case R.id.mu_exit:
                        MyApplication.getInstance().getUserData().deleteuser();
                        MyApplication.getInstance().getUserInfo().set_IsLogged_in(false);
                        MyApplication.getInstance().getUserInfo().set_isMobileSent(false);
                        MyApplication.getInstance().getUserInfo().deletMobileNumber();
                        Intent intent = new Intent(MainActivity.this, SmsActivity.class);
                        startActivity(intent);
                        finish();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "خطلا", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }

        });

        if (Build.VERSION.SDK_INT >= 23) {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST);
        } else {
            creatFolders();
        }

    }

    private void openFragmeentAbout(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in,R.anim.left_out,
                android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        transaction.add(R.id.container_main_compelete,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    @Override
    public void onBackPressed() {
        profile_file = null;
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        count = MyApplication.getInstance().getSettingPref().getDeletedCount();
        if (count != -1) {
            ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
            Call<SimpleResponse> call_count = api.getDeletedCount();
            call_count.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (!response.isSuccessful()) {

                    }else {

                        if (count < response.body().getCount()) {
                           final int temp_count = response.body().getCount();

                            ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
                            Call<SimpleResponse> call_count = api.getDeletedList();
                            call_count.enqueue(new Callback<SimpleResponse>() {
                                @Override
                                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                    if (!response.isSuccessful()) {

                                    }else {

                                        ArrayList<Integer> test = new ArrayList<Integer>();
                                        for (Message_id message_id  : response.body().getMessage_ids()) {
                                            test.add(message_id.getMessage_id());
                                        }
                                         int result = userData.updateMessageActives(test);
                                         MyApplication.getInstance().getSettingPref().setDeletedCount(temp_count);
                                    }
                                }

                                @Override
                                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                                    Log.e("failer",t.getMessage());

                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    Log.e("failer","failer");
                }
            });
        }


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(reciverChanelsTask, new IntentFilter(Config.PUSH_NEW_CHANEL));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(reciverChanelsTask, new IntentFilter(Config.PUSH_NEW_MESSAGE));

        if (firstTimeLunchForOnResume) {
            firstTimeLunchForOnResume = false;
        } else {
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

    @Override
    protected void onPause() {
        unregisterReceiver(networkChangeReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(reciverChanelsTask);
        super.onPause();

    }

    public void creatFolders() {
        File mainfoldrs = new File(Environment.getExternalStorageDirectory(), "NoorAlSalehin");
        File images = new File(mainfoldrs, "Images");
        File autdios = new File(mainfoldrs, "Auidos");
        File videos = new File(mainfoldrs, "Videos");
        File doucmnets = new File(mainfoldrs, "Documnets");
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

    public void updateRows(Message message) {
        for (Chanel chanel : chanels) {
            if (chanel.getChanel_id() == message.getChanel_id()) {
                // aval chaneli ke payam omade vasaho migirim
                int index = chanels.indexOf(chanel);

                //moshakhaste chanel ro ba tavajoh be message update mikonim
                chanel.setUsername(message.getAdmin_name());
                chanel.setLast_message(message.getMessage());
                chanel.setType(message.getType());
                chanel.setUpdated_at(message.getUpdated_at());
                chanel.setCount(chanel.getCount() + 1);
                chanels.set(index, chanel);
                userData.updateChanel(chanel);

                //tedade payam haye khonde nashode on chanel ro migirim
                int unreadCount = userData.getUnreadCount(message.getChanel_id());

                //yeki behesh ezafe mikonim  o to database va to list update mikoim
                unreadCount++;
                UnRead unRead = unreads.get(index);
                unRead.setCount(unreadCount);
                unreads.set(index, unRead);
                userData.updateUnread(unreadCount, message.getChanel_id());
                break;
            }
        }
        adaptetChanels.notifyDataSetChanged();
    }

    public void defineView() {
        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);

        tv_noChanel = (TextView) findViewById(R.id.tv_main_noChanel);
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    public void defineClasees() {
        networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReciverFordownlaod = new NetworkChangeReceiverForDownloads();
        userData = new UserData(MainActivity.this);
        userInfo = new UserInfo(MainActivity.this);
        imageCompression = new ImageCompression(MainActivity.this);
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
                        tv_toolbar.setText(R.string.toolbar_text);
                        userData.deleteChanels();
                        chanels.clear();
                        chanels.addAll(response.body().getChanels());
                        userData.addChanels(chanels);

                        if (unreads.size() < chanels.size()) {
                            for (int i = unreads.size(); i < chanels.size(); i++) {
                                UnRead unRead = new UnRead(chanels.get(i).getChanel_id(), chanels.get(i).getCount(), 0);
                                userData.addUnread(unRead);
                                Notify notify = new Notify(chanels.get(i).getChanel_id(), 1, 1);
                                userData.addNotify(notify);


                            }
                            unreads.clear();
                            unreads.addAll(userData.getAllunReads());
                        }

                        // in qestmat vase ine ke agar moghei ke offline boodim payame jadidi ezafe shode check kone bebine chnata payam jadid ezaf shode

                        for (int i = 0; i < unreads.size(); i++) {
                            int sum = unreads.get(i).getCount() + unreads.get(i).getReadCount();


                            if (sum < chanels.get(i).getCount()) {


                                int ekhtelaf = chanels.get(i).getCount() - sum;
                                userData.updateUnread(unreads.get(i).getCount() + ekhtelaf, chanels.get(i).getChanel_id());
                                UnRead tempUnread = new UnRead(unreads.get(i).getChanel_id(), unreads.get(i).getCount() + ekhtelaf, unreads.get(i).getReadCount());
                                unreads.set(i, tempUnread);
                            }else if (sum > chanels.get(i).getCount()) {
                                // age tedade payamaye server az majome ma kamtar shod bayad majomme ma ham kam beshe be andazeye payamaye server ;
                                // kam shodan olaviat ba payamaye nakhone shodas ; age dashtim az on kam mikonim .. age nadashtim az khonde shodeha


                                int ekhtelaf =  sum  - chanels.get(i).getCount() ;
                                if (unreads.get(i).getCount() > 0) {
                                    if (unreads.get(i).getCount() > ekhtelaf) {

                                        userData.updateUnread(unreads.get(i).getCount() - ekhtelaf, chanels.get(i).getChanel_id());
                                        UnRead tempUnread = new UnRead(unreads.get(i).getChanel_id(), unreads.get(i).getCount() - ekhtelaf, unreads.get(i).getReadCount());
                                        unreads.set(i, tempUnread);
                                    }else {
                                        int x = ekhtelaf - unreads.get(i).getCount() ;
                                        userData.updateUnread(unreads.get(i).getCount() -  (ekhtelaf - x) , chanels.get(i).getChanel_id());
                                        UnRead tempUnread = new UnRead(unreads.get(i).getChanel_id(), unreads.get(i).getCount() - (ekhtelaf - x), unreads.get(i).getReadCount());
                                        unreads.set(i, tempUnread);

                                        userData.updateReadForDelete(unreads.get(i).getReadCount() - x, chanels.get(i).getChanel_id());
                                        UnRead tempRead = new UnRead(unreads.get(i).getChanel_id(), unreads.get(i).getCount() , unreads.get(i).getReadCount() - x);
                                        unreads.set(i, tempRead);
                                    }


                                }else {

                                userData.updateReadForDelete(unreads.get(i).getReadCount() - ekhtelaf, chanels.get(i).getChanel_id());
                                UnRead tempUnread = new UnRead(unreads.get(i).getChanel_id(), unreads.get(i).getCount() , unreads.get(i).getReadCount() - ekhtelaf);
                                       unreads.set(i, tempUnread);
                                }



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
                Log.e(TAG, t.getMessage() + " ");
                if (!userData.hasUnreadData() && !userData.hasChanelsData()) {
                    tv_noChanel.setVisibility(View.VISIBLE);
                }

            }
        });


    }


    public void subscribeToAllChanels(ArrayList<Chanel> chanels) {
        for (Chanel chanel : chanels) {
            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_CHANEL + chanel.getChanel_id());

        }
    }

    // az recivver akharin chanele ezafe shodaro migirim o be list ezafe mionim

    public void handleNewChanel(Intent intent) {
        Chanel chanel = (Chanel) intent.getParcelableExtra("chanel");
        chanels.add(chanel);
        // meghdare khande shodaro barabe ba 0 mizarim chon hanooz messagi khande nashode
        UnRead unread = new UnRead(chanel.getChanel_id(), chanel.getCount(), 0);
        Notify notify = new Notify(chanel.getChanel_id(), 1, 1);
        unreads.add(unread);
        userData.addChanel(chanel);
        userData.addUnread(unread);
        userData.addNotify(notify);
        adaptetChanels.notifyDataSetChanged();
    }

    @Override
    public void chanel_clicked(int position, View view) {
        if (Build.VERSION.SDK_INT>=23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("اجازه دسترسی");
                    alert.setMessage("نور صالحین احتیاج به نوشتن اطلاعات بر روی  حافظه دارد .. لطفا اجازه را صادر فرمایید");
                    alert.setPositiveButton("بسیار خوب", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_REQUEST );
                        }
                    });
                    alert.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
                }

            } else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("اجازه دسترسی");
                    alert.setMessage("نور صالحین احتیاج به دسترسی به اطلاعات حافظه دارد .. لطفا اجازه را صادر فرمایید");
                    alert.setPositiveButton("بسیار خوب", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_STORAGE );                    }
                    });
                    alert.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                }
            }

            else {
                Chanel chanel = chanels.get(position);
                Intent intent = new Intent(MainActivity.this, InsideActivity.class);
                intent.putExtra("chanel", chanel);
                startActivity(intent);
                userData.updateRead(unreads.get(position).getCount(), unreads.get(position).getReadCount(), unreads.get(position).getChanel_id());

            }
        } else {

            Chanel chanel = chanels.get(position);
            Intent intent = new Intent(MainActivity.this, InsideActivity.class);
            intent.putExtra("chanel", chanel);
            startActivity(intent);
            userData.updateRead(unreads.get(position).getCount(), unreads.get(position).getReadCount(), unreads.get(position).getChanel_id());
        }


    }

    public void checkFiireBaseState(){

    Retrofit    retrofit=new Retrofit.Builder()
                .baseUrl(Config.FIREBASE_PIC_ADDRESS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<SimpleResponse> firebase = apiInterface.checkFirebase();
        firebase.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("error" , "coud not access firebase messaging");
                }else {
                    boolean error = response.body().isError() ;
                    if (error) {
                        Intent intent = new Intent(MainActivity.this,FireBaseActivity.class);
                        intent.putExtra("message",response.body().getMessage());
                        startActivity(intent);
                    }else {

                    }

                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
             Log.e("failure" , "coud not access firebase messaging");
            }
        });


    }


    // vase zamani ke dobare connect mishe . check mikonim age activiry bare avaleshe run shode in code ejra nashe

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (firstTimeLunchForBroadCast) {
                firstTimeLunchForBroadCast = false;
            } else {
                if (InternetCheck.isOnline(MainActivity.this)) {

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

    public class NetworkChangeReceiverForDownloads extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {

            if (InternetCheck.isOnline(MainActivity.this)) {
               if (userData.hasDownloadsData()) {
                      Gson json = new Gson();
                   ArrayList<Download> downloads = new ArrayList<>();
                   downloads.addAll(userData.getDownloads());
                   for (Download download : downloads) {
                       Intent intentt = new Intent(MainActivity.this, DownloadService.class);
                       intentt.putExtra(DownloadService.MESSAGE_ID, download.getMessage_id());
                       intentt.putExtra(DownloadService.ADDRESS, download.getAddress());
                       intentt.putExtra(DownloadService.POSITION, download.getPosition());
                       intentt.putExtra(DownloadService.DIRPATH, download.getDirpath());
                       intentt.putExtra(DownloadService.FILENAME, download.getFilename());
                       Message message = json.fromJson(download.getMessage(),Message.class);
                       Bundle bundle = new Bundle();
                       bundle.putParcelable(DownloadService.MESSAGE, message);
                       intentt.putExtra(DownloadService.BUNDLE, bundle);
                       intentt.putExtra(DownloadService.CHANEL_ID, download.getChanel_id());

                       startService(intentt);

                   }



               }

            } else {

            }
        }
    }


    public void headerFunction() {
        View view = navigationView.getHeaderView(0);
        CircleImageView iv_profile = (CircleImageView) view.findViewById(R.id.iv_header_profilePic);


        if (user.getPic_thumb() != null) {

            Glide.with(MainActivity.this).load(Config.PROFILE_PIC_THUMB_ADDRESS + user.getPic_thumb()).apply(new RequestOptions().placeholder(R.drawable.profile).error(R.drawable.profile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            ).into(iv_profile);
        }


        final String username = userData.getUser().getUsername();
        LinearLayout linearlayout = (LinearLayout) view.findViewById(R.id.linear_header);
        Button btn_send = (Button) view.findViewById(R.id.btn_header_sendusername);
        TextView tv_username = (TextView) view.findViewById(R.id.tv_header_username);
        TextView tv_number = (TextView) view.findViewById(R.id.tv_header_number);
        final EditText et_username = (EditText) view.findViewById(R.id.et_header_username);
        tv_number.setText(user.getMobile());

        if (username.equalsIgnoreCase("e")) {
            linearlayout.setVisibility(View.VISIBLE);
            btn_send.setVisibility(View.VISIBLE);
            et_username.setVisibility(View.VISIBLE);
            et_username.setFilters(new InputFilter[]{getUsernameFilter()});
            tv_username.setVisibility(View.GONE);
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        final String typed_username = et_username.getText().toString();
                        if (TextUtils.isEmpty(typed_username)) {
                            Toast.makeText(getApplication(), "نام کاربری نمیتواند خالی بماند", Toast.LENGTH_SHORT).show();

                        } else if (typed_username.equalsIgnoreCase("e")) {
                            Toast.makeText(getApplication(), "این نام کاربری قابل انتخاب نیست", Toast.LENGTH_SHORT).show();
                        } else {
                            ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
                            Call<SimpleResponse> call = api.updateUsername(user.getApikey(), typed_username);
                            call.enqueue(new Callback<SimpleResponse>() {
                                @Override
                                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                    if (!response.isSuccessful()) {
                                        try {

                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            String message = jsonObject.getString("message");
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                            Log.e("MyError" , response.errorBody().string());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.e("MyError" ,"to response");
                                        if (response.body().isError()) {
                                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            userData.updateUsername(typed_username);
                                            user = userData.getUser();
                                            headerFunction();
                                        }
                                    }
                                }


                                @Override
                                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                                    Log.e("MyError" ,"to failute");
                                    Repetetive.handleFailureError(t,MainActivity.this,"MainActivity");
                                }
                            });



                    }
                }
            });

        } else {
            et_username.setVisibility(View.GONE);
            linearlayout.setVisibility(View.INVISIBLE);
            btn_send.setVisibility(View.GONE);
            tv_username.setVisibility(View.VISIBLE);
            tv_username.setText(username);
        }

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!InternetCheck.isOnline(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, R.string.No_Internet, Toast.LENGTH_SHORT).show();
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, GALLERY_REQUEST);

                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RESULT_LOAD_IMG_Gallery);

                    }
                }
            }
        });

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG_Gallery && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String realPath = getRealPathFromURI(MainActivity.this, selectedImage);
            String filePath = imageCompression.compressImage(realPath);
            profile_file = new File(filePath);
            MyProgress.showProgress(MainActivity.this, "در حال ارسال ...");
            String last_pic = user.getPic_thumb() == null ? "n" : user.getPic_thumb();
            RequestBody req_lastPicName = RequestBody.create(MediaType.parse("text/plain"), last_pic);
            RequestBody req_pic = RequestBody.create(MediaType.parse("image/jpeg"), profile_file);
            MultipartBody.Part part_pic = MultipartBody.Part.createFormData("pic", profile_file.getName(), req_pic);

            ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
            Call<SimpleResponse> profile_call = api.updateProfile(user.getApikey(), part_pic, req_lastPicName);
            profile_call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (!response.isSuccessful()) {
                        try {
                            MyProgress.cancelProgress();
                            JSONObject jsonobject = new JSONObject(response.errorBody().string());
                            String message = jsonobject.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        boolean error = response.body().isError();
                        String message = response.body().getMessage();
                        if (!error) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            MyProgress.cancelProgress();
                            user.setPic(message);
                            user.setPic_thumb(message);
                            userData.updatePicAndThumb(message);
                            headerFunction();

                        } else {
                            MyProgress.cancelProgress();
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "خطایی پیش آمده لطفا دوباره تلاش کنید", Toast.LENGTH_SHORT).show();
                    MyProgress.cancelProgress();
                }
            });


        }


    }



    private void askForPermission(String permission, Integer requestCode) {
        if (requestCode == STORAGE_REQUEST) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                }
            } else {

                creatFolders();
            }
        }else if (requestCode==READ_STORAGE) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                }
            } else {


            }
        }

        else if (requestCode == GALLERY_REQUEST) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, RESULT_LOAD_IMG_Gallery);


            }
        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {


            case STORAGE_REQUEST:


                if (ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    creatFolders();

                   if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_STORAGE);
                    }

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("عدم اجازه دسترسی ");
                    alert.setMessage("رفتن به صفحه تنظیمات جهت صادر کردن اجازه");
                    alert.setPositiveButton("بسیار خوب", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    });
                    alert.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    alert.show();

                }
                break;

            case READ_STORAGE :
                if (ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("عدم اجازه دسترسی ");
                    alert.setMessage("رفتن به صفحه تنظیمات جهت صادر کردن اجازه");
                    alert.setPositiveButton("بسیار خوب", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    });
                    alert.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    alert.show();
                }

                break;

            case GALLERY_REQUEST:
                if (ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMG_Gallery);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("عدم اجازه دسترسی ");
                    alert.setMessage("رفتن به صفحه تنظیمات جهت صادر کردن اجازه");
                    alert.setPositiveButton("بسیار خوب", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    });
                    alert.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    alert.show();
                }

        }
    }



    // vase check kardane inke user space nazane to username
    public InputFilter getUsernameFilter() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }

        };
        return filter;
    }

}
