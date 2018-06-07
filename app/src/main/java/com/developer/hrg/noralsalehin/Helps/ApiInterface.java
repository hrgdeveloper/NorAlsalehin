package com.developer.hrg.noralsalehin.Helps;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by hamid on 5/25/2018.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("register")
    Call<SimpleResponse> register(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("verify")
    Call<SimpleResponse> verify(@Field("mobile") String mobile , @Field("otp") String otp);

    @GET("getAllChanelsUser")
    Call<SimpleResponse> getAllChanels(@Header("Authorization") String header);
    @GET("getUnRead")
    Call<SimpleResponse> getUnread(@Header("Authorization") String header);
}
