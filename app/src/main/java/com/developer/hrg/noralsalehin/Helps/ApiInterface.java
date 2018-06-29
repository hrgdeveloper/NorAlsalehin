package com.developer.hrg.noralsalehin.Helps;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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

    @GET("getUserCount")
    Call<SimpleResponse> getUserCount();

    @GET("chanels/{chanel_id}/getMessages/{page}")
    Call<SimpleResponse> getAllMessages(@Header("Authorization") String header , @Path("chanel_id") int chanel_id , @Path("page") int page,
    @Query("last_id") int last_id
    );
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWhiturl(@Url String fileUrl);


}
