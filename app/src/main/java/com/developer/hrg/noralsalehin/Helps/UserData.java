package com.developer.hrg.noralsalehin.Helps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.DownloadListener;

import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.Download;
import com.developer.hrg.noralsalehin.Models.DownloadBack;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.Models.Message_id;
import com.developer.hrg.noralsalehin.Models.Notify;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.developer.hrg.noralsalehin.Models.User;

import java.util.ArrayList;

/**
 * Created by hamid on 5/29/2018.
 */

public class UserData extends SQLiteOpenHelper {
    public static final String DB_NAME="user_data";
    SQLiteDatabase sqLiteDatabase;


    /////////////////////////////////////////////////USerTable\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public static final String TABLE_USER="user_table" ;
    public static final String USER_USER_ID="user_id" ;
    public static final String USER_MOBILE="mobile" ;
    public static final String USER_APIKEY="apikey" ;
    public static final String USER_USERNAME="username" ;
    public static final String USER_PIC="pic" ;
    public static final String USER_ACTIVE="active" ;
    public static final String USER_PIC_THUMB="pic_thumb" ;
    public static final String CREATED_AT="created_At" ;
    String CREATE_TABLE= "CREATE TABLE " + TABLE_USER+ " ("+USER_USER_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            +USER_MOBILE+" TEXT NOT NULL , "
            +USER_APIKEY+ " TEXT NOT NULL , " +
            USER_USERNAME+" TEXT , " +
            USER_ACTIVE+ " INTEGER NOT NULL , " +
            USER_PIC+" TEXT ,"+
            USER_PIC_THUMB+ " TEXT ,"+
            CREATED_AT+" TEXT NOT NULL )";



    /////////////////////////////////////////////////ChaneeTable\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

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
    public static final String CHANEL_COUNT="count";





    String CREATE_TABLE_CHANEL = "CREATE TABLE " + TABLE_CHANEL+"( "+ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , "+
            CHANEL_ID+ " INTEGER NOT NULL ,"+
            CHANEL_NAME+ " TEXT NOT NULL ,"+
            CHANEL_DESCRIPTION+ " TEXT NOT NULL ," +
            CHANEL_THUMB+ " TEXT  NOT NULL , " +
            CHANEL_USERNAME+ " TEXT , " +
            CHANEL_UPDATED_AT + " TEXT  ," +
            CHANEL_LAST_MESSAGE + " TEXT ," +
            CHANEL_TYPE + " INTEGER ," +
            CHANEL_COUNT+" INTEGER )";



/////////////////////////////////////////////////UnreadTable\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final String TABLE_UNREAD="unread_Table" ;
    public static final String ID_UNREAD="unread_id" ;
    public static final String UNREAD_CHANEL_ID="unread_chanel_id" ;
    public static final String UNREAD_COUNT="unread_cout" ;
    public static final String READ_COUNT="read_count" ;
    String CREATE_TABLE_UNREAD= "CREATE TABLE " + TABLE_UNREAD+ " ("+ID_UNREAD+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            +UNREAD_CHANEL_ID+" INTEGER NOT NULL , "
            +UNREAD_COUNT+ " INTEGER NOT NULL,"+
            READ_COUNT+ " INTEGER NOT NULL)";



    /////////////////////////////////////////////////NotificationTable\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public static final String TABLE_NOTIFY = "notify_table";
    public static final String NOTIFYI_D="notify_id";
    public static final String NOTIFY_CHANEL_ID="notify_chanel_id";
    public static final String NOTIFY_SHOW_NOTIFY="notify_show_notify";
    public static final String NOTIFY_PLAY_SOUND="notify_play_sound";

    String CREATE_TABLE_NOTIFY= "CREATE TABLE " + TABLE_NOTIFY+ " ("+NOTIFYI_D+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            +NOTIFY_CHANEL_ID+" INTEGER NOT NULL , "
            +NOTIFY_SHOW_NOTIFY+ " INTEGER default 1,"+
            NOTIFY_PLAY_SOUND+ " INTEGER DEFAULT 1)";

