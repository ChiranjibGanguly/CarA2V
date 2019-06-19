package com.cara2v.ui.activity.owner;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.bean.StateBean;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
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

public class StripeBankAddActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "StripeBankAddActivity";
    private LinearLayout mainLayout;
    private EditText firstName;
    private EditText lastName;
    private EditText accNo;
    private EditText routNo;
    /*private EditText city;*/
    private EditText postalCode;
    private EditText ssnLast;
    /* private Spinner spinnCountry;
     private Spinner spinnCurrency;*/
 //   private Spinner spinnCity;
  //  private Spinner spinnHolderType;

  //  private TextView dobTxt;
  //  private EditText address;
  //  private TextView country;
   // private TextView currency;
  //  private TextView state;
  //  private DatePickerDialog fromDate;
 //   private ArrayList<String> stateList;
 //   private ArrayList<String> cityList;
 //   private ArrayAdapter<String> spinnCityAdapter;
 //   private ArrayList<String> holderTypeList;
  //  private ArrayAdapter<String> spinnHolderTypeAdapter;

    private ImageView menuBtn;
    private TextView titleTxt;

    private PlaceAutocompleteFragment placeAutocompleteAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bankacc);
        initializeView();

       /* spinnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state.setText(stateList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    private void initializeView() {
        mainLayout = findViewById(R.id.mainLayout);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        accNo = findViewById(R.id.accNo);
        routNo = findViewById(R.id.routNo);
       // address = findViewById(R.id.address);
     //   spinnHolderType = findViewById(R.id.spinnHolderType);
     //   spinnCity = findViewById(R.id.spinnCity);
        //    spinnCurrency = (Spinner) findViewById(R.id.spinnCurrency);
      //  dobTxt = findViewById(R.id.dobTxt);
       // country = findViewById(R.id.country);
      //  state = findViewById(R.id.state);
      //  currency = (TextView) findViewById(R.id.currency);
     //   dobTxt.setOnClickListener(this);
        findViewById(R.id.addBankBtn).setOnClickListener(this);
        // findViewById(R.id.backBtn).setOnClickListener(this);
        // city = (EditText) findViewById(R.id.city);
      //  state = (TextView) findViewById(R.id.state);
        postalCode = (EditText) findViewById(R.id.postalCode);
        ssnLast = (EditText) findViewById(R.id.ssnLast);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Add Account");
        menuBtn.setOnClickListener(this);

     //   holderTypeList = new ArrayList<>();
     //   holderTypeList.add("Individual");
     //   holderTypeList.add("Company");
    //    stateList = new ArrayList<>();
    //    cityList = new ArrayList<>();
        //Collections.sort(cityList);
        //getCountrylist();

        /*spinnCityAdapter = new ArrayAdapter<>(StripeBankAddActivity.this, R.layout.seat_spinner_item_offer, R.id.spinn_text, cityList);
        spinnCity.setAdapter(spinnCityAdapter);
        spinnHolderTypeAdapter = new ArrayAdapter<>(StripeBankAddActivity.this, R.layout.seat_spinner_item_offer, R.id.spinn_text, holderTypeList);
        spinnHolderType.setAdapter(spinnHolderTypeAdapter);*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBankBtn:
                if (isValidData()) addBankInfo();
                break;
            case R.id.menuBtn:
                finish();
                break;
            /*case R.id.dobTxt:
                setDateField();
                break;*/
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
        }/* else if (v.isEmpty(address)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(state)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        }*/ else if (v.isEmpty(postalCode)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        } else if (v.isEmpty(ssnLast)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        }/* else if (v.isEmpty(dobTxt)) {
            Constant.snackbar(mainLayout, msg);
            return false;
        }*/
        return true;
    }

    /*private void setDateField() {
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
        fromDate.show(getFragmentManager(), "");
        fromDate.setAccentColor(ContextCompat.getColor(StripeBankAddActivity.this, R.color.colorPrimaryDark));
        fromDate.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
                fromDate.dismiss();
            }
        });
    }*/

    private void addBankInfo() {
        if (Constant.isNetworkAvailable(StripeBankAddActivity.this, mainLayout)) {
            final ProgressDialog progressDialog = new ProgressDialog(StripeBankAddActivity.this);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.addStripeBankAccountUrl,
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
                                if (status.equals("success")) {
                                    progressDialog.dismiss();
                                    Constant.snackbar(mainLayout, message);
                                    PreferenceConnector.writeBoolean(StripeBankAddActivity.this, PreferenceConnector.IsBankAccAdd, true);
                                    finish();
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
                            Constant.errorHandle(error, StripeBankAddActivity.this);
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(StripeBankAddActivity.this, PreferenceConnector.AuthToken, ""));
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
            VolleySingleton.getInstance(StripeBankAddActivity.this).addToRequestQueue(stringRequest);
        }
    }

    /*var firstName = req.body.firstName;
    var lastName = req.body.lastName;
    var country = req.body.country;
    var routingNumber = req.body.routingNumber;
    var accountNo = req.body.accountNo;
    var ssnLast = req.body.ssnLast;
    var postal_code = req.body.postalcode;\*/

    /*private void getCountrylist() {
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

    /*public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("us.json");

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
    }*/
}

