package com.developer.hrg.noralsalehin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamid on 6/5/2018.
 */

public class Chanel {

    @SerializedName("chanel_id")
    String chanel_id ;
    @SerializedName("name")
    String name ;
    @SerializedName("description")
    String description;
    @SerializedName("username")
    String username;
    @SerializedName("thumb")
    String thumb ;
    String last_message ;
    @SerializedName("type")
    Integer type ;

    @SerializedName("updated_at")
    String updated_at ;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @SerializedName("last_message")



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

    public String getChanel_id() {
        return chanel_id;
    }

    public void setChanel_id(String chanel_id) {
        this.chanel_id = chanel_id;
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



    public Chanel(String chanel_id, String name, String description, String thumb, String updated_at, String created_at, String username ,
                  String lase_message , Integer type) {
        this.chanel_id = chanel_id;
        this.name = name;
        this.description = description;
        this.thumb = thumb;

        this.username = username;
        this.last_message=lase_message;
        this.type=type;
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
                typee=  "تصویر متحرک" ;
                break;
            case 6 :
                typee=  "فایل" ;
                default:
                    typee = "فرمت ناشناس";

        }

        return typee ;

    }


}
