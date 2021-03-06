package com.developer.hrg.noralsalehin.Models;

import android.content.Intent;
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
    Long lenth ;
    @SerializedName("time")
    String time ;
    @SerializedName("url")
    String url ;
    @SerializedName("admin_name")
    String admin_name ;
    @SerializedName("updated_at")
    String updated_at ;
    @SerializedName("filename")
    String filename ;
    @SerializedName("liked")
    Integer liked ;
    Integer dl_state  ;
    Integer dl_id ;
    Integer dl_percent ;
    Integer audio_percent  ;
    Integer complete ;

    public Integer getComplete() {
        return complete;
    }
    public void setComplete(Integer complete) {
        this.complete = complete;
    }
    public Integer getAudio_percent() {
        return audio_percent;
    }
    public void setAudio_percent(Integer audio_percent) {
        this.audio_percent = audio_percent;
    }

    public Integer getDl_percent() {
        return dl_percent;
    }

    public void setDl_percent(Integer dl_percent) {
        this.dl_percent = dl_percent;
    }

    public Integer getDl_id() {
        return dl_id;
    }

    public void setDl_id(Integer dl_id) {
        this.dl_id = dl_id;
    }

    public Integer getDl_state() {
        return dl_state;
    }

    public void setDl_state(Integer dl_state) {
        this.dl_state = dl_state;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }

    public Message(int message_id, int admin_id, int chanel_id, String message, String thumb, int type, Long lenth, String time , String filename, String url
     , String updated_at , int liked , Integer dl_state , Integer dl_id , Integer dl_percent, Integer audio_percent,Integer complete ) {
        this.message_id = message_id;
        this.admin_id = admin_id;
        this.chanel_id = chanel_id;
        this.message = message;
        this.thumb = thumb;
        this.type = type;
        this.lenth = lenth;
        this.time = time;
        this.url = url;
        this.filename=filename;
        this.updated_at=updated_at;
        this.liked=liked;
        this.dl_state=dl_state;
        this.dl_id=dl_id;
        this.dl_percent=dl_percent;
        this.audio_percent=audio_percent;
        this.complete=complete;
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

    public Long getLenth() {
        return lenth;
    }

    public void setLenth(Long lenth) {
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
        lenth = in.readLong();
        time = in.readString();
        url = in.readString();
        filename= in.readString();
        admin_name=in.readString();
        updated_at=in.readString();
        liked=in.readInt();
        dl_state=in.readInt();
        dl_id=in.readInt();
        dl_percent=in.readInt();
        audio_percent=in.readInt();
        complete=in.readInt();
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
        dest.writeLong(lenth);
        dest.writeString(time);
        dest.writeString(url);
        dest.writeString(filename);
        dest.writeString(admin_name);
        dest.writeString(updated_at);
        dest.writeInt(liked);
        dest.writeInt(dl_state);
        dest.writeInt(dl_id);
        dest.writeInt(dl_percent);
        dest.writeInt(audio_percent);
        dest.writeInt(complete);

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
