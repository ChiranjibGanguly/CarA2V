package com.cara2v.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.support.design.widget.CoordinatorLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.R;
import com.cara2v.adapter.MyNotificationAdapter;
import com.cara2v.responceBean.GetMyNotifcationResponce;
import com.cara2v.ui.activity.ActivityMyJobServiceDetailsLayout;
import com.cara2v.ui.activity.ActivityServiceDetails;
import com.cara2v.ui.activity.MainActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentNotifications extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MyOnCheckListioner {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView notificationRecycler;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout snackLayout;
    private Context mContext;
    private ArrayList<GetMyNotifcationResponce.DataBean> notificationList;
    private MyNotificationAdapter myNotificationAdapter;
    private boolean isFirstTime = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        view.requestFocus();
        initializeView(view);
        getMyNotification();
    }

    @Override
    public void onResume() {
        super.onResume();
        isFirstTime = false;
        getMyNotification();
    }

    private void initializeView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        notificationRecycler = view.findViewById(R.id.notificationRecycler);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        progressBar = view.findViewById(R.id.progressBar);
        snackLayout = view.findViewById(R.id.snackLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        notificationList = new ArrayList<>();
    }

    @Override
    public void onRefresh() {
        getMyNotification();
    }

    public void getMyNotification() {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            if (!isFirstTime) progressBar.setVisibility(View.VISIBLE);
            isFirstTime = true;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getMyNotification,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                            System.out.println("#" + response);
                            Log.e("Notification", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    GetMyNotifcationResponce myNotifcationResponce = new Gson().fromJson(response, GetMyNotifcationResponce.class);
                                    updateUi(myNotifcationResponce);
                                    if (myNotifcationResponce.getData().size() > 0) {
                                        visibleLayout.setVisibility(View.GONE);
                                    } else {
                                        visibleLayout.setVisibility(View.VISIBLE);
                                    }
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null)
                                        Constant.showLogOutDialog(getActivity());
                                } else {
                                    visibleLayout.setVisibility(View.VISIBLE);
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
                            swipeRefreshLayout.setRefreshing(false);
                            Constant.errorHandle(error, getActivity());
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(mContext, PreferenceConnector.AuthToken, ""));
                    return header;
                }


               /* @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }*/
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }
    }

    private void updateUi(GetMyNotifcationResponce myNotifcationResponce) {
        notificationList.clear();
        notificationList.addAll(myNotifcationResponce.getData());
        myNotificationAdapter = new MyNotificationAdapter(mContext, notificationList, myNotifcationResponce.getDate(), this);
        notificationRecycler.setAdapter(myNotificationAdapter);
    }

    @Override
    public void OnCheck(int position) {
        GetMyNotifcationResponce.DataBean dataBean = notificationList.get(position);
        switch (dataBean.getNotification().getNotifyType()) {
            case "request":
            case "Quote":
                Intent intent = new Intent(mContext, ActivityServiceDetails.class);
                intent.putExtra(Constant.REQUEST_ID, Integer.parseInt(dataBean.getNotification().getRequestId()));
                startActivity(intent);
                break;
            case "Start":
            case "Progress":
            case "Almost":
            case "End":
            case "Ask":
            case "payFirst":
            case "paySecond":
            case "reminder":
            case "Accept":
                Intent intentProgrerss = new Intent(mContext, ActivityMyJobServiceDetailsLayout.class);
                intentProgrerss.putExtra("requestId", Integer.parseInt(dataBean.getNotification().getRequestId()));
                intentProgrerss.putExtra("STATE", "pending");
                startActivity(intentProgrerss);
                break;
                case "Review":
                Intent intentComplete = new Intent(mContext, ActivityMyJobServiceDetailsLayout.class);
                    intentComplete.putExtra("requestId", Integer.parseInt(dataBean.getNotification().getRequestId()));
                    intentComplete.putExtra("STATE", "complete");
                startActivity(intentComplete);
                break;
           /* case "Quote":
               // requestcreateNQuoteNotification(remoteMessage, title, body, type);
                break;*/
            /*case "Accept":
               // requestAcceptNotification(remoteMessage, title, body, type);
                break;*/
            /*case "chat":
               // chatNotification(remoteMessage, title);
                break;*/
            case "licence":
            case "Commission":
                MainActivity activity = (MainActivity) getActivity();
                activity.manageRdTabLay("");
                /*String status = remoteMessage.getData().get("status").toString();
                if (status.equals("1")) {
                    PreferenceConnector.writeBoolean(getApplicationContext(), PreferenceConnector.IsLcnAprv, true);
                } else {
                    PreferenceConnector.writeBoolean(getApplicationContext(), PreferenceConnector.IsLcnAprv, false);
                }
                sendNotification(title, body);*/
                break;
        }
    }
}
