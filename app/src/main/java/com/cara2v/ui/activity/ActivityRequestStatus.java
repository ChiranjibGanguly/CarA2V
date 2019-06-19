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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityRequestStatus extends Activity implements View.OnClickListener {
    private TextView vinCodeTxt;
    private TextView brandName;
    private TextView vehicleNo;

    private FrameLayout cnfrmFrm;
    private FrameLayout startFrm;
    private FrameLayout inProFrm;
    private FrameLayout almostDoneFrm;
    private FrameLayout endServiceFrm;
    private FrameLayout payFrm;
    private FrameLayout reviewFrm;

    private View cnfrmView;
    private View startView;
    private View inProView;
    private View almostDoneView;
    private View endServiceView;
    private View payView;

    private LinearLayout confirmLay;
    private RelativeLayout mainLayout;
    private TextView cnfrmTxt;
    private TextView cnfrmDateTime;
    private LinearLayout startLay;
    private TextView startTxt;
    private TextView startDateTime;
    private LinearLayout inProLay;
    private TextView inProTxt;
    private TextView inProDateTime;
    private LinearLayout almostDoneLay;
    private TextView almostDoneTxt;
    private TextView almostDoneDateTime;
    private LinearLayout endServiceLay;
    private TextView endServiceTxt;
    private TextView endServiceDateTime;
    private LinearLayout payLay;
    private TextView payTxt;
    private TextView payDateTime;
    private LinearLayout reviewLay;
    private TextView reviewTxt;
    private TextView reviewDateTime;
    private ImageView menuBtn;
    private TextView titleTxt;
    EditText rattingCommentTxt = null;

    private ProgressBar progressBar;

    private String requestDetailsJson = "", from = "";
    private int requestId = 0;
    private Gson gson = new Gson();
    private ServiceDetailsResponce serviceDetailsResponce;

    private int width = 0, height = 0;
    private int userType = 0;
    private String myInfoJson = "";
    private SignInRespoce signInRespoce;

    private float rate = 0;
    private int clickId = 0;
    private boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);
        Bundle bundle = getIntent().getExtras();
        userType = PreferenceConnector.readInteger(ActivityRequestStatus.this, PreferenceConnector.UserType, 0);
        initializeView();
        if (bundle != null) {

            from = bundle.getString(Constant.FROM);
            if (from.equals("notification")) {
                requestId = bundle.getInt("requestId");
                getServiceDetails();
            } else {
                requestDetailsJson = bundle.getString("requestDetailsJson");
                if (!TextUtils.isEmpty(requestDetailsJson)) {
                    updateUi();
                }
            }
        }


    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        mainLayout = findViewById(R.id.mainLayout);
        vinCodeTxt = findViewById(R.id.vinCodeTxt);
        brandName = findViewById(R.id.brandName);
        vehicleNo = findViewById(R.id.vehicleNo);
        confirmLay = findViewById(R.id.confirmLay);
        cnfrmTxt = findViewById(R.id.cnfrmTxt);
        cnfrmDateTime = findViewById(R.id.cnfrmDateTime);
        startLay = findViewById(R.id.startLay);
        startTxt = findViewById(R.id.startTxt);
        startDateTime = findViewById(R.id.startDateTime);
        inProLay = findViewById(R.id.inProLay);
        inProTxt = findViewById(R.id.inProTxt);
        inProDateTime = findViewById(R.id.inProDateTime);
        almostDoneLay = findViewById(R.id.almostDoneLay);
        almostDoneTxt = findViewById(R.id.almostDoneTxt);
        almostDoneDateTime = findViewById(R.id.almostDoneDateTime);
        endServiceLay = findViewById(R.id.endServiceLay);
        endServiceTxt = findViewById(R.id.endServiceTxt);
        endServiceDateTime = findViewById(R.id.endServiceDateTime);
        payLay = findViewById(R.id.payLay);
        payTxt = findViewById(R.id.payTxt);
        payDateTime = findViewById(R.id.payDateTime);
        reviewLay = findViewById(R.id.reviewLay);
        reviewTxt = findViewById(R.id.reviewTxt);
        reviewDateTime = findViewById(R.id.reviewDateTime);

        cnfrmFrm = findViewById(R.id.cnfrmFrm);
        startFrm = findViewById(R.id.startFrm);
        inProFrm = findViewById(R.id.inProFrm);
        almostDoneFrm = findViewById(R.id.almostDoneFrm);
        endServiceFrm = findViewById(R.id.endServiceFrm);
        payFrm = findViewById(R.id.payFrm);
        reviewFrm = findViewById(R.id.reviewFrm);

        cnfrmView = findViewById(R.id.cnfrmView);
        startView = findViewById(R.id.startView);
        inProView = findViewById(R.id.inProView);
        almostDoneView = findViewById(R.id.almostDoneView);
        endServiceView = findViewById(R.id.endServiceView);
        payView = findViewById(R.id.payView);
        progressBar = findViewById(R.id.progressBar);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Request Status");
        menuBtn.setOnClickListener(this);

        startLay.setOnClickListener(this);
        inProLay.setOnClickListener(this);
        almostDoneLay.setOnClickListener(this);
        endServiceLay.setOnClickListener(this);
        payLay.setOnClickListener(this);
        reviewLay.setOnClickListener(this);

        myInfoJson = PreferenceConnector.readString(ActivityRequestStatus.this, PreferenceConnector.userInfoJson, "");
        signInRespoce = gson.fromJson(myInfoJson, SignInRespoce.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;
            case R.id.startLay:
            case R.id.inProLay:
            case R.id.almostDoneLay:
            case R.id.endServiceLay:
                if (userType == Constant.userTypeOwner)
                    popUpChangeStatus(serviceDetailsResponce.getData().getStatus());
                break;
            case R.id.payLay:
                // 1:new, 2:accept, 3:start, 4:In Progress, 5:Almost Done,6:End Service,7:Ask for payment,8:payment done, 9:review complete
                if (userType == Constant.uerTypeConsumer) {
                    gotoAskForPaymentActivity();
                    //   popUpChangeStatus(serviceDetailsResponce.getData().getStatus());
                    //Constant.snackbar(mainLayout, "You have to Pay !!!");
                } else if (userType == Constant.userTypeOwner) {
                    if (serviceDetailsResponce.getData().getStatus() == 6) {
                        gotoAskForPaymentActivity();
                        //  popUpChangeStatus(serviceDetailsResponce.getData().getStatus());
                    } else if (serviceDetailsResponce.getData().getStatus() == 7) {
                        gotoAskForPaymentActivity();
                        // Constant.snackbar(mainLayout, "You have already asked for payment");
                    }
                }
                break;
            case R.id.reviewLay:
                showReviewPopup();
                break;
        }
    }

    private void gotoAskForPaymentActivity() {
        isBack = true;
        Intent intent = new Intent(ActivityRequestStatus.this, ActivityAskForPayment.class);
        String jsonBean = gson.toJson(serviceDetailsResponce.getData().getOrderdetails().get(0));
        intent.putExtra("orderBean", jsonBean);
        intent.putExtra("requestDetailsJson", requestDetailsJson);
        intent.putExtra("status", String.valueOf(serviceDetailsResponce.getData().getStatus()));
        startActivity(intent);
    }

    private void updateUi() {
        serviceDetailsResponce = gson.fromJson(requestDetailsJson, ServiceDetailsResponce.class);
        brandName.setText(serviceDetailsResponce.getData().getCar().getBrand().getBrandName());
        vinCodeTxt.setText(serviceDetailsResponce.getData().getCar().getVINCode());
        vehicleNo.setText(serviceDetailsResponce.getData().getCar().getPlateNumber());
        requestId = serviceDetailsResponce.getData().getOrderdetails().get(0).getRequestId();
        setFrame();
        updateStatus(serviceDetailsResponce.getData().getStatus());
        manageStatusClick();
    }

    private void manageStatusClick() {
        startLay.setClickable(false);
        startLay.setFocusable(false);
        inProLay.setClickable(false);
        inProLay.setFocusable(false);
        almostDoneLay.setClickable(false);
        almostDoneLay.setFocusable(false);
        endServiceLay.setClickable(false);
        endServiceLay.setFocusable(false);
        payLay.setClickable(false);
        payLay.setFocusable(false);
        reviewLay.setClickable(false);
        reviewLay.setFocusable(false);
// 1:new, 2:accept, 3:start, 4:In Progress, 5:Almost Done,6:End Service,7:Ask for payment,8:payment done, 9:review complete
        if (userType == Constant.uerTypeConsumer) {
            switch (serviceDetailsResponce.getData().getStatus()) {
                case 7:
                    payLay.setClickable(true);
                    payLay.setFocusable(true);
                    break;
                case 8:
                    reviewLay.setClickable(true);
                    reviewLay.setFocusable(true);
                    break;
                case 9:
                    reviewLay.setClickable(true);
                    reviewLay.setFocusable(true);
                    break;
            }
        } else {
            switch (serviceDetailsResponce.getData().getStatus()) {
                case 2:
                    startLay.setClickable(true);
                    startLay.setFocusable(true);
                    break;
                case 3:
                    inProLay.setClickable(true);
                    inProLay.setFocusable(true);
                    break;
                case 4:
                    almostDoneLay.setClickable(true);
                    almostDoneLay.setFocusable(true);
                    break;
                case 5:
                    endServiceLay.setClickable(true);
                    endServiceLay.setFocusable(true);
                    break;
                case 6:
                    payLay.setClickable(true);
                    payLay.setFocusable(true);
                    break;
                case 7:
                    payLay.setClickable(true);
                    payLay.setFocusable(true);
                    break;
                case 8:
                    reviewLay.setClickable(true);
                    reviewLay.setFocusable(true);
                    break;
                case 9:
                    reviewLay.setClickable(true);
                    reviewLay.setFocusable(true);
                    break;
            }
        }
    }

    private void setFrame() {
        RelativeLayout.LayoutParams cnfrmLayoutParams = (RelativeLayout.LayoutParams) cnfrmFrm.getLayoutParams();
        cnfrmLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmFrm.setLayoutParams(cnfrmLayoutParams);

        RelativeLayout.LayoutParams startLayoutParams = (RelativeLayout.LayoutParams) startFrm.getLayoutParams();
        startLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        startLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        startFrm.setLayoutParams(startLayoutParams);

        RelativeLayout.LayoutParams inProLayoutParams = (RelativeLayout.LayoutParams) inProFrm.getLayoutParams();
        inProLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        inProLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        inProFrm.setLayoutParams(inProLayoutParams);

        RelativeLayout.LayoutParams almostDoneLayoutParams = (RelativeLayout.LayoutParams) almostDoneFrm.getLayoutParams();
        almostDoneLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        almostDoneLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        almostDoneFrm.setLayoutParams(almostDoneLayoutParams);

        RelativeLayout.LayoutParams endServiceLayoutParams = (RelativeLayout.LayoutParams) endServiceFrm.getLayoutParams();
        endServiceLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        endServiceLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        endServiceFrm.setLayoutParams(endServiceLayoutParams);

        RelativeLayout.LayoutParams payLayoutParams = (RelativeLayout.LayoutParams) payFrm.getLayoutParams();
        payLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        payLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        payFrm.setLayoutParams(payLayoutParams);

        RelativeLayout.LayoutParams reviewLayoutParams = (RelativeLayout.LayoutParams) reviewFrm.getLayoutParams();
        reviewLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        reviewLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        reviewFrm.setLayoutParams(reviewLayoutParams);

        cnfrmFrm.setBackgroundResource(R.drawable.circle_white_bg_blue_stroke);
        startFrm.setBackgroundResource(R.drawable.circle_white_bg_blue_stroke);
        inProFrm.setBackgroundResource(R.drawable.circle_white_bg_blue_stroke);
        almostDoneFrm.setBackgroundResource(R.drawable.circle_white_bg_blue_stroke);
        endServiceFrm.setBackgroundResource(R.drawable.circle_white_bg_blue_stroke);
        payFrm.setBackgroundResource(R.drawable.circle_white_bg_blue_stroke);
        reviewFrm.setBackgroundResource(R.drawable.circle_white_bg_blue_stroke);

        cnfrmView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorDevider));
        startView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorDevider));
        inProView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorDevider));
        almostDoneView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorDevider));
        endServiceView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorDevider));
        payView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorDevider));

        confirmLay.setBackgroundDrawable(null);
        startLay.setBackgroundDrawable(null);
        inProLay.setBackgroundDrawable(null);
        almostDoneLay.setBackgroundDrawable(null);
        endServiceLay.setBackgroundDrawable(null);
        payLay.setBackgroundDrawable(null);
        reviewLay.setBackgroundDrawable(null);

        cnfrmTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorBlueText));
        cnfrmDateTime.setVisibility(View.GONE);

        startTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorBlueText));
        startDateTime.setVisibility(View.GONE);

        inProTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorBlueText));
        inProDateTime.setVisibility(View.GONE);

        almostDoneTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorBlueText));
        almostDoneDateTime.setVisibility(View.GONE);

        endServiceTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorBlueText));
        endServiceDateTime.setVisibility(View.GONE);

        payTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorBlueText));
        payDateTime.setVisibility(View.GONE);

        reviewTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorBlueText));
        reviewDateTime.setVisibility(View.GONE);
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
                                    serviceDetailsResponce = gson.fromJson(response, ServiceDetailsResponce.class);
                                    updateUi();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityRequestStatus.this);
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
                    header.put("authToken", PreferenceConnector.readString(ActivityRequestStatus.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", "" + requestId);
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
            VolleySingleton.getInstance(ActivityRequestStatus.this).addToRequestQueue(stringRequest);
        }
    }

    private void updateStatus(int status) {
        // 1:new, 2:accept, 3:start, 4:In Progress, 5:Almost Done,6:End Service,7:Ask for payment,8:payment done, 9:review complete
        switch (status) {
            case 2:
                confirmStatus();
                break;
            case 3:
                startStatus();
                break;
            case 4:
                inProgressStatus();
                break;
            case 5:
                almostDoneStatus();
                break;
            case 6:
                endServiceStats();
                break;
            case 7:
                paymentStatus(7);
                break;
            case 8:
                paymentStatus(8);
                break;
            case 9:
                reviewStatus();
                break;
        }
    }

    private void reviewStatus() {
        RelativeLayout.LayoutParams cnfrmLayoutParams = (RelativeLayout.LayoutParams) cnfrmFrm.getLayoutParams();
        cnfrmLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmFrm.setLayoutParams(cnfrmLayoutParams);

        cnfrmFrm.setBackgroundResource(R.drawable.double_layer_circle);
        confirmLay.setBackgroundResource(R.drawable.status_bg);

        cnfrmTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        cnfrmDateTime.setVisibility(View.VISIBLE);
        cnfrmDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getConfirm());

        RelativeLayout.LayoutParams startLayoutParams = (RelativeLayout.LayoutParams) startFrm.getLayoutParams();
        startLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        startLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        startFrm.setLayoutParams(startLayoutParams);

        startFrm.setBackgroundResource(R.drawable.double_layer_circle);
        startLay.setBackgroundResource(R.drawable.status_bg);

        startTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        startDateTime.setVisibility(View.VISIBLE);
        startDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getStart());
        cnfrmView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams inProLayoutParams = (RelativeLayout.LayoutParams) inProFrm.getLayoutParams();
        inProLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        inProLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        inProFrm.setLayoutParams(inProLayoutParams);

        inProFrm.setBackgroundResource(R.drawable.double_layer_circle);
        inProLay.setBackgroundResource(R.drawable.status_bg);

        inProTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        inProDateTime.setVisibility(View.VISIBLE);
        inProDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getProgress());
        startView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams almostDoneLayoutParams = (RelativeLayout.LayoutParams) almostDoneFrm.getLayoutParams();
        almostDoneLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        almostDoneLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        almostDoneFrm.setLayoutParams(almostDoneLayoutParams);

        almostDoneFrm.setBackgroundResource(R.drawable.double_layer_circle);
        almostDoneLay.setBackgroundResource(R.drawable.status_bg);

        almostDoneTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        almostDoneDateTime.setVisibility(View.VISIBLE);
        almostDoneDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getAlmost());
        inProView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams endServiceLayoutParams = (RelativeLayout.LayoutParams) endServiceFrm.getLayoutParams();
        endServiceLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        endServiceLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        endServiceFrm.setLayoutParams(endServiceLayoutParams);

        endServiceFrm.setBackgroundResource(R.drawable.double_layer_circle);
        endServiceLay.setBackgroundResource(R.drawable.status_bg);

        endServiceTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        endServiceDateTime.setVisibility(View.VISIBLE);
        endServiceDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getEnd());
        almostDoneView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams payLayoutParams = (RelativeLayout.LayoutParams) payFrm.getLayoutParams();
        payLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        payLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        payFrm.setLayoutParams(payLayoutParams);

        payFrm.setBackgroundResource(R.drawable.double_layer_circle);
        payLay.setBackgroundResource(R.drawable.status_bg);

        payTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        payDateTime.setVisibility(View.VISIBLE);
        payDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getAsk());
        endServiceView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
        payTxt.setText("Payment Done");
        RelativeLayout.LayoutParams reviewLayoutParams = (RelativeLayout.LayoutParams) reviewFrm.getLayoutParams();
        reviewLayoutParams.width = (int) getResources().getDimension(R.dimen._30sdp);
        reviewLayoutParams.height = (int) getResources().getDimension(R.dimen._30sdp);
        reviewFrm.setLayoutParams(reviewLayoutParams);

        reviewFrm.setBackgroundResource(R.drawable.car_thumb);
        reviewLay.setBackgroundResource(R.drawable.status_bg);
        reviewDateTime.setVisibility(View.VISIBLE);

        payView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
        if (Float.parseFloat(serviceDetailsResponce.getData().getConReview().getRating()) > 0 && Float.parseFloat(serviceDetailsResponce.getData().getOwnReview().getRating()) > 0) {
            reviewTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        } else {
            reviewTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorOrange));
        }
        if (userType == Constant.userTypeOwner) {
            reviewDateTime.setText(serviceDetailsResponce.getData().getOwnReview().getTime());
        } else if (userType == Constant.uerTypeConsumer) {
            reviewDateTime.setText(serviceDetailsResponce.getData().getConReview().getTime());
        }
    }

    private void paymentStatus(int status) {
        RelativeLayout.LayoutParams cnfrmLayoutParams = (RelativeLayout.LayoutParams) cnfrmFrm.getLayoutParams();
        cnfrmLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmFrm.setLayoutParams(cnfrmLayoutParams);

        cnfrmFrm.setBackgroundResource(R.drawable.double_layer_circle);
        confirmLay.setBackgroundResource(R.drawable.status_bg);

        cnfrmTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        cnfrmDateTime.setVisibility(View.VISIBLE);
        cnfrmDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getConfirm());

        RelativeLayout.LayoutParams startLayoutParams = (RelativeLayout.LayoutParams) startFrm.getLayoutParams();
        startLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        startLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        startFrm.setLayoutParams(startLayoutParams);

        startFrm.setBackgroundResource(R.drawable.double_layer_circle);
        startLay.setBackgroundResource(R.drawable.status_bg);

        startTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        startDateTime.setVisibility(View.VISIBLE);
        startDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getStart());
        cnfrmView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams inProLayoutParams = (RelativeLayout.LayoutParams) inProFrm.getLayoutParams();
        inProLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        inProLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        inProFrm.setLayoutParams(inProLayoutParams);

        inProFrm.setBackgroundResource(R.drawable.double_layer_circle);
        inProLay.setBackgroundResource(R.drawable.status_bg);

        inProTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        inProDateTime.setVisibility(View.VISIBLE);
        inProDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getProgress());
        startView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams almostDoneLayoutParams = (RelativeLayout.LayoutParams) almostDoneFrm.getLayoutParams();
        almostDoneLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        almostDoneLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        almostDoneFrm.setLayoutParams(almostDoneLayoutParams);

        almostDoneFrm.setBackgroundResource(R.drawable.double_layer_circle);
        almostDoneLay.setBackgroundResource(R.drawable.status_bg);

        almostDoneTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        almostDoneDateTime.setVisibility(View.VISIBLE);
        almostDoneDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getAlmost());
        inProView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams endServiceLayoutParams = (RelativeLayout.LayoutParams) endServiceFrm.getLayoutParams();
        endServiceLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        endServiceLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        endServiceFrm.setLayoutParams(endServiceLayoutParams);

        endServiceFrm.setBackgroundResource(R.drawable.double_layer_circle);
        endServiceLay.setBackgroundResource(R.drawable.status_bg);

        endServiceTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        endServiceDateTime.setVisibility(View.VISIBLE);
        endServiceDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getEnd());
        almostDoneView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams payLayoutParams = (RelativeLayout.LayoutParams) payFrm.getLayoutParams();
        payLayoutParams.width = (int) getResources().getDimension(R.dimen._30sdp);
        payLayoutParams.height = (int) getResources().getDimension(R.dimen._30sdp);
        payFrm.setLayoutParams(payLayoutParams);

        payLay.setBackgroundResource(R.drawable.status_bg);
        payFrm.setBackgroundResource(R.drawable.car_thumb);

        payTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        if (status == 7) {
            if (userType == Constant.userTypeOwner) {
                payTxt.setText("Asked For Payment");
            } else if (userType == Constant.uerTypeConsumer) {
                payTxt.setText("Pay");
            }
            payDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getAsk());
        }
        if (status == 8) {
            payTxt.setText("Payment Done");
            payDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getPayment());
        }
        payDateTime.setVisibility(View.VISIBLE);

        endServiceView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

    }

    private void endServiceStats() {
        RelativeLayout.LayoutParams cnfrmLayoutParams = (RelativeLayout.LayoutParams) cnfrmFrm.getLayoutParams();
        cnfrmLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmFrm.setLayoutParams(cnfrmLayoutParams);

        cnfrmFrm.setBackgroundResource(R.drawable.double_layer_circle);
        confirmLay.setBackgroundResource(R.drawable.status_bg);

        cnfrmTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        cnfrmDateTime.setVisibility(View.VISIBLE);
        cnfrmDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getConfirm());

        RelativeLayout.LayoutParams startLayoutParams = (RelativeLayout.LayoutParams) startFrm.getLayoutParams();
        startLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        startLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        startFrm.setLayoutParams(startLayoutParams);

        startFrm.setBackgroundResource(R.drawable.double_layer_circle);
        startLay.setBackgroundResource(R.drawable.status_bg);

        startTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        startDateTime.setVisibility(View.VISIBLE);
        startDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getStart());
        cnfrmView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams inProLayoutParams = (RelativeLayout.LayoutParams) inProFrm.getLayoutParams();
        inProLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        inProLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        inProFrm.setLayoutParams(inProLayoutParams);

        inProFrm.setBackgroundResource(R.drawable.double_layer_circle);
        inProLay.setBackgroundResource(R.drawable.status_bg);

        inProTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        inProDateTime.setVisibility(View.VISIBLE);
        inProDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getProgress());
        startView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams almostDoneLayoutParams = (RelativeLayout.LayoutParams) almostDoneFrm.getLayoutParams();
        almostDoneLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        almostDoneLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        almostDoneFrm.setLayoutParams(almostDoneLayoutParams);

        almostDoneFrm.setBackgroundResource(R.drawable.double_layer_circle);
        almostDoneLay.setBackgroundResource(R.drawable.status_bg);

        almostDoneTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        almostDoneDateTime.setVisibility(View.VISIBLE);
        almostDoneDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getAlmost());
        inProView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams endServiceLayoutParams = (RelativeLayout.LayoutParams) endServiceFrm.getLayoutParams();
        endServiceLayoutParams.width = (int) getResources().getDimension(R.dimen._30sdp);
        endServiceLayoutParams.height = (int) getResources().getDimension(R.dimen._30sdp);
        endServiceFrm.setLayoutParams(endServiceLayoutParams);

        endServiceFrm.setBackgroundResource(R.drawable.car_thumb);
        endServiceLay.setBackgroundResource(R.drawable.status_bg);

        endServiceTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        endServiceDateTime.setVisibility(View.VISIBLE);
        endServiceDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getEnd());
        almostDoneView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
    }

    private void almostDoneStatus() {
        RelativeLayout.LayoutParams cnfrmLayoutParams = (RelativeLayout.LayoutParams) cnfrmFrm.getLayoutParams();
        cnfrmLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmFrm.setLayoutParams(cnfrmLayoutParams);

        cnfrmFrm.setBackgroundResource(R.drawable.double_layer_circle);
        confirmLay.setBackgroundResource(R.drawable.status_bg);

        cnfrmTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        cnfrmDateTime.setVisibility(View.VISIBLE);
        cnfrmDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getConfirm());

        RelativeLayout.LayoutParams startLayoutParams = (RelativeLayout.LayoutParams) startFrm.getLayoutParams();
        startLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        startLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        startFrm.setLayoutParams(startLayoutParams);

        startFrm.setBackgroundResource(R.drawable.double_layer_circle);
        startLay.setBackgroundResource(R.drawable.status_bg);

        startTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        startDateTime.setVisibility(View.VISIBLE);
        startDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getStart());
        cnfrmView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams inProLayoutParams = (RelativeLayout.LayoutParams) inProFrm.getLayoutParams();
        inProLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        inProLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        inProFrm.setLayoutParams(inProLayoutParams);

        inProFrm.setBackgroundResource(R.drawable.double_layer_circle);
        inProLay.setBackgroundResource(R.drawable.status_bg);

        inProTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        inProDateTime.setVisibility(View.VISIBLE);
        inProDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getProgress());
        startView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams almostDoneLayoutParams = (RelativeLayout.LayoutParams) almostDoneFrm.getLayoutParams();
        almostDoneLayoutParams.width = (int) getResources().getDimension(R.dimen._30sdp);
        almostDoneLayoutParams.height = (int) getResources().getDimension(R.dimen._30sdp);
        almostDoneFrm.setLayoutParams(almostDoneLayoutParams);

        almostDoneFrm.setBackgroundResource(R.drawable.car_thumb);
        almostDoneLay.setBackgroundResource(R.drawable.status_bg);

        almostDoneTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        almostDoneDateTime.setVisibility(View.VISIBLE);
        almostDoneDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getAlmost());
        inProView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
    }

    private void inProgressStatus() {
        RelativeLayout.LayoutParams cnfrmLayoutParams = (RelativeLayout.LayoutParams) cnfrmFrm.getLayoutParams();
        cnfrmLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmFrm.setLayoutParams(cnfrmLayoutParams);

        cnfrmFrm.setBackgroundResource(R.drawable.double_layer_circle);
        confirmLay.setBackgroundResource(R.drawable.status_bg);

        cnfrmTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        cnfrmDateTime.setVisibility(View.VISIBLE);
        cnfrmDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getConfirm());

        RelativeLayout.LayoutParams startLayoutParams = (RelativeLayout.LayoutParams) startFrm.getLayoutParams();
        startLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        startLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        startFrm.setLayoutParams(startLayoutParams);

        startFrm.setBackgroundResource(R.drawable.double_layer_circle);
        startLay.setBackgroundResource(R.drawable.status_bg);

        startTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        startDateTime.setVisibility(View.VISIBLE);
        startDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getStart());
        cnfrmView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));

        RelativeLayout.LayoutParams inProLayoutParams = (RelativeLayout.LayoutParams) inProFrm.getLayoutParams();
        inProLayoutParams.width = (int) getResources().getDimension(R.dimen._30sdp);
        inProLayoutParams.height = (int) getResources().getDimension(R.dimen._30sdp);
        inProFrm.setLayoutParams(inProLayoutParams);

        inProFrm.setBackgroundResource(R.drawable.car_thumb);
        inProLay.setBackgroundResource(R.drawable.status_bg);

        inProTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        inProDateTime.setVisibility(View.VISIBLE);
        inProDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getProgress());
        startView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
    }

    private void startStatus() {
        RelativeLayout.LayoutParams cnfrmLayoutParams = (RelativeLayout.LayoutParams) cnfrmFrm.getLayoutParams();
        cnfrmLayoutParams.width = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmLayoutParams.height = (int) getResources().getDimension(R.dimen._20sdp);
        cnfrmFrm.setLayoutParams(cnfrmLayoutParams);

        cnfrmFrm.setBackgroundResource(R.drawable.double_layer_circle);
        confirmLay.setBackgroundResource(R.drawable.status_bg);

        cnfrmTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        cnfrmDateTime.setVisibility(View.VISIBLE);
        cnfrmDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getConfirm());

        RelativeLayout.LayoutParams startLayoutParams = (RelativeLayout.LayoutParams) startFrm.getLayoutParams();
        startLayoutParams.width = (int) getResources().getDimension(R.dimen._30sdp);
        startLayoutParams.height = (int) getResources().getDimension(R.dimen._30sdp);
        startFrm.setLayoutParams(startLayoutParams);

        startFrm.setBackgroundResource(R.drawable.car_thumb);
        startLay.setBackgroundResource(R.drawable.status_bg);

        startTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        startDateTime.setVisibility(View.VISIBLE);
        startDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getStart());
        cnfrmView.setBackgroundColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
    }

    private void confirmStatus() {
        RelativeLayout.LayoutParams cnfrmLayoutParams = (RelativeLayout.LayoutParams) cnfrmFrm.getLayoutParams();
        cnfrmLayoutParams.width = (int) getResources().getDimension(R.dimen._30sdp);
        cnfrmLayoutParams.height = (int) getResources().getDimension(R.dimen._30sdp);
        cnfrmFrm.setLayoutParams(cnfrmLayoutParams);

        cnfrmFrm.setBackgroundResource(R.drawable.car_thumb);
        confirmLay.setBackgroundResource(R.drawable.status_bg);

        cnfrmTxt.setTextColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorGreen));
        cnfrmDateTime.setVisibility(View.VISIBLE);
        cnfrmDateTime.setText(serviceDetailsResponce.getData().getChangeStatusDate().getConfirm());
    }

    void popUpChangeStatus(final int status) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_change_status);
        dialog.getWindow().setLayout((width * 10) / 11, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = dialog.findViewById(R.id.popCancel);
        final TextView popDescription = dialog.findViewById(R.id.popDescription);
        final Button btnYes = dialog.findViewById(R.id.btnYes);
        // 1:new, 2:accept, 3:start, 4:In Progress, 5:Almost Done,6:End Service,7:Ask for payment,8:payment done, 9:review complete
        switch (status) {
            case 2:
                popDescription.setText(Html.fromHtml("Are you sure you want" + "<br/>" + "to start this service?"));
                break;
            case 3:
                popDescription.setText(Html.fromHtml("Are you sure you want" + "<br/>" + "to Progress this service?"));
                break;
            case 4:
                popDescription.setText(Html.fromHtml("Are you sure your Service" + "<br/>" + "is almost done?"));
                break;
            case 5:
                popDescription.setText(Html.fromHtml("Are you sure you want" + "<br/>" + "to end this service?"));
                break;
            case 6:
                popDescription.setText(Html.fromHtml("Are you sure you want" + "<br/>" + "to ask for payment?"));
                break;
            case 7:
                popDescription.setText(Html.fromHtml("Are you sure you want" + "<br/>" + "to Pay?"));
                break;
        }
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
                btnYes.setClickable(false);
                changeServiceStatus(dialog);
            }
        });

    }

    public void changeServiceStatus(final Dialog dialog) {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.changeServiceStatusUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("#" + response);
                            Log.e("ServiceDetails", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    dialog.dismiss();
                                    getServiceDetails();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    progressBar.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    Constant.showLogOutDialog(ActivityRequestStatus.this);
                                } else if (status.equalsIgnoreCase("fail")) {
                                    progressBar.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    Constant.snackbar(mainLayout, message);
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
                            dialog.dismiss();
                            try {
                                Log.e("ActivitySerViceDetails", error.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityRequestStatus.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", "" + serviceDetailsResponce.getData().getOrderdetails().get(0).getRequestId());
                    params.put("requestStatus", "" + (serviceDetailsResponce.getData().getStatus() + 1));
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
            VolleySingleton.getInstance(ActivityRequestStatus.this).addToRequestQueue(stringRequest);
        }
    }

    void showReviewPopup() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_give_review);
        dialog.getWindow().setLayout((width * 10) / 11, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = dialog.findViewById(R.id.popCancel);
        final CircleImageView ownImage = dialog.findViewById(R.id.ownImage);
        final RatingBar ownRating = dialog.findViewById(R.id.ownRating);
        final TextView ownName = dialog.findViewById(R.id.ownName);
        //final TextView ownType = dialog.findViewById(R.id.ownType);
        final CircleImageView oppoImage = dialog.findViewById(R.id.oppoImage);
        final RatingBar oppoRating = dialog.findViewById(R.id.oppoRating);
        final TextView oppoName = dialog.findViewById(R.id.oppoName);
        // final TextView oppoType = dialog.findViewById(R.id.oppoType);
        final TextView askForReview = dialog.findViewById(R.id.askForReview);
        //   final EditText rattingCommentTxt = dialog.findViewById(R.id.rattingCommentTxt);
        final LinearLayout ownLay = dialog.findViewById(R.id.ownLay);
        final LinearLayout oopoLay = dialog.findViewById(R.id.oopoLay);
        final LinearLayout innerLay = dialog.findViewById(R.id.innerLay);
        final ImageView sendFrm = dialog.findViewById(R.id.sendFrm);
        final LinearLayout containerLay = dialog.findViewById(R.id.containerLay);
        final ProgressBar dlproBar = dialog.findViewById(R.id.dlproBar);

        clickId = R.id.ownLay;

        rate = 0;

        if (userType == Constant.uerTypeConsumer) {
            ServiceDetailsResponce.DataBean.OrderdetailsBean orderdetailsBean = serviceDetailsResponce.getData().getOrderdetails().get(0);
            String oppoImg = orderdetailsBean.getUserInfo().getProfileImage();
            if (!TextUtils.isEmpty(oppoImg)) {
                Picasso.with(oppoImage.getContext()).load(oppoImg).placeholder(R.drawable.user_place_holder).into(ownImage);
            }
            ownRating.setRating(Float.parseFloat(serviceDetailsResponce.getData().getOwnReview().getRating()));
            ownName.setText(orderdetailsBean.getUserInfo().getBusinessName());
            // ownType.setText("(Owner Side)");
            if (Float.parseFloat(serviceDetailsResponce.getData().getOwnReview().getRating()) > 0) {
                if (innerLay.getChildCount() > 0) {
                    innerLay.removeAllViews();
                }
                TextView myTxt = new EditText(dialog.getContext());
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                myTxt.setLayoutParams(p);
                myTxt.setBackground(null);
                ownRating.setIsIndicator(true);
                sendFrm.setVisibility(View.GONE);

                if (serviceDetailsResponce.getData().getOwnReview().getReview().equals("")) {
                    myTxt.setText("No review available");
                } else {
                    myTxt.setText(serviceDetailsResponce.getData().getOwnReview().getReview());
                }
                myTxt.setTextSize(12);
                myTxt.setClickable(false);
                myTxt.setFocusable(false);
                innerLay.addView(myTxt);
            } else {
                sendFrm.setVisibility(View.VISIBLE);
                if (innerLay.getChildCount() > 0) {
                    innerLay.removeAllViews();
                }
                rattingCommentTxt = new EditText(dialog.getContext());
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rattingCommentTxt.setLayoutParams(p);
                rattingCommentTxt.setHint("Add your comment");
                rattingCommentTxt.setBackground(null);
                rattingCommentTxt.setTextSize(12);
                innerLay.addView(rattingCommentTxt);
            }
            ServiceDetailsResponce.DataBean.ConDetailBean conDetailBean = serviceDetailsResponce.getData().getConDetail().get(0);
            String myPrImg = conDetailBean.getProfileImage();
            if (!TextUtils.isEmpty(myPrImg)) {
                Picasso.with(oppoImage.getContext()).load(myPrImg).placeholder(R.drawable.user_place_holder).into(oppoImage);
            }
            oppoRating.setRating(Float.parseFloat(serviceDetailsResponce.getData().getConReview().getRating()));
            oppoName.setText(conDetailBean.getUserName());
            //  oppoType.setText("(Consumer Side)");
            ownImage.setBorderColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
            ownImage.setBorderWidth(2);
        } else if (userType == Constant.userTypeOwner) {
            ServiceDetailsResponce.DataBean.ConDetailBean conDetailBean = serviceDetailsResponce.getData().getConDetail().get(0);

            String oppoImg = conDetailBean.getProfileImage();
            if (!TextUtils.isEmpty(oppoImg)) {
                Picasso.with(oppoImage.getContext()).load(oppoImg).into(ownImage);
            }
            ownRating.setRating(Float.parseFloat(serviceDetailsResponce.getData().getConReview().getRating()));
            ownName.setText(conDetailBean.getUserName());
            //   ownType.setText("(Consumer Side)");
            if (Float.parseFloat(serviceDetailsResponce.getData().getConReview().getRating()) > 0) {
                ownRating.setIsIndicator(true);
                sendFrm.setVisibility(View.GONE);
                if (innerLay.getChildCount() > 0) {
                    innerLay.removeAllViews();
                }
                TextView myTxt = new EditText(dialog.getContext());
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                myTxt.setLayoutParams(p);
                if (serviceDetailsResponce.getData().getConReview().getReview().equals("")) {
                    myTxt.setText("No review available");
                } else {
                    myTxt.setText(serviceDetailsResponce.getData().getConReview().getReview());
                }
                myTxt.setBackground(null);
                myTxt.setTextSize(12);
                myTxt.setClickable(false);
                myTxt.setFocusable(false);
                innerLay.addView(myTxt);
            } else {
                sendFrm.setVisibility(View.VISIBLE);
                if (innerLay.getChildCount() > 0) {
                    innerLay.removeAllViews();
                }
                rattingCommentTxt = new EditText(dialog.getContext());
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rattingCommentTxt.setLayoutParams(p);
                rattingCommentTxt.setHint("Add your comment");
                rattingCommentTxt.setBackground(null);
                rattingCommentTxt.setTextSize(12);
                innerLay.addView(rattingCommentTxt);
            }
            ServiceDetailsResponce.DataBean.OrderdetailsBean orderdetailsBean = serviceDetailsResponce.getData().getOrderdetails().get(0);
            String myPrImg = orderdetailsBean.getUserInfo().getProfileImage();
            if (!TextUtils.isEmpty(myPrImg)) {
                Picasso.with(oppoImage.getContext()).load(myPrImg).into(oppoImage);
            }
            oppoRating.setRating(Float.parseFloat(serviceDetailsResponce.getData().getOwnReview().getRating()));
            oppoName.setText(orderdetailsBean.getUserInfo().getBusinessName());
            //  oppoType.setText("(Owner Side)");
            ownImage.setBorderColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
            ownImage.setBorderWidth(2);
        }

        dialog.setCancelable(true);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ownLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickId != R.id.ownLay) {
                    clickId = R.id.ownLay;
                    if (userType == Constant.userTypeOwner) {
                        // rattingCommentTxt.setText(serviceDetailsResponce.getData().getConReview().getReview());
                        if (Float.parseFloat(serviceDetailsResponce.getData().getConReview().getRating()) > 0) {
                            sendFrm.setVisibility(View.GONE);
                        /*rattingCommentTxt.setClickable(false);
                        rattingCommentTxt.setFocusable(false);*/
                            innerLay.removeAllViews();
                            TextView myTxt = new EditText(dialog.getContext());
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            myTxt.setLayoutParams(p);
                            myTxt.setBackground(null);
                            myTxt.setText(serviceDetailsResponce.getData().getConReview().getReview());
                            if (serviceDetailsResponce.getData().getConReview().getReview().equals("")) {
                                myTxt.setText("No review available");
                            }
                            myTxt.setTextSize(12);
                            myTxt.setClickable(false);
                            myTxt.setFocusable(false);
                            innerLay.addView(myTxt);
                        } else {
                            sendFrm.setVisibility(View.VISIBLE);
                            innerLay.removeAllViews();
                            rattingCommentTxt = new EditText(dialog.getContext());
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            rattingCommentTxt.setLayoutParams(p);
                            rattingCommentTxt.setHint("Add your comment");
                            rattingCommentTxt.setBackground(null);
                            rattingCommentTxt.setTextSize(12);
                            innerLay.addView(rattingCommentTxt);
                            //getEdittextFocuse(dialog, rattingCommentTxt);
                        }
                    } else if (userType == Constant.uerTypeConsumer) {
                        // rattingCommentTxt.setText(serviceDetailsResponce.getData().getOwnReview().getReview());
                        if (Float.parseFloat(serviceDetailsResponce.getData().getOwnReview().getRating()) > 0) {
                        /*rattingCommentTxt.setClickable(false);
                        rattingCommentTxt.setFocusable(false);*/
                            innerLay.removeAllViews();
                            TextView myTxt = new EditText(dialog.getContext());
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            myTxt.setLayoutParams(p);
                            myTxt.setText(serviceDetailsResponce.getData().getOwnReview().getReview());
                            myTxt.setBackground(null);

                            //loseFocusEditText(rattingCommentTxt);
                            sendFrm.setVisibility(View.GONE);
                            if (serviceDetailsResponce.getData().getOwnReview().getReview().equals("")) {
                                myTxt.setText("No review available");
                            }
                            myTxt.setTextSize(12);
                            myTxt.setClickable(false);
                            myTxt.setFocusable(false);
                            innerLay.addView(myTxt);
                        } else {
                            sendFrm.setVisibility(View.VISIBLE);
                            innerLay.removeAllViews();
                            rattingCommentTxt = new EditText(dialog.getContext());
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            rattingCommentTxt.setLayoutParams(p);
                            rattingCommentTxt.setHint("Add your comment");
                            rattingCommentTxt.setBackground(null);
                            rattingCommentTxt.setTextSize(12);
                            innerLay.addView(rattingCommentTxt);
                            // getEdittextFocuse(innerLay);
                        }
                    }
                    ownImage.setBorderColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
                    ownImage.setBorderWidth(2);
                    oppoImage.setBorderWidth(0);

                }
            }
        });
        oopoLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickId != R.id.oopoLay) {
                    clickId = R.id.oopoLay;
                    innerLay.removeAllViews();
                    TextView myTxt = new EditText(dialog.getContext());
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    myTxt.setLayoutParams(p);

                    if (userType == Constant.userTypeOwner) {
                        myTxt.setText(serviceDetailsResponce.getData().getOwnReview().getReview());
                    } else if (userType == Constant.uerTypeConsumer) {
                        myTxt.setText(serviceDetailsResponce.getData().getConReview().getReview());
                    }
                    oppoImage.setBorderColor(ContextCompat.getColor(ActivityRequestStatus.this, R.color.colorPrimaryDark));
                    oppoImage.setBorderWidth(2);
                    ownImage.setBorderWidth(0);
                    //  rattingCommentTxt.setClickable(false);
                    // rattingCommentTxt.setFocusable(false);
                    if (userType == Constant.uerTypeConsumer) {
                        if (serviceDetailsResponce.getData().getConReview().getReview().equals("")) {
                            myTxt.setText("No review available");
                        }
                    } else if (userType == Constant.userTypeOwner) {
                        if (serviceDetailsResponce.getData().getOwnReview().getReview().equals("")) {
                            myTxt.setText("No review available");
                        }
                    }
                    sendFrm.setVisibility(View.GONE);
                    myTxt.setBackground(null);
                    myTxt.setTextSize(12);
                    myTxt.setClickable(false);
                    myTxt.setFocusable(false);
                    innerLay.addView(myTxt);
                }
            }
        });
        ownRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
            }
        });
        sendFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rate > 0) {
                    giveReview(dialog, rattingCommentTxt.getText().toString().trim(), String.valueOf(rate), containerLay, dlproBar);
                } else {
                    Constant.snackbar(containerLay, "Please give rating");
                }
            }
        });
        askForReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.snackbar(containerLay, getString(R.string.development_mode));
            }
        });

    }

    public void giveReview(final Dialog dialog, final String review, final String rating, LinearLayout containerLay, final ProgressBar dlproBar) {
        if (Constant.isNetworkAvailable(this, containerLay)) {
            dlproBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.giveReviewUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("#" + response);
                            Log.e("ServiceDetails", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    dialog.dismiss();
                                    getServiceDetails();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    dlproBar.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    Constant.showLogOutDialog(ActivityRequestStatus.this);
                                } else if (status.equalsIgnoreCase("fail")) {
                                    dlproBar.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    Constant.snackbar(mainLayout, message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dlproBar.setVisibility(View.GONE);
                            dialog.dismiss();
                            try {
                                Log.e("ActivitySerViceDetails", error.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityRequestStatus.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", "" + serviceDetailsResponce.getData().getOrderdetails().get(0).getRequestId());
                    params.put("review", review);
                    params.put("rating", rating);
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
            VolleySingleton.getInstance(ActivityRequestStatus.this).addToRequestQueue(stringRequest);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int notiRequedId = intent.getIntExtra(Constant.REQUEST_ID, 0);
            String notiTitle = intent.getStringExtra(Constant.TITLE);
            String notiMsg = intent.getStringExtra(Constant.MSG);
            if (notiRequedId == requestId) {
                getServiceDetails();
            } else {
                ServiceProgressDetailsNotification(notiTitle, notiRequedId, notiMsg);
            }
        }
    };

    private void ServiceProgressDetailsNotification(String title, int requestId, String content) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, new Intent(this, ActivityMyJobServiceDetailsLayout.class)
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
    protected void onResume() {
        super.onResume();
        ActivityRequestStatus.this.registerReceiver(receiver, new IntentFilter("FILTERSERVICEPROGRESS"));
        if (isBack) {
            getServiceDetails();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        ActivityRequestStatus.this.unregisterReceiver(receiver);
    }
}