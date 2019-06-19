package com.cara2v.ui.activity;

import android.Manifest;
import android.app.Activity;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cara2v.responceBean.SignInRespoce;
import com.cara2v.ui.activity.consumer.AddVehicleInfoActivity;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

public class SignupActivity extends Activity implements View.OnClickListener {

    private RelativeLayout prflImgLay;
    private ImageView prflImage;
    private TextView addressTxt;
    private TextView licenceTxt;
    private PlaceAutocompleteFragment placeAddressFragment;
    private TextView spinCountryCode;
    private LinearLayout signInLay;
    private RelativeLayout mainLayout;
    private EditText userNameTxt;
    private EditText userEmailTxt;
    private EditText passWordTxt;
    private EditText userphnNumTxt;
    private EditText businessNameTxt;
    private View busiView;
    private View licenceView;
    private View pwdView;
    private CheckBox checkTrmsPolcs;
    private String lat = "", lng = "";
    private int userType = 0;
    private Bitmap profileImageBitmap;
    private Bitmap licenceImageBitmap;
    private ProgressDialog progressDialog;
    private int LICENCE_CAMERA = 150;
    private int LICENCE_FILE = 151;
    private String socialId = "", socialEmail = "", socialImage = "", socialType = "";
    private Bundle bundle;
    private boolean isCameraSelected;
    private Gson gson = new Gson();
    Picasso picasso;
    private ArrayList<CountryListBean.Country> countryListBeanArrayList;
    private Uri mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        bundle = getIntent().getExtras();

        initializeView();
        if (bundle != null) {
            socialId = bundle.getString("SOCIALID");
            socialEmail = bundle.getString("SOCIALEMAIL");
            socialImage = bundle.getString("SOCIALIMAGE");
            socialType = bundle.getString("SOCIALTYPE");
            if (socialId == null) socialId = "";
            if (socialEmail == null) socialEmail = "";
            if (socialImage == null) socialImage = "";
            if (socialType == null) socialType = "";
            if (!socialId.equals("")) {
                pwdView.setVisibility(View.GONE);
                passWordTxt.setVisibility(View.GONE);
                userEmailTxt.setText(socialEmail);
                userEmailTxt.setText(socialEmail);
                if (!socialEmail.equals("")) {
                    userEmailTxt.setClickable(false);
                    userEmailTxt.setFocusable(false);
                }
                if (!TextUtils.isEmpty(socialImage)) {
                    try {
                        picasso.load(socialImage)
                                .placeholder(R.drawable.user_place_holder)
                                //.resize(userProfileImage.getWidth(), userProfileImage.getHeight())
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
            }
        }
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
        passWordTxt.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        if (/*actionId == EditorInfo.IME_ACTION_SEARCH
                                ||*/ actionId == EditorInfo.IME_ACTION_DONE
                                || actionId == EditorInfo.IME_ACTION_NEXT) {
                            Constant.hideSoftKeyboard(SignupActivity.this);
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });
    }

    private void initializeView() {
        userType = PreferenceConnector.readInteger(SignupActivity.this, PreferenceConnector.UserType, 0);
        picasso = Htps_loader.get(this);
        prflImgLay = findViewById(R.id.prflImgLay);
        prflImage = findViewById(R.id.prflImage);
        addressTxt = findViewById(R.id.addressTxt);
        placeAddressFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.placeAddressFragment);
        spinCountryCode = findViewById(R.id.spinCountryCode);
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        spinCountryCode.setOnClickListener(this);
        prflImgLay.setOnClickListener(this);
        signInLay = findViewById(R.id.signInLay);
        mainLayout = findViewById(R.id.mainLayout);
        userNameTxt = findViewById(R.id.userNameTxt);
        userEmailTxt = findViewById(R.id.userEmailTxt);
        passWordTxt = findViewById(R.id.passWordTxt);
        userphnNumTxt = findViewById(R.id.userphnNumTxt);
        businessNameTxt = findViewById(R.id.businessNameTxt);
        licenceTxt = findViewById(R.id.licenceTxt);
        busiView = findViewById(R.id.busiView);
        licenceView = findViewById(R.id.licenceView);
        pwdView = findViewById(R.id.pwdView);
        checkTrmsPolcs = findViewById(R.id.checkTrmsPolcs);
        signInLay.setOnClickListener(this);
        licenceTxt.setOnClickListener(this);
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        if (userType == Constant.userTypeOwner) {
            businessNameTxt.setVisibility(View.VISIBLE);
            busiView.setVisibility(View.VISIBLE);
            licenceTxt.setVisibility(View.VISIBLE);
            licenceView.setVisibility(View.VISIBLE);
        } else {
            businessNameTxt.setVisibility(View.GONE);
            busiView.setVisibility(View.GONE);
            licenceTxt.setVisibility(View.GONE);
            licenceView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                if (userType == Constant.userTypeOwner) {
                    if (!socialId.equals("")) {
                        if (isValidSocialDataOwner()) doRegistration();
                    } else {
                        if (isValidDataOwner()) doRegistration();
                    }
                } else {
                    if (!socialId.equals("")) {
                        if (isValidSocialData()) doRegistration();
                    } else {
                        if (isValidData()) doRegistration();
                    }
                }
                break;
            case R.id.spinCountryCode:
                selectCountry();
                break;
            case R.id.signInLay:
                finish();
                break;
            case R.id.prflImgLay:
                selectImage("prfl");
                break;
            case R.id.licenceTxt:
                selectImage("licence");
                break;
        }
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
        CountryListBean countryListBean = gson.fromJson(countryJsonString, CountryListBean.class);
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

