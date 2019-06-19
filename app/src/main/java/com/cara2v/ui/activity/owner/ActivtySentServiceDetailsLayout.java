package com.cara2v.ui.activity.owner;

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
import android.app.Activity;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.ui.activity.ActivityMyJobServiceDetailsLayout;
import com.cara2v.ui.activity.ActivityServiceDetails;
import com.cara2v.ui.activity.ActivityServiceQuote;
import com.cara2v.ui.activity.ActivityVehicleInfoLayout;
import com.cara2v.ui.activity.UserProfileLayoutActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivtySentServiceDetailsLayout extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;

    private ScrollView scrollView;
    private LinearLayout quoteLay;
    private TextView quoteTxt;
    private TextView dateTimeTxt;
    private TextView estPriceTxt;
    private RelativeLayout vehicleInfoLay;
    private RelativeLayout consumerInfoLay;
    private TextView descriptionTxt;
    private RelativeLayout serviceQuotesLay;
    private RelativeLayout cpnLay;
    private TextView cupnAmtTxt;
    private TextView cupnCodeTxt;
    private TextView paymentModeTxt;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private View cpnView;

    private ImageView menuBtn;
    private TextView titleTxt;
    private String requestId = "", requestDetailsJson = "";
    private int userType = 0;
    private int oppoUserId = 0;
    private int userId = 0;
    private Gson gson = new Gson();
    private boolean isResume = false;
    //private int position;
    private ServiceDetailsResponce serviceDetailsResponce;
    private List<ServiceDetailsResponce.DataBean.OrderdetailsBean> orderdetailsBeanArrayList;
    private ServiceDetailsResponce.DataBean.OrderdetailsBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_sent_service_details_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestId = "" + bundle.getInt("requestId");
            Log.e("requestId", requestId);
        }
        userType = PreferenceConnector.readInteger(this, PreferenceConnector.UserType, 0);
        String responce = PreferenceConnector.readString(this, PreferenceConnector.userInfoJson, "");
        SignInRespoce signInResponce = gson.fromJson(responce, SignInRespoce.class);
        userId = signInResponce.getData().get_id();
        initializeView();
        getServiceDetails();
        isResume = true;
    }

    private void initializeView() {
        mainLayout = findViewById(R.id.mainLayout);
        scrollView = findViewById(R.id.scrollView);
        quoteLay = findViewById(R.id.quoteLay);
        quoteTxt = findViewById(R.id.quoteTxt);
        dateTimeTxt = findViewById(R.id.dateTimeTxt);
        estPriceTxt = findViewById(R.id.estPriceTxt);
        vehicleInfoLay = findViewById(R.id.vehicleInfoLay);
        consumerInfoLay = findViewById(R.id.consumerInfoLay);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        serviceQuotesLay = findViewById(R.id.serviceQuotesLay);
        cpnLay = findViewById(R.id.cpnLay);
        cupnAmtTxt = findViewById(R.id.cupnAmtTxt);
        cupnCodeTxt = findViewById(R.id.cupnCodeTxt);
        paymentModeTxt = findViewById(R.id.paymentModeTxt);
        visibleLayout = findViewById(R.id.visibleLayout);
        progressBar = findViewById(R.id.progressBar);
        cpnView = findViewById(R.id.cpnView);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Service Details");

        menuBtn.setOnClickListener(this);
        vehicleInfoLay.setOnClickListener(this);
        consumerInfoLay.setOnClickListener(this);
        serviceQuotesLay.setOnClickListener(this);
        quoteLay.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;
            case R.id.vehicleInfoLay:
                Intent veechicleIntent = new Intent(ActivtySentServiceDetailsLayout.this, ActivityVehicleInfoLayout.class);
                veechicleIntent.putExtra("requestDetailsJson", requestDetailsJson);
                startActivity(veechicleIntent);
                break;
            case R.id.consumerInfoLay:
                Intent intent1 = new Intent(ActivtySentServiceDetailsLayout.this, UserProfileLayoutActivity.class);
                intent1.putExtra("userId", oppoUserId);
                startActivity(intent1);
                break;
            case R.id.serviceQuotesLay:
                Intent intent = new Intent(ActivtySentServiceDetailsLayout.this, ActivityServiceQuote.class);

                String jsonBean = gson.toJson(bean);
                intent.putExtra("orderBean", jsonBean);
                intent.putExtra("requestDetailsJson", requestDetailsJson);
                // intent.putExtra("position", position);
                startActivity(intent);
                break;
            case R.id.quoteLay:
                /*Intent intent = new Intent(ActivtySentServiceDetailsLayout.this, ActivityServiceQuote.class);
                intent.putExtra("requestDetailsJson", requestDetailsJson);
                intent.putExtra("position", position);
                startActivity(intent);*/
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ActivtySentServiceDetailsLayout.this.registerReceiver(receiver, new IntentFilter("FILTERREQUEST"));
        if (isResume) getServiceDetails();
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
                                    serviceDetailsResponce = gson.fromJson(response, ServiceDetailsResponce.class);
                                    updateUi(serviceDetailsResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivtySentServiceDetailsLayout.this);
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
                            Log.e("ActivitySerViceDetails", error.getMessage());
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivtySentServiceDetailsLayout.this, PreferenceConnector.AuthToken, ""));
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
            VolleySingleton.getInstance(ActivtySentServiceDetailsLayout.this).addToRequestQueue(stringRequest);
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
        Html.fromHtml(dateTimeArray[0] + "\n" + nxtDateTxt);
        dateTimeTxt.setText(Html.fromHtml(dateTimeArray[0] + "<br/>" + nxtDateTxt));
        descriptionTxt.setText(serviceDetailsResponce.getData().getDescription());
        oppoUserId = serviceDetailsResponce.getData().getUserId();
           /* quoteTxt.setText("Create Quote");

            if (serviceDetailsResponce.getData().getMyRequest() == 1) {
                quoteTxt.setText("Request is pending");
            }*/

        orderdetailsBeanArrayList = serviceDetailsResponce.getData().getOrderdetails();
        String myId = PreferenceConnector.readString(ActivtySentServiceDetailsLayout.this, PreferenceConnector.Id, "");
        for (int i = 0; i < orderdetailsBeanArrayList.size(); i++) {
            ServiceDetailsResponce.DataBean.OrderdetailsBean orderdetailsBean = orderdetailsBeanArrayList.get(i);
            if (myId.equals(String.valueOf(orderdetailsBean.getUserInfo().get_id()))) {
                bean = orderdetailsBeanArrayList.get(i);
                //position = i;
                Log.e("position", "" + i);
                estPriceTxt.setText("$ " + Constant.DecimalFormat(Double.parseDouble(orderdetailsBean.getTotalAmount())));
                if (Float.parseFloat(orderdetailsBean.getDepositPrice()) > 0) {
                    paymentModeTxt.setText("Deposit $ " + Constant.DecimalFormat(Double.parseDouble(orderdetailsBean.getDepositPrice())));
                } else {
                    paymentModeTxt.setText("Pay After Repair");
                }
                if (Float.parseFloat(orderdetailsBean.getDisAmount()) > 0) {
                    cupnAmtTxt.setText("$ " + Constant.DecimalFormat(Double.parseDouble(orderdetailsBean.getDisAmount())));
                    cupnCodeTxt.setText(orderdetailsBean.getCoupon());
                } else {
                    cpnLay.setVisibility(View.GONE);
                    cpnView.setVisibility(View.GONE);
                }

                if (orderdetailsBean.getStatus() == 1) {
                    quoteTxt.setText("Your request is pending");
                    if (serviceDetailsResponce.getData().getStatus() == 2){
                        if (serviceDetailsResponce.getData().getOrderdetails().get(0).getUserInfo().get_id() == userId) {
                            quoteTxt.setText("Your request is Accepted");
                        }
                }
            }
            break;
        }
    }

        scrollView.fullScroll(ScrollView.FOCUS_UP);
}

    @Override
    protected void onPause() {
        super.onPause();
        unregisterWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        ActivtySentServiceDetailsLayout.this.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int notiRequedId = intent.getIntExtra(Constant.REQUEST_ID, 0);
            String notiTitle = intent.getStringExtra(Constant.TITLE);
            String notiMsg = intent.getStringExtra(Constant.MSG);
            String notiType = intent.getStringExtra(Constant.TYPE);
            if (notiRequedId == (Integer.parseInt(requestId))) {
                getServiceDetails();
            } else {
                sendRequestDetailsNotification(notiTitle, notiRequedId, notiMsg, notiType);
            }
        }
    };

    private void sendRequestDetailsNotification(String title, int requestId, String content, String notiType) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        Intent intent = null;
        if (notiType.equals("Accept")) {
            intent = new Intent(this, ActivityMyJobServiceDetailsLayout.class);
        } else {
            intent = new Intent(this, ActivityServiceDetails.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, new Intent(this, ActivityServiceDetails.class)
                        .putExtra(Constant.FROM, "notification")
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
}
