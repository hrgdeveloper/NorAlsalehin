package com.developer.hrg.noralsalehin.Helps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.developer.hrg.noralsalehin.Models.User;

import java.util.ArrayList;

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
            +APIKEY+ " TEXT NOT NULL , " +
            CREATED_AT+" TEXT NOT NULL )";


    public static final String TABLE_CHANEL="Chanel_table" ;
    public static final String ID="_id" ;
    public static final String CHANEL_ID="chanel_d" ;
    public static final String CHANEL_NAME="chanel_name" ;
    public static final String CHANEL_DESCRIPTION="chanel_descrition" ;
    public static final String CHANEL_THUMB="chanel_thumb";
    public static final String CHANEL_USERNAME="chanel_username";
    public static final String CHANEL_UPDATED_AT="updated_at";
    public static final String CHANEL_LAST_MESSAGE="last_message";
    public static final String CHANEL_TYPE="type";
    String CREATE_TABLE_CHANEL = "CREATE TABLE " + TABLE_CHANEL+"( "+ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , "+
            CHANEL_ID+ " INTEGER NOT NULL ,"+
            CHANEL_NAME+ " TEXT NOT NULL ,"+
            CHANEL_DESCRIPTION+ " TEXT NOT NULL ," +
            CHANEL_THUMB+ " TEXT  NOT NULL , " +
            CHANEL_USERNAME+ " TEXT NOT NULL , " +
            CHANEL_UPDATED_AT + " TEXT  ," +
            CHANEL_LAST_MESSAGE + " TEXT ," +
            CHANEL_TYPE + " INTEGER  )";

    public static final String TABLE_UNREAD="unread_Table" ;
    public static final String ID_UNREAD="unread_id" ;
    public static final String UNREAD_CHANEL_ID="unread_chanel_id" ;
    public static final String UNREAD_COUNT="unread_cout" ;

    String CREATE_TABLE_UNREAD= "CREATE TABLE " + TABLE_UNREAD+ " ("+ID_UNREAD+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            +UNREAD_CHANEL_ID+" INTEGER NOT NULL , "
            +UNREAD_COUNT+ " INTEGER NOT NULL)" ;




    public UserData(Context context) {
        super(context, DB_NAME, null, 2);
        sqLiteDatabase=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CHANEL);
        sqLiteDatabase.execSQL(CREATE_TABLE_UNREAD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANEL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_UNREAD);
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
                cursor.getString(cursor.getColumnIndexOrThrow(CREATED_AT))
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

    public void addChanel(Chanel chanel) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(CHANEL_ID,chanel.getChanel_id());
            contentValues.put(CHANEL_DESCRIPTION,chanel.getDescription());
            contentValues.put(CHANEL_NAME,chanel.getName());
            contentValues.put(CHANEL_USERNAME,chanel.getUsername());
            contentValues.put(CHANEL_THUMB,chanel.getThumb());
            contentValues.put(CHANEL_LAST_MESSAGE,chanel.getLast_message());
            contentValues.put(CHANEL_UPDATED_AT,chanel.getUpdated_at());
            contentValues.put(CHANEL_TYPE,chanel.getType());
            sqLiteDatabase.insert(TABLE_CHANEL,null,contentValues);


    }

    public void addChanels(ArrayList<Chanel> chanels) {
        for (Chanel chanel : chanels) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CHANEL_ID,chanel.getChanel_id());
            contentValues.put(CHANEL_DESCRIPTION,chanel.getDescription());
            contentValues.put(CHANEL_NAME,chanel.getName());
            contentValues.put(CHANEL_USERNAME,chanel.getUsername());
            contentValues.put(CHANEL_THUMB,chanel.getThumb());
            contentValues.put(CHANEL_LAST_MESSAGE,chanel.getLast_message());
            contentValues.put(CHANEL_UPDATED_AT,chanel.getUpdated_at());
            contentValues.put(CHANEL_TYPE,chanel.getType());


            sqLiteDatabase.insert(TABLE_CHANEL,null,contentValues);
        }

    }
    public ArrayList<Chanel> getAllChanels() {
        ArrayList<Chanel> chanels = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_CHANEL,new String [] {CHANEL_ID,CHANEL_DESCRIPTION,CHANEL_NAME,CHANEL_USERNAME,
                CHANEL_THUMB, CHANEL_LAST_MESSAGE,CHANEL_UPDATED_AT,CHANEL_TYPE },null,null,null,null,null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                int chanel_id = cursor.getInt(cursor.getColumnIndexOrThrow(CHANEL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CHANEL_NAME));
                String last_message =cursor.getString(cursor.getColumnIndexOrThrow(CHANEL_LAST_MESSAGE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(CHANEL_DESCRIPTION));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(CHANEL_USERNAME));
                String updated_at = cursor.getString(cursor.getColumnIndexOrThrow(CHANEL_UPDATED_AT));
                String thumb = cursor.getString(cursor.getColumnIndexOrThrow(CHANEL_THUMB));
                Integer type  = cursor.getInt(cursor.getColumnIndexOrThrow(CHANEL_TYPE));


                Chanel chanel = new Chanel(chanel_id,name,description,thumb,updated_at,username,last_message,type);
                chanels.add(chanel);
            }while (cursor.moveToNext());
           return chanels;
        }else {
            return  null ;
        }
    }
    public void deleteChanels(){
        sqLiteDatabase.delete(TABLE_CHANEL,null,null);
    }



    public void addUnread(UnRead unread) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(UNREAD_CHANEL_ID,unread.getChanel_id());
        contentValues.put(UNREAD_COUNT,unread.getCount());
        sqLiteDatabase.insert(TABLE_UNREAD,null,contentValues);


    }

    public void addUnReads(ArrayList<UnRead> unReads) {
        for (UnRead unread : unReads) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(UNREAD_CHANEL_ID,unread.getChanel_id());
            contentValues.put(UNREAD_COUNT,unread.getCount());
            sqLiteDatabase.insert(TABLE_UNREAD,null,contentValues);
        }

    }
    public ArrayList<UnRead> getAllunReads() {
        ArrayList<UnRead> unReads = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_UNREAD,new String [] {UNREAD_CHANEL_ID,UNREAD_COUNT},null,null,null,null,null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                int chanel_id = cursor.getInt(cursor.getColumnIndexOrThrow(UNREAD_CHANEL_ID));
                int count  = cursor.getInt(cursor.getColumnIndexOrThrow(UNREAD_COUNT));
                UnRead unRead = new UnRead(chanel_id,count);
                unReads.add(unRead);
            }while (cursor.moveToNext());
            return unReads;
        }else {
            return  null ;
        }
    }
}