    /////////////////////////////////////////////////MessageTable\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final String TABLE_MESSAGE="message_table" ;
    public static final String MESSAGE_ID="_id" ;
    public static final String MESSAGE_MESSAGE_ID="message_id" ;
    public static final String MESSAGE_ADMIN_ID="admin_id" ;
    public static final String MESSAGE_CHANEL_ID="chanel_id" ;
    public static final String MESSAGE_MESSAGE="message";
    public static final String MESSAGE_THUMB="thumb";
    public static final String MESSAGE_TYPE="type";
    public static final String MESSAGE_LENTH="lenth";
    public static final String MESSAGE_TIME="time";
    public static final String MESSAGE_URL="url";
    public static final String MESSAGE_ACTIVE="active";
    public static final String MESSAGE_FILE_NAME="filename";
    public static final String MESSAGE_LIKED="liked";
    public static final String MESSAGE_DOWLOAD_STATE="dl_state";
    public static final String MESSAGE_DOWLOAD_ID="dl_id";
    public static final String MESSAGE_DOWNLOAD_PERCENT="dl_percent";
    public static final String MESSAGE_AUDIO_PERCENT="audio_percent";
    public static final String MESSAGE_COMPELETE="compelete";
    public static final String MESSAGE_UPDATED_AT="updated_at";

    String CREATE_TABLE_MESSAGE = "CREATE TABLE " + TABLE_MESSAGE+"( "+MESSAGE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , "+
            MESSAGE_MESSAGE_ID+ " INTEGER NOT NULL ,"+
            MESSAGE_ADMIN_ID+ " INTEGER NOT NULL ,"+
            MESSAGE_CHANEL_ID+ " INTEGER NOT NULL  ," +
            MESSAGE_MESSAGE+ " TEXT , " +
            MESSAGE_THUMB+ " TEXT , " +
            MESSAGE_TYPE + " INTEGER NOT NULL  ," +
            MESSAGE_LENTH + " INTEGER ," +
            MESSAGE_TIME + " TEXT ," +
            MESSAGE_URL+" TEXT ," +
            MESSAGE_ACTIVE+ " INTEGER DEFAULT 1 , "+
            MESSAGE_FILE_NAME+ " TEXT , " +
            MESSAGE_LIKED + " smallint ," +
            MESSAGE_DOWLOAD_STATE+ " smallint DEFAULT 0 , " +
            MESSAGE_DOWLOAD_ID+ " INTEGER DEFAULT  0 ," +
            MESSAGE_DOWNLOAD_PERCENT+ " INTEGER DEFAULT 0 ,"+
            MESSAGE_AUDIO_PERCENT+ " INTEGER DEFAULT 0 , " +
            MESSAGE_COMPELETE+ " INTEGER DEFAULT 0 ," +
            MESSAGE_UPDATED_AT + " TIMESTAMP NOT NULL)"
            ;
//////////////////////////////////////////////////////DownloadsTable\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    //in jadvade vase peygrie downloadhaie ke baz vaghti internet qato o vast shod dobare edame download bedan
public static final String TABLE_DOWNLOADS="downloads_table" ;
    public static final String DOWNLOADS_ID="download_id" ;
    public static final String DOWNLOADS_MESSAGE_ID="message_id" ;
    public static final String DOWNLOADS_ADDRESS="address" ;
    public static final String DOWNLOADS_POSITION="position" ;
    public static final String DOWNLOADS_DIRPATH="dirpath";
    public static final String DOWNLOADS_FILENAME="filename";
    public static final String DOWNLOADS_MESSAGE="message";
    public static final String DOWNLOADS_CHANEL_ID="chanel_id";

    String CREATE_TABLE_DOWNLOADS = "CREATE TABLE " + TABLE_DOWNLOADS+"( "+DOWNLOADS_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , "+
            DOWNLOADS_MESSAGE_ID+ " INTEGER NOT NULL , " +
            DOWNLOADS_ADDRESS+ " TEXT NOT NULL , " +
            DOWNLOADS_POSITION+ " INTEGER NOT NULL , " +
            DOWNLOADS_DIRPATH+ " TEXT NOT NULL , " +
            DOWNLOADS_FILENAME+ " TEXT NOT NULL , "+
            DOWNLOADS_MESSAGE+" TEXT NOT NULL , " +
            DOWNLOADS_CHANEL_ID+ " INTEGER NOT NULL ) ";

 ////////////////////////////////////////////////////backgroundDownload\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // in jadval vase sabte position itemhaie ke vaghti app to background hastan estefade mishe va baes mishe bedonim vaqti dobar app
    // lunch shod kodom yeki az itemahye adapter bayad change beshan
    public static final String TABLE_BACKGROUND_DOWNLOADS="back_downloads_table" ;
    public static final String DOWNLOADS_BACK_ID="download_back_id" ;
    public static final String DOWNLOADS_BACK_CHANEL_ID="chanel_id" ;
    public static final String DOWNLOADS_BACK_POSITION="position";


