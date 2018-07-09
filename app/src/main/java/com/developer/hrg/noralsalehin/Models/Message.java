package com.developer.hrg.noralsalehin.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamid on 6/14/2018.
 */

public class Message implements Parcelable {
    @SerializedName("message_id")
    int message_id ;
    @SerializedName("admin_id")
    int admin_id ;
    @SerializedName("chanel_id")
    int chanel_id ;
    @SerializedName("message")
    String message ;
    @SerializedName("pic_thumb")
    String thumb ;
    @SerializedName("type")
    int type ;
    @SerializedName("lenth")
    int lenth ;
    @SerializedName("time")
    String time ;
    @SerializedName("url")
    String url ;
    @SerializedName("admin_name")
    String admin_name ;

    @SerializedName("updated_at")
    String updated_at ;

    @SerializedName("liked")
    Integer liked ;

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }

    public Message(int message_id, int admin_id, int chanel_id, String message, String thumb, int type, int lenth, String time, String url
    , String admin_name , String updated_at) {
        this.message_id = message_id;
        this.admin_id = admin_id;
        this.chanel_id = chanel_id;
        this.message = message;
        this.thumb = thumb;
        this.type = type;
        this.lenth = lenth;
        this.time = time;
        this.url = url;
        this.admin_name=admin_name;
        this.updated_at=updated_at;
    }



    public Message(int message_id, int admin_id, int chanel_id, String message, String thumb, int type, int lenth, String time, String url
            , String updated_at , int liked) {
        this.message_id = message_id;
        this.admin_id = admin_id;
        this.chanel_id = chanel_id;
        this.message = message;
        this.thumb = thumb;
        this.type = type;
        this.lenth = lenth;
        this.time = time;
        this.url = url;
        this.liked=liked;
        this.updated_at=updated_at;
    }
    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public int getChanel_id() {
        return chanel_id;
    }

    public void setChanel_id(int chanel_id) {
        this.chanel_id = chanel_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLenth() {
        return lenth;
    }

    public void setLenth(int lenth) {
        this.lenth = lenth;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected Message(Parcel in) {
        message_id = in.readInt();
        admin_id = in.readInt();
        chanel_id = in.readInt();
        message = in.readString();
        thumb = in.readString();
        type = in.readInt();
        lenth = in.readInt();
        time = in.readString();
        url = in.readString();
        admin_name=in.readString();
        updated_at=in.readString();
        liked=in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(message_id);
        dest.writeInt(admin_id);
        dest.writeInt(chanel_id);
        dest.writeString(message);
        dest.writeString(thumb);
        dest.writeInt(type);
        dest.writeInt(lenth);
        dest.writeString(time);
        dest.writeString(url);
        dest.writeString(admin_name);
        dest.writeString(updated_at);
        dest.writeInt(liked);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };



}
