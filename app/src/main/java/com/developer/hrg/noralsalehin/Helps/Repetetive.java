package com.developer.hrg.noralsalehin.Helps;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.developer.hrg.noralsalehin.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by hamid on 8/21/2018.
 */

public class Repetetive {
    public static void handleFailureError(Throwable t , Context context , String TAG) {
        if (t instanceof SocketTimeoutException){
            Toast.makeText(context, R.string.timeout , Toast.LENGTH_SHORT).show();
        }else if (t instanceof IOException) {
            Toast.makeText(context, R.string.no_internet_connection , Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, R.string.connection_problem , Toast.LENGTH_SHORT).show();
            Log.e(TAG,t.getMessage());

        }

    }
    public static String getTag(Fragment fragment) {
        return  fragment.getClass().getName();
    }



    public static void handleResonseError(retrofit2.Response<SimpleResponse> response , Context context) {
        try {
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            String message =      jsonObject.getString("message");
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "خطای ناشناس", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "خطای ناشناس", Toast.LENGTH_SHORT).show();
        }
    }
}