    String CREATE_TABLE_DOWNLOADS_BACK = "CREATE TABLE " + TABLE_BACKGROUND_DOWNLOADS+"( "+DOWNLOADS_BACK_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , "+
            DOWNLOADS_BACK_CHANEL_ID+ " INTEGER NOT NULL , " +
            DOWNLOADS_BACK_POSITION+ " INTEGER NOT NULL )";


//////////////////////////////////////////////PositionTable\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public static final String TABLE_POSITION = "position_table";
    public static final String POSITION_ID="_id";
    public static final String POSITION_CHANEL_ID="position_chanel_id";
    public static final String POSITION_NUMBER="position_number";

    String CREATE_TABLE_POSITION= "CREATE TABLE " + TABLE_POSITION+ " ("+POSITION_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            +POSITION_CHANEL_ID+" INTEGER NOT NULL , "
            +POSITION_NUMBER+ " INTEGER )" ;



    public UserData(Context context) {
        super(context, DB_NAME, null, 4);

        sqLiteDatabase=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CHANEL);
        sqLiteDatabase.execSQL(CREATE_TABLE_UNREAD);
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTIFY);
        sqLiteDatabase.execSQL(CREATE_TABLE_MESSAGE);
        sqLiteDatabase.execSQL(CREATE_TABLE_POSITION);
        sqLiteDatabase.execSQL(CREATE_TABLE_DOWNLOADS);
        sqLiteDatabase.execSQL(CREATE_TABLE_DOWNLOADS_BACK);

        sqLiteDatabase.execSQL("CREATE INDEX message_id_index ON " +TABLE_MESSAGE +  " ("+MESSAGE_MESSAGE_ID+");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANEL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_UNREAD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITION);
        onCreate(sqLiteDatabase);

    }
    /////////////////////////////////////////////////////////////DownloadsFunction\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public Long insertDownload(Download download) {
         ContentValues contentValues = new ContentValues();
         contentValues.put(DOWNLOADS_MESSAGE_ID,download.getMessage_id());
         contentValues.put(DOWNLOADS_ADDRESS,download.getAddress());
         contentValues.put(DOWNLOADS_POSITION,download.getPosition());
         contentValues.put(DOWNLOADS_DIRPATH,download.getDirpath());
         contentValues.put(DOWNLOADS_FILENAME,download.getFilename());
         contentValues.put(DOWNLOADS_MESSAGE,download.getMessage());
         contentValues.put(DOWNLOADS_CHANEL_ID,download.getChanel_id());
     return    sqLiteDatabase.insert(TABLE_DOWNLOADS,null,contentValues);

     }
    public boolean hasDownloadsData() {
        long cnt  = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_DOWNLOADS);
        return cnt > 0;
    }
    public ArrayList<Download> getDownloads() {
        ArrayList<Download> downloads = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_DOWNLOADS,new String [] {DOWNLOADS_MESSAGE_ID,DOWNLOADS_ADDRESS,DOWNLOADS_POSITION
        ,DOWNLOADS_DIRPATH,DOWNLOADS_FILENAME,DOWNLOADS_MESSAGE,DOWNLOADS_CHANEL_ID
        },null,null,null,null,null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                int message_id = cursor.getInt(cursor.getColumnIndexOrThrow(DOWNLOADS_MESSAGE_ID));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DOWNLOADS_ADDRESS));
                int position = cursor.getInt(cursor.getColumnIndexOrThrow(DOWNLOADS_POSITION));
                String  dirpath = cursor.getString(cursor.getColumnIndexOrThrow(DOWNLOADS_DIRPATH));
                String filename = cursor.getString(cursor.getColumnIndexOrThrow(DOWNLOADS_FILENAME));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(DOWNLOADS_MESSAGE));
                int  chanel_id = cursor.getInt(cursor.getColumnIndexOrThrow(DOWNLOADS_CHANEL_ID));
                Download download = new Download(message_id,address,position,dirpath,filename,message,chanel_id);
                downloads.add(download);
            }while (cursor.moveToNext());
            return downloads;
        }else {
            return  null ;
        }

    }
    public Integer deleteSingleDownload(int message_id) {
        return sqLiteDatabase.delete(TABLE_DOWNLOADS,message_id+ " LIKE ? ",new String[]{String.valueOf(message_id)});
    }
    public Integer deleteDownloas(){
        return sqLiteDatabase.delete(TABLE_DOWNLOADS,null,null);
    }
