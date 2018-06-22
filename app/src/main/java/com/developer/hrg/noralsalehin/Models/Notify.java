package com.developer.hrg.noralsalehin.Models;

/**
 * Created by hamid on 6/15/2018.
 */

public class Notify {

    int id ;
    int chanel_id ;
    int show_notify ;
    int play_sound ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChanel_id() {
        return chanel_id;
    }

    public void setChanel_id(int chanel_id) {
        this.chanel_id = chanel_id;
    }

    public int getShow_notify() {
        return show_notify;
    }

    public void setShow_notify(int show_notify) {
        this.show_notify = show_notify;
    }

    public int getPlay_sound() {
        return play_sound;
    }

    public void setPlay_sound(int play_sound) {
        this.play_sound = play_sound;
    }

    public Notify( int chanel_id, int show_notify, int play_sound) {

        this.chanel_id = chanel_id;
        this.show_notify = show_notify;
        this.play_sound = play_sound;
    }
}
