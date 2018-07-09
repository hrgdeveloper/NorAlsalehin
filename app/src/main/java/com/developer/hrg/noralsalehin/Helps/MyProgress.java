package com.developer.hrg.noralsalehin.Helps;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by hamid on 5/30/2018.
 */

public class MyProgress {
   static ProgressDialog progressDialog;
    public static void showProgress(Context context , String message) {
         progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public static void cancelProgress() {
        progressDialog.cancel();
    }
}
