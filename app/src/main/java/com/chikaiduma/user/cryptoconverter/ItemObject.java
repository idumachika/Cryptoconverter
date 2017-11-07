package com.chikaiduma.user.cryptoconverter;

/**
 * Created by User on 06/11/2017.
 */

public class ItemObject {
    String text;
    Integer imageId;
    public ItemObject(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }
}
