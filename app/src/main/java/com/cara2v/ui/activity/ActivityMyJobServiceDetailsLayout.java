package com.cara2v.ui.activity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.chat.ChatActivity;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.ui.activity.owner.ActivtySentServiceDetailsLayout;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ActivityMyJobServiceDetailsLayout extends Activity implements View.OnClickListener {

    private LinearLayout mapIconLay;
    private CardView providerLay;
    private TextView distanceTxt;
    private TextView mapOwnerTxt;
    private TextView buisnessNameTxt;
    private ImageView mapImg;
    private ImageView mapNevBtn;
    private TextView servicePriceTxt;
    private TextView dayTxt;
    private TextView dateTxt;
    private TextView payDepositeTxt;
    private TextView payDepositeStatus;
    private RelativeLayout vehicleInfoLay;
    private RelativeLayout providerInfoLay;
    private RelativeLayout reqStLay;
    private RelativeLayout mainLayout;
    private RelativeLayout mapLay;
    private RelativeLayout payDepositeLay;
    private RelativeLayout adminLay;
    private TextView reqStTxt;
    private RelativeLayout msgLay;
    private RelativeLayout serviceChargeLay;
    private TextView adminComTxt;
    private TextView descriptionTxt;
    private TextView prvInfoTxt;
    private LinearLayout visibleLayout;
    private LinearLayout priceLay;
    private ProgressBar progressBar;
    private ProgressBar smlProgressBar;
    private View dipositeView;
    private View adminView;
    private Gson gson = new Gson();
    private boolean isResume = false;
    private String requestDetailsJson = "", requestId = "", navLat = "", navLng = "";
    private int userType = 0;
    private int oppoUserId = 0;
    private String oppoUserName = "", oppFbToken = "", opoPrflImg = "";
    private ImageView menuBtn;
    private TextView titleTxt;
    private int height = 0, width = 0;
    private DecimalFormat df = new DecimalFormat("#.##");
    private ServiceDetailsResponce.DataBean.OrderdetailsBean orderdetailsBean;
    private String state = "";
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_job_service_details_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestId = "" + bundle.getInt("requestId");
            state = bundle.getString("STATE");
            Log.e("requestId", requestId);

        }
        userType = PreferenceConnector.readInteger(this, PreferenceConnector.UserType, 0);
        initializeView();
        isResume = true;
        try {
            if (bundle != null) {
                from = bundle.getString(Constant.FROM);
                if (from == null) from = "";
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityMyJobServiceDetailsLayout.this.registerReceiver(receiver, new IntentFilter("FILTERSERVICEPROGRESS"));
        if (isResume) getServiceDetails();
    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        if (userType == Constant.userTypeOwner) {
            titleTxt.setText("Job Details");
        } else if (userType == Constant.uerTypeConsumer) {
            titleTxt.setText("Service Details");
        }

        mainLayout = findViewById(R.id.mainLayout);
        mapIconLay = findViewById(R.id.mapIconLay);
        mapLay = findViewById(R.id.mapLay);
        providerLay = findViewById(R.id.providerLay);
        distanceTxt = findViewById(R.id.distanceTxt);
        mapImg = findViewById(R.id.mapImg);
        mapNevBtn = findViewById(R.id.mapNevBtn);
        servicePriceTxt = findViewById(R.id.servicePriceTxt);
        dayTxt = findViewById(R.id.dayTxt);
        dateTxt = findViewById(R.id.dateTxt);
        mapOwnerTxt = findViewById(R.id.mapOwnerTxt);
        buisnessNameTxt = findViewById(R.id.buisnessNameTxt);
        vehicleInfoLay = findViewById(R.id.vehicleInfoLay);
        providerInfoLay = findViewById(R.id.providerInfoLay);
        reqStLay = findViewById(R.id.reqStLay);
        payDepositeLay = findViewById(R.id.payDepositeLay);
        payDepositeTxt = findViewById(R.id.payDepositeTxt);
        reqStTxt = findViewById(R.id.reqStTxt);
        payDepositeStatus = findViewById(R.id.payDepositeStatus);
        msgLay = findViewById(R.id.msgLay);
        serviceChargeLay = findViewById(R.id.serviceChargeLay);
        priceLay = findViewById(R.id.priceLay);
        adminComTxt = findViewById(R.id.adminComTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        visibleLayout = findViewById(R.id.visibleLayout);
        progressBar = findViewById(R.id.progressBar);
        smlProgressBar = findViewById(R.id.smlProgressBar);
        dipositeView = findViewById(R.id.dipositeView);
        prvInfoTxt = findViewById(R.id.prvInfoTxt);
        adminLay = findViewById(R.id.adminLay);
        adminView = findViewById(R.id.adminView);

        menuBtn.setOnClickListener(this);
        mapNevBtn.setOnClickListener(this);
        vehicleInfoLay.setOnClickListener(this);
        providerInfoLay.setOnClickListener(this);
        reqStLay.setOnClickListener(this);
        msgLay.setOnClickListener(this);
        serviceChargeLay.setOnClickListener(this);

        if (state.equals("complete")) {
            priceLay.setBackgroundColor(ContextCompat.getColor(ActivityMyJobServiceDetailsLayout.this, R.color.colorBlue));
            dayTxt.setTextColor(ContextCompat.getColor(ActivityMyJobServiceDetailsLayout.this, R.color.colorBlue));
            dateTxt.setTextColor(ContextCompat.getColor(ActivityMyJobServiceDetailsLayout.this, R.color.colorBlue));
            reqStTxt.setTextColor(ContextCompat.getColor(ActivityMyJobServiceDetailsLayout.this, R.color.colorBlue));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                if (from.equals("notification")) {
                    Intent intent = new Intent(ActivityMyJobServiceDetailsLayout.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    if (state.equals("complete")) {
                        intent.putExtra("GO", "three");
                    } else if (state.equals("pending")) {
                        intent.putExtra("GO", "four");
                    }
                    startActivity(intent);
                } else {
                    finish();
                }
                break;
            case R.id.mapNevBtn:
                if (!TextUtils.isEmpty(navLat)) {
                    mapNavDialog();
                }
                break;
            case R.id.vehicleInfoLay:
                Intent veechicleIntent = new Intent(ActivityMyJobServiceDetailsLayout.this, ActivityVehicleInfoLayout.class);
                veechicleIntent.putExtra("requestDetailsJson", requestDetailsJson);
                startActivity(veechicleIntent);
                break;
            case R.id.providerInfoLay:
                Intent providerIntent = new Intent(ActivityMyJobServiceDetailsLayout.this, UserProfileLayoutActivity.class);
                providerIntent.putExtra("userId", oppoUserId);
                startActivity(providerIntent);
                break;
            case R.id.reqStLay:
                Intent intentRequestStatus = new Intent(ActivityMyJobServiceDetailsLayout.this, ActivityRequestStatus.class);
                intentRequestStatus.putExtra(Constant.FROM, "");
                intentRequestStatus.putExtra("requestDetailsJson", requestDetailsJson);
                startActivity(intentRequestStatus);
                break;
            case R.id.serviceChargeLay:
                Intent intent = new Intent(ActivityMyJobServiceDetailsLayout.this, ActivityMyServiceJobsChargeDetails.class);
                String jsonBean = gson.toJson(orderdetailsBean);
                intent.putExtra("orderBean", jsonBean);
                intent.putExtra("requestDetailsJson", requestDetailsJson);
                startActivity(intent);
                break;
            case R.id.msgLay:
                Intent intentChat = new Intent(ActivityMyJobServiceDetailsLayout.this, ChatActivity.class);
                intentChat.putExtra(Constant.FROM, "");
                intentChat.putExtra(Constant.rcvUId, String.valueOf(oppoUserId));
                intentChat.putExtra(Constant.rcvName, oppoUserName);
                intentChat.putExtra(Constant.rcvFireBaseToken, oppFbToken);
                intentChat.putExtra(Constant.rcvPrflImg, opoPrflImg);
                intentChat.putExtra(Constant.requestId, requestId);
                if (state.equals("complete")) {
                    intentChat.putExtra(Constant.requestType, Constant.reqComplete);
                } else if (state.equals("pending")) {
                    intentChat.putExtra(Constant.requestType, Constant.reqConfirm);
                }
                startActivity(intentChat);
                break;
        }
    }

    public void getServiceDetails() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getCarRequestDetailUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("ServiceDetails", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    requestDetailsJson = response;
                                    visibleLayout.setVisibility(View.GONE);
                                    ServiceDetailsResponce serviceDetailsResponce = gson.fromJson(response, ServiceDetailsResponce.class);
                                    updateUi(serviceDetailsResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityMyJobServiceDetailsLayout.this);
                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            try {
                                Log.e("ActivityMyJobServiceDetailsLayout", error.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityMyJobServiceDetailsLayout.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", requestId);
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(ActivityMyJobServiceDetailsLayout.this).addToRequestQueue(stringRequest);
        }
    }

    private void updateUi(ServiceDetailsResponce serviceDetailsResponce) {
        String dateTime = Constant.dateTimeFormateChange(serviceDetailsResponce.getData().getDateAndTime());
        String[] dateTimeArray = dateTime.split(" ");
        String nxtDateTxt = "";
        for (int i = 0; i < dateTimeArray.length - 1; i++) {
            if (nxtDateTxt.equals("")) {
                nxtDateTxt = dateTimeArray[i + 1];
            } else {
                nxtDateTxt = nxtDateTxt + " " + dateTimeArray[i + 1];
            }

        }


        dayTxt.setText(dateTimeArray[0]);
        dateTxt.setText(nxtDateTxt + " " + serviceDetailsResponce.getData().getTimeSlot());
        descriptionTxt.setText(serviceDetailsResponce.getData().getDescription());
        orderdetailsBean = serviceDetailsResponce.getData().getOrderdetails().get(0);
        servicePriceTxt.setText("$ " + Constant.DecimalFormat(Double.parseDouble(orderdetailsBean.getTotalAmount())));

        adminComTxt.setText(orderdetailsBean.getCommission() + "%");
        manageStatus(serviceDetailsResponce.getData().getStatus());

        if (Float.parseFloat(orderdetailsBean.getDepositPrice()) > 0) {
            payDepositeTxt.setText("$ " + Constant.DecimalFormat(Double.parseDouble(orderdetailsBean.getDepositPrice())));
            if (orderdetailsBean.getStatus() == 1) {
                payDepositeStatus.setText("Pending");
            }
            if (orderdetailsBean.getStatus() == 2) {
                payDepositeStatus.setText("Done");
            }
        } else {
            payDepositeLay.setVisibility(View.GONE);
            dipositeView.setVisibility(View.GONE);
        }
        if (userType == Constant.uerTypeConsumer) {
            providerLay.setVisibility(View.VISIBLE);
            mapLay.setVisibility(View.VISIBLE);
            prvInfoTxt.setText("Provider Info");
            buisnessNameTxt.setText(orderdetailsBean.getUserInfo().getBusinessName());
            distanceTxt.setText("" + df.format(Double.parseDouble(orderdetailsBean.getDistance())) + " mi");
            navLat = String.valueOf(orderdetailsBean.getUserInfo().getAddressLatLong().get(0));
            navLng = String.valueOf(orderdetailsBean.getUserInfo().getAddressLatLong().get(1));
            String mapUrl = "http://maps.google.com/maps/api/staticmap?center=" + navLat + "," + navLng + "&zoom=16&size=" + width + "x" + (int) getResources().getDimension(R.dimen._130sdp) + "&sensor=false";
            smlProgressBar.setVisibility(View.VISIBLE);
            Picasso.with(mapImg.getContext()).load(mapUrl).into(mapImg, new Callback() {
                @Override
                public void onSuccess() {
                    smlProgressBar.setVisibility(View.GONE);
                    mapOwnerTxt.setText(orderdetailsBean.getUserInfo().getBusinessName());
                    mapIconLay.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {

                }
            });
            adminView.setVisibility(View.GONE);
            adminLay.setVisibility(View.GONE);
            oppoUserId = orderdetailsBean.getUserInfo().get_id();
            oppoUserName = orderdetailsBean.getUserInfo().getUserName();
            opoPrflImg = orderdetailsBean.getUserInfo().getProfileImage();
            oppFbToken = orderdetailsBean.getUserInfo().getDeviceToken();
        } else if (userType == Constant.userTypeOwner) {
            providerLay.setVisibility(View.GONE);
            mapLay.setVisibility(View.GONE);
            prvInfoTxt.setText("Customer Info");
            oppoUserId = serviceDetailsResponce.getData().getConDetail().get(0).get_id();
            oppoUserName = serviceDetailsResponce.getData().getConDetail().get(0).getUserName();
            opoPrflImg = serviceDetailsResponce.getData().getConDetail().get(0).getProfileImage();
            oppFbToken = serviceDetailsResponce.getData().getConDetail().get(0).getDeviceToken();
        }
    }

    private void manageStatus(int status) {
        // 1:new, 2:accept, 3:start, 4:In Progress, 5:Almost Done,6:End Service,7:Ask for payment,8:payment done, 9:review complete
        switch (status) {
            case 2:
                reqStTxt.setText("Confirmed");
                break;
            case 3:
                reqStTxt.setText("Service Start");
                break;
            case 4:
                reqStTxt.setText("In Progress");
                break;
            case 5:
                reqStTxt.setText("Almost Done");
                break;
            case 6:
                reqStTxt.setText("End Service");
                break;
            case 7:
                if (userType == Constant.uerTypeConsumer) {
                    reqStTxt.setText("Pay Now");
                } else if (userType == Constant.userTypeOwner) {
                    reqStTxt.setText("Asked For Payment");
                }
                break;
            case 8:
                reqStTxt.setText("Payment Done");
                break;
            case 9:
                reqStTxt.setText("Completed");
                break;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int notiRequedId = intent.getIntExtra(Constant.REQUEST_ID, 0);
            String notiTitle = intent.getStringExtra(Constant.TITLE);
            String notiMsg = intent.getStringExtra(Constant.MSG);
            String notiState = intent.getStringExtra("STATE");
            if (notiRequedId == Integer.parseInt(requestId)) {
                getServiceDetails();
            } else {
                ServiceProgressDetailsNotification(notiTitle, notiRequedId, notiMsg, notiState);
            }
        }
    };

    private void ServiceProgressDetailsNotification(String title, int requestId, String content, String notiState) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, new Intent(this, ActivityMyJobServiceDetailsLayout.class)
                        .putExtra(Constant.FROM, "notification")
                        .putExtra("STATE", notiState)
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
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

    @Override
    protected void onPause() {
        super.onPause();
        unregisterWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        ActivityMyJobServiceDetailsLayout.this.unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        if (from.equals("notification")) {
            Intent intent = new Intent(ActivityMyJobServiceDetailsLayout.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (state.equals("complete")) {
                intent.putExtra("GO", "three");
            } else if (state.equals("pending")) {
                intent.putExtra("GO", "four");
            }
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void mapNavDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        //builder1.setTitle(title);
        builder1.setMessage(Html.fromHtml("This will close CarA2V and<br>open the map application<br>continue?"));
        builder1.setCancelable(true);
        builder1.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + navLat + "," + navLng);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}