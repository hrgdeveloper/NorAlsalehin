package com.developer.hrg.noralsalehin.Helps;

import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hamid on 5/25/2018.
 */

public class SimpleResponse {
    boolean error ;
    String message ;
    User user;

    @SerializedName("chanels")
    ArrayList<Chanel> chanels ;

    public ArrayList<Chanel> getChanels() {
        return chanels;
    }

    public void setChanels(ArrayList<Chanel> chanels) {
        this.chanels = chanels;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SimpleResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }
}
