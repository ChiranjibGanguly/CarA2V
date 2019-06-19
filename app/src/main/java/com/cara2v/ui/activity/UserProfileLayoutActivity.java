package com.cara2v.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.adapter.UserPostAdapter;
import com.cara2v.adapter.UserReviewAdapter;
import com.cara2v.adapter.UserServiceAdapter;
import com.cara2v.chat.ChatActivity;
import com.cara2v.responceBean.GetUserProfileInfoResponce;

import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserProfileLayoutActivity extends AppCompatActivity implements View.OnClickListener {


    private AppBarLayout appBar;
    private CoordinatorLayout coOrdinateLay;
    private CollapsingToolbarLayout toolbarLayout;
    private RelativeLayout colLay;

    private LinearLayout imgLay, visibleLayout;
    private TextView userNameTxt;
    private ImageView userProfileImage;
    private AppCompatRatingBar userRating;
    private TextView addressTxt;
    private LinearLayout firstFrm;
    private ImageView firstFrmImg;
    private LinearLayout sndFrm;
    private ImageView sndFrmImg;
    private FrameLayout chatFrm;
    private Toolbar toolbar;
    // private TextView feedTab;
    private TextView serviceTab;
    private TextView reviewTab;
    // private FrameLayout userFragmentPlace;
    private RecyclerView profleRecycler;
    private ProgressBar progressBar;

    private ImageView menuBtn;
    private TextView titleTxt;
    private TextView noDataTxt;

    private FragmentManager fm;
    // private int userType = 0;
    private String userId = "";
    private int height = 0, width = 0;

    private UserReviewAdapter userReviewAdapter;
    private UserServiceAdapter userServiceAdapter;
    // private UserPostAdapter userPostAdapter;
    private GetUserProfileInfoResponce getUserProfileInfoResponce;
    private ArrayList<GetUserProfileInfoResponce.UserServiceListBean>serviceList=new ArrayList<>();

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = "" + bundle.getInt("userId");
            Log.e("userId", userId);
        }
        //userType = PreferenceConnector.readInteger(this, PreferenceConnector.UserType, 0);
        initializeView();
        getUserInfo();
        //replaceFragment(new OnDevelopmentFragment(), false, R.id.userFragmentPlace);

    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        appBar = findViewById(R.id.app_bar);
        coOrdinateLay = findViewById(R.id.coOrdinateLay);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        colLay = findViewById(R.id.colLay);

        imgLay = findViewById(R.id.imgLay);
        userNameTxt = findViewById(R.id.userNameTxt);
        userProfileImage = findViewById(R.id.userProfileImage);
        userRating = findViewById(R.id.userRating);
        addressTxt = findViewById(R.id.addressTxt);
        noDataTxt = findViewById(R.id.noDataTxt);
        firstFrm = findViewById(R.id.firstFrm);
        firstFrmImg = findViewById(R.id.firstFrmImg);
        sndFrm = findViewById(R.id.sndFrm);
        sndFrmImg = findViewById(R.id.sndFrmImg);
        chatFrm = findViewById(R.id.chatFrm);
        toolbar = findViewById(R.id.toolbar);
        // feedTab = findViewById(R.id.feedTab);
        serviceTab = findViewById(R.id.serviceTab);
        reviewTab = findViewById(R.id.reviewTab);
        profleRecycler = findViewById(R.id.profleRecycler);
        progressBar = findViewById(R.id.progressBar);
        visibleLayout = findViewById(R.id.visibleLayout);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Profile");

        menuBtn.setOnClickListener(this);
        //feedTab.setOnClickListener(this);
        serviceTab.setOnClickListener(this);
        reviewTab.setOnClickListener(this);
        chatFrm.setOnClickListener(this);

        fm = getSupportFragmentManager();
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStackName, 0);
        int i = fm.getBackStackEntryCount();
        while (i > 0) {
            fm.popBackStackImmediate();
            i--;
        }
        if (!fragmentPopped) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //   transaction.setCustomAnimations(R.anim.slide_right_out, R.anim.slide_right_in);
            transaction.replace(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;
            /*case R.id.feedTab:
                feedTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorPrimaryDark));
                serviceTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorInActiveText));
                reviewTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorInActiveText));
                if (getUserProfileInfoResponce.getSocialPost().size() > 0) {
                    noDataTxt.setVisibility(View.GONE);
                } else {
                    noDataTxt.setVisibility(View.VISIBLE);
                    noDataTxt.setText("No post available");
                }
                profleRecycler.setAdapter(userPostAdapter);
                break;*/
            case R.id.serviceTab:
                //  feedTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorInActiveText));
                serviceTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorPrimaryDarkHome));
                reviewTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorInActiveText));
                if (getUserProfileInfoResponce.getUserServiceList().size() > 0) {
                    noDataTxt.setVisibility(View.GONE);
                } else {
                    noDataTxt.setVisibility(View.VISIBLE);
                    noDataTxt.setText("No service available");
                }
                profleRecycler.setAdapter(userServiceAdapter);
                break;
            case R.id.reviewTab:
                //   feedTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorInActiveText));
                serviceTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorInActiveText));
                reviewTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorPrimaryDarkHome));
                if (getUserProfileInfoResponce.getReview().size() > 0) {
                    noDataTxt.setVisibility(View.GONE);
                } else {
                    noDataTxt.setVisibility(View.VISIBLE);
                    noDataTxt.setText("No review available");
                }
                profleRecycler.setAdapter(userReviewAdapter);
                break;
            case R.id.chatFrm:
                Intent intent = new Intent(UserProfileLayoutActivity.this, ChatActivity.class);
                intent.putExtra(Constant.FROM, "");
                intent.putExtra(Constant.rcvUId, userId);
                intent.putExtra(Constant.rcvName, getUserProfileInfoResponce.getUserDetail().getUserr().getUserName());
                intent.putExtra(Constant.rcvFireBaseToken, getUserProfileInfoResponce.getUserDetail().getUserr().getDeviceToken());
                intent.putExtra(Constant.rcvPrflImg, getUserProfileInfoResponce.getUserDetail().getUserr().getProfileImage());
                intent.putExtra(Constant.requestId, "");
                intent.putExtra(Constant.requestType, "");
                startActivity(intent);
                break;
        }
    }

    public void getUserInfo() {
        if (Constant.isNetworkAvailable(this, coOrdinateLay)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getUserProfileInfoUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("UserInfo", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    visibleLayout.setVisibility(View.GONE);
                                    getUserProfileInfoResponce = gson.fromJson(response, GetUserProfileInfoResponce.class);
                                    updateUi();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(UserProfileLayoutActivity.this);
                                } else {
                                    Constant.snackbar(coOrdinateLay, message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Constant.errorHandle(error, UserProfileLayoutActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(UserProfileLayoutActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", userId);
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
            VolleySingleton.getInstance(UserProfileLayoutActivity.this).addToRequestQueue(stringRequest);
        }
    }

    private void updateUi() {
        userId = "" + getUserProfileInfoResponce.getUserDetail().getUserr().get_id();
        userNameTxt.setText(getUserProfileInfoResponce.getUserDetail().getUserr().getUserName());
        String imgPath = getUserProfileInfoResponce.getUserDetail().getUserr().getProfileImage();
        if (!TextUtils.isEmpty(imgPath)) {
            try {
                Picasso.with(userProfileImage.getContext()).load(imgPath)
                        .placeholder(R.drawable.user_place_holder)
                        .resize(userProfileImage.getWidth(), userProfileImage.getHeight())
                        // .fit()
                        .into(userProfileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /////////
        sndFrm.setVisibility(View.GONE);
        if (getUserProfileInfoResponce.getUserDetail().getUserr().getUserType() == Constant.userTypeOwner) {
            serviceTab.setVisibility(View.VISIBLE);
        } else if (getUserProfileInfoResponce.getUserDetail().getUserr().getUserType() == Constant.uerTypeConsumer) {
            serviceTab.setVisibility(View.GONE);
        }
        String rating = getUserProfileInfoResponce.getUserDetail().getUserr().getRating();
        if (!TextUtils.isEmpty(rating))
            userRating.setRating(Float.parseFloat(getUserProfileInfoResponce.getUserDetail().getUserr().getRating()));
        addressTxt.setText(getUserProfileInfoResponce.getUserDetail().getUserr().getAddress());

        userReviewAdapter = new UserReviewAdapter(UserProfileLayoutActivity.this, getUserProfileInfoResponce.getReview(), getUserProfileInfoResponce.getNow());

        serviceList.clear();
        //serviceList.addAll(addServicesResponce.getData());
        for (GetUserProfileInfoResponce.UserServiceListBean dataBean : getUserProfileInfoResponce.getUserServiceList()) {
            //if (dataBean.getStatus().equals("1")) {
            GetUserProfileInfoResponce.UserServiceListBean dataBean1 = new GetUserProfileInfoResponce.UserServiceListBean();
            ArrayList<GetUserProfileInfoResponce.UserServiceListBean.SubServiceBean> subServiceBeans = new ArrayList<>();
            for (GetUserProfileInfoResponce.UserServiceListBean.SubServiceBean subServiceBean : dataBean.getSubService()) {
                if (subServiceBean.getStatus() == 1) {
                    subServiceBeans.add(subServiceBean);
                }
            }
            if (subServiceBeans.size() > 0) {
                dataBean1.setSubService(subServiceBeans);
                dataBean1.set_id(dataBean.get_id());
                dataBean1.setUpd(dataBean.getUpd());
                dataBean1.setCrd(dataBean.getCrd());
                dataBean1.setStatus(dataBean.getStatus());
                dataBean1.setServiceImage(dataBean.getServiceImage());
                dataBean1.setServiceName(dataBean.getServiceName());
                dataBean1.set__v(dataBean.get__v());
                serviceList.add(dataBean1);
            }

            // }
        }

        userServiceAdapter = new UserServiceAdapter(UserProfileLayoutActivity.this, serviceList, new RemoveImageListioner() {
            @Override
            public void onClick(int position) {
                popUpServiceInfo(getUserProfileInfoResponce.getUserServiceList().get(position));
            }
        }, new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {

            }
        });
        /*if (getUserProfileInfoResponce.getSocialPost().size() > 0) {
            userPostAdapter = new UserPostAdapter(UserProfileLayoutActivity.this, getUserProfileInfoResponce.getSocialPost(), imgPath);
            profleRecycler.setAdapter(userPostAdapter);
        } else {
            noDataTxt.setVisibility(View.VISIBLE);
            noDataTxt.setText("No post available");
        }*/
        serviceTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorInActiveText));
        reviewTab.setTextColor(ContextCompat.getColor(UserProfileLayoutActivity.this, R.color.colorPrimaryDarkHome));
        if (getUserProfileInfoResponce.getReview().size() > 0) {
            noDataTxt.setVisibility(View.GONE);
        } else {
            noDataTxt.setVisibility(View.VISIBLE);
            noDataTxt.setText("No review available");
        }
        profleRecycler.setAdapter(userReviewAdapter);
    }

    void popUpServiceInfo(GetUserProfileInfoResponce.UserServiceListBean dataBean) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_service_info);
        dialog.getWindow().setLayout((width * 10) / 11, (height * 1) / 2);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = (ImageButton) dialog.findViewById(R.id.popCancel);
        final TextView popTitle = (TextView) dialog.findViewById(R.id.popTitle);
        final TextView popDescription = (TextView) dialog.findViewById(R.id.popDescription);
        popTitle.setText(dataBean.getServiceName());
        popDescription.setText(dataBean.getServiceDescription());
        dialog.setCancelable(true);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
