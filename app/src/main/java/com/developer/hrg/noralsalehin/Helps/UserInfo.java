package com.developer.hrg.noralsalehin.Helps;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hamid on 5/25/2018.
 */

public class UserInfo {

    // vase moshakhas karde inke aya user sms dade . ya logine va ...
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    public static final String IS_MOBILE_SENT = "isMobileSent";
    public static final String MOBILE_NUMBER = "mobilenumber";
    public static final String ISLOGGEDIN= "isLoggedIn";
    public static final String ISFIRSTTIME="firstTime";
    public static final String ISFIRSTTIMEMAIN="firstTimeMain";
    public static final String ISUNREAD_FETCHED="is_unread_fetched";
    public  UserInfo(Context context) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void set_isFirstTime(boolean situation) {
        editor.putBoolean(ISFIRSTTIME,situation);
        editor.apply();
    }
    public boolean get_isFirstTime() {
        return  sharedPreferences.getBoolean(ISFIRSTTIME,true);
    }


    public void set_isMobileSent(boolean situation) {
        editor.putBoolean(IS_MOBILE_SENT,situation);
        editor.apply();
    }
    public boolean get_isMobileSent() {
         return  sharedPreferences.getBoolean(IS_MOBILE_SENT,false);
    }
    public void set_IsLogged_in(boolean situation) {
        editor.putBoolean(ISLOGGEDIN,situation);
        editor.apply();
    }
    public boolean get_IsLOGGEDIN() {
        return  sharedPreferences.getBoolean(ISLOGGEDIN,false);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(MOBILE_NUMBER,mobileNumber);
        editor.apply();
    }
    public String getMobileNumber() {

        return sharedPreferences.getString(MOBILE_NUMBER,"1");
    }
   public void deletMobileNumber() {
      editor.remove(MOBILE_NUMBER);
       editor.apply();
   }
   public void set_isFirstTimeMain(boolean situtation) {
       editor.putBoolean(ISFIRSTTIMEMAIN,situtation);
       editor.apply();
   }


   public boolean get_IsFirstTimeMain()  {
       return sharedPreferences.getBoolean(ISFIRSTTIMEMAIN,true);
   }

    public void set_inUnreadFetched(boolean situtation) {
        editor.putBoolean(ISUNREAD_FETCHED,situtation);
        editor.apply();
    }


    public boolean get_inUnreadFetched()  {
        return sharedPreferences.getBoolean(ISUNREAD_FETCHED,false);
    }

}
