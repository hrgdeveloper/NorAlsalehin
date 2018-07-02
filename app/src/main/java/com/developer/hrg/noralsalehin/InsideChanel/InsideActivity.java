package com.developer.hrg.noralsalehin.InsideChanel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
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
import com.developer.hrg.noralsalehin.Main.MainActivity;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.developer.hrg.noralsalehin.Models.User;
import com.developer.hrg.noralsalehin.R;
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

public class InsideActivity extends AppCompatActivity implements View.OnClickListener,Message_Adapter.ClickListener {

    TextView tv_chanelName , tv_tedad  , tv_label;
    ImageView iv_thumb ;
    RecyclerView recyclerView ;
    Chanel chanel ;
    Toolbar toolbar ;
    Button btn_mute ;
    User user;
    private BroadcastReceiver reciverChanelsTask;
    UserData userdata ;
    ArrayList<Message> messages = new ArrayList<>();
    int last_meesage_id =1 , last_position = 0;
    Message_Adapter adapter_message ;
    ProgressBar progressBar ;
    FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);
        defineViews();
        defineClass();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        chanel=intent.getParcelableExtra("chanel");
        progressBar.setVisibility(View.VISIBLE);
        //vase inke age position recycler view save shode bood az hamoon ja biad
        if (userdata.hasPositionData(chanel.getChanel_id())) {

        }

        if (userdata.hasMessageData(chanel.getChanel_id())) {
            messages.addAll(userdata.getAllMessages(chanel.getChanel_id()));
            //vase in migirim akharin id ro ke to darkhastemoon akharin messagaiai ke bade in hastano begirim
            last_meesage_id=userdata.getLastMessage_id(chanel.getChanel_id());
        }

        tv_chanelName.setText(chanel.getName());
        Glide.with(InsideActivity.this).load(Config.CHANEL_THUMB_BASE_OFFLINE+chanel.getThumb()).apply(new RequestOptions().placeholder(R.drawable.broadcast)
        .error(R.drawable.broadcast)
        ).into(iv_thumb);
        if (InternetCheck.isOnline(InsideActivity.this)) {
            ApiInterface api = Apiclient.getClient().create(ApiInterface.class);
            Call<SimpleResponse> call = api.getUserCount();
            call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (!response.isSuccessful()) {
                        int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
                        tv_tedad.setText("تعداد اعضا : "+ user_count);
                    }else {
                        boolean error = response.body().isError();
                        if (error) {
                            int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
                            tv_tedad.setText("تعداد اعضا : "+ user_count);
                        }else {
                            int user_count = response.body().getUser_count();
                            tv_tedad.setText("تعداد اعضا : "+ user_count);
                            MyApplication.getInstance().getUserInfo().setUserCount(user_count);
                        }

                    }

                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
                    tv_tedad.setText("تعداد اعضا : "+ user_count);
                }
            });

            Call<SimpleResponse> callMessages = api.getAllMessages(user.getApikey(),chanel.getChanel_id(),1,last_meesage_id);
            callMessages.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                    if (!response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Log.e("insideActivity",message);


                            adapter_message.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }else {
                        boolean error = response.body().isError();
                        if (!error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            messages.addAll(response.body().getMessages());
                            userdata.insertIntoMessage(response.body().getMessages());
                            adapter_message.notifyDataSetChanged();
                            last_meesage_id=userdata.getLastMessage_id(chanel.getChanel_id());

                        }else {
                            if (response.body().getErrortype()==1) {
                                setNoMessage();
                                progressBar.setVisibility(View.INVISIBLE);
                                adapter_message.notifyDataSetChanged();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                adapter_message.notifyDataSetChanged();
                            }
                            Log.e("|successError",response.body().getMessage());

                        }

                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                   Log.e("failure",t.getMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                    adapter_message.notifyDataSetChanged();
                }
            });



        }else {
            int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
            tv_tedad.setText("تعداد اعضا : "+ user_count);
            progressBar.setVisibility(View.INVISIBLE);
            adapter_message.notifyDataSetChanged();
        }

        toolbar.setOnClickListener(this);
        btn_mute.setOnClickListener(this);
        updateSoundState();

        reciverChanelsTask=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               if (intent.getAction().equals(Config.PUSH_NEW_MESSAGE))
                {
                    Message message = intent.getParcelableExtra("message");

                    updateRows(message);
                }

            }
        };

         recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                 if (newState == RecyclerView.SCROLL_STATE_IDLE)
