package com.developer.hrg.noralsalehin.Helps;

/**
 * Created by hamid on 6/2/2018.
 */
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;



public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();


    private static MyApplication mInstance;
      UserInfo userInfo ;
    UserData userData ;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
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