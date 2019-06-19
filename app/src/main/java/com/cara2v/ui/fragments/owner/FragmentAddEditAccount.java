package com.cara2v.ui.fragments.owner;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.responceBean.GetAccDetailsResponce;
import com.cara2v.ui.activity.MainActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentAddEditAccount extends Fragment implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private TextView showTxt;
    private Context mContext;
    private EditText firstName;
    private EditText lastName;
    private EditText accNo;
    private EditText routNo;
    private EditText postalCode;
    private EditText ssnLast;
    private Button addBankBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_edit_account_layout, null);
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
    }

    private void initializeView(View view) {
        mainLayout = view.findViewById(R.id.mainLayout);
        showTxt = view.findViewById(R.id.showTxt);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        accNo = view.findViewById(R.id.accNo);
        routNo = view.findViewById(R.id.routNo);
        postalCode = view.findViewById(R.id.postalCode);
        ssnLast = view.findViewById(R.id.ssnLast);
        addBankBtn = view.findViewById(R.id.addBankBtn);
        addBankBtn.setOnClickListener(this);
        if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.IsBankAccAdd, true)) {
            addBankBtn.setText("Update Account Info");
            showTxt.setVisibility(View.GONE);
            getBankInfo();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBankBtn:
                if (isValidData()) addBankInfo();
                break;
        }
    }

    public boolean isValidData() {
        String msg = "Please fill all required fields.";
        Validation v = new Validation();
        if (v.isEmpty(firstName)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(lastName)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(accNo)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(routNo)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(postalCode)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(ssnLast)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        }
        return true;
    }

    private void addBankInfo() {
        if (Constant.isNetworkAvailable(mContext, mainLayout)) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.addStripeBankAccountUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("ADD Account", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equals("success")) {
                                    progressDialog.dismiss();
                                    Constant.snackbar(mainLayout, message);
                                    PreferenceConnector.writeBoolean(mContext, PreferenceConnector.IsBankAccAdd, true);
                                    MainActivity activity = (MainActivity) getActivity();
                                    assert activity != null;
                                    activity.manageSndTabAfterPayment();
                                } else {
                                    progressDialog.dismiss();
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
                            Constant.snackbar(mainLayout, "Invalid Entries");
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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("firstName", firstName.getText().toString().trim());
                    params.put("lastName", lastName.getText().toString().trim());
                    params.put("country", /*country.getText().toString()*/"US");
                    //  params.put("currency", currency.getText().toString());
                    params.put("routingNumber", routNo.getText().toString().trim());
                    params.put("accountNo", accNo.getText().toString().trim());
                    // params.put("dob", dobTxt.getText().toString().trim());
                    // params.put("address", address.getText().toString());
                    //  params.put("city", spinnCity.getSelectedItem().toString());
                    //  params.put("state", state.getText().toString().trim());
                    params.put("postalCode", postalCode.getText().toString().trim());
                    params.put("ssnLast", ssnLast.getText().toString().trim());
                    //  params.put("accountHolderType", spinnHolderType.getSelectedItem().toString().toLowerCase());
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }
    }

    private void getBankInfo() {
        if (Constant.isNetworkAvailable(mContext, mainLayout)) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getStripeBankAccountUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("Get Account", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                //  String message = result.getString("message");
                                if (status.equals("success")) {
                                    progressDialog.dismiss();
                                    GetAccDetailsResponce allPostResponce = new Gson().fromJson(response, GetAccDetailsResponce.class);
                                    updateUi(allPostResponce);
                                } else {
                                    progressDialog.dismiss();
                                    // Constant.snackbar(mainLayout, message);
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
                            Constant.snackbar(mainLayout, "Invalid Entries");
                            if (getActivity() != null) Constant.errorHandle(error, getActivity());
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(mContext, PreferenceConnector.AuthToken, ""));
                    return header;
                }

               /* @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("firstName", firstName.getText().toString().trim());
                    params.put("lastName", lastName.getText().toString().trim());
                    params.put("country", *//*country.getText().toString()*//*"US");
                    //  params.put("currency", currency.getText().toString());
                    params.put("routingNumber", routNo.getText().toString().trim());
                    params.put("accountNo", accNo.getText().toString().trim());
                    // params.put("dob", dobTxt.getText().toString().trim());
                    // params.put("address", address.getText().toString());
                    //  params.put("city", spinnCity.getSelectedItem().toString());
                    //  params.put("state", state.getText().toString().trim());
                    params.put("postalCode", postalCode.getText().toString().trim());
                    params.put("ssnLast", ssnLast.getText().toString().trim());
                    //  params.put("accountHolderType", spinnHolderType.getSelectedItem().toString().toLowerCase());
                    return params;
                }*/
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }
    }

    private void updateUi(GetAccDetailsResponce allPostResponce) {
        firstName.setText(allPostResponce.getBankAccount().getAccountDetailObj().getFirstName());
        lastName.setText(allPostResponce.getBankAccount().getAccountDetailObj().getLastName());
        routNo.setText(allPostResponce.getBankAccount().getAccountDetailObj().getRoutingNumber());
        accNo.setText(allPostResponce.getBankAccount().getAccountDetailObj().getAccountNo());
        postalCode.setText(allPostResponce.getBankAccount().getAccountDetailObj().getPostal_code());
        ssnLast.setText(allPostResponce.getBankAccount().getAccountDetailObj().getSsnLast());
    }
}
