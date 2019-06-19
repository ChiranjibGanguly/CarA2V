package com.cara2v.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cara2v.BuildConfig;
import com.cara2v.R;
import com.cara2v.bean.CountryListBean;
import com.cara2v.chat.ChatUserBean;
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.util.Constant;
import com.cara2v.util.Htps_loader;
import com.cara2v.util.ImageRotator;
import com.cara2v.util.ImageUtil;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.AppHelper;
import com.cara2v.vollyemultipart.VolleyMultipartRequest;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.github.siyamed.shapeimageview.HexagonImageView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cara2v.util.ImagePicker.getImageResized;

public class ActivityEditProfile extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private ImageView backBtn;
    private TextView titleTxt;
    private RelativeLayout prflImgLay;
    private ImageView prflImage;
    private TextView addressTxt;
    private PlaceAutocompleteFragment placeAddressFragment;
    private TextView spinCountryCode;
    private EditText businessNameTxt, userNameTxt, userEmailTxt, userphnNumTxt;
    private View busiView;
    private int userType = 0;
    private ProgressDialog progressDialog;
    private Picasso picasso;
    private String lat = "", lng = "";
    private ArrayList<CountryListBean.Country> countryListBeanArrayList;
    private Bitmap profileImageBitmap;
    private String profileImgPath = "";
    private boolean isCameraSelected;
    private Uri mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initializeView();
        initializeData();
        placeAddressFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                addressTxt.setText(place.getAddress().toString());
                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {

            }
        });
    }


    private void initializeView() {
        picasso = Htps_loader.get(this);
        userType = PreferenceConnector.readInteger(ActivityEditProfile.this, PreferenceConnector.UserType, 0);
        mainLayout = findViewById(R.id.mainLayout);
        backBtn = findViewById(R.id.backBtn);
        titleTxt = findViewById(R.id.titleTxt);
        prflImgLay = findViewById(R.id.prflImgLay);
        prflImage = findViewById(R.id.prflImage);
        addressTxt = findViewById(R.id.addressTxt);
        businessNameTxt = findViewById(R.id.businessNameTxt);
        userNameTxt = findViewById(R.id.userNameTxt);
        userEmailTxt = findViewById(R.id.userEmailTxt);
        userphnNumTxt = findViewById(R.id.userphnNumTxt);
        busiView = findViewById(R.id.busiView);
        placeAddressFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.placeAddressFragment);
        spinCountryCode = findViewById(R.id.spinCountryCode);
        findViewById(R.id.btnUpdate).setOnClickListener(this);
        backBtn.setOnClickListener(this);
        prflImgLay.setOnClickListener(this);
        spinCountryCode.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        if (userType == Constant.userTypeOwner) {
            businessNameTxt.setVisibility(View.VISIBLE);
            busiView.setVisibility(View.VISIBLE);
        } else {
            businessNameTxt.setVisibility(View.GONE);
            busiView.setVisibility(View.GONE);
        }

        countryListBeanArrayList = new ArrayList<>();
        getCountrylist();
        getCountryZipCode();

        Collections.sort(countryListBeanArrayList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                CountryListBean.Country p1 = (CountryListBean.Country) o1;
                CountryListBean.Country p2 = (CountryListBean.Country) o2;
                return p1.name.compareToIgnoreCase(p2.name);
            }
        });
    }


    private void initializeData() {
        String responce = PreferenceConnector.readString(this, PreferenceConnector.userInfoJson, "");
        SignInRespoce signInResponce = new Gson().fromJson(responce, SignInRespoce.class);
        addressTxt.setText(signInResponce.getData().getAddress());
        lat = String.valueOf(signInResponce.getData().getAddressLatLong().get(0));
        lng = String.valueOf(signInResponce.getData().getAddressLatLong().get(1));
        profileImgPath = signInResponce.getData().getProfileImage();
        Log.e("updateUi", "profile: " + profileImgPath);
        if (!TextUtils.isEmpty(profileImgPath)) {
            try {
                Picasso.with(prflImage.getContext()).load(profileImgPath)
                        .placeholder(R.drawable.user_place_holder)
                        //.resize(prflImage.getWidth(), prflImage.getHeight())
                        .fit()
                        .into(prflImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        businessNameTxt.setText(signInResponce.getData().getBusinessName());
        userNameTxt.setText(signInResponce.getData().getUserName());
        userEmailTxt.setText(signInResponce.getData().getEmail());
        userphnNumTxt.setText(signInResponce.getData().getPhoneNumber());
        spinCountryCode.setText(signInResponce.getData().getCountryCode());
    }

    private void selectCountry() {
        List<String> list = new ArrayList<>();
        for (CountryListBean.Country country : countryListBeanArrayList) {
            list.add(country.name + " (" + country.dial_code + ")");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your country");
        CharSequence[] mEntries = list.toArray(new CharSequence[list.size()]);
        builder.setItems(mEntries, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                spinCountryCode.setText(String.format(countryListBeanArrayList.get(position).dial_code));
                //  countryCode = "+" + countries.get(position).phone_code;
                //countryCodeSingal= countries.get(position).code.toUpperCase();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public String getCountryZipCode() {
        String CountryID;
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        CountryID = manager.getSimCountryIso().toUpperCase();
        if (CountryID.equals("")) {
            spinCountryCode.setText("+ 1");
            //countryCode = "1";
            return "";
        }


        for (int i = 0; i < countryListBeanArrayList.size(); i++) {
            CountryListBean.Country country = countryListBeanArrayList.get(i);
            if (CountryID.equalsIgnoreCase(country.code)) {
                CountryZipCode = countryListBeanArrayList.get(i).dial_code;

                //countryCode = "+" + country.phone_code;
                spinCountryCode.setText(String.format(country.dial_code));
                //countryCodeSingal = country.code.toUpperCase();
                break;
            }
        }
        return CountryZipCode + " " + CountryID;
    }

    private void getCountrylist() {
        String countryJsonString = loadJSONFromAsset();
        CountryListBean countryListBean = new Gson().fromJson(countryJsonString, CountryListBean.class);
        if (countryListBean.country.size() > 0) {
            for (int i = 0; i < countryListBean.country.size(); i++) {
                countryListBeanArrayList.add(countryListBean.country.get(i));
            }
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("country.json");

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate:
                if (userType == Constant.userTypeOwner) {
                    if (isValidDataOwner()) doUpdate();
                } else {
                    if (isValidData()) doUpdate();
                }
                break;
            case R.id.backBtn:
                finish();
                break;
            case R.id.prflImgLay:
                selectImage();
                break;
            case R.id.spinCountryCode:
                selectCountry();
                break;
        }
    }

    /*-------------------------------------------------------------------------------------------------------------------------*/
    private void selectImage() {

        final CharSequence[] items = {getString(R.string.text_take_photo), getString(R.string.text_chose_gellery), getString(R.string.text_cancel)};
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.text_add_photo));
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.text_take_photo))) {

                    isCameraSelected = true;
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_CAMERA);

                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mCurrentPhotoPath = FileProvider.getUriForFile(ActivityEditProfile.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                            } else {
                                mCurrentPhotoPath = Uri.fromFile(file);
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);//USE file code in_ this ca
                            startActivityForResult(intent, Constant.REQUEST_CAMERA);

                        }
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            mCurrentPhotoPath = FileProvider.getUriForFile(ActivityEditProfile.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                        } else {
                            mCurrentPhotoPath = Uri.fromFile(file);
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);//USE file code in_ this ca
                        startActivityForResult(intent, Constant.REQUEST_CAMERA);

                    }

                } else if (items[item].equals(getString(R.string.text_chose_gellery))) {

                    isCameraSelected = false;
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, Constant.SELECT_FILE);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, Constant.SELECT_FILE);
                    }
                } else if (items[item].equals(getString(R.string.text_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    /*---------------------------------------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == Constant.REQUEST_CAMERA) {
                try {
                    profileImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCurrentPhotoPath);
                } catch (Exception e) {

                }
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                profileImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                profileImageBitmap = getImageResized(this, mCurrentPhotoPath);
                int rotation = ImageRotator.getRotation(this, mCurrentPhotoPath, true);
                profileImageBitmap = ImageRotator.rotate(profileImageBitmap, rotation);
                prflImage.setImageBitmap(profileImageBitmap);
            } else if (requestCode == Constant.SELECT_FILE) {

                Uri selectedImageUri = data.getData();

                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);

                profileImageBitmap = ImageUtil.decodeFile(selectedImagePath);
                try {
                    profileImageBitmap = ImageUtil.modifyOrientation(profileImageBitmap, selectedImagePath);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    profileImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    prflImage.setImageBitmap(profileImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /*-----------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Constant.SELECT_FILE);
                } else {
                    Toast.makeText(this, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constant.REQUEST_CAMERA);
                } else {
                    Toast.makeText(this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    public boolean isValidData() {
        Validation v = new Validation();

        /*if (profileImageBitmap == null) {
            Constant.snackbar(mainLayout, "Please add profile picture");
            userNameTxt.requestFocus();
            return false;

        } else */
        if (v.isEmpty(userNameTxt)) {
            Constant.snackbar(mainLayout, "Username can't be empty");
            userNameTxt.requestFocus();
            return false;

        } else if (userNameTxt.getText().toString().contains(" ")) {
            Constant.snackbar(mainLayout, "Username doesn't hold space");
            userNameTxt.requestFocus();
            return false;

        } else if (v.isEmpty(userEmailTxt)) {
            Constant.snackbar(mainLayout, "Email address can't be empty");
            userEmailTxt.requestFocus();
            return false;

        } else if (!isEmailValid(userEmailTxt)) {
            Constant.snackbar(mainLayout, "Enter valid email address");
            userEmailTxt.requestFocus();
            return false;
        } else if (v.isEmpty(addressTxt)) {
            Constant.snackbar(mainLayout, "Address can't be empty");
            userphnNumTxt.requestFocus();
            return false;
        } else if (v.isEmpty(userphnNumTxt)) {
            Constant.snackbar(mainLayout, "Phone number can't be empty");
            userphnNumTxt.requestFocus();
            return false;
        } else if (!isContactNoValid(userphnNumTxt)) {
            Constant.snackbar(mainLayout, "Enter valid phone number");
            userphnNumTxt.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isValidDataOwner() {
        Validation v = new Validation();

        /*if (profileImageBitmap == null) {
            Constant.snackbar(mainLayout, "Please add profile picture");
            userNameTxt.requestFocus();
            return false;

        } else*/
        if (v.isEmpty(businessNameTxt)) {
            Constant.snackbar(mainLayout, "Business Name can't be empty");
            businessNameTxt.requestFocus();
            return false;
        } else if (v.isEmpty(userNameTxt)) {
            Constant.snackbar(mainLayout, "Username can't be empty");
            userNameTxt.requestFocus();
            return false;

        } else if (userNameTxt.getText().toString().contains(" ")) {
            Constant.snackbar(mainLayout, "Username doesn't hold space");
            userNameTxt.requestFocus();
            return false;

        } else if (v.isEmpty(userEmailTxt)) {
            Constant.snackbar(mainLayout, "Email address can't be empty");
            userEmailTxt.requestFocus();
            return false;

        } else if (!isEmailValid(userEmailTxt)) {
            Constant.snackbar(mainLayout, "Enter valid email address");
            userEmailTxt.requestFocus();
            return false;
        } else if (v.isEmpty(addressTxt)) {
            Constant.snackbar(mainLayout, "Address can't be empty");
            userphnNumTxt.requestFocus();
            return false;
        } else if (v.isEmpty(userphnNumTxt)) {
            Constant.snackbar(mainLayout, "Phone number can't be empty");
            userphnNumTxt.requestFocus();
            return false;
        } else if (!isContactNoValid(userphnNumTxt)) {
            Constant.snackbar(mainLayout, "Enter valid phone number");
            userphnNumTxt.requestFocus();
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

    private boolean isContactNoValid(EditText editText) {
        String getValue = editText.getText().toString().trim();
        return getValue.length() > 6 && getValue.length() < 12;
    }

    private void doUpdate() {
        if (Constant.isNetworkAvailable(ActivityEditProfile.this, mainLayout)) {
          /*  progressBar.setVisibility(View.VISIBLE);*/
            progressDialog.show();
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.BASE_URL + Constant.updateProfileUrl, new Response.Listener<NetworkResponse>() {
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
                            SignInRespoce signInResponce = new Gson().fromJson(resultResponse, SignInRespoce.class);
                            PreferenceConnector.writeString(ActivityEditProfile.this, PreferenceConnector.userInfoJson, resultResponse);
                            Toast.makeText(ActivityEditProfile.this, message, Toast.LENGTH_SHORT).show();
                            updateFirebaseToken(signInResponce);
                            finish();
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
                    Constant.errorHandle(error, ActivityEditProfile.this);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityEditProfile.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userName", userNameTxt.getText().toString().trim());
                    params.put("email", userEmailTxt.getText().toString());
                    params.put("businessName", businessNameTxt.getText().toString().trim());
                    params.put("countryCode", spinCountryCode.getText().toString());
                    params.put("phoneNumber", userphnNumTxt.getText().toString());
                    params.put("addressLat", lat);
                    params.put("addressLong", lng);
                    params.put("address", addressTxt.getText().toString().trim());
                    if (profileImageBitmap == null) {
                        params.put("filetoupload", profileImgPath);
                    }
                    return params;
                }

                /*user/updateUser


                param = ["userName":checkForNULL(obj: strUserName),
                 "email":checkForNULL(obj: strEmail),
              "businessName":checkForNULL(obj:strBusinessName),
                 "countryCode":self.strCountryCode,
                        "phoneNumber":checkForNULL(obj: strContactForSend),
            "addressLat":checkForNULL(obj:self.strLatForSend),
            "addressLong":checkForNULL(obj:self.strLongForSend),
                "address":checkForNULL(obj: self.strLocation)
         ]*/
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    if (profileImageBitmap != null) {
                        params.put("filetoupload", new DataPart("profileImage.jpg", AppHelper.getFileDataFromDrawable(profileImageBitmap), "image/jpeg"));
                    }
                    return params;
                }
            };
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);

        }
    }

    private void updateFirebaseToken(SignInRespoce signInResponce) {
        ChatUserBean chatUserBean = new ChatUserBean();
        chatUserBean.email = signInResponce.getData().getEmail();
        chatUserBean.firebaseId = String.valueOf(signInResponce.getData().get_id());
        chatUserBean.firebaseToken = signInResponce.getData().getDeviceToken();
        chatUserBean.name = signInResponce.getData().getUserName();
        chatUserBean.profilePic = signInResponce.getData().getProfileImage();
        chatUserBean.uid = String.valueOf(signInResponce.getData().get_id());
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constant.ARG_USERS)
                .child(String.valueOf(signInResponce.getData().get_id()))
                .setValue(chatUserBean).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });
    }

}
