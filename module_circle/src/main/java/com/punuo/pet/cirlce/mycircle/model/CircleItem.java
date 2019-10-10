package com.punuo.pet.cirlce.mycircle.model;

import android.provider.ContactsContract;
import android.text.Layout;
import android.widget.ImageView;

import java.util.Date;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 **/
public class CircleItem {
    private String nickName;
    private ImageView circleAvater;
    private Date date;

    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public ImageView getCircleAvater() {
        return circleAvater;
    }

    public void setCircleAvater(ImageView circleAvater) {
        this.circleAvater = circleAvater;
    }

    public Date getDate(){
        return date;
    }
}