///////////////////////////////////////////////////////////////DownloadBackFunctions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


    public Long insertDownloadBACK(DownloadBack downloadBack) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DOWNLOADS_BACK_CHANEL_ID,downloadBack.getChanel_id());
        contentValues.put(DOWNLOADS_BACK_POSITION,downloadBack.getPosition());
        return    sqLiteDatabase.insert(TABLE_BACKGROUND_DOWNLOADS,null,contentValues);
    }
    public boolean hasDownloadsBACKData() {
        long cnt  = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_BACKGROUND_DOWNLOADS);
        return cnt > 0;
    }
    public ArrayList<DownloadBack> getDownloadsBacks() {
        ArrayList<DownloadBack> downloadsBacks = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_BACKGROUND_DOWNLOADS,new String [] {DOWNLOADS_BACK_CHANEL_ID,DOWNLOADS_BACK_POSITION},null,null,null,null,null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                int position = cursor.getInt(cursor.getColumnIndexOrThrow(DOWNLOADS_BACK_POSITION));
                int  chanel_id = cursor.getInt(cursor.getColumnIndexOrThrow(DOWNLOADS_BACK_CHANEL_ID));
                DownloadBack downloadback = new DownloadBack(chanel_id,position);
                downloadsBacks.add(downloadback);
            }while (cursor.moveToNext());
            return downloadsBacks;
        }else {
            return  null ;
        }

    }
    public Integer deleteDownloasBack(){
        return sqLiteDatabase.delete(TABLE_BACKGROUND_DOWNLOADS,null,null);
    }
    ////////////////////////////////////////////////////////////////USerFunctions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void addUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USER_ID,user.getId());
        contentValues.put(USER_MOBILE,user.getMobile());
        contentValues.put(USER_APIKEY,user.getApikey());
        contentValues.put(USER_USERNAME,user.getUsername());
        contentValues.put(USER_PIC,user.getPic());
        contentValues.put(USER_ACTIVE,user.getActive());
        contentValues.put(USER_PIC_THUMB,user.getPic_thumb());
        contentValues.put(CREATED_AT,user.getCreated_at());
        sqLiteDatabase.insert(TABLE_USER,null,contentValues);

    }
    public User getUser() {
        Cursor cursor = sqLiteDatabase.query(TABLE_USER,new String[]{USER_USER_ID,USER_MOBILE,USER_APIKEY,USER_USERNAME,USER_ACTIVE,
                USER_PIC,USER_PIC_THUMB, CREATED_AT},null,null,null,null,null);
        cursor.moveToFirst();
        User user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(USER_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(USER_MOBILE)),
                cursor.getString(cursor.getColumnIndexOrThrow(USER_APIKEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(CREATED_AT)
                ),cursor.getString(cursor.getColumnIndexOrThrow(USER_USERNAME)
                ),cursor.getString(cursor.getColumnIndexOrThrow(USER_PIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(USER_PIC_THUMB)
                ),cursor.getInt(cursor.getColumnIndexOrThrow(USER_ACTIVE))
                );

        return user;
    }
    public void deleteuser() {
        sqLiteDatabase.delete(TABLE_USER,null,null);
    }
    public boolean hasUserData() {
        Cursor cursor = sqLiteDatabase.query(TABLE_USER, new String[]{USER_USER_ID}, null, null, null, null, null);
        return cursor.getCount() > 0;

    }
    public void  updateUsername(String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME,username);
        sqLiteDatabase.update(TABLE_USER,contentValues,null,null);
    }
    public void  updatePicAndThumb(String pic) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_PIC,pic);
        contentValues.put(USER_PIC_THUMB,pic);
        sqLiteDatabase.update(TABLE_USER,contentValues,null,null);
    }

    //////////////////////////////////////////////////////////ChanelFUNCTIONs\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

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
            contentValues.put(CHANEL_COUNT,chanel.getCount());
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
            contentValues.put(CHANEL_COUNT,chanel.getCount());
            sqLiteDatabase.insert(TABLE_CHANEL,null,contentValues);
        }
    }
    public String getChanelNameByid(int chanel_id){
         Cursor cursor = sqLiteDatabase.query(TABLE_CHANEL,new String[]{CHANEL_NAME},CHANEL_ID+" LIKE ? " , new String[]{String.valueOf(chanel_id)},null,
                 null,null);
         cursor.moveToFirst();
         return  cursor.getString(cursor.getColumnIndexOrThrow(CHANEL_NAME));

     }
    public void updateChanel(Chanel chanel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHANEL_USERNAME,chanel.getUsername());
        contentValues.put(CHANEL_LAST_MESSAGE,chanel.getLast_message());
        contentValues.put(CHANEL_UPDATED_AT,chanel.getUpdated_at());
        contentValues.put(CHANEL_TYPE,chanel.getType());
        contentValues.put(CHANEL_COUNT,chanel.getCount());
        sqLiteDatabase.update(TABLE_CHANEL,contentValues,CHANEL_ID+" LIKE ? ", new String[]{String.valueOf(chanel.getChanel_id())});

        }
    public boolean hasChanelsData() {

        long cnt  = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_CHANEL);
        return cnt > 0;
    }
    public ArrayList<Chanel> getAllChanels() {
        ArrayList<Chanel> chanels = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_CHANEL,new String [] {CHANEL_ID,CHANEL_DESCRIPTION,CHANEL_NAME,CHANEL_USERNAME,
                CHANEL_THUMB, CHANEL_LAST_MESSAGE,CHANEL_UPDATED_AT,CHANEL_TYPE,CHANEL_COUNT },null,null,null,null,null);
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
                Integer count  = cursor.getInt(cursor.getColumnIndexOrThrow(CHANEL_COUNT));

                Chanel chanel = new Chanel(chanel_id,name,description,thumb,updated_at,username,last_message,type,count);
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


    ///////////////////////////////////////////////UnreadFunctions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public boolean hasUnreadData() {
        long cnt  = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_UNREAD);
        return cnt > 0;

    }
    public void addUnread(UnRead unread) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNREAD_CHANEL_ID,unread.getChanel_id());
        contentValues.put(UNREAD_COUNT,unread.getCount());
        contentValues.put(READ_COUNT,unread.getReadCount());
        sqLiteDatabase.insert(TABLE_UNREAD,null,contentValues);
    }
    public void addUnReads(ArrayList<UnRead> unReads) {
        for (UnRead unread : unReads) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(UNREAD_CHANEL_ID,unread.getChanel_id());
            contentValues.put(UNREAD_COUNT,unread.getCount());
            contentValues.put(READ_COUNT,unread.getReadCount());
            sqLiteDatabase.insert(TABLE_UNREAD,null,contentValues);
        }

    }
    public ArrayList<UnRead> getAllunReads() {
        ArrayList<UnRead> unReads = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_UNREAD,new String [] {UNREAD_CHANEL_ID,UNREAD_COUNT,READ_COUNT},null,null,null,null,null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                int chanel_id = cursor.getInt(cursor.getColumnIndexOrThrow(UNREAD_CHANEL_ID));
                int count  = cursor.getInt(cursor.getColumnIndexOrThrow(UNREAD_COUNT));
                int readCount = cursor.getInt(cursor.getColumnIndexOrThrow(READ_COUNT));
                UnRead unRead = new UnRead(chanel_id,count,readCount);
                unReads.add(unRead);
            }while (cursor.moveToNext());
            return unReads;
        }else {
            return  null ;
        }
    }
    public void updateUnread(int count , int chanel_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNREAD_COUNT,count);
        sqLiteDatabase.update(TABLE_UNREAD,contentValues,UNREAD_CHANEL_ID+ " LIKE ? " ,new String[]{String.valueOf(chanel_id)});
    }
    public int getUnreadCount(int chanel_id) {

        Cursor cursor = sqLiteDatabase.query(TABLE_UNREAD,new String[]{UNREAD_COUNT},UNREAD_CHANEL_ID+ " LIKE ? ",new String[]{String.valueOf(chanel_id)},
                null,null,null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndexOrThrow(UNREAD_COUNT));
        return count;
    }
    public int getReadcount(int chanel_id) {
        Cursor cursor = sqLiteDatabase.query(TABLE_UNREAD,new String[]{READ_COUNT},UNREAD_CHANEL_ID+ " LIKE ? ",new String[]{String.valueOf(chanel_id)},
                null,null,null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndexOrThrow(READ_COUNT));
        return count;
    }
    public void updateRead(int unReadCount , int readCount , int chanel_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(READ_COUNT,unReadCount + readCount);
        contentValues.put(UNREAD_COUNT,0);
        sqLiteDatabase.update(TABLE_UNREAD,contentValues,UNREAD_CHANEL_ID+ " LIKE ? " ,new String[]{String.valueOf(chanel_id)});
    }
    public void updateReadForDelete(int count , int chanel_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(READ_COUNT,count);
        sqLiteDatabase.update(TABLE_UNREAD,contentValues,UNREAD_CHANEL_ID+ " LIKE ? " ,new String[]{String.valueOf(chanel_id)});
    }


