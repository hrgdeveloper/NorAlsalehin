package com.developer.hrg.noralsalehin.Helps;

/**
 * Created by hamid on 6/2/2018.
 */
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;


public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();


    private static MyApplication mInstance;
      UserInfo userInfo ;
    UserData userData ;
    SettingPref settingPref;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    public SettingPref getSettingPref() {
        if (settingPref==null) {
            settingPref=new SettingPref(this);
        }
        return settingPref;
    }


    public UserInfo getUserInfo () {
        if (userInfo==null) {
            userInfo=new UserInfo(this);
        }
        return userInfo;
    }

       public UserData getUserData() {
           if (userData==null) {
               userData=new UserData(this);
           }
           return userData;
       }
}