package com.cara2v.ui.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.adapter.AllPostAdapter;
import com.cara2v.multipleFileUpload.MultiPartRequestFeedPost;
import com.cara2v.multipleFileUpload.StringParser;
import com.cara2v.multipleFileUpload.Template;
import com.cara2v.multipleFileUpload.VolleyMySingleton;
import com.cara2v.responceBean.AllPostResponce;
import com.cara2v.ui.activity.ActivityComment;
import com.cara2v.ui.activity.CropActivity;
import com.cara2v.ui.activity.consumer.AddVehicleInfoActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.ImageRotator;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.cara2v.util.Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.cara2v.util.ImagePicker.decodeBitmap;

public class FragmentFeed extends Fragment implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private ImageView addPicBtn;
    private ImageView lockBtn;
    private ImageView postImg;
    private RecyclerView allPostRecycler;
    private TextView noDataTxt;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private EditText postEditTxt;
    private Button postBtn;
    private CoordinatorLayout snackLayout;

    private Context mContext;
    private int userType = 0;

    private boolean isCamera = false;
    private String s = "1";
    private Uri imageUri;
    private ArrayList<File> imageFiles;

    private RequestQueue mRequest;
    private MultiPartRequestFeedPost partRequestFeedPost;
    private ProgressDialog progressDialog;
    Map<String, String> params;
    private boolean isPrivate = false;
    private String postShowType = "1";
    private Gson gson = new Gson();
    private List<AllPostResponce.DataBean> allPostResponces;
    private AllPostAdapter allPostAdapter;
    private String postResponceString = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        userType = PreferenceConnector.readInteger(mContext, PreferenceConnector.UserType, 0);
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
        addPicBtn = view.findViewById(R.id.addPicBtn);
        lockBtn = view.findViewById(R.id.lockBtn);
        allPostRecycler = view.findViewById(R.id.allPostRecycler);
        noDataTxt = view.findViewById(R.id.noDataTxt);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        progressBar = view.findViewById(R.id.progressBar);
        postEditTxt = view.findViewById(R.id.postEditTxt);
        postBtn = view.findViewById(R.id.postBtn);
        snackLayout = view.findViewById(R.id.snackLayout);
        postImg = view.findViewById(R.id.postImg);

        addPicBtn.setOnClickListener(this);
        lockBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        imageFiles = new ArrayList<>();
        progressDialog = new ProgressDialog(mContext);
        if (isPrivate) {
            lockBtn.setImageResource(R.drawable.locked);
            postShowType = "2";
        } else {
            lockBtn.setImageResource(R.drawable.unlocked);
            postShowType = "1";
        }
        allPostResponces = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPicBtn:
                selectImage();
                break;
            case R.id.lockBtn:
                if (isPrivate) {
                    lockBtn.setImageResource(R.drawable.unlocked);
                    postShowType = "1";
                    isPrivate = false;
                } else {
                    lockBtn.setImageResource(R.drawable.locked);
                    postShowType = "2";
                    isPrivate = true;
                }
                break;
            case R.id.postBtn:
                if (isValidData()) uploadPostFile();
                break;
        }
    }

    private void selectImage() {

        final CharSequence[] items = {getString(R.string.text_take_photo), getString(R.string.text_chose_gellery), getString(R.string.text_cancel)};
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(getString(R.string.text_add_photo));
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.text_take_photo))) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(
                                    new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constant.MY_PERMISSIONS_REQUEST_CAMERA);
                            dialog.dismiss();
                        } else {
                            picImage();
                        }

                    } else {
                        picImage();
                        dialog.dismiss();
                    }


                } else if (items[item].equals(getString(R.string.text_chose_gellery))) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, Constant.SELECT_FILE);
                            isCamera = false;
                        }
                    } else {
                        dialog.dismiss();
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, Constant.SELECT_FILE);
                        isCamera = false;
                    }
                } else if (items[item].equals(getString(R.string.text_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    private void picImage() {

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName()
                + ".fileprovider", getTemporalFile(mContext));
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        takePhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        takePhotoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTemporalFile(context)));
        startActivityForResult(takePhotoIntent, Constant.REQUEST_CAMERA);
        isCamera = true;


    }

    private static File getTemporalFile(Context context) {
        return new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
    }


    public static final int PICK_IMAGE_REQUEST_CODE = 234; // the number doesn't matter
    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    private static final int DEFAULT_MIN_HEIGHT_QUALITY = 400;
    private static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;
    private static int minHeightQuality = DEFAULT_MIN_HEIGHT_QUALITY;
    private static final String TEMP_IMAGE_NAME = "tempImage.jpg";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.SELECT_FILE && resultCode == RESULT_OK && null != data) {
            Intent i = new Intent(mContext, CropActivity.class);
            //i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(mCurrentPhotoPath));
            imageUri = data.getData();
            i.putExtra("Image", imageUri.toString());
            i.putExtra("FROM", "gallery");
            startActivityForResult(i, 111);
            /*imageUri = data.getData();
            postImg.setVisibility(View.VISIBLE);
            postImg.setImageURI(imageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                File file = savebitmap(mContext, bitmap, "tmp");
                imageFiles.clear();
                imageFiles.add(file);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } else if (requestCode == Constant.REQUEST_CAMERA && resultCode == RESULT_OK) {

            Bitmap bm = null;
            File imageFile = getTemporalFile(mContext);
            Uri photoURI = Uri.fromFile(imageFile);

            bm = getImageResized(mContext, photoURI);
            int rotation = ImageRotator.getRotation(mContext, photoURI, isCamera);
            bm = ImageRotator.rotate(bm, rotation);


            File file = new File(mContext.getExternalCacheDir(), UUID.randomUUID() + ".jpg");
            imageUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileprovider", file);


            if (file != null) {
                try {
                    OutputStream outStream = null;
                    outStream = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.PNG, 80, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (imageUri != null) {
                Intent i = new Intent(mContext, CropActivity.class);
                i.putExtra("Image", imageUri.toString());
                i.putExtra("FROM", "gallery");
                startActivityForResult(i, 111);
                   /* postImg.setVisibility(View.VISIBLE);
                    imageFiles.clear();
                    imageFiles.add(file);*/
            }
            // postImg.setImageURI(imageUri);
        } else if (requestCode == 111 && resultCode == 111) {

            String result = data.getStringExtra("ImageURI");

            //getRealPathFromUri(AddVehicleInfoActivity.this,Uri.parse(result));
            try {
                File file = new File(result);
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getPath());/*BitmapFactory.decodeFile(filepath);*/
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File imagePath = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    if (imagePath.createNewFile()) {
                        fo = new FileOutputStream(imagePath);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        imageFiles.clear();
                        imageFiles.add(imagePath);
                        postImg.setVisibility(View.VISIBLE);
                        postImg.setImageURI(Uri.parse(result));
                    } else {
                        Toast.makeText(mContext, "Image not saved Try Again", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", e.getLocalizedMessage());
                }
            } catch (Exception e) {

            }
        }
    }
    // onActivityResult

    /*-----------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, Constant.SELECT_FILE);
                    isCamera = false;
                } else {
                    Toast.makeText(mContext, "YOU DENIED PERMISSION CANNOT SELECT IMAGE", Toast.LENGTH_LONG).show();
                }
            }
            break;

            case Constant.MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    picImage();
                } else {
                    Toast.makeText(mContext, "YOUR  PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    public static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            i++;
        } while (bm != null
                && (bm.getWidth() < minWidthQuality || bm.getHeight() < minHeightQuality)
                && i < sampleSizes.length);
        Log.i("FragmentFeed", "Final bitmap width = " + (bm != null ? bm.getWidth() : "No final bitmap"));
        return bm;
    }


    public boolean isValidData() {
        String postType = "";
        Validation v = new Validation();
        if (v.isEmpty(postEditTxt) && imageFiles.size() == 0) {
            Constant.snackbar(snackLayout, "Please add post");
            return false;
        }
        if (v.isEmpty(postEditTxt) && imageFiles.size() > 0) {
            postType = "2";
        }
        if (!v.isEmpty(postEditTxt) && imageFiles.size() > 0) {
            postType = "3";
        }
        if (!v.isEmpty(postEditTxt) && imageFiles.size() == 0) {
            postType = "1";
        }

        params = new HashMap<>();
        params.put("myPostStatus", postEditTxt.getText().toString());
        params.put("postType", postType);
        params.put("postShowType", postShowType);
        if (imageFiles.size() == 0) {
            params.put("postImage", "");
        }
        /*var postStatus = fields.myPostStatus;
        var postType = fields.postType; // 1:status 2:image 3:image with status
        var postShowType = fields.postShowType; // 1:public 2:private*/
        return true;
    }

    void uploadPostFile() {
        VolleyMySingleton volleySingleton = new VolleyMySingleton(mContext);
        mRequest = volleySingleton.getInstance().getRequestQueue();
        mRequest.start();
        progressDialog.show();

        partRequestFeedPost = new MultiPartRequestFeedPost(new Response.ErrorListener() {
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
                    Log.e("Post", "setResponse: " + response.toString());
                    JSONObject result = null;

                    result = new JSONObject(response.toString());
                    String status = result.getString("status");
                    String message = result.getString("message");
                    if (status.equalsIgnoreCase("success")) {
                        imageFiles.clear();
                        imageUri = null;
                        postEditTxt.setText("");
                        postImg.setImageURI(null);
                        postImg.setVisibility(View.GONE);
                        getMySocialPost();
                    } else if (status.equalsIgnoreCase("authFail")) {
                        if (getActivity() != null) Constant.showLogOutDialog(getActivity());
                    } else {
                        Constant.snackbar(mainLayout, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, imageFiles, imageFiles.size(), params, mContext);
        //Set tag, diperlukan ketika akan menggagalkan request/cancenl request
        partRequestFeedPost.setTag("MultiRequest");
        //Set retry policy, untuk mengatur socket time out, retries. Bisa disetting lewat template
        partRequestFeedPost.setRetryPolicy(new DefaultRetryPolicy(Template.VolleyRetryPolicy.SOCKET_TIMEOUT,
                Template.VolleyRetryPolicy.RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Menambahkan ke request queue untuk diproses
        mRequest.add(partRequestFeedPost);
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

    public void getMySocialPost() {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getAllSocialPostUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("AllPost", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    postResponceString = response;
                                    AllPostResponce allPostResponce = gson.fromJson(response, AllPostResponce.class);
                                    updateUi(allPostResponce);
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null)
                                        Constant.showLogOutDialog(getActivity());
                                } else {
                                    visibleLayout.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Constant.errorHandle(error, getActivity());
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(mContext, PreferenceConnector.AuthToken, ""));
                    return header;
                }


               /* @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }*/
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        }
    }

    private void updateUi(final AllPostResponce allPostResponce) {
        allPostResponces.clear();
        allPostResponces.addAll(allPostResponce.getData());
        allPostAdapter = new AllPostAdapter(mContext, allPostResponces, allPostResponce.getNow(), new RemoveImageListioner() {
            @Override
            public void onClick(int position) {
                //like
                AllPostResponce.DataBean dataBean = allPostResponce.getData().get(position);
                if (dataBean.getIsLike() == 1) {
                    dataBean.setIsLike(0);
                    dataBean.setLikeCount(dataBean.getLikeCount() - 1);
                } else if (dataBean.getIsLike() == 0) {
                    dataBean.setIsLike(1);
                    dataBean.setLikeCount(dataBean.getLikeCount() + 1);
                }
                allPostAdapter.notifyItemChanged(position);
                likePost("" + dataBean.get_id());
            }
        }, new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(mContext, ActivityComment.class);
                intent.putExtra("postResponceString", postResponceString);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        }, new MyOnCheckListioner() {
            @Override
            public void OnCheck(int position) {
                //share
            }
        });
        allPostRecycler.setAdapter(allPostAdapter);
        if (allPostResponces.size() > 0) {
            noDataTxt.setVisibility(View.GONE);
        } else {
            noDataTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMySocialPost();
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

    public void likePost(final String postId) {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            //progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getPostLikeUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //   progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("AllPost", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {

                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null)
                                        Constant.showLogOutDialog(getActivity());
                                } else {
                                    //     visibleLayout.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Constant.errorHandle(error, getActivity());
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
                    params.put("postId", postId);
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