/////////////////////////////////////////////////////////////////NotifyFunctions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void addNotify(Notify notify) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFY_CHANEL_ID,notify.getChanel_id());
        contentValues.put(NOTIFY_SHOW_NOTIFY,notify.getShow_notify());
        contentValues.put(NOTIFY_PLAY_SOUND,notify.getPlay_sound());
        sqLiteDatabase.insert(TABLE_NOTIFY,null,contentValues);
    }
    public boolean hasNotifyData() {
        long cnt  = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NOTIFY);
        return cnt > 0;

    }
    public int updateChanelnotifyState (int chanel_id , int state) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFY_SHOW_NOTIFY,state);
        return sqLiteDatabase.update(TABLE_NOTIFY,contentValues,NOTIFY_CHANEL_ID+" LIKE ? ", new String[]{String.valueOf(chanel_id)});
    }
    public int updateChanelSoundState (int chanel_id , int state) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFY_PLAY_SOUND,state);
        return sqLiteDatabase.update(TABLE_NOTIFY,contentValues,NOTIFY_CHANEL_ID+" LIKE ? ", new String[]{String.valueOf(chanel_id)});
    }
    public Integer getChanelNotifyState(int chanel_id) {
        Cursor cursor = sqLiteDatabase.query(TABLE_NOTIFY,new String[] {NOTIFY_SHOW_NOTIFY},NOTIFY_CHANEL_ID+" LIKE ? ",new String[]{String.valueOf(chanel_id)} ,
                null,null,null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndexOrThrow(NOTIFY_SHOW_NOTIFY));

    }
    public Integer getChanelSoundState(int chanel_id) {
        Cursor cursor = sqLiteDatabase.query(TABLE_NOTIFY,new String[] {NOTIFY_PLAY_SOUND},NOTIFY_CHANEL_ID+" LIKE ? ",new String[]{String.valueOf(chanel_id)} ,
                null,null,null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndexOrThrow(NOTIFY_PLAY_SOUND));

    }

