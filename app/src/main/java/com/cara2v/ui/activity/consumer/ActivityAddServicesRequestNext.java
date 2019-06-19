package com.cara2v.ui.activity.consumer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.R;
import com.cara2v.adapter.GetServiceReqAdapter;
import com.cara2v.bean.RequestServiceBean;
import com.cara2v.responceBean.AddServicesResponce;
import com.cara2v.ui.activity.ActivityServiceDetails;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAddServicesRequestNext extends Activity implements View.OnClickListener {

    private LinearLayout mainLayout;

    private RecyclerView serviceRqRecycler;
    private TextView titleTxt;
    private ImageView menuBtn;
    private ImageView tabRightIcon;
    private ProgressDialog progressDialog;
    private Gson gson = new Gson();
    private int width = 0, height = 0;
    private String requestJson = "";

    private ArrayList<AddServicesResponce.DataBean> serviceList;
    GetServiceReqAdapter getServiceReqAdapter;
    private ArrayList<RequestServiceBean> requestServiceBeanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services_requst_next);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestJson = "" + bundle.getString("requestJson");
            Log.e("requestId", requestJson);
        }

        requestServiceBeanArrayList = new ArrayList<>();
        serviceList = new ArrayList<>();
        initializeView();

        getServiceReqAdapter = new GetServiceReqAdapter(this, serviceList, new RemoveImageListioner() {
            @Override
            public void onClick(int position) {
                popUpServiceInfo(serviceList.get(position));
            }
        });
        serviceRqRecycler.setAdapter(getServiceReqAdapter);
        getServices();


    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        menuBtn = findViewById(R.id.menuBtn);
        tabRightIcon = findViewById(R.id.tabRightIcon);
        titleTxt = findViewById(R.id.titleTxt);
        mainLayout = findViewById(R.id.mainLayout);
        serviceRqRecycler = findViewById(R.id.serviceRqRecycler);
        tabRightIcon.setVisibility(View.VISIBLE);
        menuBtn.setImageResource(R.drawable.back_icon);
        tabRightIcon.setImageResource(R.drawable.ic_check_icon);
        titleTxt.setText("Select Service Request");
        menuBtn.setOnClickListener(this);
        tabRightIcon.setOnClickListener(this);

        if (!TextUtils.isEmpty(requestJson)) {
            RequestServiceBean[] requestServiceBean = gson.fromJson(requestJson, RequestServiceBean[].class);
            final List<RequestServiceBean> requestServiceBeans = Arrays.asList(requestServiceBean);
            requestServiceBeanArrayList.addAll(requestServiceBeans);
        }
    }

    public void getServices() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getServicesUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("BRAND", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    AddServicesResponce addServicesResponce = gson.fromJson(response, AddServicesResponce.class);
                                    //serviceList.clear();

                                    setSelectedServicedata(addServicesResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityAddServicesRequestNext.this);
                                } else {
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
                            progressDialog.dismiss();
                            Constant.errorHandle(error, ActivityAddServicesRequestNext.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityAddServicesRequestNext.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


               /* @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("carId", carId);
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }*/

                /*@Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }*/
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(ActivityAddServicesRequestNext.this).addToRequestQueue(stringRequest);
        }
    }

    private void setSelectedServicedata(AddServicesResponce addServicesResponce) {



        serviceList.clear();
        //serviceList.addAll(addServicesResponce.getData());
        for (AddServicesResponce.DataBean dataBean : addServicesResponce.getData()) {
            //if (dataBean.getStatus().equals("1")) {
            AddServicesResponce.DataBean dataBean1 = new AddServicesResponce.DataBean();
            ArrayList<AddServicesResponce.DataBean.SubServiceBean> subServiceBeans = new ArrayList<>();
            for (AddServicesResponce.DataBean.SubServiceBean subServiceBean : dataBean.getSubService()) {
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

        for (RequestServiceBean requestServiceBean : requestServiceBeanArrayList) {
            for (RequestServiceBean.Subservice subservice : requestServiceBean.subService) {
                for (AddServicesResponce.DataBean servicesResponce : serviceList) {
                    for (AddServicesResponce.DataBean.SubServiceBean subServiceBean : servicesResponce.getSubService()) {
                        if (subservice.id.equals(String.valueOf(subServiceBean.get_id()))) {
                            subServiceBean.setChecked(true);
                        }
                    }
                }
            }
        }
        getServiceReqAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                getServiceJson();
                AddServiceRequestActivity.getJsonData(requestServiceBeanArrayList);
                finish();
                break;
            case R.id.tabRightIcon:
                getServiceJson();
                AddServiceRequestActivity.getJsonData(requestServiceBeanArrayList);
                finish();
                break;
        }
    }

    private void getServiceJson() {
        requestServiceBeanArrayList.clear();
        for (int i = 0; i < serviceList.size(); i++) {
            AddServicesResponce.DataBean dataBean = serviceList.get(i);
            boolean isDataChecked = false;
            ArrayList<RequestServiceBean.Subservice> subservices = new ArrayList<>();

            for (int j = 0; j < dataBean.getSubService().size(); j++) {
                AddServicesResponce.DataBean.SubServiceBean subServiceBean = dataBean.getSubService().get(j);

                if (subServiceBean.isChecked()) {
                    isDataChecked = true;
                    RequestServiceBean.Subservice subservice = new RequestServiceBean.Subservice();
                    subservice.id = "" + subServiceBean.get_id();
                    subservice.name = subServiceBean.getSubServiceName();
                    subservices.add(subservice);
                }
            }

            if (isDataChecked) {
                RequestServiceBean requestServiceBean = new RequestServiceBean();
                requestServiceBean.id = "" + dataBean.get_id();
                requestServiceBean.name = dataBean.getServiceName();
                requestServiceBean.subService = subservices;
                requestServiceBeanArrayList.add(requestServiceBean);
            }
        }
    }

    void popUpServiceInfo(AddServicesResponce.DataBean dataBean) {
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
