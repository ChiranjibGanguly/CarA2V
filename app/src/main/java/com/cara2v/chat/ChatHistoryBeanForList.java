package com.cara2v.chat;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Author: Chiranjib Ganguly
 * Created on: 9/4/2016 , 12:43 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class ChatHistoryBeanForList {
    public String deleteby;
    public int image;
    public String imageUrl;
    public String firebaseToken;
    public String message;
    public String name;
    public String profilePic;
    public String uid;
    public Object timestamp;
}
