package com.developer.hrg.noralsalehin.Helps;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hamid on 8/9/2018.
 */

public class SettingPref {
    public static final String SHARED_TYPE="setting";
    public static final String TEXT_SIZE="setting";
    Context context ;

    SharedPreferences sharedPreferences ;
     SharedPreferences.Editor editor;

    public SettingPref(Context context) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(SHARED_TYPE,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void setTextSize(int size) {
        editor.putInt(TEXT_SIZE,size);
        editor.apply();

    }
    public int getTextSize() {
    return    sharedPreferences.getInt(TEXT_SIZE,14);
    }

}
