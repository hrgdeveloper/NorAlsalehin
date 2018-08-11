package com.developer.hrg.noralsalehin.Models;

/**
 * Created by hamid on 8/12/2018.
 */

public class DownloadBack {
    int chanel_id ;
    int position ;

    public DownloadBack(int chanel_id, int position) {
        this.chanel_id = chanel_id;
        this.position = position;
    }

    public int getChanel_id() {
        return chanel_id;
    }

    public void setChanel_id(int chanel_id) {
        this.chanel_id = chanel_id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
