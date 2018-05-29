package com.developer.hrg.noralsalehin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.developer.hrg.noralsalehin.Helps.UserInfo;
import com.developer.hrg.noralsalehin.Main.MainActivity;
import com.developer.hrg.noralsalehin.SmsHandeling.SmsActivity;

public class SplashActivity extends AppCompatActivity {
   ImageView imageView;
    boolean isloggedin ;
    UserInfo userInfo ;
    boolean isfirst_time , isIsloggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userInfo=new UserInfo(SplashActivity.this);

        imageView=(ImageView)findViewById(R.id.iv_splash);
        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.splash_anim);
        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isfirst_time) {
                    startActivity(new Intent(SplashActivity.this,Intero_Activity.class));
                    finish();
                }else if (!isloggedin) {
                    startActivity(new Intent(SplashActivity.this,SmsActivity.class));
                    finish();
                }else if (!isfirst_time && isloggedin) {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isfirst_time=userInfo.get_isFirstTime();
        isloggedin=userInfo.get_IsLOGGEDIN();

    }
}
