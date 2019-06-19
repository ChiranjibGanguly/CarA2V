package com.cara2v.ui.activity;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.util.Constant;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityForgotPassword extends Activity implements View.OnClickListener {

    private LinearLayout mainLayout;
    private EditText userEmailTxt;
    private ProgressDialog progressDialog;
    private String userType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userType = bundle.getString("userType");
        }
        initializeView();
    }

    private void initializeView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        mainLayout = findViewById(R.id.mainLayout);
        userEmailTxt = findViewById(R.id.userEmailTxt);
        findViewById(R.id.btnSignIn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                if (isValidData()) sendEmailForForgettingPassWord();
                break;
        }
    }

    public boolean isValidData() {
        Validation v = new Validation();
        if (v.isEmpty(userEmailTxt)) {
            Constant.snackbar(mainLayout, "Email address can't be empty");
            userEmailTxt.requestFocus();
            return false;

        } else if (!isEmailValid(userEmailTxt)) {
            Constant.snackbar(mainLayout, "Enter valid email address");
            userEmailTxt.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isEmailValid(EditText editText) {
        String getValue = editText.getText().toString().trim();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(getValue).matches();
        /*String getValue = editText.getText().toString().trim();
        return getValue.length() >5 && getValue.length()<15;*/
    }

    public void sendEmailForForgettingPassWord() {
        if (Constant.isNetworkAvailable(ActivityForgotPassword.this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.BASE_URL + Constant.forgotPasswordUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("TAG", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equals("success")) {
                                    Constant.snackbar(mainLayout, message);
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
                            Toast.makeText(ActivityForgotPassword.this, "Something went wrong, please check after some time.", Toast.LENGTH_LONG).show();
                        }
                    }) {

           /* @Override
            public Map<String, String> getParams() throws AuthFailureError {
                SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd");
                String todayDate = writeFormat.format(date);
                Map<String,String> header = new HashMap<>();
                header.put("month", todayDate);
                return header;
            }*/

                /*@Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("authToken", session.getUserInfo().getAuthToken());
                    return header;
                }*/
                /*@Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userName", userNameTxt.getText().toString().trim());
                    params.put("password", passWordTxt.getText().toString().trim());
                    params.put("deviceToken", FirebaseInstanceId.getInstance().getToken());
                    params.put("deviceType", "2");
                    return params;
                }*/

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", userEmailTxt.getText().toString().trim());
                    params.put("userType", userType);
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(stringRequest);
        }
    }
}
