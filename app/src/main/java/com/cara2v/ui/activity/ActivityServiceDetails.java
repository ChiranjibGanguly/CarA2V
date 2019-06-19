package com.cara2v.ui.activity;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.adapter.ServiceDetailServiceAdapter;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.ui.activity.consumer.ActivityQuotesLayout;
import com.cara2v.ui.activity.owner.ActivityCreateQuote;
import com.cara2v.ui.activity.owner.ActivtySentServiceDetailsLayout;
import com.cara2v.ui.activity.owner.StripeBankAddActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.MyApplication;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityServiceDetails extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private ScrollView scrollView;

    private CardView openReqLay;
    private RelativeLayout vehicleInfoLay;
    private RelativeLayout customerInfoLay;
    private TextView descriptionTxt;
    private TextView dateTimeTxt;
    private RecyclerView serviceRecycler;
    private LinearLayout visibleLayout;
    private LinearLayout quoteLay;
    private ProgressBar progressBar;
    private View cusView;

    private ImageView menuBtn;
    private TextView titleTxt;
    private TextView quoteTxt;
    private String requestId = "", requestDetailsJson = "";
    private int userType = 0, oppoUserId = 0;
    private Gson gson = new Gson();
    private boolean isResume = false;
    private ServiceDetailServiceAdapter serviceDetailServiceAdapter;
    private ArrayList<ServiceDetailsResponce.DataBean.ServiceBean> serviceBeanArrayList;
    private ServiceDetailsResponce serviceDetailsResponce;
    private String from = "";
    private int width = 0, height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_service_details_layout);
        MyApplication.addActivity(ActivityServiceDetails.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestId = "" + bundle.getInt("requestId");
            Log.e("requestId", requestId);
        }
        userType = PreferenceConnector.readInteger(this, PreferenceConnector.UserType, 0);
        initializeView();
        getServiceDetails();
        isResume = true;
        try {
            if (bundle != null) {
                from = bundle.getString(Constant.FROM);
                if (from == null) from = "";
            }
        } catch (Exception e) {

        }
    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        mainLayout = findViewById(R.id.mainLayout);
        scrollView = findViewById(R.id.scrollView);
        openReqLay = findViewById(R.id.openReqLay);
        vehicleInfoLay = findViewById(R.id.vehicleInfoLay);
        customerInfoLay = findViewById(R.id.customerInfoLay);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        dateTimeTxt = findViewById(R.id.dateTimeTxt);
        serviceRecycler = findViewById(R.id.serviceRecycler);
        visibleLayout = findViewById(R.id.visibleLayout);
        quoteLay = findViewById(R.id.quoteLay);
        progressBar = findViewById(R.id.progressBar);
        cusView = findViewById(R.id.cusView);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        quoteTxt = findViewById(R.id.quoteTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Service Details");

        menuBtn.setOnClickListener(this);
        quoteLay.setOnClickListener(this);
        vehicleInfoLay.setOnClickListener(this);
        customerInfoLay.setOnClickListener(this);

        serviceBeanArrayList = new ArrayList<>();
        serviceDetailServiceAdapter = new ServiceDetailServiceAdapter(this, serviceBeanArrayList);
        serviceRecycler.setAdapter(serviceDetailServiceAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                if (from.equals("notification")) {
                    Intent intent = new Intent(ActivityServiceDetails.this, MainActivity.class);
                    intent.putExtra("GO", "two");
                    startActivity(intent);
                } else {
                    finish();
                }
                break;
            case R.id.quoteLay:
                if (userType == Constant.userTypeOwner) {
                    if (!requestDetailsJson.equals("")) {
                        if (PreferenceConnector.readBoolean(ActivityServiceDetails.this, PreferenceConnector.IsLcnAprv, false)) {
                            if (PreferenceConnector.readBoolean(ActivityServiceDetails.this, PreferenceConnector.IsBankAccAdd, false)) {
                                Intent intent = new Intent(ActivityServiceDetails.this, ActivityCreateQuote.class);
                                intent.putExtra("requestDetailsJson", requestDetailsJson);
                                startActivity(intent);
                            } else {
                                popAddAccount();
                            }
                        } else {
                            Constant.snackbar(mainLayout, "Please wait! Until your license will approve.");
                        }
                    }
                } else if (userType == Constant.uerTypeConsumer) {
                    if (serviceDetailsResponce != null) {
                        if (serviceDetailsResponce.getData().getOrderdetails().size() > 0) {
                            Intent intent = new Intent(ActivityServiceDetails.this, ActivityQuotesLayout.class);
                            intent.putExtra("requestDetailsJson", requestDetailsJson);
                            startActivity(intent);
                        } else {
                            Constant.snackbar(mainLayout, "Sorry! No Quotation Found.");
                        }
                    }
                }
                break;
            case R.id.vehicleInfoLay:
                Intent intent = new Intent(ActivityServiceDetails.this, ActivityVehicleInfoLayout.class);
                intent.putExtra("requestDetailsJson", requestDetailsJson);
                startActivity(intent);
                break;
            case R.id.customerInfoLay:
                Intent intent1 = new Intent(ActivityServiceDetails.this, UserProfileLayoutActivity.class);
                intent1.putExtra("userId", oppoUserId);
                startActivity(intent1);
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
                                    serviceDetailsResponce = gson.fromJson(response, ServiceDetailsResponce.class);
                                    updateUi(serviceDetailsResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityServiceDetails.this);
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
                                Log.e("ActivitySerViceDetails", error.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityServiceDetails.this, PreferenceConnector.AuthToken, ""));
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
            VolleySingleton.getInstance(ActivityServiceDetails.this).addToRequestQueue(stringRequest);
        }
    }

    private void updateUi(ServiceDetailsResponce serviceDetailsResponce) {
        serviceBeanArrayList.clear();
        serviceBeanArrayList.addAll(serviceDetailsResponce.getData().getService());
        serviceDetailServiceAdapter.notifyDataSetChanged();
        if (userType == Constant.uerTypeConsumer) {
            customerInfoLay.setVisibility(View.GONE);
            cusView.setVisibility(View.GONE);
        } else if (userType == Constant.userTypeOwner) {
            customerInfoLay.setVisibility(View.VISIBLE);
            cusView.setVisibility(View.VISIBLE);
            openReqLay.setVisibility(View.GONE);
        }
        oppoUserId = serviceDetailsResponce.getData().getUserId();
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
        dateTimeTxt.setText(Html.fromHtml(dateTimeArray[0] + "<br/>" + nxtDateTxt+", "+serviceDetailsResponce.getData().getTimeSlot()));
        descriptionTxt.setText(serviceDetailsResponce.getData().getDescription());
        if (userType == Constant.uerTypeConsumer) {
            if (serviceDetailsResponce.getData().getOrderdetails().size() > 0) {
                if (serviceDetailsResponce.getData().getOrderdetails().size() == 1) {
                    quoteTxt.setText("" + serviceDetailsResponce.getData().getOrderdetails().size() + " " + "Quote");
                } else {
                    quoteTxt.setText("" + serviceDetailsResponce.getData().getOrderdetails().size() + " " + "Quotes");
                }
            } else {
                quoteTxt.setText("No Quote");
            }
        } else if (userType == Constant.userTypeOwner) {
            quoteTxt.setText("Create Quote");

            if (serviceDetailsResponce.getData().getMyRequest() == 1) {
                quoteTxt.setText("Your Request is pending");
            }


        }
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityServiceDetails.this.registerReceiver(receiver, new IntentFilter("FILTERREQUEST"));
        if (isResume) getServiceDetails();
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        ActivityServiceDetails.this.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int notiRequedId = intent.getIntExtra(Constant.REQUEST_ID, 0);
            String notiTitle = intent.getStringExtra(Constant.TITLE);
            String notiMsg = intent.getStringExtra(Constant.MSG);
            String notiType = intent.getStringExtra(Constant.TYPE);
            if (notiRequedId == (Integer.parseInt(requestId))) {
                if (notiType.equals("Accept")) {
                    Intent intent1 = new Intent(ActivityServiceDetails.this, ActivityMyJobServiceDetailsLayout.class);
                    intent1.putExtra("requestId", notiRequedId);
                    intent1.putExtra("STATE", "pending");
                    startActivity(intent1);
                    finish();
                } else {
                    getServiceDetails();
                }
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
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, intent
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

    @Override
    public void onBackPressed() {
        if (from.equals("notification")) {
            Intent intent = new Intent(ActivityServiceDetails.this, MainActivity.class);
            intent.putExtra("GO", "two");
            startActivity(intent);
        } else {
            finish();
        }
    }

    void popAddAccount() {
        final Dialog dialog = new Dialog(ActivityServiceDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_add_account);
        dialog.getWindow().setLayout((width * 10) / 11, (height * 1) / 2);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = (ImageButton) dialog.findViewById(R.id.popCancel);
        final TextView popTitle = (TextView) dialog.findViewById(R.id.popTitle);
        final Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        dialog.setCancelable(true);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityServiceDetails.this, StripeBankAddActivity.class));
                dialog.dismiss();
            }
        });
    }
}
