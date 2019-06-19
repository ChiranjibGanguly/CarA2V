package com.cara2v.ui.fragments.consumer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.cara2v.R;
import com.cara2v.ui.activity.ActivityServiceQuote;
import com.cara2v.util.Constant;
import com.cara2v.util.MyApplication;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BankPayFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BankPayFragment";
    View view;
    private RelativeLayout mainLayout;

    private EditText accNo;
    private EditText holderName;
    private EditText routNo;
    private EditText ssnLast;
    private EditText postalCode;
    //private Spinner spinnHolderType;
    // private TextView country;
    // private TextView dobTxt;
    // private TextView currency;
    private TextView showTxt;
    private Button payBtn;
    /* private DatePickerDialog fromDate;
     private ArrayList<String> stateList;
     private ArrayList<String> cityList;
     private ArrayList<String> holderTypeList;*/
    // private ArrayAdapter<String> spinnHolderTypeAdapter;
    private String requestId = "", type = "",userId="";
    private ProgressDialog progressDialog;
    private Context mContext;

    public static BankPayFragment newInstance(String requestId, String type,String userId) {
        BankPayFragment fragment = new BankPayFragment();
        Bundle args = new Bundle();
        args.putString("requestId", requestId);
        args.putString("type", type);
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bank_pay_stripe, container, false);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        view.requestFocus();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
        progressDialog = new ProgressDialog(mContext);
        if (getArguments() != null) {
            requestId = getArguments().getString("requestId");
            type = getArguments().getString("type");
            userId = getArguments().getString("userId");
            Log.e(TAG, "requestId: " + requestId);
            Log.e(TAG, "Type: " + type);
        }
    }

    private void initializeView(View view) {
        mainLayout = view.findViewById(R.id.mainLayout);

        accNo = view.findViewById(R.id.accNo);
        holderName = view.findViewById(R.id.holderName);
        routNo = view.findViewById(R.id.routNo);
        ssnLast = view.findViewById(R.id.ssnLast);
        postalCode = view.findViewById(R.id.postalCode);

        //  country = (TextView) view.findViewById(R.id.country);

        //  currency = (TextView) view.findViewById(R.id.currency);
        //  dobTxt = (TextView) view.findViewById(R.id.dobTxt);
        showTxt = view.findViewById(R.id.showTxt);
        //    spinnHolderType = (Spinner) view.findViewById(R.id.spinnHolderType);
        payBtn = view.findViewById(R.id.payBtn);

        //    stateList = new ArrayList<>();
        //     cityList = new ArrayList<>();
        //     holderTypeList=new ArrayList<>();
        //     holderTypeList.add("Individual");
        //      holderTypeList.add("Company");
        //  Collections.sort(cityList);
        //  getCountrylist();
//        spinnHolderTypeAdapter = new ArrayAdapter<>(mContext, R.layout.seat_spinner_item_offer, R.id.spinn_text, holderTypeList);
//        spinnHolderType.setAdapter(spinnHolderTypeAdapter);
        // addBankBtn = (Button) view.findViewById(R.id.addBankBtn);
        payBtn.setText("Pay Now");
        showTxt.setText("Please enter your bank\n account details");
        payBtn.setOnClickListener(this);
        //  dobTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.payBtn:
                if (isValidData()) {
                    if (type.equals("first")) {
                        payWithStripeStPayment();
                    }else if (type.equals("second")){
                        payWithStripeNdPayment();
                    }
                }
                break;
            /*case R.id.dobTxt:
                setDateField();
                break;*/
        }
    }

   /* private void setDateField() {
        Calendar now = Calendar.getInstance();
        fromDate = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                *//*day = "" + dayOfMonth;
                month = "" + monthOfYear;
                yearr = "" + year;
*//*
                dobTxt.setText(date);
                // view.setText("" + date);
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        fromDate.setMaxDate(Calendar.getInstance());
        fromDate.show(getActivity().getFragmentManager(), "");
        fromDate.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        fromDate.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
                fromDate.dismiss();
            }
        });
    }*/

   /* private void getCountrylist() {
        Gson gson = new Gson();
        String stateJsonString = loadJSONFromAsset();
        StateBean stateBean = gson.fromJson(stateJsonString, StateBean.class);
        Collections.sort(stateBean.getState());
        if (stateBean.getState().size() > 0) {
            for (int i = 0; i < stateBean.getState().size(); i++) {
                String[] splitState = stateBean.getState().get(i).split(",");
                cityList.add(splitState[0]);
                stateList.add(splitState[1]);
            }
        }
    }*/

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("us.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private boolean isValidData() {
        String msg = "Please fill all required fields.";
        Validation v = new Validation();
        if (v.isEmpty(holderName)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(accNo)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(routNo)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(ssnLast)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(postalCode)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        }
        return true;
    }

    private void payWithStripeNdPayment() {
        if (Constant.isNetworkAvailable(mContext, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.bankSecondPayUrl,
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
                                    Toast.makeText(mContext, "Payment completed successfully", Toast.LENGTH_SHORT).show();
                                    /*MyApplication.finishActivity("com.cara2v.ui.activity.ui.activity.ActivityAskForPayment");
                                    MyApplication.finishActivity("com.cara2v.ui.activity.consumer.StripPaymentActivity");*/
                                    MyApplication.finishAllActivity();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null) {
                                        Constant.showLogOutDialog(getActivity());
                                    }
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
                    header.put("authToken", PreferenceConnector.readString(mContext, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accountHolderNam", holderName.getText().toString().trim());
                    params.put("routingNumber", routNo.getText().toString().trim());
                    params.put("accountNumber", accNo.getText().toString().trim());
                    params.put("ssnLast", ssnLast.getText().toString().trim());
                    params.put("postalcode", postalCode.getText().toString().trim());
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
            VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }
    }

    private void payWithStripeStPayment() {
        if (Constant.isNetworkAvailable(mContext, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.bankFirstPayUrl,
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
                                    Toast.makeText(mContext, "Payment completed successfully now you can accept", Toast.LENGTH_SHORT).show();
                                    /*MyApplication.finishActivity("com.cara2v.ui.activity.ui.activity.ActivityAskForPayment");
                                    MyApplication.finishActivity("com.cara2v.ui.activity.consumer.StripPaymentActivity");*/
                                   // MyApplication.finishAllActivity();
                                    ActivityServiceQuote.isPay=true;
                                    if (getActivity()!=null)getActivity().finish();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null) {
                                        Constant.showLogOutDialog(getActivity());
                                    }
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
                    header.put("authToken", PreferenceConnector.readString(mContext, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accountHolderNam", holderName.getText().toString().trim());
                    params.put("routingNumber", routNo.getText().toString().trim());
                    params.put("accountNumber", accNo.getText().toString().trim());
                    params.put("ssnLast", ssnLast.getText().toString().trim());
                    params.put("postalcode", postalCode.getText().toString().trim());
                    params.put("requestId", requestId);
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
            VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }
    }

}
