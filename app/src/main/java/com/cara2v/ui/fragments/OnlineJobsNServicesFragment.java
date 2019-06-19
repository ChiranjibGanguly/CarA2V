package com.cara2v.ui.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.OnDeleteListioner;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.adapter.OnLineServicesAdapter;
import com.cara2v.responceBean.MyServiceRequestResponce;
import com.cara2v.ui.activity.ActivityServiceDetails;
import com.cara2v.ui.activity.consumer.AddServiceRequestActivity;
import com.cara2v.ui.activity.owner.ActivtySentServiceDetailsLayout;
import com.cara2v.ui.activity.owner.StripeBankAddActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnlineJobsNServicesFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView onlineServiceJobsRecycler;
    private CardView addNewReqLay;
    private CoordinatorLayout snackLayout;
    private Context mContext;
    private OnLineServicesAdapter onLineServicesAdapter;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private int userTtype = 0;
    private Gson gson = new Gson();
    private ArrayList<MyServiceRequestResponce.DataBean> requestArrayList;
    private int width = 0, height = 0;
    private boolean isFirstTime=false;

    public static OnlineJobsNServicesFragment newInstance(String requestId) {
        OnlineJobsNServicesFragment fragment = new OnlineJobsNServicesFragment();
        Bundle args = new Bundle();
        args.putString("requestId", requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online_jobs_n_services, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mContext.registerReceiver(receiver, new IntentFilter("FILTERREQUEST"));
        userTtype = PreferenceConnector.readInteger(mContext, PreferenceConnector.UserType, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        isFirstTime=false;
        if (userTtype == Constant.userTypeOwner) {
            getMyOrder();
        } else if (userTtype == Constant.uerTypeConsumer) {
            getMyRequest();
        }
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
            String requestId = "";
            requestId = args.getString("requestId");
            if (!TextUtils.isEmpty(requestId)) {
                requestId = args.getString("requestId");
                Intent intent = new Intent(mContext, ActivityServiceDetails.class);
                intent.putExtra("requestId", Integer.parseInt(requestId));
                startActivity(intent);
            }
        }
    }

    private void initializeView(View view) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        onlineServiceJobsRecycler = view.findViewById(R.id.onlineServiceJobsRecycler);
        // addFabBtn = view.findViewById(R.id.addFabBtn);
        snackLayout = view.findViewById(R.id.snackLayout);
        progressBar = view.findViewById(R.id.progressBar);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        addNewReqLay = view.findViewById(R.id.addNewReqLay);
        addNewReqLay.setOnClickListener(this);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        requestArrayList = new ArrayList<>();
        if (userTtype == Constant.userTypeOwner) {
            addNewReqLay.setVisibility(View.GONE);
        }
        onLineServicesAdapter = new OnLineServicesAdapter(mContext, requestArrayList, new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {
                if (userTtype == Constant.userTypeOwner) {
                    if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.IsLcnAprv, false)) {
                        if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.IsBankAccAdd, false)) {
                            MyServiceRequestResponce.DataBean dataBean = requestArrayList.get(position);
                            if (dataBean.getStatus() == 1 && dataBean.getMyRequest() == 1) {
                                Intent intent = new Intent(mContext, ActivtySentServiceDetailsLayout.class);
                                intent.putExtra("requestId", requestArrayList.get(position).get_id());
                                startActivity(intent);
                            } else if (dataBean.getStatus() == 1 && dataBean.getMyRequest() == 0) {
                                Intent intent = new Intent(mContext, ActivityServiceDetails.class);
                                intent.putExtra("requestId", requestArrayList.get(position).get_id());
                                startActivity(intent);
                            } else if (dataBean.getStatus() == 2) {
                                Intent intent = new Intent(mContext, ActivtySentServiceDetailsLayout.class);
                                intent.putExtra("requestId", requestArrayList.get(position).get_id());
                                startActivity(intent);
                            }
                        } else {
                            popAddAccount();
                        }
                    }else {
                        Constant.snackbar(snackLayout,"Please wait! Until your license will approve.");
                    }
                } else {
                    Intent intent = new Intent(mContext, ActivityServiceDetails.class);
                    intent.putExtra("requestId", requestArrayList.get(position).get_id());
                    startActivity(intent);
                }

            }
        }, new RemoveImageListioner() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), AddServiceRequestActivity.class);
                intent.putExtra("FROM", "edit");
                intent.putExtra("requestId", requestArrayList.get(position).get_id());
                startActivity(intent);
            }
        }, new OnDeleteListioner() {
            @Override
            public void onDelete(int position) {
                Constant.snackbar(snackLayout, getString(R.string.development_mode));
              //    showConfirmDialog(String.valueOf(requestArrayList.get(position).get_id()));
            }
        });
        onlineServiceJobsRecycler.setAdapter(onLineServicesAdapter);
    }

    private void deleteService(final String requestId) {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.updateServiceRequestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("ServiceRequest", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    getMyRequest();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null)
                                        Constant.showLogOutDialog(getActivity());
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
                            if (getActivity() != null) Constant.errorHandle(error, getActivity());
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
                    params.put("requestId", requestId);
                    params.put("deleteYes", "1");
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewReqLay:
                if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.IsCarAdd, false)) {
                    Intent intent = new Intent(getActivity(), AddServiceRequestActivity.class);
                    intent.putExtra("FROM", "new");
                    startActivity(intent);
                } else {
                    Constant.snackbar(snackLayout, "Please add vehicle first");
                }
                break;
        }
    }

    public void getMyRequest() {
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
                            Log.e("MyRequesttttt", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    MyServiceRequestResponce myServiceRequestResponce = gson.fromJson(response, MyServiceRequestResponce.class);
                                    requestArrayList.clear();
                                    requestArrayList.addAll(myServiceRequestResponce.getData());
                                    // onLineServicesAdapter.notifyDataSetChanged();
                                    onLineServicesAdapter.notifyDataSetChanged();
                                    if (myServiceRequestResponce.getData().size() > 0) {
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

    public void getMyOrder() {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            if (!isFirstTime)progressBar.setVisibility(View.VISIBLE);
            isFirstTime=true;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getMyOrderUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                            System.out.println("#" + response);
                            Log.e("MyOrder", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    MyServiceRequestResponce myServiceRequestResponce = gson.fromJson(response, MyServiceRequestResponce.class);
                                    if (myServiceRequestResponce.getLicenseStatus() == 1) {
                                        PreferenceConnector.writeBoolean(mContext, PreferenceConnector.IsLcnAprv, true);
                                    } else {
                                        PreferenceConnector.writeBoolean(mContext, PreferenceConnector.IsLcnAprv, false);
                                    }
                                    requestArrayList.clear();
                                    requestArrayList.addAll(myServiceRequestResponce.getData());
                                    onLineServicesAdapter.notifyDataSetChanged();
                                    if (myServiceRequestResponce.getData().size() > 0) {
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

    void popAddAccount() {
        final Dialog dialog = new Dialog(mContext);
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
                startActivity(new Intent(getActivity(), StripeBankAddActivity.class));
                dialog.dismiss();
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (userTtype == Constant.userTypeOwner) {
                getMyOrder();
            } else if (userTtype == Constant.uerTypeConsumer) {
                getMyRequest();
            }
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

    public void showConfirmDialog(final String requestId) {

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(mContext);
        builder1.setTitle("Alart !!");
        builder1.setMessage("Are you sure you want to Delete this service request");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        deleteService(requestId);
                    }
                });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });

        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onRefresh() {
        if (userTtype == Constant.userTypeOwner) {
            getMyOrder();
        } else if (userTtype == Constant.uerTypeConsumer) {
            getMyRequest();
        }
    }
}
