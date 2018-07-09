package com.developer.hrg.noralsalehin.Helps;

import com.developer.hrg.noralsalehin.Models.Chanel;
import com.developer.hrg.noralsalehin.Models.Comment;
import com.developer.hrg.noralsalehin.Models.Message;
import com.developer.hrg.noralsalehin.Models.UnRead;
import com.developer.hrg.noralsalehin.Models.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hamid on 5/25/2018.
 */

public class SimpleResponse {
    @SerializedName("error")
    boolean error ;
    @SerializedName("error_type")
    int errortype ;

    @SerializedName("message")
    String message ;
    @SerializedName("user")
    User user;
    @SerializedName("user_count")
    int user_count ;

    @SerializedName("likes")
    int likes ;
    @SerializedName("comments")
    ArrayList<Comment> comments;


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @SerializedName("messages")
   ArrayList<Message> messages ;
    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    public ArrayList<UnRead> getUnReads() {
        return unReads;
    }

    public void setUnReads(ArrayList<UnRead> unReads) {
        this.unReads = unReads;
    }

    @SerializedName("chanels")
    ArrayList<Chanel> chanels ;
    @SerializedName("unreads")
    ArrayList<UnRead> unReads;



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
    public int getErrortype() {
        return errortype;
    }

    public void setErrortype(int errortype) {
        this.errortype = errortype;
    }

    public SimpleResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }
}
