package com.developer.hrg.noralsalehin.Helps;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by hamid on 8/5/2018.
 */

public class AudioService extends Service {
    public static final String FILEPATH="filepath";
    public static final String POSITION="position";
    public static final String CHANEL_ID="chanel_id";

    public static final String BROADCAST_AUDIO_PROGRESS="progress";
    public static final String BROADCAST_AUDIO_FINISH="finish";
    public static final String BROADCAST_AUDIO_PAUSE="pause";

   MediaPlayer mediaPlayer ;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String filepath = intent.getStringExtra(FILEPATH);
        int  position = intent.getIntExtra(POSITION,0);
        int chanel_id = intent.getIntExtra(CHANEL_ID,0);

         if (mediaPlayer!=null) {
             mediaPlayer.stop();
             mediaPlayer.release();
         }
        try {
            mediaPlayer.setDataSource(filepath);



        } catch (IOException e) {
            e.printStackTrace();
        }


        return START_NOT_STICKY ;
    }
}
