package com.developer.hrg.noralsalehin.InsideChanel;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.developer.hrg.noralsalehin.Helps.DownloadService;
import com.developer.hrg.noralsalehin.Helps.InternetCheck;

import com.developer.hrg.noralsalehin.Helps.MyApplication;

import com.developer.hrg.noralsalehin.Helps.SimpleResponse;
import com.developer.hrg.noralsalehin.Helps.UserData;
import com.developer.hrg.noralsalehin.InsideChanel.comment.CommentFragment;
import com.developer.hrg.noralsalehin.InsideChanel.toolbar.Fragment_insideToolbar;

import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.Download;
import com.developer.hrg.noralsalehin.Models.DownloadBack;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.Models.User;
import com.developer.hrg.noralsalehin.R;


import com.downloader.PRDownloader;

import com.google.gson.Gson;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import java.io.IOException;

import java.util.ArrayList;


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
    int download_id = 0;
    MediaPlayer mediaPlayer;
    boolean executeOnResumeTask;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);
        executeOnResumeTask = false;
        mediaPlayer = new MediaPlayer();
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
                            messages.addAll(messages.size(), response.body().getMessages());
                            // vase qarar dadane meghdare avalie barabare ba sefr
                            for (int i = 0; i < messages.size(); i++) {
                                messages.get(i).setDl_percent(0);
                                messages.get(i).setDl_state(0);
                                messages.get(i).setDl_id(0);
                                messages.get(i).setAudio_percent(0);
                                messages.get(i).setComplete(0);
                            }

                            userdata.insertIntoMessage(response.body().getMessages());
                            adapter_message.notifyItemRangeInserted(messages.size(), response.body().getMessages().size());

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
                } else if (intent.getAction().equals(DownloadService.BROADCAST_PROGRESS)) {
                    int percent = intent.getIntExtra(DownloadService.PROGRESS_PERCENT, 0);
                    int position = intent.getIntExtra(DownloadService.PROGRESS_POSITION, 0);
                    int chanel_id = intent.getIntExtra(DownloadService.CHANEL_ID, 0);
                    Log.e("chanel_id", chanel_id + "  : " + chanel.getChanel_id());

                    if (chanel.getChanel_id() == chanel_id) {
                        Log.e("progress", percent + "");
                        if (messages.get(position).getDl_state() == 0) {
                            messages.get(position).setDl_state(1);
                        }
                        Log.e("broadcastt", "resume mishe  " + position + "darsad " + percent);
                        messages.get(position).setDl_percent(percent);
                        adapter_message.notifyItemChanged(position);
                    }


                } else if (intent.getAction().equals(DownloadService.BROADCAST_DL_FINISH)) {

                    int position = intent.getIntExtra(DownloadService.FINISH_POSITION, 0);
                    int message_id = intent.getIntExtra(DownloadService.MESSAGE_ID, 0);
                    int chanel_id_here = intent.getIntExtra(DownloadService.CHANEL_ID, 0);
                    userdata.setCompleteState(1,message_id);
                    if (chanel.getChanel_id()==chanel_id_here) {
                        messages.get(position).setComplete(1);
                        adapter_message.notifyItemChanged(position);
                    }

                } else if (intent.getAction().equals(DownloadService.BROADCAST_PAUSE)) {
                    int position = intent.getIntExtra(DownloadService.PROGRESS_POSITION, 0);
                    messages.get(position).setDl_state(0);
                    adapter_message.notifyItemChanged(position);


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
                                } else {
                                    boolean error = response.body().isError();
                                    if (error) {

                                        progressBar.setVisibility(View.GONE);
                                        //      Toast.makeText(InsideActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {

                                        progressBar.setVisibility(View.GONE);
                                        messages.addAll(0, response.body().getMessages());
                                        for (int i = 0; i < response.body().getMessages().size(); i++) {
                                            messages.get(i).setDl_percent(0);
                                            messages.get(i).setDl_state(0);
                                            messages.get(i).setDl_id(0);
                                            messages.get(i).setAudio_percent(0);
                                            messages.get(i).setComplete(0);
                                        }
                                        userdata.insertIntoMessage(response.body().getMessages());
                                        //    adapter_message.notifyDataSetChanged();
                                        adapter_message.notifyItemRangeInserted(0, response.body().getMessages().size());

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

                if (dy < 0) {

                    fab.show();
                }

            }

        });


    }

    public void updateRows(final Message message) {
        // aval check mikonim bebinim aya payami ke omade vase hamin chanele ya na age bood bahash kar darim
        if (chanel.getChanel_id() == message.getChanel_id()) {

            messages.add(messages.size(), message);
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
        LocalBroadcastManager.getInstance(InsideActivity.this).registerReceiver(reciverChanelsTask, new IntentFilter(DownloadService.BROADCAST_PROGRESS));
        LocalBroadcastManager.getInstance(InsideActivity.this).registerReceiver(reciverChanelsTask, new IntentFilter(DownloadService.BROADCAST_DL_FINISH));
        LocalBroadcastManager.getInstance(InsideActivity.this).registerReceiver(reciverChanelsTask, new IntentFilter(DownloadService.BROADCAST_PAUSE));


        //vase raftan be ye akharin positon
        if (messages.size() > 0 && last_position != 0) {
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(last_position, 0);

        }

        //check mikonim agar itemi moghei ke barname toye background boode downloadesh kamel shode ma positionesho save kardim
        // va adapter ro tebqe hamoon position be rooz mikonim
        if (userdata.hasDownloadsBACKData()) {
            ArrayList<DownloadBack> downloadBacks = new ArrayList<>();
            downloadBacks.addAll(userdata.getDownloadsBacks());

            for (DownloadBack dowloadBack : downloadBacks) {
                if (dowloadBack.getChanel_id()==chanel.getChanel_id()) {
                    messages.get(dowloadBack.getPosition()).setComplete(1);
                    adapter_message.notifyItemChanged(dowloadBack.getPosition());
                }
            }
            userdata.deleteDownloasBack();
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
            fab.hide();
        }
    }

    public void defineViews() {

        user = MyApplication.getInstance().getUserData().getUser();

        tv_chanelName = (TextView) findViewById(R.id.tv_inside_chanelName);
        tv_label = (TextView) findViewById(R.id.tv_inside_lable);
        tv_tedad = (TextView) findViewById(R.id.tv_inside_users);
        iv_thumb = (ImageView) findViewById(R.id.iv_inside_thumb);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_messages);
        if (last_meesage_id == 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InsideActivity.this);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(InsideActivity.this));
        }

        recyclerView.setItemAnimator(null);


        toolbar = (Toolbar) findViewById(R.id.toolbar_inside);
        btn_mute = (Button) findViewById(R.id.btn_inside_mute);
        progressBar = (ProgressBar) findViewById(R.id.myprogressbar);
        fab = (FloatingActionButton) findViewById(R.id.floating_inside);
        fab.setOnClickListener(this);


    }

    public void defineClass() {
        userdata = new UserData(InsideActivity.this);
        adapter_message = new Message_Adapter(InsideActivity.this, messages , userdata);
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

    ////////////////////////////////////////////////////////pictureFunctions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void picture_imageClicked(final int position, View view, final CircularProgressBar circularProgressBar, final ImageView iv_download) {

        if (isFileExists(Config.Folders.IMAGES, messages.get(position).getUrl())) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_inside, Fragment_InsidePicture.newInstance(messages.get(position).getUrl(),
                    messages.get(position).getMessage()));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } else {

            if (ActivityCompat.checkSelfPermission(InsideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST);
            } else {
                if (messages.get(position).getDl_state() == 0) {
                    messages.get(position).setDl_state(1);
                    adapter_message.notifyItemChanged(position);
                    download_service(position, Config.MESSAGE_PIC_ADDRES, Config.Folders.IMAGES);

                } else {
                    int dl_id = userdata.getDl_id(messages.get(position).getMessage_id());
                    PRDownloader.pause(dl_id);
                    messages.get(position).setDl_state(0);
                    adapter_message.notifyItemChanged(position);

                }

            }

        }
    }

    @Override
    public void picture_likeClicked(final int position, View view) {
        likeFunction(position);

    }


    @Override
    public void picture_commentClicked(int position, View view) {
        openFragment(CommentFragment.getInstance(messages.get(position).getMessage_id(), chanel.getChanel_id()));
    }

    /////////////////////////////////////////////////////simpleFunction\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void simple_likeClicked(int position, View view) {
        likeFunction(position);
    }

    @Override
    public void simple_commentClicked(int position, View view) {
        openFragment(CommentFragment.getInstance(messages.get(position).getMessage_id(), chanel.getChanel_id()));
    }

    /////////////////////////////////////////////////////videoFuctions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void video_imageClicked(final int position, View view, final CircularProgressBar circularProgressBar, final ImageView iv_download

    ) {

        if (messages.get(position).getComplete()==1) {
            if (isFileExists(Config.Folders.VIDEOS, messages.get(position).getUrl())) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_inside, InsideVideoFragment.newInstance(messages.get(position).getUrl(),
                        messages.get(position).getMessage()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
        }else {
                reDownload_ifFile_Deleted(position,Config.MESSAGE_VIDEO_ADDRESS,Config.Folders.VIDEOS);
            }
        } else {

            if (ActivityCompat.checkSelfPermission(InsideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST);
            } else {

                if (messages.get(position).getDl_state() == 0) {
                    messages.get(position).setDl_state(1);
                    adapter_message.notifyItemChanged(position);
                    download_service(position, Config.MESSAGE_VIDEO_ADDRESS, Config.Folders.VIDEOS);
                } else {
                    int dl_id = userdata.getDl_id(messages.get(position).getMessage_id());
                    Log.e("download_id", dl_id + " ");
                    PRDownloader.pause(dl_id);
                    userdata.deleteSingleDownload(messages.get(position).getMessage_id());
                    messages.get(position).setDl_state(0);
                    adapter_message.notifyItemChanged(position);
                }

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

    //////////////////////////////////////////////////////////////AduioFunctions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public void audio_imageClicked(final int position, View view, CircularProgressBar circularProgressBar, ImageView iv_download) {
         if (messages.get(position).getComplete()==1) {
             if (isFileExists(Config.Folders.AUDIOS, messages.get(position).getUrl())) {

                 Toast.makeText(this, "playing...", Toast.LENGTH_SHORT).show();
             }else {
//                 messages.get(position).setComplete(0);
//                 messages.get(position).setDl_state(0);
//                 userdata.setCompleteState(0,messages.get(position).getMessage_id());
//                 adapter_message.notifyItemChanged(position);
//                 messages.get(position).setDl_state(1);
//                 messages.get(position).setDl_percent(0);
//                 adapter_message.notifyItemChanged(position);
//                 download_service(position, Config.MESSAGE_AUDIO_ADDRESS, Config.Folders.AUDIOS);
                 reDownload_ifFile_Deleted(position,Config.MESSAGE_AUDIO_ADDRESS,Config.Folders.AUDIOS);
             }
         }
      else {

            if (ActivityCompat.checkSelfPermission(InsideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST);
            } else {

                if (messages.get(position).getDl_state() == 0) {
                    messages.get(position).setDl_state(1);
                    if (messages.get(position).getDl_id() == 0) {
                        adapter_message.notifyItemChanged(position);
                        download_service(position, Config.MESSAGE_AUDIO_ADDRESS, Config.Folders.AUDIOS);
                    } else {
                        PRDownloader.resume(messages.get(position).getDl_id());
                    }

                } else {
                    int dl_id = userdata.getDl_id(messages.get(position).getMessage_id());
                    PRDownloader.pause(dl_id);
                    messages.get(position).setDl_state(0);
                    adapter_message.notifyItemChanged(position);

                }


            }
        }

    }

    @Override
    public void audio_likeClicked(int position, View view) {
        likeFunction(position);
    }

    @Override
    public void audio_commentClicked(int position, View view) {
        openFragment(CommentFragment.getInstance(messages.get(position).getMessage_id(), chanel.getChanel_id()));
    }

    //////////////////////////////////////////////////////////////FileFunction\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void file_imageClicked(final int position, View view, CircularProgressBar circularProgressBar, ImageView iv_download) {
      if (messages.get(position).getComplete()==1) {
          if (isFileExists(Config.Folders.DOCUMENTS, messages.get(position).getUrl())) {

              try {
                  openFile(InsideActivity.this, getFile(Config.Folders.DOCUMENTS, messages.get(position).getUrl()));
              } catch (IOException e) {
                  e.printStackTrace();
              }

          }else {
              reDownload_ifFile_Deleted(position,Config.MESSAGE_File_ADDRESS,Config.Folders.DOCUMENTS);
          }
      }

        else {

            if (ActivityCompat.checkSelfPermission(InsideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST);
            } else {


                if (messages.get(position).getDl_state() == 0) {
                    messages.get(position).setDl_state(1);
                    adapter_message.notifyItemChanged(position);
                    download_service(position, Config.MESSAGE_File_ADDRESS, Config.Folders.DOCUMENTS);
                } else {
                    int dl_id = userdata.getDl_id(messages.get(position).getMessage_id());
                    PRDownloader.pause(dl_id);
                    messages.get(position).setDl_state(0);
                    adapter_message.notifyItemChanged(position);
                }
            }
        }

    }

    @Override
    public void file_likeClicked(int position, View view) {
        likeFunction(position);
    }

    @Override
    public void file_commentClicked(int position, View view) {
        openFragment(CommentFragment.getInstance(messages.get(position).getMessage_id(), chanel.getChanel_id()));
    }


    ///////////////////////////////////////////////////neeDedFunction\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public boolean isFileExists(String folderName, String filename) {
        File file = new File(Environment.getExternalStorageDirectory() + "/NoorAlSalehin/" + folderName, filename);
        return file.exists();
    }

    public void download_service(int position, String urlAddress, String folderAddress) {
        Intent intent = new Intent(InsideActivity.this, DownloadService.class);
        intent.putExtra(DownloadService.MESSAGE_ID, messages.get(position).getMessage_id());
        intent.putExtra(DownloadService.ADDRESS, urlAddress + messages.get(position).getUrl());
        intent.putExtra(DownloadService.POSITION, position);
        intent.putExtra(DownloadService.DIRPATH, getPath(folderAddress));
        intent.putExtra(DownloadService.FILENAME, messages.get(position).getUrl());
        Bundle bundle = new Bundle();
        bundle.putParcelable(DownloadService.MESSAGE, messages.get(position));
        intent.putExtra(DownloadService.BUNDLE, bundle);
        intent.putExtra(DownloadService.CHANEL_ID, chanel.getChanel_id());


        Gson json = new Gson();
        String message = json.toJson(messages.get(position));
        Download download = new Download( messages.get(position).getMessage_id(),urlAddress + messages.get(position).getUrl(),
                position,getPath(folderAddress),messages.get(position).getUrl(),message,chanel.getChanel_id()
                );
        userdata.insertDownload(download);

        startService(intent);
    }

    //re download for when want to oepn file and it dose not exists
    public void reDownload_ifFile_Deleted(int position,String dowmload_address,String folder) {
        Toast.makeText(getApplicationContext(), "فایل مورد نظر یافت نشد. در حال دانلود دوباره ..", Toast.LENGTH_SHORT).show();
        messages.get(position).setComplete(0);
        messages.get(position).setDl_state(0);
        userdata.setCompleteState(0,messages.get(position).getMessage_id());
        adapter_message.notifyItemChanged(position);
        messages.get(position).setDl_state(1);
        messages.get(position).setDl_percent(0);
        adapter_message.notifyItemChanged(position);
        download_service(position, dowmload_address, folder);
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
        return file.getAbsolutePath();

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

    public Message_Adapter getMessageAdapter() {
        return adapter_message;
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
    protected void onDestroy() {
        super.onDestroy();
        userdata.resetDlstate();
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    //vase baz kardane file download shode dar list
    public void openFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;
        Uri uri;
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".share", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/zip");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")
                || url.toString().contains(".mkv")
                ) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else if (url.toString().contains(".apk")) {
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "برنامه ای برای باز کردن فایل مورد نظر یافت نشد", Toast.LENGTH_LONG).show();
        }

    }


}



