package com.cara2v.fcm_services;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cara2v.R;
import com.cara2v.chat.ChatActivity;
import com.cara2v.ui.activity.ActivityMyJobServiceDetailsLayout;
import com.cara2v.ui.activity.ActivityServiceDetails;
import com.cara2v.ui.activity.MainActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.MyApplication;
import com.cara2v.util.PreferenceConnector;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by Chiranjib on 22/9/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private String className = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Data: " + remoteMessage.getData());
        Log.e(TAG, "Notification: " + remoteMessage.getNotification());
        try {
            MyApplication app = (MyApplication) getApplication();
            className = app.getActiveActivity().getClass().getSimpleName();
            if (className == null) {
                className = "";
            }
        } catch (NullPointerException e) {

        }
        if (remoteMessage.getData() != null) {
            if (PreferenceConnector.readBoolean(this, PreferenceConnector.IsLogIN, false)) {
                int badgeCount = 1;
                //ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+
                handleNotificationType(remoteMessage);
            }
        }
    }

    private void handleNotificationType(RemoteMessage remoteMessage) {
        String title = "";
        String body = "";
        String type = "";
        int requestId = 0;
        try {
            title = remoteMessage.getData().get("title").toString();
            body = remoteMessage.getData().get("body").toString();
            if (remoteMessage.getData().containsKey("NotifyType")) {
                type = remoteMessage.getData().get("NotifyType").toString();
            } else {
                type = remoteMessage.getData().get("type").toString();
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            type = "";
        }

        Intent intent = new Intent("NOTIFICATIONCOUNT");
        if (type.equals("chat")) {
            MainActivity.chatCount = MainActivity.chatCount + 1;
            PreferenceConnector.writeInteger(this,PreferenceConnector.chatCount,MainActivity.chatCount);
            intent.putExtra(Constant.COUNTYPE, "chatCount");
        } else {
            MainActivity.notificationCount = MainActivity.notificationCount + 1;
            PreferenceConnector.writeInteger(this,PreferenceConnector.notiCount,MainActivity.notificationCount);
            intent.putExtra(Constant.COUNTYPE, "notiCount");
        }
        sendBroadcast(intent);

        switch (type) {
            case "request":
                requestcreateNQuoteNotification(remoteMessage, title, body, type);
                break;
            case "Start":
            case "Progress":
            case "Almost":
            case "End":
            case "Ask":
            case "payFirst":
            case "reminder":
                ServiceProgressNotification(remoteMessage, title, body, type,"pending");
                break;
            case "Quote":
                requestcreateNQuoteNotification(remoteMessage, title, body, type);
                break;
            case "Review":
            case "paySecond":
                ServiceProgressNotification(remoteMessage, title, body, type,"complete");
                break;
            case "Accept":
                requestAcceptNotification(remoteMessage, title, body, type,"pending");
                break;
            case "chat":
                chatNotification(remoteMessage, title);
                break;
            case "licence":
                String status = remoteMessage.getData().get("status").toString();
                if (status.equals("1")) {
                    PreferenceConnector.writeBoolean(getApplicationContext(), PreferenceConnector.IsLcnAprv, true);
                } else {
                    PreferenceConnector.writeBoolean(getApplicationContext(), PreferenceConnector.IsLcnAprv, false);
                }
                sendNotification(title, body);
                break;
        }
    }

    private void requestAcceptNotification(RemoteMessage remoteMessage, String title, String body, String type, String state) {
        int requestId;
        requestId = Integer.parseInt(remoteMessage.getData().get("requestId").toString());
        if (className.equals("ActivityMyJobServiceDetailsLayout") || className.equals("ActivityMyServiceJobsChargeDetails") || className.equals("ActivityRequestStatus")) {
            Intent intent = new Intent("FILTERSERVICEPROGRESS"); //FILTER is a string to identify this intent
            intent.putExtra(Constant.REQUEST_ID, requestId);
            intent.putExtra("STATE", state);
            intent.putExtra(Constant.TITLE, title);
            intent.putExtra(Constant.MSG, body);
            intent.putExtra(Constant.TYPE, type);
            sendBroadcast(intent);
        } else if (className.equals("ActivityServiceDetails") || className.equals("ActivtySentServiceDetailsLayout")) {
            Intent intent = new Intent("FILTERREQUEST"); //FILTER is a string to identify this intent
            intent.putExtra(Constant.REQUEST_ID, requestId);
            intent.putExtra(Constant.TITLE, title);
            intent.putExtra(Constant.MSG, body);
            intent.putExtra(Constant.TYPE, type);
            sendBroadcast(intent);
        } else if (className.equals("MainActivity")) {
            if (MainActivity.BaseFragment.equals("FragmentMyJobs") || MainActivity.BaseFragment.equals("FragmentMyServices")) {
                Intent intent = new Intent("FILTERSERVICEPROGRESS"); //FILTER is a string to identify this intent
                intent.putExtra(Constant.REQUEST_ID, requestId);
                intent.putExtra("STATE", state);
                intent.putExtra(Constant.TITLE, title);
                intent.putExtra(Constant.MSG, body);
                intent.putExtra(Constant.TYPE, type);
                sendBroadcast(intent);
            } else if (MainActivity.BaseFragment.equals("OnlineJobsNServicesFragment")) {
                Intent intent = new Intent("FILTERREQUEST"); //FILTER is a string to identify this intent
                intent.putExtra(Constant.REQUEST_ID, requestId);
                intent.putExtra(Constant.TITLE, title);
                intent.putExtra(Constant.MSG, body);
                intent.putExtra(Constant.TYPE, type);
                sendBroadcast(intent);
            }
            sendServiceProgressDetailsNotification(title, requestId, body,state);
        } else {
            sendServiceProgressDetailsNotification(title, requestId, body,state);
            Log.e(TAG, "sendChatNotification: ");
        }
    }

    private void requestcreateNQuoteNotification(RemoteMessage remoteMessage, String title, String body, String type) {
        int requestId;
        requestId = Integer.parseInt(remoteMessage.getData().get("requestId").toString());
        if (className.equals("ActivityServiceDetails")) {
            Intent intent = new Intent("FILTERREQUEST"); //FILTER is a string to identify this intent
            intent.putExtra(Constant.REQUEST_ID, requestId);
            intent.putExtra(Constant.TITLE, title);
            intent.putExtra(Constant.MSG, body);
            intent.putExtra(Constant.TYPE, type);
            sendBroadcast(intent);
        } else if (className.equals("MainActivity")) {
            if (MainActivity.BaseFragment.equals("OnlineJobsNServicesFragment")) {
                Intent intent = new Intent("FILTERREQUEST"); //FILTER is a string to identify this intent
                intent.putExtra(Constant.REQUEST_ID, requestId);
                intent.putExtra(Constant.TITLE, title);
                intent.putExtra(Constant.MSG, body);
                intent.putExtra(Constant.TYPE, type);
                sendBroadcast(intent);
            }
            sendRequestDetailsNotification(title, requestId, body);
        } else {
            sendRequestDetailsNotification(title, requestId, body);
            Log.e(TAG, "sendChatNotification: ");
        }
    }

    private void ServiceProgressNotification(RemoteMessage remoteMessage, String title, String body, String type,String state) {
        int requestId;
        requestId = Integer.parseInt(remoteMessage.getData().get("requestId").toString());
        if (className.equals("ActivityMyJobServiceDetailsLayout") || className.equals("ActivityMyServiceJobsChargeDetails") || className.equals("ActivityRequestStatus")) {
            Intent intent = new Intent("FILTERSERVICEPROGRESS"); //FILTER is a string to identify this intent
            intent.putExtra(Constant.REQUEST_ID, requestId);
            intent.putExtra("STATE", state);
            intent.putExtra(Constant.TITLE, title);
            intent.putExtra(Constant.MSG, body);
            intent.putExtra(Constant.TYPE, type);
            sendBroadcast(intent);
        } else if (className.equals("MainActivity")) {
            if (MainActivity.BaseFragment.equals("FragmentMyJobs") || MainActivity.BaseFragment.equals("FragmentMyServices")) {
                Intent intent = new Intent("FILTERSERVICEPROGRESS"); //FILTER is a string to identify this intent
                intent.putExtra(Constant.REQUEST_ID, requestId);
                intent.putExtra("STATE", state);
                intent.putExtra(Constant.TITLE, title);
                intent.putExtra(Constant.MSG, body);
                intent.putExtra(Constant.TYPE, type);
                sendBroadcast(intent);
            }
            sendServiceProgressDetailsNotification(title, requestId, body,state);
        } else {
            sendServiceProgressDetailsNotification(title, requestId, body,state);
            Log.e(TAG, "sendChatNotification: ");
        }
    }

    private void sendRequestDetailsNotification(String title, int requestId, String content) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, new Intent(this, ActivityServiceDetails.class)
                        .putExtra(Constant.FROM, "notification")
                        .putExtra(Constant.REQUEST_ID, requestId)
                        .putExtra(Constant.TITLE, title)
                        .putExtra(Constant.MSG, content)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendServiceProgressDetailsNotification(String title, int requestId, String content,String state) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, new Intent(this, ActivityMyJobServiceDetailsLayout.class)
                        .putExtra(Constant.FROM, "notification")
                        .putExtra("STATE", state)
                        .putExtra(Constant.REQUEST_ID, requestId)
                        .putExtra(Constant.TITLE, title)
                        .putExtra(Constant.MSG, content)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Abc";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);

        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void chatNotification(RemoteMessage remoteMessage, String title) {
        String rcvUId = remoteMessage.getData().get(Constant.rcvUId).toString();
        String rcvName = remoteMessage.getData().get(Constant.rcvName).toString();
        String rcvPrflImg = remoteMessage.getData().get(Constant.rcvPrflImg).toString();
        String requestId = remoteMessage.getData().get(Constant.requestId).toString();
        String requestType = remoteMessage.getData().get(Constant.requestType).toString();
        String message = remoteMessage.getData().get(Constant.message).toString();
        String senderToken = remoteMessage.getData().get(Constant.rcvFireBaseToken).toString();
        if (className.equals("ChatActivity")) {
            Intent intent = new Intent("FILTERCHAT"); //FILTER is a string to identify this intent
            intent.putExtra(Constant.FROM, "notification");
            intent.putExtra(Constant.rcvUId, rcvUId);
            intent.putExtra(Constant.rcvName, rcvName);
            intent.putExtra(Constant.rcvPrflImg, rcvPrflImg);
            intent.putExtra(Constant.requestId, requestId);
            intent.putExtra(Constant.requestType, requestType);
            intent.putExtra(Constant.message, message);
            intent.putExtra(Constant.notyficationTitle, title);
            intent.putExtra(Constant.rcvFireBaseToken, senderToken);
            sendBroadcast(intent);
        } else if (className.equals("MainActivity")) {
            if (MainActivity.BaseFragment.equals("ChatFragment")) {
                Intent intent = new Intent("FILTERCHAT"); //FILTER is a string to identify this intent
                intent.putExtra(Constant.FROM, "");
                intent.putExtra(Constant.rcvUId, rcvUId);
                intent.putExtra(Constant.rcvName, rcvName);
                intent.putExtra(Constant.rcvPrflImg, rcvPrflImg);
                intent.putExtra(Constant.requestId, requestId);
                intent.putExtra(Constant.requestType, requestType);
                intent.putExtra(Constant.message, message);
                intent.putExtra(Constant.notyficationTitle, title);
                intent.putExtra(Constant.rcvFireBaseToken, senderToken);
                sendBroadcast(intent);
            }
            sendChatNotification(rcvUId, rcvName, rcvPrflImg, requestId, requestType, message, title, senderToken);
        } else {
            sendChatNotification(rcvUId, rcvName, rcvPrflImg, requestId, requestType, message, title, senderToken);
            //Log.e(TAG, "sendChatNotification: ");
        }
    }

    private void sendChatNotification(String rcvUId, String rcvName, String rcvPrflImg, String requestId, String requestType, String message, String title, String senderToken) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, new Intent(this, ChatActivity.class)
                        .putExtra(Constant.FROM, "notification")
                        .putExtra(Constant.rcvUId, rcvUId)
                        .putExtra(Constant.rcvName, rcvName)
                        .putExtra(Constant.rcvPrflImg, rcvPrflImg)
                        .putExtra(Constant.requestId, requestId)
                        .putExtra(Constant.requestType, requestType)
                        .putExtra(Constant.message, message)
                        .putExtra(Constant.notyficationTitle, title)
                        .putExtra(Constant.rcvFireBaseToken, senderToken)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_ONE_SHOT);

        /*Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);*/

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Abc";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.filling_your_inbox))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);

        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotification(String title, String message) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, new Intent(this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_ONE_SHOT);

        /*Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);*/

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Abc";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.filling_your_inbox))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}