/////////////////////////////////////////////////////////////////MessageFunction\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void insertIntoMessage(Message message) {
        ContentValues contentValues =new ContentValues();
        contentValues.put(MESSAGE_MESSAGE_ID,message.getMessage_id());
        contentValues.put(MESSAGE_ADMIN_ID,message.getAdmin_id());
        contentValues.put(MESSAGE_CHANEL_ID,message.getChanel_id());
        contentValues.put(MESSAGE_MESSAGE,message.getMessage());
        contentValues.put(MESSAGE_THUMB,message.getThumb());
        contentValues.put(MESSAGE_TYPE,message.getType());
        contentValues.put(MESSAGE_LENTH,message.getLenth());
        contentValues.put(MESSAGE_TIME,message.getTime());
        contentValues.put(MESSAGE_URL,message.getUrl());
        contentValues.put(MESSAGE_FILE_NAME,message.getFilename());
        contentValues.put(MESSAGE_LIKED,message.getLiked()==0 ? 0 : 1);
        contentValues.put(MESSAGE_UPDATED_AT,message.getUpdated_at());
        sqLiteDatabase.insert(TABLE_MESSAGE,null,contentValues);
    }
    public void insertIntoMessage(ArrayList<Message> messages) {
        for (Message message : messages) {
            ContentValues contentValues =new ContentValues();
            contentValues.put(MESSAGE_MESSAGE_ID,message.getMessage_id());
            contentValues.put(MESSAGE_ADMIN_ID,message.getAdmin_id());
            contentValues.put(MESSAGE_CHANEL_ID,message.getChanel_id());
            contentValues.put(MESSAGE_MESSAGE,message.getMessage());
            contentValues.put(MESSAGE_THUMB,message.getThumb());
            contentValues.put(MESSAGE_TYPE,message.getType());
            contentValues.put(MESSAGE_LENTH,message.getLenth());
            contentValues.put(MESSAGE_TIME,message.getTime());
            contentValues.put(MESSAGE_URL,message.getUrl());
            contentValues.put(MESSAGE_FILE_NAME,message.getFilename());
            contentValues.put(MESSAGE_LIKED,message.getLiked()==0 ? 0 : 1);
            contentValues.put(MESSAGE_UPDATED_AT,message.getUpdated_at());
            sqLiteDatabase.insert(TABLE_MESSAGE,null,contentValues);
        }
    }
    public boolean hasMessageData(int chanel_id) {
        long cnt  = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_MESSAGE , MESSAGE_CHANEL_ID+" LIKE ? " ,new String[] {String.valueOf(chanel_id)});
        return cnt > 0;
    }
    public ArrayList<Message> getAllMessages(int chanel_id)  {
       ArrayList<Message> messages = new ArrayList<>();

        Cursor cursor =    sqLiteDatabase.rawQuery("select sub."+MESSAGE_MESSAGE_ID+" , sub."+MESSAGE_ADMIN_ID+", sub."+MESSAGE_CHANEL_ID+" ,\n" +
                "                sub."+MESSAGE_MESSAGE+" , sub."+MESSAGE_TYPE+",sub."+MESSAGE_THUMB+",sub."+MESSAGE_LENTH+"" +
                ",sub."+MESSAGE_TIME+",sub."+MESSAGE_FILE_NAME+",sub."+MESSAGE_URL+",sub."+MESSAGE_UPDATED_AT+" ,sub."+MESSAGE_DOWLOAD_STATE+
             " ,sub."+MESSAGE_DOWNLOAD_PERCENT  +  ", sub."+MESSAGE_DOWLOAD_ID+ " , sub."+MESSAGE_AUDIO_PERCENT +" ,sub."+MESSAGE_COMPELETE+ " , sub."+MESSAGE_LIKED+" from\n" +
                "                (select * from " +  TABLE_MESSAGE + " where " + MESSAGE_CHANEL_ID + " like "+ chanel_id+ " AND " +MESSAGE_ACTIVE+" = 1"+
                "  ORDER by "  +  MESSAGE_MESSAGE_ID+ " DESC) sub\n" +
                "         order by sub." + MESSAGE_MESSAGE_ID+" ASC",null,null);

//        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGE,new String[]{MESSAGE_MESSAGE_ID,MESSAGE_ADMIN_ID,MESSAGE_CHANEL_ID,MESSAGE_MESSAGE
//                ,MESSAGE_THUMB,MESSAGE_TYPE,MESSAGE_LENTH,MESSAGE_TIME,MESSAGE_URL,MESSAGE_LIKED, MESSAGE_UPDATED_AT},MESSAGE_CHANEL_ID+" LIKE ? ",
//                new String[] {String.valueOf(chanel_id)},null,null,null,null
//
//                );
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                int message_id = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_MESSAGE_ID));
                int admin_id = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_ADMIN_ID));
                int chanel_id_temp = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_CHANEL_ID));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_MESSAGE));
                String thumb = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_THUMB));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_TYPE));
                Long lenth = cursor.getLong(cursor.getColumnIndexOrThrow(MESSAGE_LENTH));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_TIME));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_URL));
                String filename = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_FILE_NAME));
                int liked = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_LIKED));
                String updated_at  = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_UPDATED_AT));
                Integer dl_state = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_DOWLOAD_STATE));
                Integer dl_id = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_DOWLOAD_ID));
                Integer dl_percent = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_DOWNLOAD_PERCENT));
                Integer audio_percent = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_AUDIO_PERCENT));
                Integer complete = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_COMPELETE));
                Message message_temp = new Message(message_id,admin_id,chanel_id_temp,message,thumb,type,lenth,time,filename,
                        url ,updated_at,liked,dl_state,dl_id,dl_percent,audio_percent,complete);
                messages.add(message_temp);
            }while (cursor.moveToNext());
            return messages;
        }else {
            return  null;
        }

    }
    public  int getLastMessage_id(int chanel_id) {
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGE,new String[]{MESSAGE_MESSAGE_ID},MESSAGE_CHANEL_ID+" LIKE ? AND " +MESSAGE_ACTIVE
                +" = 1"
                ,new String[]{String.valueOf(chanel_id)},null
                ,null,MESSAGE_MESSAGE_ID+" DESC","0,1"
                );
        cursor.moveToFirst();
        return  cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_MESSAGE_ID));
    }
    public void setDlState(int dl_state,int message_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_DOWLOAD_STATE,dl_state);
        sqLiteDatabase.update(TABLE_MESSAGE,contentValues,MESSAGE_MESSAGE_ID+" LIKE ? " , new String[]{String.valueOf(message_id)});
    }
    public int setDownloaadId(int dl_id,int message_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_DOWLOAD_ID , dl_id);
   return         sqLiteDatabase.update(TABLE_MESSAGE,contentValues,MESSAGE_MESSAGE_ID+" LIKE ? " , new String[]{String.valueOf(message_id)});

    }
    public int getDl_id(int message_id) {
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGE,new String[] {MESSAGE_DOWLOAD_ID},MESSAGE_MESSAGE_ID+" like ? " ,new String[]{String.valueOf(message_id)
        },null,null,null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            int dl_state = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_DOWLOAD_ID));
            Log.e("download_id", dl_state+" " + "cheraaaa ");
            Log.e("download_id",message_id+ "message to get " );

            return dl_state;
        }else {
            return  0 ;
        }


    }
    public void resetDlstate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_DOWLOAD_STATE,0);
        sqLiteDatabase.update(TABLE_MESSAGE,contentValues,null,null);
    }
    public void setCompleteState(int state , int message_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_COMPELETE,state);
        sqLiteDatabase.update(TABLE_MESSAGE,contentValues,MESSAGE_MESSAGE_ID+" LIKE ? " , new String[]{String.valueOf(message_id)});

    }
    public void setLikeState(int likeState,int message_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_LIKED,likeState);
        sqLiteDatabase.update(TABLE_MESSAGE,contentValues,MESSAGE_MESSAGE_ID+" LIKE ? " , new String[]{String.valueOf(message_id)});
    }
    public void updateMessageActive(int message_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_ACTIVE,0);
        sqLiteDatabase.update(TABLE_MESSAGE,contentValues,MESSAGE_MESSAGE_ID+" LIKE ? " , new String[]{String.valueOf(message_id)});

    }
    public Integer updateMessageActives(ArrayList<Integer> idis) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_ACTIVE,0);
        String all_id = TextUtils.join(", ", idis);
        String Where=  MESSAGE_MESSAGE_ID+" IN "+"(" +all_id+")" ;
        return    sqLiteDatabase.update(TABLE_MESSAGE,contentValues, Where,null);
    }
