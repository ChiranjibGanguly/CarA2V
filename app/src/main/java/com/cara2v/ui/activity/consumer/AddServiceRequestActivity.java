package com.cara2v.ui.activity.consumer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.R;
import com.cara2v.adapter.MyCarAdapter;
import com.cara2v.adapter.SelectedServiceAdapter;
import com.cara2v.bean.RequestServiceBean;
import com.cara2v.responceBean.GetMyCarResponce;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddServiceRequestActivity extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private RelativeLayout titleLay;
    private RelativeLayout vehicleLay;
    private RelativeLayout timeSlotLay;
    private RecyclerView vehcleRecycler;
    private LinearLayout dateTimeLay;
    private TextView vehicleTxt;
    private TextView dateTimeTxt;
    private TextView timeSlotTxt;
    private TextView addressTxt;
    private PlaceAutocompleteFragment placeAddressFragment;
    private RelativeLayout serviceLay;
    private RecyclerView serviceRecycler;
    private View serviceView;
    private View vehicleView;
    private EditText descriptionTxt;
    private ImageButton vehicleBtn;
    private Button btnRequestQuate;

    private ImageView menuBtn;
    private TextView titleTxt;

    private ProgressDialog progressDialog;
    private ArrayList<GetMyCarResponce.DataBean> mycarlist;
    private static ArrayList<RequestServiceBean> requestServiceBeans;
    private MyCarAdapter myCarAdapter;
    private SelectedServiceAdapter selectedServiceAdapter;
    private boolean isVehicleOpen = false;
    private Gson gson = new Gson();

    private DatePickerDialog fromDate;
    private TimePickerDialog myTime;
    private Calendar now;
    private String carId = "", carName = "", addressReq = "", lat = "", lng = "", dateTime = "", requestId = "", from = "";
    private static String requestJson = "";
    private int width = 0, height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_request);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from = "" + bundle.getString("FROM");
        }
        initializeView();
        getMyCars();
        setdataAccordingFrom(bundle);
    }

    private void setdataAccordingFrom(Bundle bundle) {
        requestServiceBeans.clear();
        requestJson = "";
        if (from.equals("new")) {
            titleTxt.setText("Add Service Request");
            String responce = PreferenceConnector.readString(AddServiceRequestActivity.this, PreferenceConnector.userInfoJson, "");
            SignInRespoce signInResponce = gson.fromJson(responce, SignInRespoce.class);
            addressReq = signInResponce.getData().getAddress();
            addressTxt.setText(signInResponce.getData().getAddress());
            lat = String.valueOf(signInResponce.getData().getAddressLatLong().get(0));
            lng = String.valueOf(signInResponce.getData().getAddressLatLong().get(1));
        } else if (from.equals("edit")) {
            titleTxt.setText("Edit Service Request");
            btnRequestQuate.setText("Update Request");
            requestId = "" + bundle.getInt("requestId");
            Log.e("requestId", requestId);
            getServiceDetails();
        }
    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        mainLayout = findViewById(R.id.mainLayout);
        titleLay = findViewById(R.id.titleLay);
        vehicleLay = findViewById(R.id.vehicleLay);
        vehicleTxt = findViewById(R.id.vehicleTxt);
        vehcleRecycler = findViewById(R.id.vehcleRecycler);
        dateTimeLay = findViewById(R.id.dateTimeLay);
        dateTimeTxt = findViewById(R.id.dateTimeTxt);
        addressTxt = findViewById(R.id.addressTxt);
        vehicleView = findViewById(R.id.vehicleView);
        serviceView = findViewById(R.id.serviceView);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        vehicleBtn = findViewById(R.id.vehicleBtn);

        timeSlotLay = findViewById(R.id.timeSlotLay);
        timeSlotTxt = findViewById(R.id.timeSlotTxt);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);


        placeAddressFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.placeAddressFragment);
        serviceLay = findViewById(R.id.serviceLay);
        serviceRecycler = findViewById(R.id.serviceRecycler);

        btnRequestQuate = findViewById(R.id.btnRequestQuate);
        btnRequestQuate.setOnClickListener(this);
        vehicleLay.setOnClickListener(this);
        serviceLay.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        dateTimeLay.setOnClickListener(this);
        timeSlotLay.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        now = Calendar.getInstance();

        placeAddressFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                addressTxt.setText(place.getAddress().toString());
                addressReq = place.getAddress().toString();
                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {

            }
        });

        mycarlist = new ArrayList<>();
        requestServiceBeans = new ArrayList<>();
        myCarAdapter = new MyCarAdapter(this, mycarlist, new MyOnCheckListioner() {
            @Override
            public void OnCheck(int position) {
                carId = "" + mycarlist.get(position).get_id();
                carName = mycarlist.get(position).getBrand().getBrandName();
                if (mycarlist.get(position).getPlateNumber().equals("")) {
                    vehicleTxt.setText(mycarlist.get(position).getBrand().getBrandName());
                } else {
                    vehicleTxt.setText(mycarlist.get(position).getBrand().getBrandName() + " (" + mycarlist.get(position).getPlateNumber() + ")");
                }
                isVehicleOpen = false;
                vehcleRecycler.setVisibility(View.GONE);
                vehicleView.setVisibility(View.GONE);
                vehicleBtn.setBackgroundResource(R.drawable.down_arrow_service);
            }
        });
        vehcleRecycler.setAdapter(myCarAdapter);
        selectedServiceAdapter = new SelectedServiceAdapter(this, requestServiceBeans);
        serviceRecycler.setAdapter(selectedServiceAdapter);
        serviceRecycler.setHasFixedSize(true);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRequestQuate:
                if (from.equals("new")) {
                    if (isValidData()) sendServiceRequestQuote();
                } else if (from.equals("edit")) {
                    if (isValidData()) updateServiceRequestQuote();
                }
                break;
            case R.id.vehicleLay:
                if (isVehicleOpen) {
                    isVehicleOpen = false;
                    vehcleRecycler.setVisibility(View.GONE);
                    vehicleView.setVisibility(View.GONE);
                    vehicleBtn.setBackgroundResource(R.drawable.down_arrow_service);
                } else {
                    isVehicleOpen = true;
                    vehcleRecycler.setVisibility(View.VISIBLE);
                    vehicleView.setVisibility(View.VISIBLE);
                    vehicleBtn.setBackgroundResource(R.drawable.up_arrow_service);
                }
                break;
            case R.id.serviceLay:
                Intent intent = new Intent(this, ActivityAddServicesRequestNext.class);
                intent.putExtra("requestJson", requestJson);
                startActivity(intent);
                break;
            case R.id.menuBtn:
                finish();
                break;
            case R.id.dateTimeLay:
                setDateField(dateTimeTxt);
                break;
            case R.id.timeSlotLay:
                popUpTimeSlot(timeSlotTxt);
                break;
        }
    }

    public void getMyCars() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getMyCarUrl,
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
                                    GetMyCarResponce getMyCarResponce = gson.fromJson(response, GetMyCarResponce.class);
                                    //serviceList.clear();

                                    mycarlist.addAll(getMyCarResponce.getData());
                                    //serviceRecycler.setAdapter(getServiceAdapter);
                                    myCarAdapter.notifyDataSetChanged();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(AddServiceRequestActivity.this);
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
                            Constant.errorHandle(error, AddServiceRequestActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(AddServiceRequestActivity.this, PreferenceConnector.AuthToken, ""));
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
            VolleySingleton.getInstance(AddServiceRequestActivity.this).addToRequestQueue(stringRequest);
        }
    }

    private void setDateField(final TextView view) {
        fromDate = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                String month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
                String day = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
                String date = year + "-" + month + "-" + day;
                view.setText(Constant.dateFormateChange(date));
                dateTime = date + " " + "" + "00" + ":" + "00" + ":" + "00";
                // setTimeField(view, date);
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        fromDate.setMinDate(Calendar.getInstance());
        fromDate.show(getFragmentManager(), "");
        fromDate.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        fromDate.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
                fromDate.dismiss();
            }
        });
    }

    private void setTimeField(final TextView textView, final String date) {
        myTime = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                String hour = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String min = minute < 10 ? "0" + minute : "" + minute;
                String sec = second < 10 ? "0" + second : "" + second;
                String time = "" + hour + ":" + min;
                dateTime = date + " " + time + ":" + sec;
                if (Constant.checkReturnDateTimeGrater(Constant.getCurrentDate() + " " + Constant.getCurrentTime(), dateTime)) {
                    textView.setText(Constant.dateFormateChange(date) + " " + Constant.setTimeFormat(time));
                } else {
                    Constant.snackbar(mainLayout, " Drop off date & time should be greater than current time");
                }
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);

        myTime.show(getFragmentManager(), "");
        myTime.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //   myTime.is24HourMode();
        myTime.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                myTime.dismiss();
            }
        });
    }

    public static void getJsonData(ArrayList<RequestServiceBean> requestServiceList) {
        requestServiceBeans.clear();
        requestServiceBeans.addAll(requestServiceList);
        Gson gson = new Gson();
        requestJson = "";
        requestJson = gson.toJson(requestServiceBeans);
        Log.e("JSON", requestJson);
    }

    @Override
    protected void onResume() {
        Log.e("RESUME", "");
        if (requestServiceBeans.size() > 0) {
            serviceView.setVisibility(View.VISIBLE);
        } else {
            serviceView.setVisibility(View.GONE);
        }
        selectedServiceAdapter.notifyDataSetChanged();
        super.onResume();
    }

    public boolean isValidData() {
        Validation v = new Validation();
        if (v.isEmpty(vehicleTxt)) {
            Constant.snackbar(mainLayout, "Please select vehicle");
            return false;
        } else if (v.isEmpty(dateTimeTxt)) {
            Constant.snackbar(mainLayout, "Please add drop off date & time");
            return false;
        } else if (v.isEmpty(addressTxt)) {
            Constant.snackbar(mainLayout, "Please add Address");
            return false;
        } else if (requestServiceBeans.size() == 0) {
            Constant.snackbar(mainLayout, "Please select services");
            return false;
        } else if (v.isEmpty(timeSlotTxt)) {
            Constant.snackbar(mainLayout, "Please select time slot");
            return false;
        }
        return true;
    }

    public void getServiceDetails() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getCarRequestDetailUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("ServiceDetails", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {

                                    ServiceDetailsResponce serviceDetailsResponce = gson.fromJson(response, ServiceDetailsResponce.class);
                                    updateUiWhenEdit(serviceDetailsResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(AddServiceRequestActivity.this);
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
                            progressDialog.dismiss();
                            try {
                                Log.e("ActivitySerViceDetails", error.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(AddServiceRequestActivity.this, PreferenceConnector.AuthToken, ""));
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
            VolleySingleton.getInstance(AddServiceRequestActivity.this).addToRequestQueue(stringRequest);
        }
    }

    private void updateUiWhenEdit(ServiceDetailsResponce serviceDetailsResponce) {
        serviceView.setVisibility(View.VISIBLE);
        ArrayList<RequestServiceBean> requestServiceList = new ArrayList<>();
        for (ServiceDetailsResponce.DataBean.ServiceBean serviceBean : serviceDetailsResponce.getData().getService()) {
            RequestServiceBean requestServiceBean = new RequestServiceBean();
            requestServiceBean.id = serviceBean.getId();
            requestServiceBean.name = serviceBean.getName();
            ArrayList<RequestServiceBean.Subservice> subservices = new ArrayList<>();
            for (ServiceDetailsResponce.DataBean.ServiceBean.SubServiceBean subServiceBean : serviceBean.getSubService()) {
                RequestServiceBean.Subservice subservice = new RequestServiceBean.Subservice();
                subservice.id = subServiceBean.getId();
                subservice.name = subServiceBean.getName();
                subservices.add(subservice);
            }
            requestServiceBean.subService = subservices;
            requestServiceList.add(requestServiceBean);
        }
        getJsonData(requestServiceList);
        selectedServiceAdapter.notifyDataSetChanged();
///////
        addressReq = serviceDetailsResponce.getData().getAddress();
        addressTxt.setText(serviceDetailsResponce.getData().getAddress());
        lat = String.valueOf(serviceDetailsResponce.getData().getAddressLatLong().get(0));
        lng = String.valueOf(serviceDetailsResponce.getData().getAddressLatLong().get(1));
        descriptionTxt.setText(serviceDetailsResponce.getData().getDescription());
        dateTime = serviceDetailsResponce.getData().getDateAndTime();
        String[] dateTimeArray = dateTime.split(" ");
        dateTimeTxt.setText(Constant.dateFormateChange(dateTimeArray[0]) + " " + Constant.setTimeFormat(dateTimeArray[1]));
        carId = "" + serviceDetailsResponce.getData().getCar().get_id();
        carName = serviceDetailsResponce.getData().getCar().getBrand().getBrandName();
        vehicleTxt.setText(serviceDetailsResponce.getData().getCar().getBrand().getBrandName() + " (" + serviceDetailsResponce.getData().getCar().getPlateNumber() + ")");

    }

    public void sendServiceRequestQuote() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.addServiceRequestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("ServiceRequest", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(AddServiceRequestActivity.this, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(AddServiceRequestActivity.this);
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
                            progressDialog.dismiss();
                            Constant.errorHandle(error, AddServiceRequestActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(AddServiceRequestActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("carId", carId);
                    params.put("carName", carName);
                    params.put("dateAndTime", dateTime);
                    params.put("service", requestJson);
                    params.put("description", descriptionTxt.getText().toString().trim());
                    params.put("address", addressReq);
                    params.put("addressLat", lat);
                    params.put("addressLong", lng);
                    params.put("timeSlot", timeSlotTxt.getText().toString().trim());
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
            VolleySingleton.getInstance(AddServiceRequestActivity.this).addToRequestQueue(stringRequest);
        }
    }

    public void updateServiceRequestQuote() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.updateServiceRequestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("ServiceRequest", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(AddServiceRequestActivity.this, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(AddServiceRequestActivity.this);
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
                            progressDialog.dismiss();
                            Constant.errorHandle(error, AddServiceRequestActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(AddServiceRequestActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", requestId);
                    params.put("carId", carId);
                    params.put("carName", carName);
                    params.put("dateAndTime", dateTime);
                    params.put("service", requestJson);
                    params.put("description", descriptionTxt.getText().toString().trim());
                    params.put("address", addressReq);
                    params.put("addressLat", lat);
                    params.put("addressLong", lng);
                    params.put("timeSlot", timeSlotTxt.getText().toString().trim());
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
            VolleySingleton.getInstance(AddServiceRequestActivity.this).addToRequestQueue(stringRequest);
        }
    }

    void popUpTimeSlot(final TextView textView) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_time_slot);
        dialog.getWindow().setLayout((width * 8) / 11, (height * 1) / 2);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = dialog.findViewById(R.id.popCancel);
        final CardView mrngSlot = dialog.findViewById(R.id.mrngSlot);
        final CardView aftrnSlot = dialog.findViewById(R.id.aftrnSlot);
        final CardView evenSlot = dialog.findViewById(R.id.evenSlot);
        dialog.setCancelable(true);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mrngSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Morning");
                dialog.dismiss();
            }
        });
        aftrnSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Afternoon");
                dialog.dismiss();
            }
        });
        evenSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Evening");
                dialog.dismiss();
            }
        });

    }

}
