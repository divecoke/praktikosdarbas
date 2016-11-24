package com.example.ernestas.myapplication65651;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Ernestas on 11/24/2016.
 */

public class PostInformation {
    public String user_id;
    public String postText;
    public String postImageUrl;
    public String postDate;

    public PostInformation() {

    }

    public PostInformation(String user_id, String postText, String postImageUrl, String postDate) {
        this.user_id = user_id;
        this.postText = postText;
        this.postImageUrl = postImageUrl;
        this.postDate = postDate;
    }

    public String getUserID() {
        return user_id;
    }

    public String getPostText() {
        return postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }


    public void setPostText(String postText) {
        this.postText = postText;
    }



}
