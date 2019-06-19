package com.cara2v.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.PreferenceConnectorRem;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleyMultipartRequest;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.cara2v.util.Constant.MY_PERMISSIONS_REQUEST_LOCATION;

public class ActivityLogin extends Activity implements View.OnClickListener, LocationListener {

    private TextView forgotPwd;
    private ImageView btnFbLogIn;
    private ImageView btnGLogIn;
    private LinearLayout signUpLay, mainLayout;
    private EditText userNameTxt;
    private EditText passWordTxt;
    private CheckBox checkSignIn;
    private ProgressDialog progressDialog;
    private int RC_SIGN_IN = 11;
    private Bitmap profileImageBitmap;
    private ImageView logo;

    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    private Gson gson = new Gson();
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    CallbackManager callbackManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private String socialEmail = "", socialId = "", socialType = "", socialImage = "", refreshedToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeView();
    }

    private void initializeView() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Login", "Refreshed token: " + refreshedToken);
        forgotPwd = (TextView) findViewById(R.id.forgotPwd);
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        btnFbLogIn = (ImageView) findViewById(R.id.btnFbLogIn);
        logo = (ImageView) findViewById(R.id.logo);
        btnGLogIn = (ImageView) findViewById(R.id.btnGLogIn);
        signUpLay = (LinearLayout) findViewById(R.id.signUpLay);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        userNameTxt = (EditText) findViewById(R.id.userNameTxt);
        passWordTxt = (EditText) findViewById(R.id.passWordTxt);
        checkSignIn = (CheckBox) findViewById(R.id.checkSignIn);
        signUpLay.setOnClickListener(this);
        btnFbLogIn.setOnClickListener(this);
        btnGLogIn.setOnClickListener(this);
        forgotPwd.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ActivityLogin.this);
        userNameTxt.setText(PreferenceConnectorRem.readString(ActivityLogin.this, PreferenceConnectorRem.UserNameRem, ""));
        passWordTxt.setText(PreferenceConnectorRem.readString(ActivityLogin.this, PreferenceConnectorRem.pwdRem, ""));
        checkSignIn.setChecked(PreferenceConnectorRem.readBoolean(ActivityLogin.this, PreferenceConnectorRem.IsChecked, false));
        initializeFacebook();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                if (isValidData()) doSignInParse();
                /*if(isValidData()){
                    if (checkSignIn.isChecked()) {
                        PreferenceConnectorRem.writeString(ActivityLogin.this, PreferenceConnectorRem.UserNameRem, userNameTxt.getText().toString().trim());
                        PreferenceConnectorRem.writeString(ActivityLogin.this, PreferenceConnectorRem.pwdRem, passWordTxt.getText().toString().trim());
                        PreferenceConnectorRem.writeBoolean(ActivityLogin.this, PreferenceConnectorRem.IsChecked, true);
                    }else {
                        PreferenceConnectorRem.writeString(ActivityLogin.this, PreferenceConnectorRem.UserNameRem, "");
                        PreferenceConnectorRem.writeString(ActivityLogin.this, PreferenceConnectorRem.pwdRem, "");
                        PreferenceConnectorRem.writeBoolean(ActivityLogin.this, PreferenceConnectorRem.IsChecked, false);
                    }
                    startActivity(new Intent(ActivityLogin.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }*/
                break;
            case R.id.btnFbLogIn:
                if (Constant.isNetworkAvailable(ActivityLogin.this, mainLayout)) {
                    //    LoginManager.getInstance().logOut();
                    LoginManager.getInstance().logInWithReadPermissions(ActivityLogin.this, Arrays.asList("public_profile", "email"));
                }
                break;
            case R.id.signUpLay:
                startActivity(new Intent(ActivityLogin.this, SignupActivity.class));
                break;
            case R.id.forgotPwd:
                Intent intent=new Intent(ActivityLogin.this, ActivityForgotPassword.class);
                intent.putExtra("userType","" + PreferenceConnector.readInteger(ActivityLogin.this, PreferenceConnector.UserType, 0));
                startActivity(intent);
                break;
            case R.id.btnGLogIn:
                if (Constant.isNetworkAvailable(ActivityLogin.this, mainLayout)) {
                    googleSignIn();
                }
                break;
        }
    }

    public boolean isValidData() {
        Validation v = new Validation();
        if (v.isEmpty(userNameTxt)) {
            Constant.snackbar(mainLayout, "Username can't be empty");
            passWordTxt.requestFocus();
            return false;

        } else if (userNameTxt.getText().toString().trim().contains(" ")) {
            Constant.snackbar(mainLayout, "Username can't hold space");
            userNameTxt.requestFocus();
            return false;
        } else if (v.isEmpty(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password can't be empty");
            passWordTxt.requestFocus();
            return false;

        } else if (!isPasswordValid(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password should be 6 characters long");
            passWordTxt.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isEmailValid(EditText editText) {
        String getValue = editText.getText().toString().trim();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(getValue).matches();
    }

    private boolean isPasswordValid(EditText editText) {
        String getValue = editText.getText().toString().trim();
        return getValue.length() > 5;
    }

    public void doSignInParse() {
        if (Constant.isNetworkAvailable(ActivityLogin.this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.BASE_URL + Constant.logInUrl,
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
                                    SignInRespoce signInResponce = gson.fromJson(response, SignInRespoce.class);
                                    PreferenceConnector.writeString(ActivityLogin.this, PreferenceConnector.userInfoJson, response);
                                    if (checkSignIn.isChecked()) {
                                        PreferenceConnectorRem.writeString(ActivityLogin.this, PreferenceConnectorRem.UserNameRem, userNameTxt.getText().toString().trim());
                                        PreferenceConnectorRem.writeString(ActivityLogin.this, PreferenceConnectorRem.pwdRem, passWordTxt.getText().toString().trim());
                                        PreferenceConnectorRem.writeBoolean(ActivityLogin.this, PreferenceConnectorRem.IsChecked, true);
                                    } else {
                                        PreferenceConnectorRem.writeString(ActivityLogin.this, PreferenceConnectorRem.UserNameRem, "");
                                        PreferenceConnectorRem.writeString(ActivityLogin.this, PreferenceConnectorRem.pwdRem, "");
                                        PreferenceConnectorRem.writeBoolean(ActivityLogin.this, PreferenceConnectorRem.IsChecked, false);
                                    }
                                    setValues(signInResponce);
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
                            Toast.makeText(ActivityLogin.this, "Something went wrong, please check after some time.", Toast.LENGTH_LONG).show();
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
                    params.put("userName", userNameTxt.getText().toString().trim());
                    params.put("password", passWordTxt.getText().toString().trim());
                    params.put("deviceToken", FirebaseInstanceId.getInstance().getToken());
                    params.put("deviceType", "2");
                    params.put("userType", "" + PreferenceConnector.readInteger(ActivityLogin.this, PreferenceConnector.UserType, 0));
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

    private void googleSignIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constant.GOOGLE);
    }

    public void initializeFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        callbackManager = CallbackManager.Factory.create();
        profileImageBitmap = null;
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            //socialId = object.getString("id");

                            /*if (object.has("email")) {
                                sEmail1 = object.getString("email");
                            }*/
                            if (response.getJSONObject().has("email")) {
                                socialEmail = response.getJSONObject().getString("email");
                            } /*else {
                                socialEmail = sSocialId + "@facebook.com";
                            }*/
                            if (response.getJSONObject().has("id")) {

                                socialId = response.getJSONObject().getString("id");
                            }
                            socialType = Constant.FACEBOOK;
                            Log.e("TAG", "onCompleted: " + socialId + " " + socialEmail);
                            socialImage = "https://graph.facebook.com/" + socialId + "/picture?height=300&type=normal&width=300";
                            Log.e("TAG", "ID " + socialId + ", socialEmail: " + socialEmail
                                    + ", Image: " + socialImage);
                            // Signed in successfully, show authenticated UI.
                            checkLocationability();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name,gender, email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                Constant.snackbar(mainLayout, "Facebook login cancelled");
                //  AppUtility.showToast(LoginActivity.this, "Facebook login cancelled", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("TAG", exception + "");
                Constant.snackbar(mainLayout, "Error in Facebook login");
                //   AppUtility.showToast(LoginActivity.this, "Error in Facebook login", Toast.LENGTH_SHORT);
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        profileImageBitmap = null;
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            socialEmail = account.getEmail();
            socialId = account.getId();
            socialType = Constant.GOOGLE_LOGIN;
            try {
                socialImage = account.getPhotoUrl().toString();
            } catch (Exception e) {

            }
            /*if (!TextUtils.isEmpty(socialImage)) {
                Picasso.with(this)
                        .load(socialImage)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                profileImageBitmap = bitmap;
                                logo.setImageBitmap(profileImageBitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }*/

            Log.e("TAG", "ID " + socialId + ", socialEmail: " + socialEmail
                    + ", Image: " + socialImage);
            // Signed in successfully, show authenticated UI.
            checkLocationability();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void doSocialRegistration(final double latitude, final double longitude,
                                      final String addressFromLocation) {
        if (Constant.isNetworkAvailable(ActivityLogin.this, mainLayout)) {
            progressDialog.show();
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.BASE_URL + Constant.registrationUrl, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    progressDialog.dismiss();
                    Log.e("TAG", "onResponse: " + resultResponse);
                    try {
                        JSONObject result = new JSONObject(resultResponse);
                        String status = result.getString("status");
                        String message = result.getString("message");
                        if (status.equals("success")) {
                            PreferenceConnector.writeString(ActivityLogin.this, PreferenceConnector.userInfoJson, resultResponse);
                            SignInRespoce signInResponce = gson.fromJson(resultResponse, SignInRespoce.class);
                            if (signInResponce.getData() == null) {
                                Intent intent = new Intent(ActivityLogin.this, SignupActivity.class);
                                intent.putExtra("SOCIALID", socialId);
                                intent.putExtra("SOCIALEMAIL", socialEmail);
                                intent.putExtra("SOCIALIMAGE", socialImage);
                                intent.putExtra("SOCIALTYPE", socialType);
                                startActivity(intent);
                            } else {
                                setValues(signInResponce);
                            }

                        } else {
                            Constant.snackbar(mainLayout, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    progressDialog.dismiss();
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", "" + status);
                            Log.e("Error Message", message);

                            if (networkResponse.statusCode == 404) {
                                errorMessage = "Resource not found";
                            } else if (networkResponse.statusCode == 401) {
                                errorMessage = message + " Please login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = message + " Check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = message + " Something is getting wrong";
                            }
                            Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).setAction("ok", null).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                }
            }) {
                /*@Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(SignUpActivity.this, PreferenceConnector.AUTHTOKEN, ""));
                    return header;
                }*/

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userName", "");
                    //    params.put("contactNo", PreferenceConnector.readString(SignUpActivity.this, PreferenceConnector.USERNO, ""));
                    params.put("email", socialEmail);
                    params.put("password", "");
                    params.put("phoneNumber", "");
                    params.put("countryCode", "");
                    params.put("address", addressFromLocation);
                    params.put("addressLat", String.valueOf(latitude));
                    params.put("addressLong", String.valueOf(longitude));
                    params.put("locationLat", String.valueOf(latitude));
                    params.put("locationLong", String.valueOf(longitude));
                    params.put("location", addressFromLocation);
                    params.put("socialId", socialId);
                    params.put("socialType", socialType);
                    params.put("deviceToken", FirebaseInstanceId.getInstance().getToken());
                    params.put("deviceType", "2");
                    params.put("userType", "" + PreferenceConnector.readInteger(ActivityLogin.this, PreferenceConnector.UserType, 0));
                    params.put("checkLogin", "1");
                    //  if (profileImageBitmap == null) {
                    params.put("filetoupload", socialImage);
                    // }
                    return params;
                }

               /* @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    if (profileImageBitmap != null) {
                        params.put("filetoupload", new DataPart("profileImage.jpg", AppHelper.getFileDataFromDrawable(profileImageBitmap), "image/jpeg"));
                    }
                    return params;
                }*/
            };
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);

        }
    }

    private void checkLocationability() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                getFusedLocation();
            }
        } else {
            if (checkLocationPermissionLowerApi()) {
                try {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                    // getting GPS status
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    // getting network status
                    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    Location location = null;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        //     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            if (location != null) {

                                doSocialRegistration(location.getLatitude(), location.getLongitude(),
                                        Constant.getAddressFromLocation(ActivityLogin.this, location.getLatitude(), location.getLongitude()));
                                Log.e("TAG", "onSuccess: " + location.getLatitude() + " " + location.getLongitude());
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    doSocialRegistration(location.getLatitude(), location.getLongitude(),
                                            Constant.getAddressFromLocation(ActivityLogin.this, location.getLatitude(), location.getLongitude()));
                                    Log.e("TAG", "onSuccess: " + location.getLatitude() + " " + location.getLongitude());
                                } else {
                                    checkLocationability();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showSettingsAlert();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(ActivityLogin.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityLogin.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //(just doing it here for now, note that with this code, no explanation is shown)
                ActivityCompat.requestPermissions(ActivityLogin.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {

            return true;
        }
    }

    private boolean checkLocationPermissionLowerApi() {
        boolean isEnable = true;
        try {
            locationManager = (LocationManager) getApplication().getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                isEnable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEnable;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityLogin.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void getFusedLocation() {
        if (ActivityCompat.checkSelfPermission(ActivityLogin.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ActivityLogin.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(ActivityLogin.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            doSocialRegistration(location.getLatitude(), location.getLongitude(),
                                    Constant.getAddressFromLocation(ActivityLogin.this, location.getLatitude(), location.getLongitude()));
                            Log.e("TAG", "onSuccess: " + location.getLatitude() + " " + location.getLongitude());
                        } else {
                            Toast.makeText(ActivityLogin.this, "Please turn on location", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(ActivityLogin.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LocationFail", "onFailure: " + e.getMessage());
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(ActivityLogin.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("TAG", "onRequestPermissionsResult: if");
                        getFusedLocation();
                    }
                } else {
                    Toast.makeText(ActivityLogin.this, "permission denied", Toast.LENGTH_LONG).show();
                    //Util.displayToast(getActivity(), "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Util.SHORT_TOAST);
                    Log.e("TAG", "onRequestPermissionsResult: else");
                }
            }
        }
    }

    private void setValues(SignInRespoce signInResponce) {
        Log.e("AuthToken", signInResponce.getData().getAuthToken());
        PreferenceConnector.writeString(ActivityLogin.this, PreferenceConnector.AuthToken, signInResponce.getData().getAuthToken());
        PreferenceConnector.writeString(ActivityLogin.this, PreferenceConnector.Id, String.valueOf(signInResponce.getData().get_id()));
        PreferenceConnector.writeInteger(ActivityLogin.this, PreferenceConnector.UserType, signInResponce.getData().getUserType());
        PreferenceConnector.writeBoolean(ActivityLogin.this, PreferenceConnector.IsLogIN, true);
        if (signInResponce.getData().getLicenseStatus() == 1) {
            PreferenceConnector.writeBoolean(ActivityLogin.this, PreferenceConnector.IsLcnAprv, true);
        }
        if (signInResponce.getMyCar() != null) {
            if (signInResponce.getMyCar().size() > 0) {
                PreferenceConnector.writeBoolean(ActivityLogin.this, PreferenceConnector.IsCarAdd, true);
            }
        }
        if (signInResponce.getData().getAccountAdd() == 1) {
            PreferenceConnector.writeBoolean(ActivityLogin.this, PreferenceConnector.IsBankAccAdd, true);
        }
        startActivity(new Intent(ActivityLogin.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
