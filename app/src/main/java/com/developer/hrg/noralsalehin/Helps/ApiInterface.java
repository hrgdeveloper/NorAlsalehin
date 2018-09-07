package com.developer.hrg.noralsalehin.Helps;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @GET("getDeletedCount")
    Call<SimpleResponse> getDeletedCount();

    @GET("getDeletedList")
    Call<SimpleResponse> getDeletedList();

    @GET("noorFirebase")
    Call<SimpleResponse> checkFirebase();

    @FormUrlEncoded
    @PUT("updateUsername")
    Call<SimpleResponse> updateUsername(@Header("Authorization") String header ,@Field("username") String username );


    @FormUrlEncoded
    @PUT("deleteMessage/{message_id}")
    Call<SimpleResponse> deleteMessage(@Header("AuthorizationInside") String header ,@Path("message_id") int message_id ,
                                       @Field("file_name") String fileName,@Field("type") int type);

    @GET("getAllChanelsUser")
    Call<SimpleResponse> getAllChanels(@Header("Authorization") String header);

    @GET("getUserCount")
    Call<SimpleResponse> getUserCount();

    @GET("chanels/{chanel_id}/getMessages")
    Call<SimpleResponse> getAllMessages(@Header("Authorization") String header , @Path("chanel_id") int chanel_id ,
    @Query("last_id") int last_id
    );



    @GET("chanels/{chanel_id}/getMessagesTop")
    Call<SimpleResponse> getAllMessagesTop(@Header("Authorization") String header , @Path("chanel_id") int chanel_id ,
                                        @Query("top_id") int last_id
    );

    @GET("getAllChanelsPhotos/{chanel_id}")
    Call<SimpleResponse> getAllProfiles(@Path("chanel_id") int chanel_di);

    @GET("chanels/{chanel_id}/getlastCount")
    Call<SimpleResponse> getLastCount(@Header("Authorization") String header , @Path("chanel_id") int chanel_id ,
                                        @Query("last_id") int last_id
    );

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWhiturl(@Url String fileUrl);
    @FormUrlEncoded
    @POST("likeMessage/{message_id}")
    Call<SimpleResponse> setLike(@Header("Authorization") String header , @Path("message_id") int message_id , @Field("type") int type
    );

    @Multipart
    @POST("updateUserProfile")
    Call<SimpleResponse> updateProfile(@Header("Authorization") String header , @Part MultipartBody.Part pic , @Part("last_pic") RequestBody content
                                       );
    @GET("comment/{message_id}")
    Call<SimpleResponse> getAllComments(@Header("Authorization") String header , @Path("message_id") int message_id);
    @FormUrlEncoded
    @POST("comment/{message_id}")
    Call<SimpleResponse> sendComment(@Header("Authorization") String header ,  @Path("message_id") int message_id ,@Field("chanel_id") int chanel_id,
    @Field("text") String text);

}
