package com.example.ernestas.myapplication65651;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Ernestas on 11/24/2016.
 */

public class PostInformation {
    public String userID;
    public String postText;
    public String postImageUrl;
    public String postDate;
    public String bit64;


    public PostInformation() {

    }

    public PostInformation(String userID, String postText, String postImageUrl, String postDate, String bit64) {
        this.userID = userID;
        this.postText = postText;
        this.postImageUrl = postImageUrl;
        this.postDate = postDate;
        this.bit64 = bit64;
    }

    public String getUserID() {
        return userID;
    }

    public String getPostText() {
        return postText;
    }

    public String getBit64() {
        return bit64;
    }


    public void setPostText(String postText) {
        this.postText = postText;
    }



}
