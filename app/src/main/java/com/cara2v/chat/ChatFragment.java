package com.cara2v.chat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.R;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class ChatFragment extends Fragment /*implements View.OnClickListener*/ {
    List<ChatHistoryBeanForList> chatHistoryBeanList = new ArrayList<>();
    List<ChatUserBean> users = new ArrayList<>();
    UserListingRecyclerAdapter mUserListingRecyclerAdapter;

    private TextView noDataTxt;
    private ProgressBar progressbar;
    private RecyclerView mRecyclerViewAllUserListing;
    private String myUId = "";

    private Gson gson = new Gson();
    private Context mContext;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mContext.registerReceiver(receiver, new IntentFilter("FILTERCHAT"));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        progressbar.setVisibility(View.VISIBLE);
        String userInfoJson = PreferenceConnector.readString(mContext, PreferenceConnector.userInfoJson, "");
        SignInRespoce signInRespoce = gson.fromJson(userInfoJson, SignInRespoce.class);
        myUId = String.valueOf(signInRespoce.getData().get_id());
        getAllUsersFromFirebase();
    }


    private void initView(View view) {
        mRecyclerViewAllUserListing = view.findViewById(R.id.recycler_view_all_user_listing);
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(mContext, chatHistoryBeanList, new MyOnCheckListioner() {
            @Override
            public void OnCheck(int position) {
                ChatHistoryBeanForList chatHistoryBeanForList = chatHistoryBeanList.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constant.FROM, "");
                intent.putExtra(Constant.rcvUId, chatHistoryBeanForList.uid);
                intent.putExtra(Constant.rcvName, chatHistoryBeanForList.name);
                intent.putExtra(Constant.rcvFireBaseToken, chatHistoryBeanForList.firebaseToken);
                intent.putExtra(Constant.rcvPrflImg, chatHistoryBeanForList.profilePic);
                intent.putExtra(Constant.requestId, "");
                intent.putExtra(Constant.requestType, "");
                startActivity(intent);
            }
        });
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        noDataTxt = view.findViewById(R.id.noDataTxt);
        progressbar = view.findViewById(R.id.progressbar);
    }

    private void shortList() {

        Collections.sort(chatHistoryBeanList, new Comparator<ChatHistoryBeanForList>() {
            @Override
            public int compare(ChatHistoryBeanForList a1, ChatHistoryBeanForList a2) {
                if (a1.timestamp == null || a2.timestamp == null) return -1;
                else {
                    Long long1 = Long.parseLong(String.valueOf(a1.timestamp));
                    Long long2 = Long.parseLong(String.valueOf(a2.timestamp));
                    return long2.compareTo(long1);
                }
            }
        });
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    public void getAllUsersFromFirebase() {
        users.clear();
        chatHistoryBeanList.clear();

        FirebaseDatabase.getInstance().getReference().child(Constant.ARG_CHAT_HISTORY).child(myUId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressbar.setVisibility(View.GONE);
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                ChatHistoryBean historyBean = dataSnapshot.getValue(ChatHistoryBean.class);
                getSenderProfileImage(historyBean);
                noDataTxt.setVisibility(View.GONE);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                progressbar.setVisibility(View.GONE);
                ChatHistoryBean historyBean = dataSnapshot.getValue(ChatHistoryBean.class);
                getSenderProfileImage(historyBean);
                noDataTxt.setVisibility(View.GONE);
                /*boolean isfind = false;
                if (chatHistoryBeanList.size() > 0) {
                    for (int i = 0; i < chatHistoryBeanList.size(); i++) {
                        if (chatHistoryBeanList.get(i).uid.equals(historyBean.uid)) {
                            chatHistoryBeanList.set(i, historyBean);
                            isfind = true;
                            break;
                        }
                    }
                    if (!isfind) chatHistoryBeanList.add(historyBean);
                } else {
                    chatHistoryBeanList.add(historyBean);
                }

                shortList();*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
            }
        });
        if (chatHistoryBeanList.size()==0){
            progressbar.setVisibility(View.GONE);
            noDataTxt.setVisibility(View.VISIBLE);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String notiRcvUId = intent.getStringExtra(Constant.rcvUId);
            String notiRcvName = intent.getStringExtra(Constant.rcvName);
            String notiRcvPrflImg = intent.getStringExtra(Constant.rcvPrflImg);
            String notiRequestId = intent.getStringExtra(Constant.requestId);
            String notiRequestType = intent.getStringExtra(Constant.requestType);
            String notiMsg = intent.getStringExtra(Constant.message);
            String notiTitle = intent.getStringExtra(Constant.notyficationTitle);
            String notiFbToken = intent.getStringExtra(Constant.rcvFireBaseToken);


            sendChatNotification(notiRcvUId, notiRcvName, notiRcvPrflImg, notiRequestId, notiRequestType, notiMsg, notiTitle, notiFbToken);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        mContext.unregisterReceiver(receiver);
    }

    private void sendChatNotification(String rcvUId, String rcvName, String rcvPrflImg, String requestId, String requestType, String message, String title, String notiFbToken) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, iUniqueId, new Intent(mContext, ChatActivity.class)
                        .putExtra(Constant.FROM, "notification")
                        .putExtra(Constant.rcvUId, rcvUId)
                        .putExtra(Constant.rcvName, rcvName)
                        .putExtra(Constant.rcvPrflImg, rcvPrflImg)
                        .putExtra(Constant.requestId, requestId)
                        .putExtra(Constant.requestType, requestType)
                        .putExtra(Constant.message, message)
                        .putExtra(Constant.notyficationTitle, title)
                        .putExtra(Constant.rcvFireBaseToken, notiFbToken)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Abc";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext,CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void getSenderProfileImage(final ChatHistoryBean historyBean) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child(Constant.ARG_USERS)
                .child(historyBean.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressbar.setVisibility(View.GONE);
                final ChatUserBean user = dataSnapshot.getValue(ChatUserBean.class);
                ChatHistoryBeanForList chatHistoryBeanFrList = new ChatHistoryBeanForList();
                chatHistoryBeanFrList.deleteby = historyBean.deleteby;
                chatHistoryBeanFrList.firebaseToken = user.firebaseToken;
                chatHistoryBeanFrList.message = historyBean.message;
                chatHistoryBeanFrList.image = historyBean.image;
                chatHistoryBeanFrList.imageUrl = historyBean.imageUrl;
                chatHistoryBeanFrList.name = user.name;
                chatHistoryBeanFrList.profilePic = user.profilePic;
                chatHistoryBeanFrList.uid = user.uid;
                chatHistoryBeanFrList.timestamp = historyBean.timestamp;

                boolean isfind = false;
                if (chatHistoryBeanList.size() > 0) {
                    for (int i = 0; i < chatHistoryBeanList.size(); i++) {
                        if (chatHistoryBeanList.get(i).uid.equals(user.uid)) {
                            chatHistoryBeanList.set(i, chatHistoryBeanFrList);
                            isfind = true;
                            break;
                        }
                    }
                    if (!isfind) chatHistoryBeanList.add(chatHistoryBeanFrList);
                } else {
                    chatHistoryBeanList.add(chatHistoryBeanFrList);
                }
                progressbar.setVisibility(View.GONE);
                shortList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }
}
