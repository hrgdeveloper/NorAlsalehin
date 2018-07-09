package com.developer.hrg.noralsalehin.Helps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.Message;
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
    public static final String USER_PIC_THUMB="pic_thumb" ;
    public static final String CREATED_AT="created_At" ;
    String CREATE_TABLE= "CREATE TABLE " + TABLE_USER+ " ("+USER_USER_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            +USER_MOBILE+" TEXT NOT NULL , "
            +USER_APIKEY+ " TEXT NOT NULL , " +
            USER_USERNAME+" TEXT , " +
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
            CHANEL_USERNAME+ " TEXT NOT NULL , " +
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
    public static final String MESSAGE_LIKED="liked";
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
            MESSAGE_LIKED + " smallint ," +
            MESSAGE_UPDATED_AT + " TIMESTAMP NOT NULL)"
            ;




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





    ////////////////////////////////////////////////////////////////USerFunctions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void addUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USER_ID,user.getId());
        contentValues.put(USER_MOBILE,user.getMobile());
        contentValues.put(USER_APIKEY,user.getApikey());
        contentValues.put(USER_USERNAME,user.getUsername());
        contentValues.put(USER_PIC,user.getPic());
        contentValues.put(USER_PIC_THUMB,user.getPic_thumb());
        contentValues.put(CREATED_AT,user.getCreated_at());
        sqLiteDatabase.insert(TABLE_USER,null,contentValues);

    }
    public User getUser() {
        Cursor cursor = sqLiteDatabase.query(TABLE_USER,new String[]{USER_USER_ID,USER_MOBILE,USER_APIKEY,USER_USERNAME,USER_PIC,USER_PIC_THUMB, CREATED_AT},null,null,null,null,null);
        cursor.moveToFirst();
        User user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(USER_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(USER_MOBILE)),
                cursor.getString(cursor.getColumnIndexOrThrow(USER_APIKEY)),
                cursor.getString(cursor.getColumnIndexOrThrow(CREATED_AT)
                ),cursor.getString(cursor.getColumnIndexOrThrow(USER_USERNAME)
                ),cursor.getString(cursor.getColumnIndexOrThrow(USER_PIC)),
                cursor.getString(cursor.getColumnIndexOrThrow(USER_PIC_THUMB))
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
            contentValues.put(MESSAGE_LIKED,message.getLiked()==0 ? 0 : 1);
            contentValues.put(MESSAGE_UPDATED_AT,message.getUpdated_at());
            sqLiteDatabase.insert(TABLE_MESSAGE,null,contentValues);
        }
    }
    public boolean hasMessageData(int chanel_id) {
        long cnt  = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_MESSAGE , MESSAGE_CHANEL_ID+" LIKE ? " ,new String[] {String.valueOf(chanel_id)});
        return cnt > 0;
    }

    public ArrayList<Message> getAllMessages(int chanel_id) {
       ArrayList<Message> messages = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGE,new String[]{MESSAGE_MESSAGE_ID,MESSAGE_ADMIN_ID,MESSAGE_CHANEL_ID,MESSAGE_MESSAGE
                ,MESSAGE_THUMB,MESSAGE_TYPE,MESSAGE_LENTH,MESSAGE_TIME,MESSAGE_URL,MESSAGE_LIKED, MESSAGE_UPDATED_AT},MESSAGE_CHANEL_ID+" LIKE ? ",
                new String[] {String.valueOf(chanel_id)},null,null,null,null

                );
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            do {


                int message_id = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_MESSAGE_ID));
                int admin_id = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_ADMIN_ID));
                int chanel_id_temp = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_CHANEL_ID));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_MESSAGE));
                String thumb = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_THUMB));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_TYPE));
                int lenth = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_LENTH));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_TIME));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_URL));
                int liked = cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_LIKED));
                String updated_at  = cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE_UPDATED_AT));
                Message message_temp = new Message(message_id,admin_id,chanel_id_temp,message,thumb,type,lenth,time,url ,updated_at,liked);
                messages.add(message_temp);
            }while (cursor.moveToNext());
            return messages;
        }else {
            return  null;
        }

    }
    public  int getLastMessage_id(int chanel_id) {
        Cursor cursor = sqLiteDatabase.query(TABLE_MESSAGE,new String[]{MESSAGE_MESSAGE_ID},MESSAGE_CHANEL_ID+" LIKE ? " ,new String[]{String.valueOf(chanel_id)},null
                ,null,MESSAGE_MESSAGE_ID+" DESC","0,1"
                );
        cursor.moveToFirst();
        return  cursor.getInt(cursor.getColumnIndexOrThrow(MESSAGE_MESSAGE_ID));
    }

    public void setLikeState(int likeState,int message_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_LIKED,likeState);
        sqLiteDatabase.update(TABLE_MESSAGE,contentValues,MESSAGE_MESSAGE_ID+" LIKE ? " , new String[]{String.valueOf(message_id)});
    }
    ///////////////////////////////////positionFunction\\\\\\\\\\\\\\\\\\\\\\\
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
