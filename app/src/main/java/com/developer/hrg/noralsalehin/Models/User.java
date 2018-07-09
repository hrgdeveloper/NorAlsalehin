package com.developer.hrg.noralsalehin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamid on 5/29/2018.
 */

public class User {
    @SerializedName("user_id")
    int id ;
    @SerializedName("mobile")
    String mobile ;
    @SerializedName("apikey")
    String apikey ;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("username")
    String username ;
    @SerializedName("pic")
    String pic ;
    @SerializedName("pic_thumb")
    String pic_thumb;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic_thumb() {
        return pic_thumb;
    }

    public void setPic_thumb(String pic_thumb) {
        this.pic_thumb = pic_thumb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User(int id, String mobile, String apikey, String created_at , String username, String pic , String pic_thumb) {
        this.id = id;
        this.mobile = mobile;
        this.apikey = apikey;
        this.created_at = created_at;
        this.username=username;
        this.pic=pic;
        this.pic_thumb=pic_thumb;
    }
}
