package com.cara2v.ui.fragments.consumer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.ui.activity.ActivityAskForPayment;
import com.cara2v.ui.activity.ActivityServiceQuote;
import com.cara2v.ui.activity.consumer.StripPaymentActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.MyApplication;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CardPayFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CardPayFragment";
    private EditText /*cardHolderName,*/ oneTxt, twoTxt, threeTxt, fourTxt;
    private TextView expireDate, showTxt;
    private EditText cvv;
    private Button addCardBtn;
    private RelativeLayout mainLayout;
    private View view;
    private Context mContext;
    // private Bundle bundle;
    private int width = 0, height = 0;
    private String requestId = "", type = "", number = "", expireMnth = "", expireYear = "",userId="";
    private ProgressDialog progressDialog;

    public static CardPayFragment newInstance(String requestId, String type,String userId) {
        CardPayFragment fragment = new CardPayFragment();
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
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card_pay, container, false);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        view.requestFocus();
        //  bundle = savedInstanceState;
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
        }
        oneTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    twoTxt.requestFocus();
                }
            }
        });
        twoTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    threeTxt.requestFocus();
                }
            }
        });
        threeTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    fourTxt.requestFocus();
                }
            }
        });
    }

    private void initializeView(View view) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //cardHolderName = (EditText) view.findViewById(R.id.cardHolderName);
        mainLayout = view.findViewById(R.id.mainLayout);
        expireDate = view.findViewById(R.id.expireDate);
        showTxt = view.findViewById(R.id.showTxt);
        oneTxt = view.findViewById(R.id.oneTxt);
        twoTxt = view.findViewById(R.id.twoTxt);
        threeTxt = view.findViewById(R.id.threeTxt);
        cvv = view.findViewById(R.id.cvv);
        fourTxt = view.findViewById(R.id.fourTxt);
        addCardBtn = view.findViewById(R.id.addCardBtn);
        showTxt.setText("Please enter your card\ndetails");
        addCardBtn.setText("Pay Now");
        addCardBtn.setOnClickListener(this);
        expireDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCardBtn:
                number = oneTxt.getText().toString().trim() + twoTxt.getText().toString().trim() + threeTxt.getText().toString().trim() + fourTxt.getText().toString().trim();
                if (isValidData()) {
                    if (type.equals("first")){
                        payWithStripeStPayment();
                    }else if (type.equals("second")){
                        payWithStripeNdPayment();
                    }

                }
                break;
            case R.id.expireDate:
                expireMnth = "";
                expireYear = "";
                expireDate.setText("");
                showMonnthYearDialog();
                break;
        }
    }

    public void showMonnthYearDialog() {
        final Dialog d = new Dialog(mContext);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.year_month_dialog);
        d.getWindow().setLayout((width * 9) / 10, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker yearPicker = (NumberPicker) d.findViewById(R.id.yearPicker);
        final NumberPicker monthPicker = (NumberPicker) d.findViewById(R.id.monthPicker);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(month + 1);
        yearPicker.setMaxValue(year + 20);
        yearPicker.setMinValue(year);
        yearPicker.setWrapSelectorWheel(false);
        yearPicker.setValue(year);
        yearPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expireDate.setText(String.valueOf(monthPicker.getValue()) + "/" + String.valueOf(yearPicker.getValue()));
                expireMnth = String.valueOf(monthPicker.getValue());
                expireYear = String.valueOf(yearPicker.getValue()).substring(2, 4);
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public boolean isValidData() {
        String msg = "Please fill all required fields.";
        Validation v = new Validation();
        if (v.isEmpty(oneTxt)) {
            Constant.snackbar(mainLayout, "Incorrect card number.");
            return false;
        } else if (oneTxt.getText().toString().trim().length() < 4) {
            oneTxt.requestFocus();
            Constant.snackbar(mainLayout, "Incorrect card number.");
            return false;
        } else if (v.isEmpty(twoTxt)) {
            Constant.snackbar(mainLayout, "Incorrect card number");
            return false;
        } else if (twoTxt.getText().toString().trim().length() < 4) {
            twoTxt.requestFocus();
            Constant.snackbar(mainLayout, "Incorrect card number.");
            return false;
        } else if (v.isEmpty(threeTxt)) {
            Constant.snackbar(mainLayout, "Incorrect card number.");
            return false;
        } else if (threeTxt.getText().toString().trim().length() < 4) {
            threeTxt.requestFocus();
            Constant.snackbar(mainLayout, "Incorrect card number.");
            return false;
        } else if (v.isEmpty(fourTxt)) {
            Constant.snackbar(mainLayout, "Incorrect card number.");
            return false;
        } else if (fourTxt.getText().toString().trim().length() < 4) {
            fourTxt.requestFocus();
            Constant.snackbar(mainLayout, "Incorrect card number.");
            return false;
        } else if (v.isEmpty(expireDate)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(cvv)) {
            Constant.snackbar(mainLayout, "Incorrect CVV number.");
            return false;
        } else if (cvv.getText().toString().trim().length() < 3) {
            Constant.snackbar(mainLayout, "Incorrect CVV number.");
            return false;
        }
        return true;
    }

    private void payWithStripeNdPayment() {
        if (Constant.isNetworkAvailable(mContext, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.cardSecondPayUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e(TAG, "onResponse: " + response);
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
                            if (getActivity() != null) {
                                progressDialog.dismiss();
                                Constant.snackbar(mainLayout, error.getMessage());
                                Constant.errorHandle(error, getActivity());
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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", requestId);
                    params.put("cardNumber", number);
                    params.put("exp_month", expireMnth);
                    params.put("exp_year", expireYear);
                    params.put("cvc", cvv.getText().toString());
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
    private void payWithStripeStPayment() {
        if (Constant.isNetworkAvailable(mContext, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.cardFirstPayUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e(TAG, "onResponse: " + response);
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
                            if (getActivity() != null) {
                                progressDialog.dismiss();
                                Constant.snackbar(mainLayout, error.getMessage());
                                Constant.errorHandle(error, getActivity());
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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", requestId);
                    params.put("cardNumber", number);
                    params.put("exp_month", expireMnth);
                    params.put("exp_year", expireYear);
                    params.put("cvc", cvv.getText().toString());
                    params.put("userId", userId);
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

}
