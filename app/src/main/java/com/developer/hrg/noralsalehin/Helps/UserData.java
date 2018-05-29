package com.developer.hrg.noralsalehin.Helps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developer.hrg.noralsalehin.Models.User;

/**
 * Created by hamid on 5/29/2018.
 */

public class UserData extends SQLiteOpenHelper {
    public static final String DB_NAME="user_data";
    SQLiteDatabase sqLiteDatabase;
    public static final String TABLE_USER="user_table" ;
    public static final String USER_ID="user_id" ;
    public static final String MOBILE="mobile" ;
    public static final String APIKEY="apikey" ;
    public static final String CREATED_AT="created_At" ;
    String CREATE_TABLE= "CREATE TABLE " + TABLE_USER+ " ("+USER_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            +MOBILE+" TEXT NOT NULL , "
            +APIKEY+ "TEXT NOT NULL , " +
            CREATED_AT+" TEXT NOT NULL )";

    public UserData(Context context) {
        super(context, DB_NAME, null, 1);
        sqLiteDatabase=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(sqLiteDatabase);

    }
    public void addUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID,user.getId());
        contentValues.put(MOBILE,user.getMobile());
        contentValues.put(APIKEY,user.getApikey());
        contentValues.put(CREATED_AT,user.getCreated_at());
        sqLiteDatabase.insert(TABLE_USER,null,contentValues);

    }
    public User getUser() {
        Cursor cursor = sqLiteDatabase.query(TABLE_USER,new String[]{USER_ID,MOBILE,APIKEY,CREATED_AT},null,null,null,null,null);
        cursor.moveToFirst();
        User user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(MOBILE)),
                cursor.getString(cursor.getColumnIndexOrThrow(APIKEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(CREATE_TABLE))
                );
        return user;
    }
    public void deleteuser() {
        sqLiteDatabase.delete(TABLE_USER,null,null);
    }
    public boolean hasUserData(){
        Cursor cursor =   sqLiteDatabase.query(TABLE_USER,new String[] {USER_ID},null,null,null,null,null);
        return  cursor.getCount() > 0 ;

    }
}
