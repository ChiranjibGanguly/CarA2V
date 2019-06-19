package com.cara2v.fcm_services;

import android.util.Log;

import com.cara2v.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: Chiranjib Ganguly
 * Created on: 15/03/2018
 * Project: FirebaseChat
 */

public class FcmNotificationBuilder {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "FcmNotificationBuilder";
    private static final String SERVER_API_KEY = "AAAALkUSc-8:APA91bGzeW7QtE85VRQT8Ofkui8uU2DNIUqtuETK3FHVTH6jgTBxemrNbN50T_a7vzhbLDnrHFl9MxgvKSMOh5-tdjngL_Uyy42e4ElUHSWisqgCmScMsz-xElCbKN7QKMazrdP55wmW";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_KEY = "key=" + SERVER_API_KEY;
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    // json related keys
    private static final String KEY_TO = "to";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "message";
    private static final String KEY_DATA = "data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_UID = "uid";
    private static final String KEY_FCM_TOKEN = "fcm_token";
    private static final String KEY_FCM_TYPE = "type";

    private String mTitle;
    private String mMessage;
    private String mUsername;
    private String mUid;
    private String mFirebaseToken;
    private String mReceiverFirebaseToken;
    private String mchatRoomId;
    private String mRequestId;
    private String mprofileImage;
    private String mRequestType;

    private FcmNotificationBuilder() {

    }

    public static FcmNotificationBuilder initialize() {
        return new FcmNotificationBuilder();
    }

    public FcmNotificationBuilder title(String title) {
        mTitle = title;
        return this;
    }

    public FcmNotificationBuilder message(String message) {
        mMessage = message;
        return this;
    }

    public FcmNotificationBuilder username(String username) {
        mUsername = username;
        return this;
    }

    public FcmNotificationBuilder uid(String uid) {
        mUid = uid;
        return this;
    }

    public FcmNotificationBuilder firebaseToken(String firebaseToken) {
        mFirebaseToken = firebaseToken;
        return this;
    }


    public FcmNotificationBuilder chatRoomId(String chatRoomId) {
        mchatRoomId = chatRoomId;
        return this;
    }

    public FcmNotificationBuilder requestId(String requestId) {
        mRequestId = requestId;
        return this;
    }

    public FcmNotificationBuilder profileImage(String profileImage) {
        mprofileImage = profileImage;
        return this;
    }

    public FcmNotificationBuilder requestType(String requestType) {
        mRequestType = requestType;
        return this;
    }
    public FcmNotificationBuilder receiverFirebaseToken(String receiverFirebaseToken) {
        mReceiverFirebaseToken = receiverFirebaseToken;
        return this;
    }

    public void send() {
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, getValidJsonBody().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, AUTH_KEY)
                .url(FCM_URL)
                .post(requestBody)
                .build();

        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onGetAllUsersFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    private JSONObject getValidJsonBody() throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put(KEY_TO, mReceiverFirebaseToken);
        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put(Constant.notyficationTitle, mTitle);
        jsonObjectData.put(Constant.notyficationBody, mMessage);
        jsonObjectData.put(Constant.message, mMessage);
        jsonObjectData.put(Constant.rcvName, mUsername);
        jsonObjectData.put(Constant.rcvUId, mUid);
        jsonObjectData.put(Constant.rcvFireBaseToken, mFirebaseToken);
        jsonObjectData.put(Constant.rcvPrflImg, mprofileImage);
        jsonObjectData.put(Constant.notifactionType, "chat");
        jsonObjectData.put(Constant.requestId, mRequestId);
        jsonObjectData.put(Constant.requestType, mRequestType);


        JSONObject jsonObjectNotification = new JSONObject();
        jsonObjectNotification.put(Constant.notyficationTitle, mTitle);
        jsonObjectNotification.put(Constant.notyficationBody, mMessage);
        jsonObjectNotification.put(Constant.message, mMessage);
        jsonObjectNotification.put(Constant.rcvName, mUsername);
        jsonObjectNotification.put(Constant.rcvUId, mUid);
        jsonObjectNotification.put(Constant.rcvFireBaseToken, mFirebaseToken);
        jsonObjectNotification.put(Constant.rcvPrflImg, mprofileImage);
        jsonObjectNotification.put(Constant.notifactionType, "chat");
        jsonObjectNotification.put(Constant.requestId, mRequestId);
        jsonObjectNotification.put(Constant.requestType, mRequestType);
        jsonObjectNotification.put("chatRoomId", mchatRoomId);
        jsonObjectNotification.put("icon", "icon");
        jsonObjectNotification.put("sound", "default");
        jsonObjectNotification.put("badge", "1");
        jsonObjectNotification.put("click_action", "ChatActivity");
        jsonObjectBody.put(KEY_DATA, jsonObjectData);
        jsonObjectBody.put(KEY_NOTIFICATION, jsonObjectNotification);
        jsonObjectBody.put("sound", "default");
        jsonObjectBody.put("priority", "high");
        return jsonObjectBody;
    }
}