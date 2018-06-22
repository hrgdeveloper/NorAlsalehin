package com.developer.hrg.noralsalehin.InsideChanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.developer.hrg.noralsalehin.Helps.ApiInterface;
import com.developer.hrg.noralsalehin.Helps.Apiclient;
import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.InternetCheck;
import com.developer.hrg.noralsalehin.Helps.MyApplication;
import com.developer.hrg.noralsalehin.Helps.SimpleResponse;
import com.developer.hrg.noralsalehin.Main.MainActivity;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsideActivity extends AppCompatActivity implements View.OnClickListener {
  TextView tv_chanelName , tv_tedad ;
    ImageView iv_thumb ;
    RecyclerView recyclerView ;
    Chanel chanel ;
    Toolbar toolbar ;
    Button btn_mute ;
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

        }else {
            int user_count = MyApplication.getInstance().getUserInfo().getUserCount();
            tv_tedad.setText("تعداد اعضا : "+ user_count);
        }

toolbar.setOnClickListener(this);
        btn_mute.setOnClickListener(this);


        updateSoundState();


    }

    public void defineViews() {
        tv_chanelName=(TextView)findViewById(R.id.tv_inside_chanelName);
        tv_tedad=(TextView)findViewById(R.id.tv_inside_users);
        iv_thumb=(ImageView) findViewById(R.id.iv_inside_thumb);
        recyclerView=(RecyclerView)findViewById(R.id.recycverview_inside);
        toolbar=(Toolbar)findViewById(R.id.toolbar_inside);
        btn_mute=(Button)findViewById(R.id.btn_inside_mute);

    }
    public void defineClass() {

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
}
