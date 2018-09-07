package com.developer.hrg.noralsalehin.fcm;

/**
 * Created by hamid on 6/3/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.developer.hrg.noralsalehin.Helps.Config;
import com.developer.hrg.noralsalehin.Helps.DateConvertor;
import com.developer.hrg.noralsalehin.Helps.MyApplication;
import com.developer.hrg.noralsalehin.Helps.UserData;
import com.developer.hrg.noralsalehin.Main.MainActivity;
import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e("testOmad", "omaaaad");
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
           handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("GAVEEEEE 2", "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        Log.e("omadeee","areeeee");

            try {

                JSONObject data = json.getJSONObject("data");
                String flag   = data.getString("flag");
                if (Integer.valueOf(flag)==Config.PUSH_NEW_CHANEL_FLAG) {

                    JSONObject payload = data.getJSONObject("payload");
                    Log.e("newChanel",payload.getString("name"));
                    Gson gson = new Gson();
                    Chanel chanel = gson.fromJson(payload.toString(),Chanel.class);
                    // vase inke chaneli ke taze sakhte shode hich payami tosh nadare tedad payam ro 0 mizarim
                    chanel.setCount(0);
                    Intent intent = new Intent(Config.PUSH_NEW_CHANEL);
                    intent.putExtra("chanel",chanel);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }else if (Integer.valueOf(flag)==Config.PUSH_NEW_MESSAGE_FLAG) {
                    Log.e("newMessage","areeeee");
                    JSONObject payload = data.getJSONObject("payload");
                    Log.e("newMessage",payload.toString());
                    Gson gson = new Gson();
                    Message message = gson.fromJson(payload.toString(),Message.class);
                    message.setLiked(0);
                    message.setDl_percent(0);
                    message.setDl_id(0);
                    message.setDl_state(0);

                    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        Intent intent = new Intent(Config.PUSH_NEW_MESSAGE);
                        intent.putExtra("message",message);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);



                    }else
                        {
                            Log.e("Backeee" , "backeee");
                            if (MyApplication.getInstance().getUserData().getChanelNotifyState(message.getChanel_id())==1) {
                                Log.e("okeye","okeyee");
                                String chanel_name = MyApplication.getInstance().getUserData().getChanelNameByid(message.getChanel_id());
                                int  unread_count = MyApplication.getInstance().getUserData().getUnreadCount(message.getChanel_id());
                                Log.e("newMessage",unread_count+"");
                                unread_count++;
                                Log.e("newMessage",unread_count+"");
                                MyApplication.getInstance().getUserData().updateUnread(unread_count,message.getChanel_id());
                                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                resultIntent.putExtra("chanel_id", message.getChanel_id());
                                showNotificationMessage(getApplicationContext(),"نور الصالحین", unread_count+   "پیام جدید در " + chanel_name, message.getUpdated_at(), resultIntent);




                            }else {
                                int  unread_count = MyApplication.getInstance().getUserData().getUnreadCount(message.getChanel_id());
                                unread_count++;
                                MyApplication.getInstance().getUserData().updateUnread(unread_count,message.getChanel_id());

                            }




                    }


                }else if (Integer.valueOf(flag)==Config.PUSH_NEW_NOTIFY) {
                    String imageURL = data.getString("image");
                    String title = data.getString("title");
                    String message = data.getString("message");
                    Calendar calendar = getCurrentTimestamp();
                    String shamsi_date = DateConvertor.shamsiDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
                    String curTime = String.format("%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
                    String timestamp = shamsi_date + " : " +  curTime;
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra(Config.PUSH_NOTIFICATION,1);
                    showNotificationMessageWithBigImage(this,title,message,timestamp,resultIntent,Config.NOTIFY_ADDRESS+imageURL);
                }


                else {

                    String title = data.getString("title");
                    String message = data.getString("message");
                    boolean isBackground = data.getBoolean("is_background");
                    String imageUrl = data.getString("image");
                    String timestamp = data.getString("timestamp");
                    JSONObject payload = data.getJSONObject("payload");
                    Log.e("Gaveeeeeee", "title: " + title);
                    Log.e(TAG, "title: " + title);
                    Log.e(TAG, "message: " + message);
                    Log.e(TAG, "isBackground: " + isBackground);
                    Log.e(TAG, "payload: " + payload.toString());
                    Log.e(TAG, "imageUrl: " + imageUrl);
                    Log.e(TAG, "timestamp: " + timestamp);
                    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        Log.e("toyeroo",message);
                        Log.e("toyeroo",payload.toString());
                        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                        pushNotification.putExtra("message", message);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                        // play notification sound
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.playNotificationSound();
                    } else {
                        Log.e("toyeback",message);
                        Log.e("toyeback",payload.toString());
                        // app is in background, show the notification in notification tray
                        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                        resultIntent.putExtra("message", message);

                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                        }
                    }

                }




        } catch (JSONException e) {
            Log.e("Gaveeeeeee", "Json Exception: " + e.getMessage());
            Log.e("Gaveeeeeee", "Exceptionnnnnnnnnnnnnnnnnn: " );
        } catch (Exception e) {
            Log.e("Gaveeeeeee", "Exception: " + e.getMessage());
            Log.e("Gaveeeeeee", "Exceptionnnnnnnnnnnnnnnnnn: " );
        }
    }
    public Calendar getCurrentTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d hh:mm");
        String format = simpleDateFormat.format(new Date());

        Calendar calendar = Calendar.getInstance() ;

        try {
            Date date = simpleDateFormat.parse(format);
            calendar.setTime(date);
        }
        catch(ParseException pe) {
        }
        return calendar;
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}