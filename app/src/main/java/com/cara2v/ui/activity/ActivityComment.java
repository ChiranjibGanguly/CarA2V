package com.cara2v.ui.activity;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.R;
import com.cara2v.adapter.CommentAdapter;
import com.cara2v.responceBean.AllPostResponce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityComment extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private RecyclerView commentRecycler;
    private RelativeLayout commentLay;
    private ImageView cmntImgBtn;
    private ImageView menuBtn;
    private TextView titleTxt;
    private EditText cmntTxt;
    private ProgressDialog progressDialog;

    private int position = 0, postId = 0;
    private String postResponceString = "";
    private Gson gson = new Gson();
    private ArrayList<AllPostResponce.DataBean.CommentBean> commentBeanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
            postResponceString = bundle.getString("postResponceString");
        }
        initializeView();
    }

    private void initializeView() {
        mainLayout = findViewById(R.id.mainLayout);
        commentRecycler = findViewById(R.id.commentRecycler);
        commentLay = findViewById(R.id.commentLay);
        cmntImgBtn = findViewById(R.id.cmntImgBtn);
        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        cmntTxt = findViewById(R.id.cmntTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Comments");
        menuBtn.setOnClickListener(this);
        cmntImgBtn.setOnClickListener(this);
        progressDialog = new ProgressDialog(ActivityComment.this);
        progressDialog.setCancelable(false);
        AllPostResponce allPostResponce = gson.fromJson(postResponceString, AllPostResponce.class);
        commentBeanArrayList = new ArrayList<>();
        commentBeanArrayList.addAll(allPostResponce.getData().get(position).getComment());
        postId = allPostResponce.getData().get(position).get_id();
        CommentAdapter commentAdapter = new CommentAdapter(ActivityComment.this, commentBeanArrayList, allPostResponce.getNow());
        commentRecycler.setAdapter(commentAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;
            case R.id.cmntImgBtn:
                String cmnt = cmntTxt.getText().toString();
                if (!TextUtils.isEmpty(cmnt)) giveComment(cmnt);
                else Constant.snackbar(mainLayout, "Please add comment");
                break;
        }
    }

    public void giveComment(final String cmnt) {
        if (Constant.isNetworkAvailable(ActivityComment.this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getPostCommentUrl,
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
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityComment.this);
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
                            progressDialog.dismiss();
                            Constant.errorHandle(error, ActivityComment.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityComment.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("postId", "" + postId);
                    params.put("comment", cmnt);
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
            VolleySingleton.getInstance(ActivityComment.this).addToRequestQueue(stringRequest);
        }
    }
}
