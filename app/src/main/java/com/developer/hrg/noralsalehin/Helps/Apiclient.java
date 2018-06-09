package com.developer.hrg.noralsalehin.Helps;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by hamid on 5/25/2018.
 */

public class Apiclient {
  private static   Retrofit retrofit =null ;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit=new Retrofit.Builder()
                    .baseUrl(Config.OFFLINE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


        }
        return retrofit;
    }
}
