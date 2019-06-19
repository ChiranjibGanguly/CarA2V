package com.cara2v.ui.activity.owner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.ui.activity.ActivityComment;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActivityAddEditpromoCode extends Activity implements View.OnClickListener {


    private RelativeLayout dateLay;
    private LinearLayout mainLayout;
    private ImageView calIcon;
    private TextView expireTxt;
    private ImageView menuBtn;
    private TextView titleTxt;
    private EditText promoTitleTxt;
    private EditText promoNameTxt;
    private EditText discountTxt;
    private Button btnAddUpdate;
    private String promoId = "", promoName = "", promoCode = "", promoDiscount = "", expireDate = "";
    private DatePickerDialog fromDate;
    private ProgressDialog progressDialog;
    private boolean isAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_editpromo_code);
        initializeView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            promoId = bundle.getString("PROMOID");
            promoName = bundle.getString("PROMONAME");
            promoCode = bundle.getString("PROMOCODE");
            promoDiscount = bundle.getString("PROMODISCOUNT");
            expireDate = bundle.getString("EXPIREDATE");
            if (TextUtils.isEmpty(promoId)) {
                titleTxt.setText("Generate Promo Code");
                btnAddUpdate.setText("Generate Code");
                isAdd = true;
            } else {
                titleTxt.setText("Update Promo Code");
                btnAddUpdate.setText("Update Code");
                promoTitleTxt.setText(promoCode);
                promoNameTxt.setText(promoName);
                expireTxt.setText(expireDate);
                discountTxt.setText(promoDiscount);
                promoTitleTxt.setClickable(false);
                promoTitleTxt.setFocusable(false);
                isAdd = false;
            }
        }
    }

    private void initializeView() {
        dateLay = findViewById(R.id.dateLay);
        mainLayout = findViewById(R.id.mainLayout);
        calIcon = findViewById(R.id.calIcon);
        expireTxt = findViewById(R.id.expireTxt);
        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        promoTitleTxt = findViewById(R.id.promoTitleTxt);
        promoNameTxt = findViewById(R.id.promoNameTxt);
        discountTxt = findViewById(R.id.discountTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        menuBtn.setOnClickListener(this);
        btnAddUpdate = findViewById(R.id.btnAddUpdate);
        btnAddUpdate.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        dateLay.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        //promoTitleTxt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        discountTxt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                String str = discountTxt.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 2, 2);
                if (!str2.equals(str)) {
                    discountTxt.setText(str2);
                    int pos = discountTxt.getText().length();
                    discountTxt.setSelection(pos);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddUpdate:
                Constant.hideSoftKeyboard(this);
                if (isValidData()) addUpdatePromoCode();
                break;
            case R.id.dateLay:
                setDateField();
                break;
            case R.id.menuBtn:
                finish();
                break;
        }
    }

    private void setDateField() {
        Calendar now = Calendar.getInstance();
        fromDate = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                String month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
                String day = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
                String date = year + "-" + month + "-" + day;
                expireTxt.setText(date);

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

    public boolean isValidData() {
        Validation v = new Validation();
        if (v.isEmpty(promoNameTxt)) {
            Constant.snackbar(mainLayout, "Please enter promo code name..");
            promoNameTxt.requestFocus();
            return false;
        } else if (v.isEmpty(promoTitleTxt)) {
            Constant.snackbar(mainLayout, "Please enter code..");
            promoTitleTxt.requestFocus();
            return false;
        } else if (promoTitleTxt.getText().toString().trim().length() != 6) {
            Constant.snackbar(mainLayout, "Promo code should be 6 digit in length..");
            promoTitleTxt.requestFocus();
            return false;
        } else if (v.isEmpty(expireTxt)) {
            Constant.snackbar(mainLayout, "Please select expiry date..");
            return false;
        } else if (v.isEmpty(discountTxt)) {
            Constant.snackbar(mainLayout, "Please enter discount..");
            return false;
        } else if (Float.parseFloat(discountTxt.getText().toString()) == 0) {
            Constant.snackbar(mainLayout, "Discount should not be zero");
            return false;
        }
        promoCode = promoTitleTxt.getText().toString();
        promoName = promoNameTxt.getText().toString();
        expireDate = expireTxt.getText().toString();
        promoDiscount = discountTxt.getText().toString();
        return true;
    }

    public void addUpdatePromoCode() {
        if (Constant.isNetworkAvailable(ActivityAddEditpromoCode.this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.addUpdatePromoCodeUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("AllPost", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    finish();
                                    Toast.makeText(ActivityAddEditpromoCode.this, message, Toast.LENGTH_SHORT).show();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityAddEditpromoCode.this);
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
                            Constant.errorHandle(error, ActivityAddEditpromoCode.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityAddEditpromoCode.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    if (!isAdd) {
                        params.put("promoId", promoId);
                        params.put("actionType", "1");
                    }
                    params.put("title", promoCode);
                    params.put("amount", promoDiscount);
                    params.put("expire", expireDate);
                    params.put("name", promoName);
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
            VolleySingleton.getInstance(ActivityAddEditpromoCode.this).addToRequestQueue(stringRequest);
        }
    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL) {
        if (str.charAt(0) == '.') str = "0" + str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && after == false) {
                up++;
                if (up > MAX_BEFORE_POINT) return rFinal;
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }
        return rFinal;
    }
}
