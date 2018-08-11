package com.developer.hrg.noralsalehin.Models;

/**
 * Created by hamid on 8/11/2018.
 */

public class Download {
    Integer message_id ;
    String address ;
    int position ;
    String dirpath ;
    String filename ;
    String message ;
    int chanel_id ;


    public Download(Integer message_id, String address, int position, String dirpath, String filename, String message, int chanel_id) {
        this.message_id = message_id;
        this.address = address;
        this.position = position;
        this.dirpath = dirpath;
        this.filename = filename;
        this.message = message;
        this.chanel_id = chanel_id;
    }

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDirpath() {
        return dirpath;
    }

    public void setDirpath(String dirpath) {
        this.dirpath = dirpath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getChanel_id() {
        return chanel_id;
    }

    public void setChanel_id(int chanel_id) {
        this.chanel_id = chanel_id;
    }


}
