package com.cara2v.ui.fragments.consumer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
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
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.adapter.MyServicesAdapter;
import com.cara2v.responceBean.MyServicesResponce;
import com.cara2v.ui.activity.ActivityMyJobServiceDetailsLayout;
import com.cara2v.ui.activity.ActivityMyServiceJobsChargeDetails;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentMyServices extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView myServicesRecycler;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout snackLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private Gson gson = new Gson();
    private MyServicesAdapter myServicesAdapter;
    private ArrayList<MyServicesResponce.DataBean> servicesArrayList;
    private boolean isFirstTime=false;
    private String state="";

    public static FragmentMyServices newInstance(String requestId,String state) {
        FragmentMyServices fragment = new FragmentMyServices();
        Bundle args = new Bundle();
        args.putString("requestId", requestId);
        args.putString("STATE", state);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_services, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        view.requestFocus();
        initializeView(view);
        Bundle args = getArguments();
        if (args != null) {
            String requestId="";
            requestId= args.getString("requestId");
            state= args.getString("STATE");
            if (!TextUtils.isEmpty(requestId)){
                Intent intent = new Intent(mContext, ActivityMyJobServiceDetailsLayout.class);
                intent.putExtra("requestId", Integer.parseInt(requestId));
                intent.putExtra("STATE", state);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mContext.registerReceiver(receiver, new IntentFilter("FILTERSERVICEPROGRESS"));
    }

    @Override
    public void onResume() {
        super.onResume();
        isFirstTime=false;
        getMyServices();
    }

    private void initializeView(View view) {
        myServicesRecycler = view.findViewById(R.id.myServicesRecycler);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        progressBar = view.findViewById(R.id.progressBar);
        snackLayout = view.findViewById(R.id.snackLayout);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        servicesArrayList = new ArrayList<>();
        myServicesAdapter = new MyServicesAdapter(mContext, servicesArrayList, new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(mContext, ActivityMyJobServiceDetailsLayout.class);
                intent.putExtra("requestId", servicesArrayList.get(position).get_id());
                intent.putExtra("STATE", state);
                startActivity(intent);
            }
        });
        myServicesRecycler.setAdapter(myServicesAdapter);
    }

    public void getMyServices() {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            if (!isFirstTime)progressBar.setVisibility(View.VISIBLE);
            isFirstTime=true;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getMyRequestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                            System.out.println("#" + response);
                            Log.e("MyService", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    MyServicesResponce myServicesResponce = gson.fromJson(response, MyServicesResponce.class);
                                    servicesArrayList.clear();
                                    servicesArrayList.addAll(myServicesResponce.getData());
                                    myServicesAdapter.notifyDataSetChanged();
                                    if (myServicesResponce.getData().size() > 0) {
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
                            mSwipeRefreshLayout.setRefreshing(false);
                            Constant.errorHandle(error, getActivity());
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(mContext, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("status", "1");
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
            VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getMyServices();
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

    @Override
    public void onRefresh() {
        getMyServices();
    }
}
