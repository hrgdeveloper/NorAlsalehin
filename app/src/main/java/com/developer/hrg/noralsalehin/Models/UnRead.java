package com.developer.hrg.noralsalehin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hamid on 6/6/2018.
 */

public class UnRead {
    @SerializedName("chanel_id")
    int chanel_id ;

    public int getChanel_id() {
        return chanel_id;
    }

    public void setChanel_id(int chanel_id) {
        this.chanel_id = chanel_id;
    }

    public int getCount() {
        return count;
    }

    public UnRead(int chanel_id, int count) {
        this.chanel_id = chanel_id;
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @SerializedName("count")

    int count ;
}