//    public Integer UpdateGroups(int user_id,ArrayList<Integer> ids,String name){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(GP,name);
//        String all_id = TextUtils.join(", ", ids);
//        String Where=  WORD_ID+" IN "+"(" +all_id+")"+  " AND "+USER_ID_FOREIGN+" LIKE "+user_id;
//        Log.e("hamiiiiid",Where);
//        return    sqLiteDatabase.update(WORD_TABLE,contentValues, Where,null);
//
//
//
//    }

    ////////////////////////////////////////////////////////////////positionFunction\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  //  check mikonim mibinim age dash ke update she age nadasht besaze
    public Long addPosition(int chanel_id , int position , boolean updateshe) {
        if (!updateshe) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(POSITION_NUMBER,position);
            contentValues.put(POSITION_CHANEL_ID,chanel_id);
      return       sqLiteDatabase.insert(TABLE_POSITION,null,contentValues);
        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(POSITION_NUMBER,position);
       int temp =      sqLiteDatabase.update(TABLE_POSITION,contentValues,POSITION_CHANEL_ID+
            " LIKE ? " ,new String[]{String.valueOf(chanel_id)}
            );

            return Long.valueOf(temp);

        }

    }
    public boolean hasPositionData(int chanel_id) {
        long cnt  = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_POSITION,
                POSITION_CHANEL_ID+ " LIKE ?" ,new String[]{String.valueOf(chanel_id)}
                );
        return cnt > 0;
    }
    public Integer getLastPosition(int chanel_id) {
        Cursor cursor = sqLiteDatabase.query(TABLE_POSITION,new String[]{POSITION_NUMBER},
                POSITION_CHANEL_ID+ " LIKE ? ",new String[]{String.valueOf(chanel_id)},
                null,null,null
                );
        cursor.moveToFirst();
        return  cursor.getInt(cursor.getColumnIndexOrThrow(POSITION_NUMBER));
    }

}
