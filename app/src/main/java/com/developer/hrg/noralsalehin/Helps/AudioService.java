package com.developer.hrg.noralsalehin.Helps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by hamid on 8/5/2018.
 */

public class AudioService extends Service {
    public static final String FILEPATH="filepath";
    public static final String POSITION="position";
    public static final String CHANEL_ID="chanel_id";
    public static final String DURATION="duration";

    public static final String BROADCAST_AUDIO_PROGRESS="progress";
    public static final String BROADCAST_AUDIO_FINISH="finish";
    public static final String BROADCAST_AUDIO_PAUSE="pause";

    public static final String CURRENT_POSITION="current";

   MediaPlayer mediaPlayer ;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
        context=this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasCategory("pause")) {
            if (mediaPlayer!=null   && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Log.e("audioTest","pause mishe");
            }

        }else {
            String filepath = intent.getStringExtra(FILEPATH);
            Log.e("audioTest",filepath);
            final int  position = intent.getIntExtra(POSITION,0);
            final int chanel_id = intent.getIntExtra(CHANEL_ID,0);

//            if (mediaPlayer!=null) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//            }
            try {
                mediaPlayer.setDataSource(filepath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        while (mediaPlayer.getCurrentPosition()<mediaPlayer.getDuration()) {
                            try {
                                Log.e("audioTest",mediaPlayer.getCurrentPosition()+" ");
                                sleep(1000);
                                final  Intent    intent = new Intent(BROADCAST_AUDIO_PROGRESS);
                                intent.putExtra(CURRENT_POSITION,mediaPlayer.getCurrentPosition());
                                intent.putExtra(CHANEL_ID,chanel_id);
                                intent.putExtra(POSITION,position);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                };
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        return START_NOT_STICKY ;
    }

    public void pausePlayer() {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

}
