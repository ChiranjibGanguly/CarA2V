package com.cara2v.ui.activity.owner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.cara2v.adapter.GetServiceAdapter;
import com.cara2v.responceBean.AddServicesResponce;
import com.cara2v.ui.activity.MainActivity;
import com.cara2v.ui.activity.consumer.AddVehicleInfoActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddServicesActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView serviceRecycler;
    private LinearLayout mainLayout;
    private RelativeLayout titleLay;
    private Button skipBtn;
    private ImageView menuBtn;
    private ImageView backImg;
    private TextView titleTxt;
    private ProgressDialog progressDialog;
    private Gson gson = new Gson();
    private GetServiceAdapter getServiceAdapter;
    private ArrayList<AddServicesResponce.DataBean> serviceList;
    private int width = 0, height = 0;
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getString("FROM");
        }
        initializeView();
        getServices();
    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        serviceRecycler = findViewById(R.id.serviceRecycler);

        mainLayout = findViewById(R.id.mainLayout);
        menuBtn = findViewById(R.id.menuBtn);
        backImg = findViewById(R.id.backImg);
        titleTxt = findViewById(R.id.titleTxt);
        titleLay = findViewById(R.id.titleLay);
        skipBtn = findViewById(R.id.skipBtn);
        titleLay.setVisibility(View.GONE);

        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Add car service");
        menuBtn.setOnClickListener(this);
        skipBtn.setOnClickListener(this);
        backImg.setOnClickListener(this);
        serviceList = new ArrayList<>();
        // serviceRecycler.setHasFixedSize(true);
        getServiceAdapter = new GetServiceAdapter(this, serviceList, new RemoveImageListioner() {
            @Override
            public void onClick(int position) {
                popUpServiceInfo(serviceList.get(position));
            }
        }, new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {
                addRemoveServices(position);
            }
        }, "addService");
        serviceRecycler.setAdapter(getServiceAdapter);


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
                            Log.e("SERVICES", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    AddServicesResponce addServicesResponce = gson.fromJson(response, AddServicesResponce.class);
                                    updateUi(addServicesResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(AddServicesActivity.this);
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
                            Constant.errorHandle(error, AddServicesActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(AddServicesActivity.this, PreferenceConnector.AuthToken, ""));
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
            VolleySingleton.getInstance(AddServicesActivity.this).addToRequestQueue(stringRequest);
        }
    }

    private void updateUi(AddServicesResponce addServicesResponce) {
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
        getServiceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;
            case R.id.backImg:
                finish();
                break;
            case R.id.skipBtn:
                if (from.equals("SignupActivity")) {
                    startActivity(new Intent(AddServicesActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                } else {
                    finish();
                }
                break;
        }
    }

    void popUpServiceInfo(AddServicesResponce.DataBean dataBean) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_service_info);
        dialog.getWindow().setLayout((width * 10) / 11, (height * 1) / 2);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = dialog.findViewById(R.id.popCancel);
        final TextView popTitle = dialog.findViewById(R.id.popTitle);
        final TextView popDescription = dialog.findViewById(R.id.popDescription);
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

    private void addRemoveServices(final int position) {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.addRemoveServicesUrl,
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
                                    if (serviceList.get(position).getStatus().equals("1")) {
                                        serviceList.get(position).setStatus("0");
                                    } else if (serviceList.get(position).getStatus().equals("0")) {
                                        serviceList.get(position).setStatus("1");
                                    }
                                    getServiceAdapter.notifyItemChanged(position);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(AddServicesActivity.this);
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
                            Constant.errorHandle(error, AddServicesActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(AddServicesActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("serviceId", "" + serviceList.get(position).get_id());
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
            VolleySingleton.getInstance(AddServicesActivity.this).addToRequestQueue(stringRequest);
        }
    }

}
