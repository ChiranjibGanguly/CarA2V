package com.cara2v.ui.fragments.owner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.support.design.widget.CoordinatorLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.AddEditPromocodeListioner;
import com.cara2v.R;
import com.cara2v.adapter.PromoCodeAdapter;
import com.cara2v.responceBean.GetAllPromoCodeResponce;
import com.cara2v.ui.activity.owner.ActivityAddEditpromoCode;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPromoCode extends Fragment implements View.OnClickListener, AddEditPromocodeListioner {

    private RecyclerView myPromocode;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout snackLayout;
    private FrameLayout addPromocodeBtn;
    private PromoCodeAdapter promoCodeAdapter;
    private Context mContext;
    private String promoId = "", promoName = "", promoCode = "", promoDiscount = "", expireDate = "";
    private List<GetAllPromoCodeResponce.DataBean> allPromoCodeList = new ArrayList<>();

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promo_code, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
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
        myPromocode = view.findViewById(R.id.myPromocode);
        visibleLayout = view.findViewById(R.id.visibleLayout);
        progressBar = view.findViewById(R.id.progressBar);
        snackLayout = view.findViewById(R.id.snackLayout);
        addPromocodeBtn = view.findViewById(R.id.addPromocodeBtn);
        addPromocodeBtn.setOnClickListener(this);
        promoCodeAdapter = new PromoCodeAdapter(mContext, allPromoCodeList, this);
        myPromocode.setAdapter(promoCodeAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPromocodeBtn:
                if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.IsLcnAprv, false)) {
                    goToAddEdit(true);
                } else {
                    Constant.snackbar(snackLayout,"Please wait! Until your license will approve.");
                }
                break;
        }
    }

    private void goToAddEdit(boolean isAdd) {
        if (isAdd) {
            promoId = "";
            promoName = "";
            promoCode = "";
            promoDiscount = "";
            expireDate = "";
        }
        Intent intent = new Intent(mContext, ActivityAddEditpromoCode.class);
        intent.putExtra("PROMOID", promoId);
        intent.putExtra("PROMONAME", promoName);
        intent.putExtra("PROMOCODE", promoCode);
        intent.putExtra("PROMODISCOUNT", promoDiscount);
        intent.putExtra("EXPIREDATE", expireDate);
        startActivity(intent);
    }

    public void getPromoCode() {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getPromoCodeUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("AllPromo", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    GetAllPromoCodeResponce getAllPromoCodeResponce = new Gson().fromJson(response, GetAllPromoCodeResponce.class);
                                    updateUi(getAllPromoCodeResponce);
                                    if (getAllPromoCodeResponce.getData().size() > 0) {
                                        visibleLayout.setVisibility(View.GONE);
                                    } else {
                                        visibleLayout.setVisibility(View.VISIBLE);
                                    }

                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null)
                                        Constant.showLogOutDialog(getActivity());
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
                            progressBar.setVisibility(View.GONE);
                            if (getActivity() != null) Constant.showLogOutDialog(getActivity());
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
                    if (isAdd) {
                        params.put("promoId", promoId);
                        params.put("actionType", "1");
                    }
                    params.put("title", promoCode);
                    params.put("amount", promoDiscount);
                    params.put("expire", expireDate);
                    params.put("name", promoName);
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }*/

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

    private void updateUi(GetAllPromoCodeResponce getAllPromoCodeResponce) {
        allPromoCodeList.clear();
        allPromoCodeList.addAll(getAllPromoCodeResponce.getData());
        promoCodeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPromoCode();
    }

    @Override
    public void onEdit(GetAllPromoCodeResponce.DataBean dataBean) {
        if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.IsLcnAprv, false)) {
            promoId = String.valueOf(dataBean.getId());
            promoName = dataBean.getName();
            promoCode = dataBean.getTitle();
            promoDiscount = dataBean.getAmount();
            expireDate = Constant.dateFormateChangePromoUpdate(dataBean.getExpire());
            goToAddEdit(false);
        }else {
            Constant.snackbar(snackLayout,"Please wait! Until your license will approve.");
        }
    }

    @Override
    public void onDelete(GetAllPromoCodeResponce.DataBean dataBean) {
        if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.IsLcnAprv, false)) {
                showDeleteAlertDialog(String.valueOf(dataBean.getId()));
            }else {
            Constant.snackbar(snackLayout,"Please wait! Until your license will approve.");
        }
    }

    public void deletePromoCode(final String id) {
        if (Constant.isNetworkAvailable(mContext, snackLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.addUpdatePromoCodeUrl,
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
                                    getPromoCode();
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    if (getActivity() != null)
                                        Constant.showLogOutDialog(getActivity());
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
                            progressBar.setVisibility(View.GONE);
                            if (getActivity() != null) Constant.showLogOutDialog(getActivity());
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

                    params.put("promoId", id);
                    params.put("actionType", "2");

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


    public void showDeleteAlertDialog(final String promoId) {

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(mContext);
        builder1.setTitle("Alert !!");
        builder1.setMessage("Are you sure you want to delete this code?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        deletePromoCode(promoId);
                        }
                });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });
        android.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