//                 {
//                     fab.show();
//                 }
//                 super.onScrollStateChanged(recyclerView,newState);
             }

             @Override
             public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                 if (dy > 0)
                 {

                     fab.hide();
                 }else if (dy<0) {
                  if (dy > -3) {
                      fab.show();
                  }

                 }
             }
         });



    }

    public void updateRows(Message message) {

            if (chanel.getChanel_id()==message.getChanel_id()) {
                messages.add(message);
                int readcount = userdata.getReadcount(chanel.getChanel_id());
                readcount++;
                userdata.updateRead(0,readcount,message.getChanel_id());
                userdata.insertIntoMessage(message);
                chanel.setUsername(message.getAdmin_name());
                chanel.setLast_message(message.getMessage());
                chanel.setType(message.getType());
                chanel.setUpdated_at(message.getUpdated_at());
                chanel.setCount(chanel.getCount()+1);
                userdata.updateChanel(chanel);
                last_meesage_id=userdata.getLastMessage_id(chanel.getChanel_id());
                adapter_message.notifyDataSetChanged();

            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(InsideActivity.this).registerReceiver(reciverChanelsTask,new IntentFilter(Config.PUSH_NEW_MESSAGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reciverChanelsTask);
    }

    public void setNoMessage() {
        if (messages.size()==0) {
            tv_label.setVisibility(View.VISIBLE);
            tv_label.setText("هیچ پیامی ثبت نشده است");
        }
    }
    public void defineViews() {

        user=MyApplication.getInstance().getUserData().getUser();
        tv_chanelName=(TextView)findViewById(R.id.tv_inside_chanelName);
        tv_label=(TextView)findViewById(R.id.tv_inside_lable);
        tv_tedad=(TextView)findViewById(R.id.tv_inside_users);
        iv_thumb=(ImageView) findViewById(R.id.iv_inside_thumb);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_messages);
         recyclerView.setLayoutManager(new LinearLayoutManager(InsideActivity.this));
        toolbar=(Toolbar)findViewById(R.id.toolbar_inside);
        btn_mute=(Button)findViewById(R.id.btn_inside_mute);
        progressBar=(ProgressBar)findViewById(R.id.myprogressbar);
        fab=(FloatingActionButton)findViewById(R.id.floating_inside);
        fab.setOnClickListener(this);


    }
    public void defineClass() {
        userdata=new UserData(InsideActivity.this);
        adapter_message=new Message_Adapter(InsideActivity.this,messages);
        adapter_message.setCliclListener(this);
        recyclerView.setAdapter(adapter_message);
    }

    public void updateSoundState() {
        if (MyApplication.getInstance().getUserData().getChanelSoundState(chanel.getChanel_id())==1)
        {
            btn_mute.setText("بیصدا کردن");

        }else {
            btn_mute.setText("روشن کردن صدا");

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home) {
          onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    //vase inke toye fragmentemoon az tarighe in method be chanel dastresi dashte bashim
    public Chanel getChanel(){
        return  chanel;
    }

    @Override
    public void onClick(View view) {
        if (view==toolbar) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_inside,new Fragment_insideToolbar());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (view==btn_mute) {
            int currentState = MyApplication.getInstance().getUserData().getChanelSoundState(chanel.getChanel_id());
            if (currentState==1) {
                currentState=0;
            }else {
                currentState = 1;
            }
            MyApplication.getInstance().getUserData().updateChanelSoundState(chanel.getChanel_id(),currentState);
            updateSoundState();
        }else if (view==fab) {
            recyclerView.scrollToPosition(messages.size() - 1);
        }
    }

    @Override
        public void imageClicked(final int position, View view, final CircularProgressBar circularProgressBar) {
        if (isFileExists(Config.Folders.IMAGES,messages.get(position).getUrl())) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_inside,Fragment_InsidePicture.newInstance(messages.get(position).getUrl(),
                    messages.get(position).getMessage()));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }else {
            ApiInterface apiInterface = Apiclient.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.downloadFileWhiturl(Config.MESSAGE_PIC_ADDRES+messages.get(position).getUrl());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        new DownloadFile(response.body(),messages.get(position).getUrl(),Config.Folders.IMAGES,circularProgressBar).execute();

                    }else {
                        try {
                            Toast.makeText(getApplicationContext(), "hal nist", Toast.LENGTH_SHORT).show();
                            MyAlert.showAlert(InsideActivity.this,"error",response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "errore", Toast.LENGTH_SHORT).show();
                    Log.e("errorTodownload",t.getMessage());
                }
            });
        }
    }
    public class DownloadFile extends AsyncTask<Void,String,String> {
        ResponseBody responsebody ;
        String filename;
        String foldername ;
        CircularProgressBar circularProgressBar ;

        public DownloadFile(ResponseBody responsebody , String fileName, String foldername, CircularProgressBar circularProgressBar) {
            this.responsebody=responsebody;
            this.filename=fileName;
            this.foldername=foldername;
            this.circularProgressBar=circularProgressBar;
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {

                File destinationFile = new File(Environment.getExternalStorageDirectory()+"/NoorAlSalehin/"+foldername ,filename);

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
                        progress +=count;
                        Log.d("imageDownload", "Progress: " + progress + "/" + responsebody.contentLength() + " >>>> " + (float) progress/responsebody.contentLength());
                        publishProgress(""+(int)((progress*100)/responsebody.contentLength()));
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
            circularProgressBar.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s=="success") {
                adapter_message.notifyDataSetChanged();
            }
        }
    }

public boolean  isFileExists (String folderName, String filename) {
    File file = new File(Environment.getExternalStorageDirectory()+"/NoorAlSalehin/"+folderName,filename);
    return file.exists();
}
}
