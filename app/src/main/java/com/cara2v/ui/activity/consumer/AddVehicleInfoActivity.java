package com.cara2v.ui.activity.consumer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.BuildConfig;
import com.cara2v.Interface.AddImageListioner;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.R;
import com.cara2v.adapter.AddImageAdapter;
import com.cara2v.multipleFileUpload.MultiPartRequest;
import com.cara2v.multipleFileUpload.StringParser;
import com.cara2v.multipleFileUpload.Template;
import com.cara2v.multipleFileUpload.VolleyMySingleton;
import com.cara2v.responceBean.GetCarBrandResponce;
import com.cara2v.responceBean.GetCarModelResponce;
import com.cara2v.ui.activity.CropActivity;
import com.cara2v.ui.activity.MainActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;

import com.cara2v.util.cropper.CropImage;
import com.cara2v.util.cropper.CropImageView;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.cara2v.util.Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.cara2v.util.Constant.REQUEST_CAMERA;

public class AddVehicleInfoActivity extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private Spinner spinnBrand;
    private Spinner spinnModel;
    private Spinner spinnVehicleType;
    private Spinner spinnFuelType;
    private EditText vehNameTxt;
    private EditText vinCodeTxt;
    private EditText plateNoTxt;
    private EditText mileageTxt;
    private TextView caryearTxt;
    private RecyclerView addVechileImage;

    //private RadioGroup vinRadioGr;
   // private RadioButton vinRadioBtn;

    private RadioGroup plateRadioGr;
    private RadioButton plateRadioBtn;

    //-----
    int REQUEST_CODE_PICKER = 1;
    private ArrayAdapter<GetCarBrandResponce> spinnBrandAdapter;
    private ArrayAdapter<GetCarModelResponce> spinnModelAdapter;
    private ArrayAdapter<String> spinnVehicleTypeAdapter;
    private ArrayAdapter<String> spinnFuelTypeAdapter;

    private List<GetCarBrandResponce.DataBean> brandList;
    private List<GetCarModelResponce.DataBean> modelList;
    private ArrayList vehicleTypeList;
    private ArrayList fuelTypeList;

    private ArrayList<Image> addProductImageList = new ArrayList();
    private ArrayList<Uri> addProductList = new ArrayList();
    private ArrayList<File> productImages = new ArrayList();

    private ProgressDialog progressDialog;
    private AddImageAdapter addImageAdapter;
    private Gson gson = new Gson();
    private String[] vehicleType = {"Hatchback", "Sedan", "MPV", "SUV", "Crossover", "Coupe", "Convertible"};
    private String[] fuelType = {"Diesel", "Gasoline"};

    private String brand = "", brandName = "", model = "", modelName = "", VINCode = "", plateNumber = "", carType = "", mFuelType = "", mileage = "";

    private Uri mCurrentPhotoPath;

    private RequestQueue mRequest;
    private MultiPartRequest mMultiPartRequest;
    Map<String, String> params;
    private boolean isFirstCome = false;
    private int width = 0, height = 0;
    private String from = "";
    private Boolean isCameraSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_info);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getString("FROM");
        }
        initializeView();
        initializeSpinner();
        spinnBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getBrandNdModel(String.valueOf(brandList.get(i).get_id()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initializeSpinner() {
        getBrandNdModel("");
        brandList = new ArrayList<>();
        modelList = new ArrayList<>();
        spinnVehicleTypeAdapter = new ArrayAdapter<String>(AddVehicleInfoActivity.this, R.layout.spinner_item, R.id.spinnText, vehicleTypeList);
        spinnFuelTypeAdapter = new ArrayAdapter<String>(AddVehicleInfoActivity.this, R.layout.spinner_item, R.id.spinnText, fuelTypeList);
        spinnVehicleType.setAdapter(spinnVehicleTypeAdapter);
        spinnFuelType.setAdapter(spinnFuelTypeAdapter);
    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        mainLayout = findViewById(R.id.mainLayout);
        spinnBrand = findViewById(R.id.spinnBrand);
        spinnModel = findViewById(R.id.spinnModel);
        spinnVehicleType = findViewById(R.id.spinnVehicleType);
        spinnFuelType = findViewById(R.id.spinnFuelType);
        addVechileImage = findViewById(R.id.addVechileImage);
        // vehNameTxt = findViewById(R.id.vehNameTxt);

        vinCodeTxt = findViewById(R.id.vinCodeTxt);
        plateNoTxt = findViewById(R.id.plateNoTxt);
        mileageTxt = findViewById(R.id.mileageTxt);
        caryearTxt = findViewById(R.id.caryearTxt);

     //   vinCodeTxt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        findViewById(R.id.btnAddDetails).setOnClickListener(this);
        findViewById(R.id.btnSkip).setOnClickListener(this);
        caryearTxt.setOnClickListener(this);

     //   vinRadioGr = (RadioGroup) findViewById(R.id.vinRadioGr);
        plateRadioGr = (RadioGroup) findViewById(R.id.plateRadioGr);

        vehicleTypeList = new ArrayList(Arrays.asList(vehicleType));
        fuelTypeList = new ArrayList(Arrays.asList(fuelType));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        addProductList.add(null);

        addImageAdapter = new AddImageAdapter(this, addProductList, new AddImageListioner() {
            @Override
            public void onClick() {
                getPermissionAndPicImage();
            }
        }, new RemoveImageListioner() {
            @Override
            public void onClick(int position) {
                Log.e("TAG", "onClick: " + position);
                addProductList.remove(position);
                if (addProductList.size() == 3 && addProductList.get(0) != null) {
                    addProductList.add(0, null);
                }
                if (position == 0) {
                    productImages.remove(position);
                } else {
                    productImages.remove(position - 1);
                }
                addImageAdapter.notifyDataSetChanged();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setReverseLayout(true);
        addVechileImage.setLayoutManager(layoutManager);
        addVechileImage.setAdapter(addImageAdapter);

        spinnBrand.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Constant.hideSoftKeyboard(AddVehicleInfoActivity.this);
                return false;
            }
        });
        spinnModel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Constant.hideSoftKeyboard(AddVehicleInfoActivity.this);
                return false;
            }
        });
        spinnVehicleType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Constant.hideSoftKeyboard(AddVehicleInfoActivity.this);
                return false;
            }
        });
        spinnFuelType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Constant.hideSoftKeyboard(AddVehicleInfoActivity.this);
                return false;
            }
        });
        mileageTxt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                String str = mileageTxt.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 5, 2);
                if (!str2.equals(str)) {
                    mileageTxt.setText(str2);
                    int pos = mileageTxt.getText().length();
                    mileageTxt.setSelection(pos);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddDetails:
                String vinStatus = "";
                String pltStatus = "";
                //int vinId = vinRadioGr.getCheckedRadioButtonId();
              //  vinRadioBtn = (RadioButton) findViewById(vinId);
              //  String vinStatusTxt = vinRadioBtn.getText().toString().trim();
               /* if (vinStatusTxt.equalsIgnoreCase("Private")) {
                    vinStatus = "1";
                } else if (vinStatusTxt.equalsIgnoreCase("Public")) {*/
                    vinStatus = "0";
               // }
                int pltId = plateRadioGr.getCheckedRadioButtonId();
                plateRadioBtn = (RadioButton) findViewById(pltId);
                String plateStatusTxt = plateRadioBtn.getText().toString().trim();
                if (plateStatusTxt.equalsIgnoreCase("Private")) {
                    pltStatus = "1";
                } else if (plateStatusTxt.equalsIgnoreCase("Public")) {
                    pltStatus = "0";
                }
                if (plateNoTxt.getText().toString().trim().equals("")){
                    pltStatus = "1";
                }
                if (isValidData(vinStatus, pltStatus)) uploadFile();
                break;
            case R.id.btnSkip:
                if (from.equals("SignupActivity")) {
                    startActivity(new Intent(AddVehicleInfoActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                } else {
                    finish();
                }
                break;
            case R.id.caryearTxt:
                showYearDialog();
                break;
        }
    }


    public void getBrandNdModel(final String carId) {

        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getCarUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (isFirstCome) {
                                progressDialog.dismiss();
                            } else {
                                isFirstCome = true;
                            }

                            System.out.println("#" + response);
                            Log.e("BRAND", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    if (carId.equals("")) {
                                        GetCarBrandResponce getCarBrandResponce = gson.fromJson(response, GetCarBrandResponce.class);
                                        brandList = getCarBrandResponce.getData();
                                        Log.e("TAG", "onResponse: " + brandList.size());
                                        spinnBrandAdapter = new ArrayAdapter<GetCarBrandResponce>(AddVehicleInfoActivity.this, R.layout.spinner_item, R.id.spinnText, (ArrayList) brandList);
                                        spinnBrand.setAdapter(spinnBrandAdapter);
                                    } else {
                                        GetCarModelResponce getCarModelResponce = gson.fromJson(response, GetCarModelResponce.class);
                                        modelList = getCarModelResponce.getData();
                                        spinnModelAdapter = new ArrayAdapter<GetCarModelResponce>(AddVehicleInfoActivity.this, R.layout.spinner_item, R.id.spinnText, (ArrayList) modelList);
                                        spinnModel.setAdapter(spinnModelAdapter);

                                    }
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(AddVehicleInfoActivity.this);
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
                            Constant.errorHandle(error, AddVehicleInfoActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(AddVehicleInfoActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }

                /*@Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("carId", carId);
                    return params;
                }*/
                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("carId", carId);
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
            VolleySingleton.getInstance(AddVehicleInfoActivity.this).addToRequestQueue(stringRequest);
        }
    }

    public void getPermissionAndPicImage() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constant.MY_PERMISSIONS_REQUEST_CEMERA);
            } else {
                selectImage();
            }
        } else {
            selectImage();
        }
    }

    private void selectImage() {

        final CharSequence[] items = {getString(R.string.text_take_photo), getString(R.string.text_chose_gellery), getString(R.string.text_cancel)};
        AlertDialog.Builder alert = new AlertDialog.Builder(AddVehicleInfoActivity.this);
        alert.setTitle(getString(R.string.text_add_photo));
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.text_take_photo))) {
                    isCameraSelected = true;
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (AddVehicleInfoActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            callIntent(Constant.INTENTREQUESTCAMERA);
                        } else if (AddVehicleInfoActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if (productImages.size() <= 4) {
                                callIntent(Constant.INTENTREQUESTREAD);
                                dialog.dismiss();
                            } else {
                                showmaximageDialog();
                            }

                        } else {
                            if (productImages.size() <= 4) {
                                callIntent(Constant.INTENTCAMERA);
                                dialog.dismiss();
                            } else {
                                showmaximageDialog();
                            }

                        }
                    } else {
                        if (productImages.size() <= 4) {
                            callIntent(Constant.INTENTCAMERA);
                            dialog.dismiss();
                        } else {
                            showmaximageDialog();
                        }
                    }

                } else if (items[item].equals(getString(R.string.text_chose_gellery))) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        isCameraSelected = false;
                        if (AddVehicleInfoActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {
                            if (productImages.size() <= 4) {
                                callIntent(Constant.INTENTREQUESTREAD);
                                dialog.dismiss();
                            } else {
                                showmaximageDialog();
                            }

                        } else {
                            if (productImages.size() <= 4) {
                                callIntent(Constant.INTENTGALLERY);
                                dialog.dismiss();
                            } else {
                                showmaximageDialog();
                            }
                        }
                    } else {
                        if (productImages.size() <= 4) {
                            callIntent(Constant.INTENTGALLERY);
                            dialog.dismiss();
                        } else {
                            showmaximageDialog();
                        }
                    }
                } else if (items[item].equals(getString(R.string.text_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!isCameraSelected) callIntent(Constant.INTENTGALLERY);
                } else {
                    Toast.makeText(AddVehicleInfoActivity.this, "Permission denied can't select image.", Toast.LENGTH_LONG).show();
                }
                break;
            case Constant.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isCameraSelected) callIntent(Constant.INTENTCAMERA);
                } else {
                    Toast.makeText(AddVehicleInfoActivity.this, "Camera  permission denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case Constant.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!isCameraSelected) callIntent(Constant.INTENTGALLERY);
                    else callIntent(Constant.INTENTCAMERA);
                } else {
                    Toast.makeText(AddVehicleInfoActivity.this, "Permission not granted for Read.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void callIntent(int caseid) {
        switch (caseid) {
            case Constant.INTENTCAMERA:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "image.jpg");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mCurrentPhotoPath = FileProvider.getUriForFile(AddVehicleInfoActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                } else {
                    mCurrentPhotoPath = Uri.fromFile(file);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);//USE file code in_ this case
                startActivityForResult(intent, Constant.REQUEST_CAMERA);
                break;
            case Constant.INTENTGALLERY:
                //  ImagePicker.pickImage(AddVehicleInfoActivity.this);
                Intent intentgallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentgallery, Constant.SELECT_FILE);
                break;
            case Constant.INTENTREQUESTCAMERA:
                ActivityCompat.requestPermissions(AddVehicleInfoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constant.MY_PERMISSIONS_REQUEST_CAMERA);
                break;
            case Constant.MY_PERMISSIONS_REQUEST_CAMERA:
                ActivityCompat.requestPermissions(AddVehicleInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constant.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                break;
            case Constant.INTENTREQUESTWRITE:
                ActivityCompat.requestPermissions(AddVehicleInfoActivity.this, new String[]{Manifest.permission.INTERNET},
                        Constant.MY_PERMISSIONS_REQUEST_INTERNET);
                break;
            case Constant.INTENTREQUESTNET:
                ActivityCompat.requestPermissions(AddVehicleInfoActivity.this, new String[]{Manifest.permission.INTERNET},
                        Constant.MY_PERMISSIONS_REQUEST_INTERNET);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1) {
            if (requestCode == Constant.SELECT_FILE) {
                mCurrentPhotoPath = com.cara2v.util.picker.ImagePicker.getImageURIFromResult(AddVehicleInfoActivity.this, requestCode, resultCode, data);
                if (mCurrentPhotoPath != null) {
                    CropImage.activity(mCurrentPhotoPath).setCropShape(CropImageView.CropShape.RECTANGLE).setMinCropResultSize(300, 200).setMaxCropResultSize(4000, 4000).setAspectRatio(300, 200).start(AddVehicleInfoActivity.this);
                } else {
                    Toast.makeText(AddVehicleInfoActivity.this, "Something went to wrong. please try again.", Toast.LENGTH_SHORT).show();
                }
            }
            if (requestCode == Constant.REQUEST_CAMERA) {
                // val imageUri :Uri= com.tulia.Picker.ImagePicker.getImageURIFromResult(this, requestCode, resultCode, data);
                if (mCurrentPhotoPath != null) {
                    CropImage.activity(mCurrentPhotoPath).setCropShape(CropImageView.CropShape.RECTANGLE).setMinCropResultSize(300, 200).setMaxCropResultSize(4000, 4000).setAspectRatio(300, 200).start(AddVehicleInfoActivity.this);
                } else {
                    Toast.makeText(AddVehicleInfoActivity.this, "Something went to wrong. please try again.", Toast.LENGTH_SHORT).show();
                }
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result != null) {
                    mCurrentPhotoPath = result.getUri();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCurrentPhotoPath);
                        File file = savebitmap(this, bitmap, UUID.randomUUID() + ".jpg");
                        productImages.add(file);
                        addProductList.add(1, mCurrentPhotoPath);
                        if (addProductList.size() == 5) {
                            addProductList.remove(0);
                        }
                        addImageAdapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }
            }
            isCameraSelected = false;
        }
    }

    public void showmaximageDialog() {
        Toast.makeText(this, "Maximum 4 Images can be selected", Toast.LENGTH_SHORT).show();
    }




    public boolean isValidData(String vinStatus, String pltStatus) {
        Validation v = new Validation();
        if (v.isEmpty(caryearTxt)) {
            Constant.snackbar(mainLayout, "Please set vehicle year");
            return false;
        } else if (v.isEmpty(mileageTxt)) {
            Constant.snackbar(mainLayout, "Mileage can't be empty");
            mileageTxt.requestFocus();
            return false;
        } else if (Float.parseFloat(mileageTxt.getText().toString())<0) {
            Constant.snackbar(mainLayout, "Mileage should not be less zero");
            mileageTxt.requestFocus();
            return false;
        }else if (v.isEmpty(vinCodeTxt)) {
            Constant.snackbar(mainLayout, "VIN code can't be empty");
            vinCodeTxt.requestFocus();
            return false;
        }else if (vinCodeTxt.getText().toString().length()<17) {
            Constant.snackbar(mainLayout, "VIN code should be 17 digits in length");
            vinCodeTxt.requestFocus();
            return false;
        }/* else if (v.isEmpty(plateNoTxt)) {
            Constant.snackbar(mainLayout, "Plate number can't be empty");
            plateNoTxt.requestFocus();
            return false;

        }*//* else if (productImages.size() == 0) {
            Constant.snackbar(mainLayout, "Please upload image");
            mileageTxt.requestFocus();
            return false;
        }*/

        VINCode = vinCodeTxt.getText().toString().toUpperCase().trim();
        brand = "" + brandList.get(spinnBrand.getSelectedItemPosition()).get_id();
        brandName = brandList.get(spinnBrand.getSelectedItemPosition()).getCarName();
        model = "" + modelList.get(spinnModel.getSelectedItemPosition()).get_id();
        modelName = "" + modelList.get(spinnModel.getSelectedItemPosition()).getCarModelName();

        plateNumber = plateNoTxt.getText().toString().trim();
        carType = vehicleType[spinnVehicleType.getSelectedItemPosition()];
        mFuelType = fuelType[spinnFuelType.getSelectedItemPosition()];
        mileage = mileageTxt.getText().toString();
        params = new HashMap<>();
        params.put("brandId", brand);
        params.put("brandName", brandName);
        params.put("modelId", model);
        params.put("modelName", modelName);
        params.put("VINCode", VINCode);
        params.put("plateNumber", plateNumber);
        params.put("carType", carType);
        params.put("fuelType", mFuelType);
        params.put("mileage", mileage);
        params.put("VINCodeStatus", vinStatus);
        params.put("plateNumberStatus", pltStatus);
        params.put("carYear", caryearTxt.getText().toString().trim());
        if(productImages.size()==0){
            params.put("carPhoto", "");
        }
        return true;
    }

    void uploadFile() {
        VolleyMySingleton volleySingleton = new VolleyMySingleton(AddVehicleInfoActivity.this);
        mRequest = volleySingleton.getInstance().getRequestQueue();
        mRequest.start();
        progressDialog.show();
        mMultiPartRequest = new MultiPartRequest(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                setResponse(null, error);
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                try {
                    Log.e("ADDVECH", "setResponse: " + response.toString());
                    JSONObject result = null;

                    result = new JSONObject(response.toString());
                    String status = result.getString("status");
                    String message = result.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        PreferenceConnector.writeBoolean(AddVehicleInfoActivity.this, PreferenceConnector.IsCarAdd, true);
                        Toast.makeText(AddVehicleInfoActivity.this, R.string.vehicleAddSuccess, Toast.LENGTH_SHORT).show();
                        // AddCarResponce addCarResponce = gson.fromJson(response.toString(), AddCarResponce.class);
                        if (from.equals("SignupActivity")) {
                            startActivity(new Intent(AddVehicleInfoActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            finish();
                        }
                    } else if (status.equalsIgnoreCase("authFail")) {
                        Constant.showLogOutDialog(AddVehicleInfoActivity.this);
                    } else {
                        Constant.snackbar(mainLayout, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, productImages, productImages.size(), params, AddVehicleInfoActivity.this);
        //Set tag, diperlukan ketika akan menggagalkan request/cancenl request
        mMultiPartRequest.setTag("MultiRequest");
        //Set retry policy, untuk mengatur socket time out, retries. Bisa disetting lewat template
        mMultiPartRequest.setRetryPolicy(new DefaultRetryPolicy(Template.VolleyRetryPolicy.SOCKET_TIMEOUT,
                Template.VolleyRetryPolicy.RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Menambahkan ke request queue untuk diproses
        mRequest.add(mMultiPartRequest);
    }

    //Respon dari volley, untuk menampilkan keterengan upload, seperti error, message dari server
    void setResponse(Object response, VolleyError error) {

        if (response == null) {

        } else {
            if (StringParser.getCode(response.toString()).equals(Template.Query.VALUE_CODE_SUCCESS)) {
                Constant.snackbar(mainLayout, StringParser.getMessage(response.toString()));
            } else {
                Constant.snackbar(mainLayout, "Error\n" + StringParser.getMessage(response.toString()));
            }
        }
    }

    public void showYearDialog() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        final Dialog yearDialog = new Dialog(AddVehicleInfoActivity.this);
        yearDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        yearDialog.setContentView(R.layout.yeardialog);
        yearDialog.getWindow().setLayout((width * 9) / 10, LinearLayout.LayoutParams.WRAP_CONTENT);
        yearDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button set = (Button) yearDialog.findViewById(R.id.button1);
        Button cancel = (Button) yearDialog.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) yearDialog.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year);
        nopicker.setMinValue(1960);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caryearTxt.setText(String.valueOf(nopicker.getValue()));
                yearDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearDialog.dismiss();
            }
        });
        yearDialog.show();
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
              //  if (up > MAX_BEFORE_POINT) return rFinal;
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

    public File savebitmap(Context mContext, Bitmap bitmap, String name) {
        File filesDir = mContext.getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(mContext.getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return null;
    }
}
