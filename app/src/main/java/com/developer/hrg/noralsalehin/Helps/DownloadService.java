package com.developer.hrg.noralsalehin.Helps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.developer.hrg.noralsalehin.InsideChanel.InsideActivity;
import com.developer.hrg.noralsalehin.Models.DownloadBack;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.R;
import com.developer.hrg.noralsalehin.fcm.NotificationUtils;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hamid on 8/2/2018.
 */

public class DownloadService extends Service {
    public static final String MESSAGE="message";
    public static final String MESSAGE_ID="message_id";
    public static final String BUNDLE="bundle";

    public static final String ADDRESS="address";
    public static final String FILENAME="filename";
    public static final String DIRPATH="dirpath";
    public static final String POSITION="position";
    public static final String CHANEL_ID="chanel_id";

    public static final String BROADCAST_PROGRESS="progress";
    public static final String BROADCAST_DL_FINISH="finish";
    public static final String BROADCAST_PAUSE="pause";

    public static final String PROGRESS_POSITION="pposition";
    public static final String PROGRESS_PERCENT="ppercent" ;

    public static final String FINISH_POSITION="fPosition";
    UserData userData ;
    Context context ;
   // int previous_percent = 0  ;



    @Override
    public void onCreate() {
        super.onCreate();
        userData=new UserData(this);
        context=this;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {


        final int  message_id = intent.getIntExtra(MESSAGE_ID,0);
        String address = intent.getStringExtra(ADDRESS);
        String filename = intent.getStringExtra(FILENAME);
        String dirpath = intent.getStringExtra(DIRPATH);
        final int position = intent.getIntExtra(POSITION,0);
        final int    chanel_id = intent.getIntExtra(CHANEL_ID,0);
        Bundle data = intent.getBundleExtra(BUNDLE);
        final Message message = data.getParcelable(MESSAGE);
        Log.e("firstValue",message.getDl_state()+" ");
        int download_id = PRDownloader.download(address,
                dirpath,filename)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        final  Intent    intent = new Intent(BROADCAST_PAUSE);
                        intent.putExtra(PROGRESS_POSITION,position);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        Log.e("pauseee " , "pause mishe ?");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress)  {
                        Long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        //in qesmat vase inke ke darsad vaqti yedo
                        // ne raft bala ersal she

                        if (progressPercent.intValue() > message.getDl_state() +1 ) {
                            final  Intent    intent = new Intent(BROADCAST_PROGRESS);
                            intent.putExtra(PROGRESS_PERCENT,progressPercent.intValue());
                            intent.putExtra(PROGRESS_POSITION,position);
                            intent.putExtra(CHANEL_ID,chanel_id);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            message.setDl_state(message.getDl_state()+1);
                   }

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Intent intent = new Intent(BROADCAST_DL_FINISH);
                        intent.putExtra(FINISH_POSITION,position);
                        intent.putExtra(MESSAGE_ID,message_id);
                        intent.putExtra(CHANEL_ID,chanel_id);
                        //vase downloadhaie ke vaghti offline mishim va dobare online mishim edame dashte bashe
                        userData.deleteSingleDownload(message_id);

                        // inja save mikonim chon age ye vaqt to in activity naboodim vaziate downloade in moshakhash she

                        userData.setCompleteState(1,message_id);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        if (NotificationUtils.isAppIsInBackground(context)) {
                            // baraye peygirie vaziate download vaqti ke barnamee to backgrounde
                            DownloadBack downloadBack = new DownloadBack(chanel_id,position);
                            userData.insertDownloadBACK(downloadBack);
                        }
                    }

                    @Override
                    public void onError(Error error) {

                    }


                });

         userData.setDownloaadId(download_id,message_id);



        return START_NOT_STICKY;
    }
}
