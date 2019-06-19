package com.cara2v.chat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cara2v.Interface.ChatAdapterClickListner;
import com.cara2v.R;
import com.cara2v.fcm_services.FcmNotificationBuilder;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.ui.activity.ActivityMyJobServiceDetailsLayout;
import com.cara2v.ui.activity.ActivityServiceDetails;
import com.cara2v.ui.activity.CropActivity;
import com.cara2v.ui.activity.MainActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.ImageRotator;
import com.cara2v.util.ImageUtil;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.zoomage.ZoomageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import static com.cara2v.util.ImagePicker.decodeBitmap;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_PHOTO_PICKER = 1;
    private static final int RC_FILE_PICKER = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private RelativeLayout activity_chat;
    //  private TextView userName;
    private TextView noDataTxt;

    private ImageView send, imageUpload, attachedButton, tabBlockIcon, tabDeleteIcon;
    private ProgressDialog mProgressDialog;
    private String rcvName = "", rcvUId = "", mUid = "", mName = "", rcvFbTkn = "", mFbTkn = "", rcvPrflImg = "", mPrflImg = "", requestId = "", requestType = "", chatRoom = "";
    private String TAG = "ChatActivity";
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private FirebaseStorage mstorage;
    private StorageReference storageRef;
    private FirebaseApp app;
    private ProgressBar progressbar;
    private boolean isCamera = false;
    private String s = "1";

    private ImageView menuBtn;
    private TextView titleTxt;
    private Gson gson = new Gson();
    private int userType = 0;
    private int height = 0;
    private int width = 0;

    private final String BLOCKBYNONE = "blockByNone";
    private final String BLOCKBYME = "blockByMe";
    private final String BLOCKBYOPP = "blockByOpp";
    private final String BLOCKBYBOTH = "blockByBoth";
    private String blockStatus = BLOCKBYNONE;
    private String from = "";

    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //from = intent.getStringExtra(Constant.FROM);
            String notiRcvUId = intent.getStringExtra(Constant.rcvUId);
            String notiRcvName = intent.getStringExtra(Constant.rcvName);
            String notiRcvPrflImg = intent.getStringExtra(Constant.rcvPrflImg);
            String notiRequestId = intent.getStringExtra(Constant.requestId);
            String notiRequestType = intent.getStringExtra(Constant.requestType);
            String notiMsg = intent.getStringExtra(Constant.message);
            String notiTitle = intent.getStringExtra(Constant.notyficationTitle);
            String notiFbToken = intent.getStringExtra(Constant.rcvFireBaseToken);

            titleTxt.setText(notiRcvName);
            if (TextUtils.equals(notiRcvUId, rcvUId)) {
                requestId = notiRequestId;
                requestType = notiRequestType;
            } else {
                sendChatNotification(notiRcvUId, notiRcvName, notiRcvPrflImg, notiRequestId, notiRequestType, notiMsg, notiTitle, notiFbToken);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle = getIntent().getExtras();

        //receverDetails
        from = bundle.getString(Constant.FROM);
        rcvUId = bundle.getString(Constant.rcvUId);
        rcvName = bundle.getString(Constant.rcvName);
        rcvFbTkn = bundle.getString(Constant.rcvFireBaseToken);
        rcvPrflImg = bundle.getString(Constant.rcvPrflImg);
        requestId = bundle.getString(Constant.requestId);
        requestType = bundle.getString(Constant.requestType);
        if (from == null) from = "notification";

        //myDetails
        String userInfoJson = PreferenceConnector.readString(ChatActivity.this, PreferenceConnector.userInfoJson, "");
        SignInRespoce signInRespoce = gson.fromJson(userInfoJson, SignInRespoce.class);
        mUid = String.valueOf(signInRespoce.getData().get_id());
        mName = signInRespoce.getData().getUserName();
        mFbTkn = FirebaseInstanceId.getInstance().getToken();
        mPrflImg = signInRespoce.getData().getProfileImage();

        app = FirebaseApp.getInstance();
        mstorage = FirebaseStorage.getInstance(app);
        init();

    }

    private void init() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        mRecyclerViewChat = findViewById(R.id.recycler_view_chat);
        activity_chat = findViewById(R.id.activity_chat);
        mETxtMessage = findViewById(R.id.edit_text_message);

        progressbar = findViewById(R.id.progressbar);
        noDataTxt = findViewById(R.id.noDataTxt);
        findViewById(R.id.send).setOnClickListener(this);

        findViewById(R.id.image_button).setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loding...");
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setIndeterminate(true);

        menuBtn = findViewById(R.id.menuBtn);
        tabBlockIcon = findViewById(R.id.tabBlockIcon);
        tabDeleteIcon = findViewById(R.id.tabRightIcon);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        tabDeleteIcon.setImageResource(R.drawable.delete);
        tabBlockIcon.setVisibility(View.VISIBLE);
        tabDeleteIcon.setVisibility(View.VISIBLE);
        menuBtn.setOnClickListener(this);
        tabBlockIcon.setOnClickListener(this);
        tabDeleteIcon.setOnClickListener(this);
        mETxtMessage.setOnClickListener(this);
        titleTxt.setText(rcvName);
        getMessageFromFirebaseUser(mUid, rcvUId);
        // getblockStatus();
    }

    private void sendMessage(final String message, final String msgType) {
        if (!message.equals("")) {
            String msg = "";
            mETxtMessage.setText("");
            //   Constant.hideSoftKeyboard(ChatActivity.this);
            final ChatBean chatBean = new ChatBean();
            chatBean.deleteby = "";
            if (msgType.equals("text")){
                chatBean.image=0;
                chatBean.imageUrl="";
                chatBean.message = message;
                msg = message;
            }else {
                chatBean.image=1;
                chatBean.imageUrl=message;
                chatBean.message = "";
                msg = "Image";
            }

            chatBean.name = mName;
            chatBean.uid = mUid;
            chatBean.timestamp = ServerValue.TIMESTAMP;

            if (Integer.parseInt(mUid)<Integer.parseInt(rcvUId)){
                chatRoom=mUid + "_" + rcvUId;
            }else {
                chatRoom=rcvUId + "_" + mUid;
            }

            /*final String room_type_1 = mUid + "_" + rcvUId;
            final String room_type_2 = rcvUId + "_" + mUid;*/

            final String finalMsg = msg;
            databaseReference.child(Constant.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(chatRoom)) {
                        Log.e(TAG, "sendMessageToFirebaseUser: " + chatRoom + " exists");
                        DatabaseReference gen_key = databaseReference.child(Constant.ARG_CHAT_ROOMS).child(chatRoom).getRef().push();
                        //chatRoom = room_type_1;
                        gen_key.setValue(chatBean);
                    }/* else if (dataSnapshot.hasChild(room_type_2)) {
                        Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                        DatabaseReference gen_key = databaseReference.child(Constant.ARG_CHAT_ROOMS).child(room_type_2).getRef().push();

                        chatRoom = room_type_2;
                        gen_key.setValue(chatBean);
                    } */else {
                        Log.e(TAG, "sendMessageToFirebaseUser: success");
                        DatabaseReference gen_key = databaseReference.child(Constant.ARG_CHAT_ROOMS).child(chatRoom).getRef().push();

                       // chatRoom = room_type_1;
                        gen_key.setValue(chatBean);
                        getMessageFromFirebaseUser(mUid, rcvUId);
                    }

                    sendmyChatHistory(chatBean, databaseReference,msgType);
                    sendOppChatHistory(chatBean, databaseReference,msgType);
                    sendPushNotificationToReceiver(chatBean.name, chatBean.name,
                            finalMsg,
                            chatBean.uid,
                            FirebaseInstanceId.getInstance().getToken(),
                            chatRoom,
                            requestId,
                            requestType,
                            rcvPrflImg,
                            rcvFbTkn);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //   mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
                }
            });
        }
    }

    private void sendOppChatHistory(ChatBean chatBean, DatabaseReference databaseReference,String msgType) {

        ChatHistoryBean oppChathistory = new ChatHistoryBean();
        oppChathistory.deleteby = "";
        /*oppChathistory.firebaseId = "";
        oppChathistory.firebaseToken = mFbTkn;*/
        if (msgType.equals("text")){
            oppChathistory.image=0;
        }else {
            oppChathistory.image=1;
        }
        oppChathistory.message = chatBean.message;
        oppChathistory.imageUrl = chatBean.imageUrl;
        oppChathistory.name = mName;
        oppChathistory.profilePic = mPrflImg;
        oppChathistory.uid = mUid;
        oppChathistory.timestamp = chatBean.timestamp;
        databaseReference.child(Constant.ARG_CHAT_HISTORY).getRef().child(rcvUId).child(mUid).setValue(oppChathistory);
    }

    private void sendmyChatHistory(ChatBean chatBean, DatabaseReference databaseReference,String msgType) {
        ChatHistoryBean myChathistory = new ChatHistoryBean();
        myChathistory.deleteby = "";
        myChathistory.image=0;
        if (msgType.equals("text")){
            myChathistory.image=0;
        }else {
            myChathistory.image=1;
        }
        /*myChathistory.firebaseId = "";
        myChathistory.firebaseToken = rcvFbTkn;*/
        myChathistory.message = chatBean.message;
        myChathistory.imageUrl = chatBean.imageUrl;
        myChathistory.name = rcvName;
        myChathistory.profilePic = rcvPrflImg;
        myChathistory.uid = rcvUId;
        myChathistory.timestamp = chatBean.timestamp;
        databaseReference.child(Constant.ARG_CHAT_HISTORY).getRef().child(mUid).child(rcvUId).setValue(myChathistory);
    }

    private void sendPushNotificationToReceiver(String title,
                                                String userName,
                                                String message,
                                                String uid,
                                                String senderToken,
                                                String chatRoomId,
                                                String requestId,
                                                String requestType,
                                                String rcvPrflImg,
                                                String rcvFbTkn) {
        FcmNotificationBuilder.initialize()
                .title(title)
                .message(message)
                .username(userName)
                .uid(uid)
                .firebaseToken(senderToken)
                .profileImage(rcvPrflImg)
                .requestId(requestId)
                .requestType(requestType)
                .chatRoomId(chatRoomId).receiverFirebaseToken(rcvFbTkn).send();
    }

    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
        if (Integer.parseInt(senderUid)<Integer.parseInt(receiverUid)){
            chatRoom=senderUid + "_" + receiverUid;
        }else {
            chatRoom=receiverUid + "_" + senderUid;
        }
       /* final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;*/
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constant.ARG_CHAT_ROOMS).getRef().orderByKey().
                limitToLast(25).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if (dataSnapshot.hasChild(chatRoom)) {
                   /* Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    chatRoom = room_type_1;*/
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constant.ARG_CHAT_ROOMS)
                            .child(chatRoom).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ChatBean chatBean = dataSnapshot.getValue(ChatBean.class);
                            /// mOnGetMessagesListener.onGetMessagesSuccess(chatBean);
                            if (mChatRecyclerAdapter == null) {
                                mChatRecyclerAdapter = new ChatRecyclerAdapter(ChatActivity.this, new ArrayList<ChatBean>(), new ChatAdapterClickListner() {
                                    @Override
                                    public void clickedItemPosition(String url) {
                                        showZoomImage(url);
                                    }
                                });
                                mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
                            }
                            mChatRecyclerAdapter.add(chatBean);
                            mRecyclerViewChat.scrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
                            noDataTxt.setVisibility(View.GONE);
                            progressbar.setVisibility(View.GONE);
                            getblockStatus();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressbar.setVisibility(View.GONE);
                        }
                    });
                /*} else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    chatRoom = room_type_2;
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constant.ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ChatBean chatBean = dataSnapshot.getValue(ChatBean.class);
                            // mOnGetMessagesListener.onGetMessagesSuccess(chatBean);
                            if (mChatRecyclerAdapter == null) {
                                mChatRecyclerAdapter = new ChatRecyclerAdapter(ChatActivity.this, new ArrayList<ChatBean>(), new ChatAdapterClickListner() {
                                    @Override
                                    public void clickedItemPosition(String url) {
                                        showZoomImage(url);
                                    }
                                });
                                mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
                            }
                            mChatRecyclerAdapter.add(chatBean);
                            mRecyclerViewChat.scrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
                            getblockStatus();
                            //          mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount());
                            noDataTxt.setVisibility(View.GONE);
                            progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //   mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                            progressbar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                    progressbar.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //  mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == Constant.REQUEST_CAMERA) {

                Bitmap bm = null;
                File imageFile = getTemporalFile(ChatActivity.this);
                Uri photoURI = Uri.fromFile(imageFile);

                bm = getImageResized(ChatActivity.this, photoURI);
                int rotation = ImageRotator.getRotation(ChatActivity.this, photoURI, isCamera);
                bm = ImageRotator.rotate(bm, rotation);

                File file = new File(ChatActivity.this.getExternalCacheDir(), UUID.randomUUID() + ".jpg");
                Uri imageUri = FileProvider.getUriForFile(ChatActivity.this, getApplicationContext().getPackageName() + ".fileprovider", file);


                if (file != null) {
                    try {
                        OutputStream outStream = null;
                        outStream = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.PNG, 80, outStream);
                        outStream.flush();
                        outStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /*Intent i = new Intent(ChatActivity.this, CropActivity.class);
                i.putExtra("Image", imageUri.toString());
                i.putExtra("FROM", "gallery");
                startActivityForResult(i, 111);*/

                 sendFileFireBase(imageUri);

            } else if (requestCode == Constant.SELECT_FILE) {
                Uri selectedImageUri = data.getData();
               /* Intent i = new Intent(ChatActivity.this, CropActivity.class);
                i.putExtra("Image", selectedImageUri.toString());
                i.putExtra("FROM", "gallery");
                startActivityForResult(i, 111);*/
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bitmap = ImageUtil.decodeFile(selectedImagePath);
                File destination = getFile(bitmap);
                Uri uri = Uri.fromFile(destination);


                sendFileFireBase(uri);
            }
        } else if (requestCode == 111 && resultCode == 111) {

            String result = data.getStringExtra("ImageURI");
            sendFileFireBase(Uri.fromFile(new File(result)));
                /*//getRealPathFromUri(AddVehicleInfoActivity.this,Uri.parse(result));
                try {
                    File file = new File(result);
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getPath());*//*BitmapFactory.decodeFile(filepath);*//*
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    File imagePath = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        if (imagePath.createNewFile()) {
                            fo = new FileOutputStream(imagePath);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            imageFiles.clear();
                            imageFiles.add(imagePath);
                            postImg.setVisibility(View.VISIBLE);
                            postImg.setImageURI(Uri.parse(result));
                        } else {
                            Toast.makeText(mContext, "Image not saved Try Again", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("TAG", e.getLocalizedMessage());
                    }
                } catch (Exception e) {

                }*/
        }

    }

    @NonNull
    private File getFile(Bitmap bm) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }

    private void sendFileFireBase(Uri selectedImageUri) {
        progressbar.setVisibility(View.VISIBLE);
        storageRef = mstorage.getReference("chat_photos_" + getString(R.string.app_name));
        final StorageReference photoRef = storageRef.child(selectedImageUri.getLastPathSegment());
        photoRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri fireBaseUri = taskSnapshot.getDownloadUrl();
                Log.e("TAG", "onSuccess: ");
                progressbar.setVisibility(View.GONE);
                sendMessage(fireBaseUri.toString(),"image");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressbar.setVisibility(View.GONE);
                        Log.e("TAG", "onFailure: " + e.getMessage());
                        Toast.makeText(ChatActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        Log.e(TAG, "onProgress: " + progress);
                    }
                });
    }


    private void sendChatNotification(String rcvUId, String rcvName, String rcvPrflImg, String requestId, String requestType, String message, String title, String notiFbToken) {
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
                        .putExtra(Constant.rcvFireBaseToken, notiFbToken)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Abc";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
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

    /*-----------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Constant.SELECT_FILE);
                } else {
                    Toast.makeText(ChatActivity.this, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constant.REQUEST_CAMERA);
                } else {
                    Toast.makeText(ChatActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatActivity.this.registerReceiver(receiver, new IntentFilter("FILTERCHAT"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        ChatActivity.this.unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                switch (blockStatus) {
                    case BLOCKBYNONE:
                        if (!TextUtils.isEmpty(mETxtMessage.getText().toString().toString().trim())) {
                            sendMessage(mETxtMessage.getText().toString().trim(),"text");
                        }
                        break;
                    case BLOCKBYME:
                        Constant.snackbar(activity_chat, "You blocked " + rcvName + ". Can't send any message");
                        break;
                    case BLOCKBYOPP:
                        Constant.snackbar(activity_chat, "You are blocked by " + rcvName + ". Can't send any message");
                        break;
                    case BLOCKBYBOTH:
                        Constant.snackbar(activity_chat, "You blocked " + rcvName + ". Can't send any message");
                        break;
                }

                break;
            case R.id.image_button:
                switch (blockStatus) {
                    case BLOCKBYNONE:
                        selectImage();
                        break;
                    case BLOCKBYME:
                        Constant.snackbar(activity_chat, "You blocked " + rcvName + ". Can't send any image");
                        break;
                    case BLOCKBYOPP:
                        Constant.snackbar(activity_chat, "You are blocked by " + rcvName + ". Can't send any image");
                        break;
                    case BLOCKBYBOTH:
                        Constant.snackbar(activity_chat, "You blocked " + rcvName + ". Can't send any message");
                        break;
                }

                break;
            case R.id.menuBtn:
                Constant.hideSoftKeyboard(this);
                setBackSpace();
                break;
            case R.id.tabBlockIcon:
                switch (blockStatus) {
                    case BLOCKBYNONE:
                        showBlockAlertDialog("block");
                        break;
                    case BLOCKBYME:
                        showBlockAlertDialog("unblock");
                        break;
                    case BLOCKBYOPP:
                        showBlockAlertDialog("block");
                        break;
                    case BLOCKBYBOTH:
                        showBlockAlertDialog("unblock");
                        break;
                }
                break;
        }
    }

    private void doBlock(final String s) {
       /* final String room_type_1 = mUid + "_" + rcvUId;
        final String room_type_2 = rcvUId + "_" + mUid;*/
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constant.ARG_CHAT_BLOCK).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*if (dataSnapshot.hasChild(room_type_1)) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constant.ARG_CHAT_BLOCK)
                            .child(room_type_1)
                            .child(Constant.ARG_CHAT_BLOCK_KEY)
                            .setValue(s);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constant.ARG_CHAT_BLOCK)
                            .child(room_type_2)
                            .child(Constant.ARG_CHAT_BLOCK_KEY)
                            .setValue(s);
                } else {*/
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constant.ARG_CHAT_BLOCK)
                            .child(chatRoom)
                            .child(Constant.ARG_CHAT_BLOCK_KEY)
                            .setValue(s);
              //  }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getblockStatus() {
        /*final String room_type_1 = mUid + "_" + rcvUId;
        final String room_type_2 = rcvUId + "_" + mUid;*/
        /*final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constant.ARG_CHAT_BLOCK).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "blockUser: " + room_type_1 + " exists");
                    databaseReference.child(Constant.ARG_CHAT_BLOCK).child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String blockBy = dataSnapshot.getValue().toString();
                            Log.e("BlockBy", blockBy);

                            if (blockBy.equals("")) {
                                blockStatus = BLOCKBYNONE;
                            } else if (blockBy.equals(mUid)) {
                                blockStatus = BLOCKBYME;
                            } else if (blockBy.equals(rcvUId)) {
                                blockStatus = BLOCKBYOPP;
                            } else if (blockBy.equals("Both")) {
                                blockStatus = BLOCKBYBOTH;
                            }
                            manageblockButton(blockStatus);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            String blockBy = dataSnapshot.getValue().toString();
                            Log.e("BlockBy", blockBy);

                            if (blockBy.equals("")) {
                                blockStatus = BLOCKBYNONE;
                            } else if (blockBy.equals(mUid)) {
                                blockStatus = BLOCKBYME;
                            } else if (blockBy.equals(rcvUId)) {
                                blockStatus = BLOCKBYOPP;
                            } else if (blockBy.equals("Both")) {
                                blockStatus = BLOCKBYBOTH;
                            }
                            manageblockButton(blockStatus);
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (dataSnapshot.hasChild(room_type_2)) {

                    databaseReference.child(Constant.ARG_CHAT_BLOCK).child(room_type_2).addChildEventListener(new ChildEventListener() {


                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String blockBy = dataSnapshot.getValue().toString();
                            Log.e("BlockBy", blockBy);

                            if (blockBy.equals("")) {
                                blockStatus = BLOCKBYNONE;
                            } else if (blockBy.equals(mUid)) {
                                blockStatus = BLOCKBYME;
                            } else if (blockBy.equals(rcvUId)) {
                                blockStatus = BLOCKBYOPP;
                            } else if (blockBy.equals("Both")) {
                                blockStatus = BLOCKBYBOTH;
                            }
                            manageblockButton(blockStatus);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            String blockBy = dataSnapshot.getValue().toString();
                            Log.e("BlockBy", blockBy);

                            if (blockBy.equals("")) {
                                blockStatus = BLOCKBYNONE;
                            } else if (blockBy.equals(mUid)) {
                                blockStatus = BLOCKBYME;
                            } else if (blockBy.equals(rcvUId)) {
                                blockStatus = BLOCKBYOPP;
                            } else if (blockBy.equals("Both")) {
                                blockStatus = BLOCKBYBOTH;
                            }
                            manageblockButton(blockStatus);
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //   mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });*/

        databaseReference.child(Constant.ARG_CHAT_BLOCK).child(chatRoom).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String blockBy = dataSnapshot.getValue().toString();
                Log.e("BlockBy", blockBy);

                if (blockBy.equals("")) {
                    blockStatus = BLOCKBYNONE;
                } else if (blockBy.equals(mUid)) {
                    blockStatus = BLOCKBYME;
                } else if (blockBy.equals(rcvUId)) {
                    blockStatus = BLOCKBYOPP;
                } else if (blockBy.equals("Both")) {
                    blockStatus = BLOCKBYBOTH;
                }
                manageblockButton(blockStatus);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String blockBy = dataSnapshot.getValue().toString();
                Log.e("BlockBy", blockBy);

                if (blockBy.equals("")) {
                    blockStatus = BLOCKBYNONE;
                } else if (blockBy.equals(mUid)) {
                    blockStatus = BLOCKBYME;
                } else if (blockBy.equals(rcvUId)) {
                    blockStatus = BLOCKBYOPP;
                } else if (blockBy.equals("Both")) {
                    blockStatus = BLOCKBYBOTH;
                }
                manageblockButton(blockStatus);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void manageblockButton(String blockStatus) {

        switch (blockStatus) {
            case BLOCKBYNONE:
                tabBlockIcon.setImageResource(R.drawable.un_block);
                break;
            case BLOCKBYME:
                tabBlockIcon.setImageResource(R.drawable.block);
                break;
            case BLOCKBYOPP:
                tabBlockIcon.setImageResource(R.drawable.un_block);
                break;
            case BLOCKBYBOTH:
                tabBlockIcon.setImageResource(R.drawable.block);
                break;
        }
    }


    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "From Gallery", "Cancel"};
        AlertDialog.Builder alert = new AlertDialog.Builder(ChatActivity.this);
        alert.setTitle("Add Photo");
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(
                                    new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constant.MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            picImage();
                            dialog.dismiss();
                        }
                    } else {
                        picImage();
                        dialog.dismiss();
                    }


                } else if (items[item].equals("From Gallery")) {

                    if (Build.VERSION.SDK_INT >= 23) {

                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, Constant.SELECT_FILE);
                            isCamera = false;
                            dialog.dismiss();
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, Constant.SELECT_FILE);
                        isCamera = false;
                        dialog.dismiss();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    private void picImage() {

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        Uri uri = FileProvider.getUriForFile(ChatActivity.this, getApplicationContext().getPackageName()
                + ".fileprovider", getTemporalFile(ChatActivity.this));
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        takePhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        takePhotoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTemporalFile(context)));
        startActivityForResult(takePhotoIntent, Constant.REQUEST_CAMERA);
        isCamera = true;


    }

    private static File getTemporalFile(Context context) {
        return new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
    }


    public static final int PICK_IMAGE_REQUEST_CODE = 234; // the number doesn't matter
    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    private static final int DEFAULT_MIN_HEIGHT_QUALITY = 400;
    private static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;
    private static int minHeightQuality = DEFAULT_MIN_HEIGHT_QUALITY;
    private static final String TEMP_IMAGE_NAME = "tempImage.jpg";

    public void showZoomImage(String url) {
        Log.e(TAG, "showZoomImage: " + url);
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.zoom_image);
        dialog.getWindow().setLayout((width * 10) / 11, (height * 4) / 5);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        final ZoomageView imageView = dialog.findViewById(R.id.zoomImageView);
        final ProgressBar smlProgress = dialog.findViewById(R.id.smlProgress);
        final ImageView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        Picasso.with(this).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                smlProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    public static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            i++;
        } while (bm != null
                && (bm.getWidth() < minWidthQuality || bm.getHeight() < minHeightQuality)
                && i < sampleSizes.length);
        Log.i("FragmentFeed", "Final bitmap width = " + (bm != null ? bm.getWidth() : "No final bitmap"));
        return bm;
    }

    private void setBackSpace() {
        if (from.equals("notification")) {
            if (TextUtils.isEmpty(requestId)) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("GO", "chat");
                startActivity(intent);
                finish();
            } else {
                Intent intent = null;
                switch (requestType) {
                    case Constant.reqPending:
                        intent = new Intent(ChatActivity.this, ActivityServiceDetails.class);
                        intent.putExtra(Constant.FROM, "notification");
                        intent.putExtra(Constant.REQUEST_ID, Integer.valueOf(requestId));
                        startActivity(intent);
                        finish();
                        break;
                    case Constant.reqConfirm:
                        intent = new Intent(ChatActivity.this, ActivityMyJobServiceDetailsLayout.class);
                        intent.putExtra("STATE", "pending");
                        intent.putExtra(Constant.FROM, "notification");
                        intent.putExtra(Constant.REQUEST_ID, Integer.valueOf(requestId));
                        startActivity(intent);
                        finish();
                        break;
                    case Constant.reqComplete:
                        intent = new Intent(ChatActivity.this, ActivityMyJobServiceDetailsLayout.class);
                        intent.putExtra("STATE", "complete");
                        intent.putExtra(Constant.FROM, "notification");
                        intent.putExtra(Constant.REQUEST_ID, Integer.valueOf(requestId));
                        startActivity(intent);
                        finish();
                        break;
                    case Constant.reqNone:
                        intent = new Intent(ChatActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("GO", "chat");
                        startActivity(intent);
                        finish();
                        break;
                }

            }
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setBackSpace();
    }

    public void showBlockAlertDialog(String state) {

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(this);
        builder1.setTitle("Alert !!");
        builder1.setMessage("Do you want to " + state + " " + rcvName + "?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        switch (blockStatus) {
                            case BLOCKBYNONE:
                                doBlock(mUid);
                                break;
                            case BLOCKBYME:
                                doBlock("");
                                break;
                            case BLOCKBYOPP:
                                doBlock("Both");
                                break;
                            case BLOCKBYBOTH:
                                doBlock(rcvUId);
                                break;
                        }
                    }
                });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

            }
        });
        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
