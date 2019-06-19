package com.cara2v.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
import com.cara2v.adapter.MyCompleteServicesAdapter;
import com.cara2v.adapter.MyCompletedJobsAdapter;
import com.cara2v.responceBean.GetMyJobsResponce;
import com.cara2v.responceBean.MyServicesResponce;
import com.cara2v.ui.activity.ActivityMyJobServiceDetailsLayout;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChildFragmentMyComJobService extends Fragment {

    private RecyclerView comSerJobRecycler;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout snackLayout;
    private Context mContext;
    private int userType = 0;
    private Gson gson = new Gson();
    private MyCompletedJobsAdapter myCompletedJobsAdapter;
    private MyCompleteServicesAdapter myCompleteServicesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.child_fragment_my_com_job_service, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        userType = PreferenceConnector.readInteger(mContext, PreferenceConnector.UserType, 0);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
        if (userType == Constant.userTypeOwner) {
            getMyCompleteJobs();
        } else if (userType == Constant.uerTypeConsumer) {
            getMyCompleteServices();
        }
    }

    private void initializeView(View view) {
        comSerJobRecycler = view.findViewById(R.id.comSerJobRecycler);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        progressBar = view.findViewById(R.id.progressBar);
        snackLayout = view.findViewById(R.id.snackLayout);
    }

    public void getMyCompleteJobs() {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getMyJobUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("MyCompleteJob", response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    GetMyJobsResponce getMyJobsResponce = gson.fromJson(response, GetMyJobsResponce.class);
                                    updateCompletedJobsUi(getMyJobsResponce);
                                    if (getMyJobsResponce.getData().size() > 0) {
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

    public void getMyCompleteServices() {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getMyRequestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("MyCompletedService",  response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    MyServicesResponce myServicesResponce = gson.fromJson(response, MyServicesResponce.class);
                                    /*servicesArrayList.clear();
                                    servicesArrayList.addAll(myServicesResponce.getData());
                                    myServicesAdapter.notifyDataSetChanged();*/
                                    updateCompletedServiceUi(myServicesResponce);
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
                    params.put("status", "3");
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

    private void updateCompletedJobsUi(final GetMyJobsResponce getMyJobsResponce) {
        myCompletedJobsAdapter = new MyCompletedJobsAdapter(mContext, (ArrayList<GetMyJobsResponce.DataBean>) getMyJobsResponce.getData(), new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(mContext, ActivityMyJobServiceDetailsLayout.class);
                intent.putExtra("requestId", getMyJobsResponce.getData().get(position).get_id());
                intent.putExtra("STATE", "complete");
                startActivity(intent);
            }
        });
        comSerJobRecycler.setAdapter(myCompletedJobsAdapter);
    }

    private void updateCompletedServiceUi(final MyServicesResponce myServicesResponce) {
        myCompleteServicesAdapter = new MyCompleteServicesAdapter(mContext, (ArrayList<MyServicesResponce.DataBean>) myServicesResponce.getData(), new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(mContext, ActivityMyJobServiceDetailsLayout.class);
                intent.putExtra("requestId", myServicesResponce.getData().get(position).get_id());
                intent.putExtra("STATE", "complete");
                startActivity(intent);
            }
        });
        comSerJobRecycler.setAdapter(myCompleteServicesAdapter);
    }
}
