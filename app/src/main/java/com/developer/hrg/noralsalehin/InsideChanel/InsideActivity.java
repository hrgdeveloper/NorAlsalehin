package com.developer.hrg.noralsalehin.InsideChanel;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.developer.hrg.noralsalehin.Helps.ApiInterface;
import com.developer.hrg.noralsalehin.Helps.Apiclient;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.InternetCheck;
import com.developer.hrg.noralsalehin.Helps.MyAlert;
import com.developer.hrg.noralsalehin.Helps.MyApplication;
import com.developer.hrg.noralsalehin.Helps.SimpleResponse;
import com.developer.hrg.noralsalehin.Helps.UserData;
import com.developer.hrg.noralsalehin.InsideChanel.comment.CommentFragment;
import com.developer.hrg.noralsalehin.InsideChanel.toolbar.Fragment_insideToolbar;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.Models.User;
import com.developer.hrg.noralsalehin.R;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsideActivity extends AppCompatActivity implements View.OnClickListener, Message_Adapter.ClickListener {

    TextView tv_chanelName, tv_tedad, tv_label;
    ImageView iv_thumb;
    RecyclerView recyclerView;
    Chanel chanel;
    Toolbar toolbar;
    Button btn_mute;
    User user;
    private BroadcastReceiver reciverChanelsTask;
    UserData userdata;
    ArrayList<Message> messages = new ArrayList<>();
    int last_meesage_id = 0, last_position = 0;
    Message_Adapter adapter_message;
    ProgressBar progressBar;
    FloatingActionButton fab;
    int like_state;
    public static final int STORAGE_REQUEST = 102;
    int download_id = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);
        defineViews();
        defineClass();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        chanel = intent.getParcelableExtra("chanel");
        progressBar.setVisibility(View.VISIBLE);
        //vase inke age position recycler view save shode bood az hamoon ja biad
        if (userdata.hasPositionData(chanel.getChanel_id())) {
            last_position = userdata.getLastPosition(chanel.getChanel_id());

        }
           //check mikonim bebin agar qablan payami to databse zakhire shode begrim va akharin idish ro begirim vase query zadan vae gereftan
         //messagaye bade on id
        if (userdata.hasMessageData(chanel.getChanel_id())) {
            messages.addAll(userdata.getAllMessages(chanel.getChanel_id()));
            //vase in migirim akharin id ro ke to darkhastemoon akharin messagaiai ke bade in hastano begirim
            last_meesage_id = userdata.getLastMessage_id(chanel.getChanel_id());
        }

        tv_chanelName.setText(chanel.getName());
        Glide.with(InsideActivity.this).load(Config.CHANEL_THUMB_BASE_OFFLINE + chanel.getThumb()).apply(new RequestOptions().placeholder(R.drawable.broadcast)
                .error(R.drawable.broadcast)
        ).into(iv_thumb);

        if (InternetCheck.isOnline(InsideActivity.this)) {
            ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
            Call<SimpleResponse> call_getMemberCount = api.getUserCount();
            call_getMemberCount.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (!response.isSuccessful()) {
                        int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
                        tv_tedad.setText("تعداد اعضا : " + user_count);
                    } else {
                        boolean error = response.body().isError();
                        if (error) {
                            int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
                            tv_tedad.setText("تعداد اعضا : " + user_count);
                        } else {
                            int user_count = response.body().getUser_count();
                            tv_tedad.setText("تعداد اعضا : " + user_count);
                            MyApplication.getInstance().getUserInfo().setUserCount(user_count);
                        }

                    }

                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
                    tv_tedad.setText("تعداد اعضا : " + user_count);
                }
            });

            Call<SimpleResponse> call_Messages = api.getAllMessages(user.getApikey(), chanel.getChanel_id(), last_meesage_id);
            call_Messages.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                    if (!response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Log.e("insideActivity", message);
                            adapter_message.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else {
                        boolean error = response.body().isError();
                        if (!error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            messages.addAll(messages.size(),response.body().getMessages());
                            userdata.insertIntoMessage(response.body().getMessages());
                            adapter_message.notifyItemRangeInserted(messages.size(),response.body().getMessages().size());

                            last_meesage_id = userdata.getLastMessage_id(chanel.getChanel_id());

                        } else {
                            if (response.body().getErrortype() == 1) {
                                setNoMessage();
                                progressBar.setVisibility(View.INVISIBLE);
                                adapter_message.notifyDataSetChanged();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                adapter_message.notifyDataSetChanged();
                            }
                            Log.e("|successError", response.body().getMessage());

                        }

                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    Log.e("failure", t.getMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                    adapter_message.notifyDataSetChanged();
                }
            });


        } else {
            int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
            tv_tedad.setText("تعداد اعضا : " + user_count);
            progressBar.setVisibility(View.INVISIBLE);
            adapter_message.notifyDataSetChanged();
        }

        toolbar.setOnClickListener(this);
        btn_mute.setOnClickListener(this);
        updateSoundState();

        reciverChanelsTask = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NEW_MESSAGE)) {
                    Message message = intent.getParcelableExtra("message");

                    updateRows(message);
                }

            }
        };

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    //this is the top of the RecyclerView



                        int top_id = messages.get(0).getMessage_id();



                            if (InternetCheck.isOnline(InsideActivity.this)) {

                                progressBar.setVisibility(View.VISIBLE);
                                ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
                                Call<SimpleResponse> call_Messages = api.getAllMessagesTop(user.getApikey(), chanel.getChanel_id(), top_id);
                                call_Messages.enqueue(new Callback<SimpleResponse>() {
                                    @Override
                                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                        if (!response.isSuccessful()) {
                                            try {
                                                progressBar.setVisibility(View.GONE);
                                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                String message = jsonObject.getString("message");
                                                Log.e("insideActivity", message);
                                                progressBar.setVisibility(View.INVISIBLE);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }else {
                                            boolean error = response.body().isError();
                                            if (error) {

                                                progressBar.setVisibility(View.GONE);
                                          //      Toast.makeText(InsideActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }else {

                                                progressBar.setVisibility(View.GONE);
                                                messages.addAll(0, response.body().getMessages());
                                                userdata.insertIntoMessage(response.body().getMessages());
                                            //    adapter_message.notifyDataSetChanged();
                                                adapter_message.notifyItemRangeInserted(0,response.body().getMessages().size());



                                                adapter_message.notifyItemChanged(messages.size());
                                                last_meesage_id = userdata.getLastMessage_id(chanel.getChanel_id());

                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SimpleResponse> call, Throwable t) {

                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }




                }

                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 1 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    fab.hide();
                }


//                 if (dy > 0)
//                 {
//
//                     fab.hide();
//                 }else


                if (dy < 0) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.show();
                        }
                    }, 450);

                }

            }

        });





    }

    public void updateRows(final Message message) {
        // aval check mikonim bebinim aya payami ke omade vase hamin chanele ya na age bood bahash kar darim
        if (chanel.getChanel_id()==message.getChanel_id()) {

            messages.add(messages.size(),message);
            int readcount = userdata.getReadcount(chanel.getChanel_id());
            readcount++;
            userdata.updateRead(0, readcount, message.getChanel_id());
            userdata.insertIntoMessage(message);
            chanel.setUsername(message.getAdmin_name());
            chanel.setLast_message(message.getMessage());
            chanel.setType(message.getType());
            chanel.setUpdated_at(message.getUpdated_at());
            chanel.setCount(chanel.getCount() + 1);
            userdata.updateChanel(chanel);
            last_meesage_id = message.getMessage_id();
            adapter_message.notifyItemInserted(messages.size());
            tv_label.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(InsideActivity.this).registerReceiver(reciverChanelsTask, new IntentFilter(Config.PUSH_NEW_MESSAGE));
        //vase raftan be ye akharin positon
                if (messages.size() > 0 && last_position != 0) {
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(last_position, 0);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reciverChanelsTask);
        // aval check mikonim bebinim listemon item dare
        if (messages.size() > 0) {
            //age dasht akharin position ro migirim
            last_position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            //age mokhalefe -1 bood
            if (last_position == -1) {

            } else {
                //check mikonim bebinim qablan position in chanel save shode ya na

                if (userdata.hasPositionData(chanel.getChanel_id())) {
                    //age are update mikonim
                    userdata.addPosition(chanel.getChanel_id(), last_position, true);
                } else {
                    //age na ye jadidesho misazim
                    userdata.addPosition(chanel.getChanel_id(), last_position, false);
                }
            }

        }
    }

    public void setNoMessage() {
        if (messages.size() == 0) {
            tv_label.setVisibility(View.VISIBLE);
            tv_label.setText("هیچ پیامی ثبت نشده است");
        }
    }

    public void defineViews() {

        user = MyApplication.getInstance().getUserData().getUser();

        tv_chanelName = (TextView) findViewById(R.id.tv_inside_chanelName);
        tv_label = (TextView) findViewById(R.id.tv_inside_lable);
        tv_tedad = (TextView) findViewById(R.id.tv_inside_users);
        iv_thumb = (ImageView) findViewById(R.id.iv_inside_thumb);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_messages);
        if (last_meesage_id==0) {
            LinearLayoutManager linearLayoutManager =new LinearLayoutManager(InsideActivity.this);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(InsideActivity.this));
        }



        toolbar = (Toolbar) findViewById(R.id.toolbar_inside);
        btn_mute = (Button) findViewById(R.id.btn_inside_mute);
        progressBar = (ProgressBar) findViewById(R.id.myprogressbar);
        fab = (FloatingActionButton) findViewById(R.id.floating_inside);
        fab.setOnClickListener(this);


    }

    public void defineClass() {
        userdata = new UserData(InsideActivity.this);
        adapter_message = new Message_Adapter(InsideActivity.this, messages);
        adapter_message.setCliclListener(this);
        recyclerView.setAdapter(adapter_message);
    }

    public void updateSoundState() {
        if (MyApplication.getInstance().getUserData().getChanelSoundState(chanel.getChanel_id()) == 1) {
            btn_mute.setText("بیصدا کردن");

        } else {
            btn_mute.setText("روشن کردن صدا");

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    //vase inke toye fragmentemoon az tarighe in method be chanel dastresi dashte bashim
    public Chanel getChanel() {
        return chanel;
    }

    @Override
    public void onClick(View view) {
        if (view == toolbar) {
            openFragment(new Fragment_insideToolbar());

        } else if (view == btn_mute) {
            int currentState = MyApplication.getInstance().getUserData().getChanelSoundState(chanel.getChanel_id());
            if (currentState == 1) {
                currentState = 0;
            } else {
                currentState = 1;
            }
            MyApplication.getInstance().getUserData().updateChanelSoundState(chanel.getChanel_id(), currentState);
            updateSoundState();
        } else if (view == fab) {
            recyclerView.scrollToPosition(messages.size() - 1);
            fab.hide();
        }
    }
     /////////////////////////////////////////////////////pictureClicls\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void picture_imageClicked(final int position, View view, final CircularProgressBar circularProgressBar, final ImageView iv_download)
     {

        if (isFileExists(Config.Folders.IMAGES, messages.get(position).getUrl()) &&
                getFile(Config.Folders.IMAGES, messages.get(position).getUrl()).length()==messages.get(position).getLenth()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_inside, Fragment_InsidePicture.newInstance(messages.get(position).getUrl(),
                    messages.get(position).getMessage()));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } else {

            if (ActivityCompat.checkSelfPermission(InsideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_REQUEST);
            }else {
                if (messages.get(position).getDl_state()==0) {
                    messages.get(position).setDl_state(1);
                    adapter_message.notifyItemChanged(position);
                    userdata.setDlState(1,messages.get(position).getMessage_id());


//               int download_id = PRDownloader.download(Config.MESSAGE_PIC_ADDRES + messages.get(position).getUrl(),
//                       getPath(Config.Folders.IMAGES),messages.get(position).getUrl())
//                    .build()
//                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
//                                @Override
//                                public void onStartOrResume() {
//
//                                }
//                            })
//                            .setOnPauseListener(new OnPauseListener() {
//                                @Override
//                                public void onPause() {
//                                    messages.get(position).setDl_state(1);
//                                    userdata.setDlState(1,messages.get(position).getMessage_id());
//                                }
//                            })
//                            .setOnCancelListener(new OnCancelListener() {
//                                @Override
//                                public void onCancel() {
//
//                                }
//                            })
//                            .setOnProgressListener(new OnProgressListener() {
//                                @Override
//                                public void onProgress(Progress progress) {
//                                    long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
//                                    circularProgressBar.setProgress(progressPercent);
//
//
//                                }
//                            })
//                            .start(new OnDownloadListener() {
//                                @Override
//                                public void onDownloadComplete() {
//                                    adapter_message.notifyItemChanged(position);
//                                }
//
//                                @Override
//                                public void onError(Error error) {
//
//                                }
//
//
//                            });

















//                    ApiInterface apiInterface = Apiclient.getClient().create(ApiInterface.class);
//                    Call<ResponseBody> call = apiInterface.downloadFileWhiturl(Config.MESSAGE_PIC_ADDRES + messages.get(position).getUrl());
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
//
//                            if (response.isSuccessful()) {
//
//                                new DownloadFile(response.body(), messages.get(position).getUrl(), Config.Folders.IMAGES,circularProgressBar,iv_download
//                                        ,position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//                            } else {
//                            try {
//
//                                MyAlert.showAlert(InsideActivity.this, "خطا در دریافت", response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                                Toast.makeText(InsideActivity.this, "خطا در دریافت", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            //         Toast.makeText(getApplicationContext(), "errore", Toast.LENGTH_SHORT).show();
//                            Log.e("errorTodownload", t.getMessage());
//                        }
//                    });
                }else {

                }

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userdata.resetDlstate();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void picture_likeClicked(final int position, View view) {
        likeFunction(position);

    }
    public void likeFunction(final int position) {
        if (!InternetCheck.isOnline(InsideActivity.this)) {
            Toast.makeText(this, "عدم دسترسی به اینترنت", Toast.LENGTH_SHORT).show();
        } else {
            like_state = messages.get(position).getLiked() > 0 ? 0 : 1;

            ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
            Call<SimpleResponse> call_setlike = api.setLike(user.getApikey(), messages.get(position).getMessage_id(), like_state);
            call_setlike.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (!response.isSuccessful()) {

                    } else {
                        boolean error = response.body().isError();
                        if (error) {

                        } else {

                            Message message = messages.get(position);
                            message.setLiked(like_state);
                            messages.set(position, message);
                            adapter_message.notifyDataSetChanged();
                            userdata.setLikeState(like_state, message.getMessage_id());
                        }

                    }

                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void picture_commentClicked(int position, View view) {
        openFragment(CommentFragment.getInstance(messages.get(position).getMessage_id(), chanel.getChanel_id()));
    }
    /////////////////////////////////////////////////////simpleClicks\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void simple_likeClicked(int position, View view) {
     likeFunction(position);
    }

    @Override
    public void simple_commentClicked(int position, View view) {
        openFragment(CommentFragment.getInstance(messages.get(position).getMessage_id(), chanel.getChanel_id()));
    }

    /////////////////////////////////////////////////////videoClicls\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void video_imageClicked(final int position, View view, final CircularProgressBar circularProgressBar, final ImageView iv_download

    ) {

        if (isFileExists(Config.Folders.VIDEOS, messages.get(position).getUrl()) &&
                getFile(Config.Folders.VIDEOS, messages.get(position).getUrl()).length()==messages.get(position).getLenth()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_inside, InsideVideoFragment.newInstance(messages.get(position).getUrl(),
                    messages.get(position).getMessage()));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {

            if (ActivityCompat.checkSelfPermission(InsideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST);
            } else {

                if (messages.get(position).getDl_state()==0) {

                 //  messages.get(position).setDl_state(1);
                    Message message = messages.get(position);
                    message.setDl_state(1);
                    messages.set(position,message);

                    int download_id = PRDownloader.download(Config.MESSAGE_VIDEO_ADDRESS + messages.get(position).getUrl(),
                            getPath(Config.Folders.VIDEOS),messages.get(position).getUrl())
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {

                                }
                            })
                            .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {

                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {

                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {

                                    Long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                    circularProgressBar.setProgress(progressPercent);


                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    adapter_message.notifyItemChanged(position);
                                }

                                @Override
                                public void onError(Error error) {

                                }


                            });


                }else {

                }




















//                ApiInterface apiInterface = Apiclient.getClient().create(ApiInterface.class);
//                Call<ResponseBody> call = apiInterface.downloadFileWhiturl(Config.MESSAGE_VIDEO_ADDRESS + messages.get(position).getUrl());
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
//
//                        if (response.isSuccessful()) {
//
//                            new DownloadFile(response.body(), messages.get(position).getUrl(), Config.Folders.VIDEOS, circularProgressBar,iv_download
//                            ,position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//                        } else {
//                            try {
//
//                                MyAlert.showAlert(InsideActivity.this, "error", response.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Toast.makeText(getApplicationContext(), "errore", Toast.LENGTH_SHORT).show();
//                        Log.e("errorTodownload", t.getMessage());
//                    }
//                });
            }
        }

    }

    @Override
    public void video_likeClicked(int position, View view) {
        likeFunction(position);
    }

    @Override
    public void video_commentClicked(int position, View view) {
        openFragment(CommentFragment.getInstance(messages.get(position).getMessage_id(), chanel.getChanel_id()));
    }

    public class DownloadFile extends AsyncTask<Void, String, String> {
        ResponseBody responsebody;
        String filename;
        String foldername;
        ImageView iv_download;
        //CircleProgressView circleProgressView;
        CircularProgressBar circularProgressBar;
        int position ;


        public DownloadFile(ResponseBody responsebody, String fileName, String foldername, CircularProgressBar circularProgressBar , ImageView iv_download
        ,int position
        ) {
            this.responsebody = responsebody;
            this.filename = fileName;
            this.foldername = foldername;
            this.circularProgressBar = circularProgressBar;
            this.iv_download=iv_download;
            this.position=position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                File destinationFile = new File(Environment.getExternalStorageDirectory() + "/NoorAlSalehin/" + foldername, filename);
                InputStream is = null;
                OutputStream os = null;

                try {
                    Log.d("imageDownload", "File Size=" + responsebody.contentLength());

                    is = responsebody.byteStream();
                    os = new FileOutputStream(destinationFile);

                    byte data[] = new byte[4096];
                    int count;
                    int progress = 0;
                    while ((count = is.read(data)) != -1) {

                        os.write(data, 0, count);
                        progress += count;
                        Log.d("imageDownload", "Progress: " + progress + "/" + responsebody.contentLength() + " >>>> " + (float) progress / responsebody.contentLength());
                        publishProgress("" + (int) ((progress * 100) / responsebody.contentLength()));
                    }

                    os.flush();

                    Log.d("imageDownload", "File saved successfully!");
                    return "success";
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("imageDownload", "Failed to save the file!");
                    return "failed";
                } finally {
                    if (is != null) is.close();
                    if (os != null) os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("imageDownload", "Failed to save the file!");
                return "failed";
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
           // circleProgressView.setValue(Integer.parseInt(values[0]));
            circularProgressBar.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == "success") {

               adapter_message.notifyItemChanged(position);
            }
        } 
    }


    public boolean isFileExists(String folderName, String filename) {
        File file = new File(Environment.getExternalStorageDirectory() + "/NoorAlSalehin/" + folderName, filename);
        return file.exists();
    }
    public File getFile(String folderName, String filename) {
        File file = new File(Environment.getExternalStorageDirectory() + "/NoorAlSalehin/" + folderName, filename);
        return file;
    }

    public String getPath(String folderName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/NoorAlSalehin/" + folderName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return  file.getAbsolutePath();

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_inside, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (requestCode == STORAGE_REQUEST) {

            if (ContextCompat.checkSelfPermission(InsideActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(InsideActivity.this, permission)) {
                    ActivityCompat.requestPermissions(InsideActivity.this, new String[]{permission}, requestCode);

                } else {
                    ActivityCompat.requestPermissions(InsideActivity.this, new String[]{permission}, requestCode);
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
                if (ActivityCompat.checkSelfPermission(InsideActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    creatFolders();

                } else {
                    Log.e("Premission", "Storrage is not Granted");
                }
                break;


        }
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

}