    /*-------------------------------------------------------------------------------------------------------------------------*/
    private void selectImage(final String type) {

        final CharSequence[] items = {getString(R.string.text_take_photo), getString(R.string.text_chose_gellery), getString(R.string.text_cancel)};
        AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivity.this);
        alert.setTitle(getString(R.string.text_add_photo));
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.text_take_photo))) {
                    isCameraSelected = true;
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (type.equals("prfl")) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_CAMERA);
                            } else if (type.equals("licence")) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, LICENCE_CAMERA);
                            }
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mCurrentPhotoPath = FileProvider.getUriForFile(SignupActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                            } else {
                                mCurrentPhotoPath = Uri.fromFile(file);
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);//USE file code in_ this ca
                            if (type.equals("prfl")) {
                                startActivityForResult(intent, Constant.REQUEST_CAMERA);
                            } else if (type.equals("licence")) {
                                startActivityForResult(intent, LICENCE_CAMERA);
                            }
                        }
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            mCurrentPhotoPath = FileProvider.getUriForFile(SignupActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                        } else {
                            mCurrentPhotoPath = Uri.fromFile(file);
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);//USE file code in_ this ca
                        if (type.equals("prfl")) {
                            startActivityForResult(intent, Constant.REQUEST_CAMERA);
                        } else if (type.equals("licence")) {
                            startActivityForResult(intent, LICENCE_CAMERA);
                        }
                    }


                } else if (items[item].equals(getString(R.string.text_chose_gellery))) {
                    isCameraSelected = false;
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (type.equals("prfl")) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            } else if (type.equals("licence")) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, LICENCE_FILE);
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            if (type.equals("prfl")) {
                                startActivityForResult(intent, Constant.SELECT_FILE);
                            } else if (type.equals("licence")) {
                                startActivityForResult(intent, LICENCE_FILE);
                            }

                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if (type.equals("prfl")) {
                            startActivityForResult(intent, Constant.SELECT_FILE);
                        } else if (type.equals("licence")) {
                            startActivityForResult(intent, LICENCE_FILE);
                        }
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
            } else if (requestCode == LICENCE_CAMERA) {
                try {
                    licenceImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCurrentPhotoPath);
                } catch (Exception e) {

                }
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                licenceImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                licenceImageBitmap = getImageResized(this, mCurrentPhotoPath);
                int rotation = ImageRotator.getRotation(this, mCurrentPhotoPath, true);
                licenceImageBitmap = ImageRotator.rotate(licenceImageBitmap, rotation);
                // prflImage.setImageBitmap(profileImageBitmap);
                licenceTxt.setText("Licence Added");
            } else if (requestCode == LICENCE_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                licenceImageBitmap = ImageUtil.decodeFile(selectedImagePath);
                try {
                    licenceImageBitmap = ImageUtil.modifyOrientation(licenceImageBitmap, selectedImagePath);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    licenceImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    // prflImage.setImageBitmap(profileImageBitmap);
                    licenceTxt.setText("Licence Added");
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
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Constant.SELECT_FILE);
                } else {
                    Toast.makeText(SignupActivity.this, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mCurrentPhotoPath = FileProvider.getUriForFile(SignupActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    } else {
                        mCurrentPhotoPath = Uri.fromFile(file);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);
                    startActivityForResult(intent, Constant.REQUEST_CAMERA);
                } else {
                    Toast.makeText(SignupActivity.this, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
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
        } else if (v.isEmpty(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password can't be empty");
            passWordTxt.requestFocus();
            return false;

        } else if (!isPasswordValid(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password should be 6 characters long");
            passWordTxt.requestFocus();
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
        } else if (!checkTrmsPolcs.isChecked()) {
            Constant.snackbar(mainLayout, "Please accept terms and policies");
            return false;
        }
        return true;
    }

    public boolean isValidSocialData() {
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
        }/* else if (v.isEmpty(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password can't be empty");
            passWordTxt.requestFocus();
            return false;

        } else if (!isPasswordValid(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password should be 6 characters long");
            passWordTxt.requestFocus();
            return false;
        }*/ else if (v.isEmpty(addressTxt)) {
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
        } else if (!checkTrmsPolcs.isChecked()) {
            Constant.snackbar(mainLayout, "Please accept terms and policies");
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
        } else if (v.isEmpty(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password can't be empty");
            passWordTxt.requestFocus();
            return false;

        } else if (!isPasswordValid(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password should be 6 characters long");
            passWordTxt.requestFocus();
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
        } else if (licenceImageBitmap == null) {
            Constant.snackbar(mainLayout, "Please add licence");
            return false;
        } else if (!checkTrmsPolcs.isChecked()) {
            Constant.snackbar(mainLayout, "Please accept terms and policies");
            return false;
        }
        return true;
    }

    public boolean isValidSocialDataOwner() {
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
        }/* else if (v.isEmpty(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password can't be empty");
            passWordTxt.requestFocus();
            return false;

        } else if (!isPasswordValid(passWordTxt)) {
            Constant.snackbar(mainLayout, "Password should be 6 characters long");
            passWordTxt.requestFocus();
            return false;
        }*/ else if (v.isEmpty(addressTxt)) {
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
        } else if (licenceImageBitmap == null) {
            Constant.snackbar(mainLayout, "Please add licence");
            return false;
        } else if (!checkTrmsPolcs.isChecked()) {
            Constant.snackbar(mainLayout, "Please accept terms and policies");
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

    private boolean isPasswordValid(EditText editText) {
        String getValue = editText.getText().toString().trim();
        return getValue.length() > 5;
    }


    private void doRegistration() {
        if (Constant.isNetworkAvailable(SignupActivity.this, mainLayout)) {
          /*  progressBar.setVisibility(View.VISIBLE);*/
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

                            SignInRespoce signInResponce = gson.fromJson(resultResponse, SignInRespoce.class);
                            setValues(signInResponce, resultResponse);
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
                    params.put("businessName", businessNameTxt.getText().toString().trim());
                    params.put("userName", userNameTxt.getText().toString().trim());
                    //    params.put("contactNo", PreferenceConnector.readString(SignUpActivity.this, PreferenceConnector.USERNO, ""));
                    params.put("userType", "" + userType);
                    params.put("email", userEmailTxt.getText().toString());
                    if (!socialId.equals("")) {
                        params.put("password", "");
                    } else {
                        params.put("password", passWordTxt.getText().toString());
                    }
                    params.put("phoneNumber", userphnNumTxt.getText().toString());
                    params.put("countryCode", spinCountryCode.getText().toString());
                    params.put("address", addressTxt.getText().toString().trim());
                    params.put("addressLat", lat);
                    params.put("addressLong", lng);
                    params.put("locationLat", lat);
                    params.put("locationLong", lng);
                    params.put("location", addressTxt.getText().toString().trim());
                    params.put("socialId", socialId);
                    params.put("socialType", socialType);
                    params.put("deviceToken", FirebaseInstanceId.getInstance().getToken());
                    params.put("deviceType", "2");
                    if (profileImageBitmap == null) {
                        params.put("filetoupload", socialImage);
                    }
                    if (licenceImageBitmap == null) {
                        params.put("licenseImage", "");
                    }
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    if (profileImageBitmap != null) {
                        params.put("filetoupload", new DataPart("profileImage.jpg", AppHelper.getFileDataFromDrawable(profileImageBitmap), "image/jpeg"));
                    }
                    if (licenceImageBitmap != null) {
                        params.put("licenseImage", new DataPart("liceceImage.jpg", AppHelper.getFileDataFromDrawable(licenceImageBitmap), "image/jpeg"));
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

    private void setValues(SignInRespoce signInResponce, String resultResponse) {
        PreferenceConnector.clear(SignupActivity.this);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.userInfoJson, resultResponse);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.AuthToken, signInResponce.getData().getAuthToken());
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Id, String.valueOf(signInResponce.getData().get_id()));
        PreferenceConnector.writeInteger(SignupActivity.this, PreferenceConnector.UserType, signInResponce.getData().getUserType());
        PreferenceConnector.writeBoolean(SignupActivity.this, PreferenceConnector.IsLogIN, true);
        if (signInResponce.getData().getLicenseStatus() == 1) {
            PreferenceConnector.writeBoolean(SignupActivity.this, PreferenceConnector.IsLcnAprv, true);
        }
        if (signInResponce.getMyCar() != null) {
            if (signInResponce.getMyCar().size() > 0) {
                PreferenceConnector.writeBoolean(SignupActivity.this, PreferenceConnector.IsCarAdd, true);
            }
        }
        if (signInResponce.getData().getAccountAdd() == 1) {
            PreferenceConnector.writeBoolean(SignupActivity.this, PreferenceConnector.IsBankAccAdd, true);
        }
        if (signInResponce.getData().getUserType() == Constant.userTypeOwner) {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.putExtra("FROM", "SignupActivity");
            startActivity(intent);
        } else {
            Intent intent = new Intent(SignupActivity.this, AddVehicleInfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("FROM", "SignupActivity");
            startActivity(intent);
        }

    }

}
