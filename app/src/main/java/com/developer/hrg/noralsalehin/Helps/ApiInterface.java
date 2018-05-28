package com.developer.hrg.noralsalehin.Helps;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by hamid on 5/25/2018.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("register")
    Call<SimpleResponse> register(@Field("mobile") String mobile);

}
