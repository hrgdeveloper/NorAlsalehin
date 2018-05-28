package com.developer.hrg.noralsalehin;

/**
 * Created by hamid on 5/25/2018.
 */

public class Intro {
    String title , des ;
    int pic , background;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Intro(String title, String des, int pic, int background) {
        this.title = title;
        this.des = des;
        this.pic = pic;
        this.background = background;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
