package com.developer.hrg.noralsalehin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamid on 7/7/2018.
 */

public class Comment {


    @SerializedName("text")
    String text ;
    @SerializedName("created_at")
    String created_at ;
    @SerializedName("username")
    String username ;

    public Comment(String text, String created_at, String username, String pic_thumb) {
        this.text = text;
        this.created_at = created_at;
        this.username = username;
        this.pic_thumb = pic_thumb;
    }

    @SerializedName("pic_thumb")

    String pic_thumb ;




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPic_thumb() {
        return pic_thumb;
    }

    public void setPic_thumb(String pic_thumb) {
        this.pic_thumb = pic_thumb;
    }





}
