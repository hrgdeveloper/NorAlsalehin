package com.developer.hrg.noralsalehin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamid on 9/5/2018.
 */

public class Message_id {
    @SerializedName("message_id")
    int message_id ;

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }
}
