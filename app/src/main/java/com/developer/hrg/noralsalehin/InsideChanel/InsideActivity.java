package com.developer.hrg.noralsalehin.InsideChanel;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    TextView tv_chanelName , tv_tedad ;
    ImageView iv_thumb ;
    RecyclerView recyclerView ;
    Chanel chanel ;
    Toolbar toolbar ;
    Button btn_mute ;
    User user;
    UserData userdata ;
    ArrayList<Message> messages = new ArrayList<>();
    int last_meesage_id =1;
   Message_Adapter adapter_message ;

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
        if (userdata.hasMessageData(chanel.getChanel_id())) {
            messages.addAll(userdata.getAllMessages(chanel.getChanel_id()));
            //vase in migirim akharin id ro ke to darkhastemoon akharin messagaiai ke bade in hastano begirim
            last_meesage_id=userdata.getLastMessage_id(chanel.getChanel_id());
            adapter_message.notifyDataSetChanged();
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
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }else {
                        boolean error = response.body().isError();
                        if (!error) {
                            Toast.makeText(InsideActivity.this,response.body().getMessages().get(0).getMessage_id()+" ", Toast.LENGTH_SHORT).show();
                            messages.addAll(response.body().getMessages());
                            userdata.insertIntoMessage(response.body().getMessages());
                            adapter_message.notifyDataSetChanged();


                        }else {
                            Toast.makeText(InsideActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    MyAlert.showAlert(InsideActivity.this,"خطلا",t.getMessage());

                }
            });



        }else {
            int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
            tv_tedad.setText("تعداد اعضا : "+ user_count);
        }

        toolbar.setOnClickListener(this);
        btn_mute.setOnClickListener(this);
        updateSoundState();


    }

    public void defineViews() {

        user=MyApplication.getInstance().getUserData().getUser();
        tv_chanelName=(TextView)findViewById(R.id.tv_inside_chanelName);
        tv_tedad=(TextView)findViewById(R.id.tv_inside_users);
        iv_thumb=(ImageView) findViewById(R.id.iv_inside_thumb);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_messages);
         recyclerView.setLayoutManager(new LinearLayoutManager(InsideActivity.this));
        toolbar=(Toolbar)findViewById(R.id.toolbar_inside);
        btn_mute=(Button)findViewById(R.id.btn_inside_mute);

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
        }
    }

    @Override
        public void imageClicked(final int position, View view, final CircularProgressBar circularProgressBar) {
//        if (isFileExists()) {
//
//        }else {
            ApiInterface apiInterface = Apiclient.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.downloadFileWhiturl(Config.MESSAGE_PIC_ADDRES+messages.get(position).getUrl());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

//                    new AsyncTask<Void, Long, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... voids) {
//
//                            saveToDisk(response.body(),messages.get(position).getUrl(),"Images");
//                            return null;
//                        }
//                    }.execute();
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
     //   }






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

//
//    public void saveToDisk(ResponseBody body , String filename , String foldername) {
//        try {
//
//            File destinationFile = new File(Environment.getExternalStorageDirectory()+"/NoorAlSalehin/"+foldername ,filename);
//
//            InputStream is = null;
//            OutputStream os = null;
//
//            try {
//                Log.d("imageDownload", "File Size=" + body.contentLength());
//
//                is = body.byteStream();
//                os = new FileOutputStream(destinationFile);
//
//                byte data[] = new byte[4096];
//                int count;
//                int progress = 0;
//                while ((count = is.read(data)) != -1) {
//                    os.write(data, 0, count);
//                    progress +=count;
//                    Log.d("imageDownload", "Progress: " + progress + "/" + body.contentLength() + " >>>> " + (float) progress/body.contentLength());
//                }
//
//                os.flush();
//
//                Log.d("imageDownload", "File saved successfully!");
//
//                adapter_message.notifyDataSetChanged();
//                return;
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d("imageDownload", "Failed to save the file!");
//                return;
//            } finally {
//                if (is != null) is.close();
//                if (os != null) os.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("imageDownload", "Failed to save the file!");
//            return;
//        }
//    }
public boolean  isFileExists (String folderName, String filename) {
    File file = new File(Environment.getExternalStorageDirectory()+"/NoorAlSalehin/"+folderName,filename);
    return file.exists();
}
}
