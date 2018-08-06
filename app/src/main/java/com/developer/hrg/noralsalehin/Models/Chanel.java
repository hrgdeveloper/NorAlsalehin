package com.developer.hrg.noralsalehin.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamid on 6/5/2018.
 */

public class Chanel implements Parcelable {

    @SerializedName("chanel_id")
    int chanel_id ;
    @SerializedName("name")
    String name ;
    @SerializedName("description")
    String description;
    @SerializedName("username")
    String username;
    @SerializedName("thumb")
    String thumb ;
    @SerializedName("last_message")
    String last_message ;
    @SerializedName("type")
    Integer type ;
    @SerializedName("count")
    int count ;
    @SerializedName("updated_at")
    String updated_at ;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getChanel_id() {
        return chanel_id;
    }

    public void setChanel_id(int chanel_id) {
        this.chanel_id = chanel_id;
    }



    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }





    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getThumb() {
        return thumb;
    }
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public Chanel(int chanel_id, String name, String description, String thumb, String updated_at, String username ,
                  String lase_message , Integer type , int count ) {
        this.chanel_id = chanel_id;
        this.name = name;
        this.description = description;
        this.thumb = thumb;
        this.updated_at=updated_at;
        this.username = username;
        this.last_message=lase_message;
        this.type=type;
        this.count=count;
    }
    // in mothod vase ine ke ba tavajoh be meghdare type yek string ke message az che noie bar migardone

    public String getStringFromType() {
        String typee ;

        switch (type) {
            case 1 :
                typee= "متن";
                break;
            case 2  :
                typee=  "عکس" ;
                break;
            case 3 :
                typee=   "فیلم" ;
                break;
            case 4 :
                typee=  "فایل صوتی" ;
                break;
            case 5 :
                typee=  "فایل" ;
                break;

                default:
                    typee ="بدون پیام";

        }

        return typee ;

    }


    protected Chanel(Parcel in) {
        chanel_id = in.readInt();
        name = in.readString();
        description = in.readString();
        username = in.readString();
        thumb = in.readString();
        last_message = in.readString();
        type = in.readByte() == 0x00 ? null : in.readInt();
        count = in.readInt();
        updated_at = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(chanel_id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(username);
        dest.writeString(thumb);
        dest.writeString(last_message);
        if (type == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(type);
        }
        dest.writeInt(count);
        dest.writeString(updated_at);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Chanel> CREATOR = new Parcelable.Creator<Chanel>() {
        @Override
        public Chanel createFromParcel(Parcel in) {
            return new Chanel(in);
        }

        @Override
        public Chanel[] newArray(int size) {
            return new Chanel[size];
        }
    };

}
