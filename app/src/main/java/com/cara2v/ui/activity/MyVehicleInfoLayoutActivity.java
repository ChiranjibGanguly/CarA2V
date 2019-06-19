package com.cara2v.ui.activity;

import android.os.Bundle;
import android.app.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.support.v7.widget.CardView;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.adapter.MyCompleteServicesAdapter;
import com.cara2v.adapter.ViewPagerAdapter;
import com.cara2v.animation.ZoomOutPageTransformer;
import com.cara2v.responceBean.GetMyCarResponce;
import com.cara2v.responceBean.MyServicesResponce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyVehicleInfoLayoutActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private RelativeLayout mainLayout;
    private ViewPager viewPager;
    private TextView vinCodeTxt;
    private TextView brandName;
    private TextView vehicleNo;
    private TextView plateNo;
    private RelativeLayout viewPagerIndicator;
    private RelativeLayout historyHeaderLay;
    private LinearLayout viewPagerCountDots;
    private LinearLayout leftLay;
    private FrameLayout leftFrem;
    private LinearLayout rightLay;
    private FrameLayout rightFrem;
    private TextView brandNameTxt;
    private TextView modelName;
    private TextView mileageTxt;
    private TextView fulTypeTxt;
    private TextView vehicleTypeTxt;
    private TextView vehicleYearTxt;
    private TextView noRecordTxt;
    private LinearLayout historyLay;
    private ImageView historyArrowBtn;
    private LinearLayout visibleLayout;
    private ProgressBar progressBar;
    private RecyclerView cmpltSerRecycler;

    private ImageView menuBtn;
    private TextView titleTxt;

    private Gson gson = new Gson();
    private MyCompleteServicesAdapter myCompleteServicesAdapter;
    private String carBeanJson = "";
    private int carId = 0;
    private int dotsCount;
    private ImageView[] dots;
    private int userType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_vehicle_info_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            carBeanJson = bundle.getString("carBeanJson");
            Log.e("carBeanJson", carBeanJson);
        }
        initializeView();
        if (!carBeanJson.equals("")) {
            setData();
        }

    }


    private void initializeView() {
        mainLayout = findViewById(R.id.mainLayout);
        viewPager = findViewById(R.id.viewPager);
        vinCodeTxt = findViewById(R.id.vinCodeTxt);
        brandName = findViewById(R.id.brandName);
        vehicleNo = findViewById(R.id.vehicleNo);
        plateNo = findViewById(R.id.plateNo);
        viewPagerIndicator = findViewById(R.id.viewPagerIndicator);
        viewPagerCountDots = findViewById(R.id.viewPagerCountDots);
        leftLay = findViewById(R.id.leftLay);
        leftFrem = findViewById(R.id.leftFrem);
        rightLay = findViewById(R.id.rightLay);
        rightFrem = findViewById(R.id.rightFrem);
        brandNameTxt = findViewById(R.id.brandNameTxt);
        modelName = findViewById(R.id.modelName);
        mileageTxt = findViewById(R.id.mileageTxt);
        fulTypeTxt = findViewById(R.id.fulTypeTxt);
        noRecordTxt = findViewById(R.id.noRecordTxt);
        vehicleTypeTxt = findViewById(R.id.vehicleTypeTxt);
        vehicleYearTxt = findViewById(R.id.vehicleYearTxt);
        historyLay = findViewById(R.id.historyLay);
        historyHeaderLay = findViewById(R.id.historyHeaderLay);
        historyArrowBtn = findViewById(R.id.historyArrowBtn);
        visibleLayout = findViewById(R.id.visibleLayout);
        progressBar = findViewById(R.id.progressBar);
        cmpltSerRecycler = findViewById(R.id.cmpltSerRecycler);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Vehicle Details");
        menuBtn.setOnClickListener(this);
        historyHeaderLay.setOnClickListener(this);

        leftLay.setOnClickListener(this);
        rightLay.setOnClickListener(this);
    }

    private void setData() {
        GetMyCarResponce.DataBean carBean = gson.fromJson(carBeanJson, GetMyCarResponce.DataBean.class);
        String carImages = carBean.getMyCarImage();
        String[] carImagesArray = carImages.split(",");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MyVehicleInfoLayoutActivity.this, carImagesArray);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        setUiPageViewController();
        brandName.setText(carBean.getBrand().getBrandName().toUpperCase());
        brandNameTxt.setText(carBean.getBrand().getBrandName());
        modelName.setText(carBean.getModel().getModelName());
        if (carBean.getPlateNumberStatus().equals("0")) {
            vehicleNo.setText(carBean.getPlateNumber());
        }
        vinCodeTxt.setText(carBean.getVINCode());
        mileageTxt.setText(carBean.getMileage());
        fulTypeTxt.setText(carBean.getFuelType());
        vehicleTypeTxt.setText(carBean.getCarType());
        carId = carBean.get_id();
        if (!TextUtils.isEmpty(carBean.getCarYear())) {
            vehicleYearTxt.setText(carBean.getCarYear());
        }
        getMyCompleteServices();
    }

    public void getMyCompleteServices() {
        if (Constant.isNetworkAvailable(MyVehicleInfoLayoutActivity.this, mainLayout)) {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.getMyRequestUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            System.out.println("#" + response);
                            Log.e("MyCompletedService", response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    MyServicesResponce myServicesResponce = gson.fromJson(response, MyServicesResponce.class);
                                    updateUi(myServicesResponce);
                                    if (myServicesResponce.getData().size() > 0) {

                                    } else {

                                    }
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(MyVehicleInfoLayoutActivity.this);
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
                            Constant.errorHandle(error, MyVehicleInfoLayoutActivity.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(MyVehicleInfoLayoutActivity.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("status", "3");
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
            VolleySingleton.getInstance(MyVehicleInfoLayoutActivity.this).addToRequestQueue(stringRequest);
        }
    }

    private void updateUi(final MyServicesResponce myServicesResponce) {
        ArrayList<MyServicesResponce.DataBean> dataBeans = new ArrayList<>();
        for (MyServicesResponce.DataBean dataBean : myServicesResponce.getData()) {
            if (dataBean.getCar().getCarId() == carId) {
                dataBeans.add(dataBean);
            }
        }
        if (dataBeans.size() > 0) {
            noRecordTxt.setVisibility(View.GONE);
        } else {
            noRecordTxt.setVisibility(View.VISIBLE);
        }
        myCompleteServicesAdapter = new MyCompleteServicesAdapter(MyVehicleInfoLayoutActivity.this, dataBeans, new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {
                /*Intent intent = new Intent(mContext, ActivityMyJobServiceDetailsLayout.class);
                intent.putExtra("requestId", myServicesResponce.getData().get(position).get_id());
                intent.putExtra("FROM", "complete");
                startActivity(intent);*/
            }
        });
        cmpltSerRecycler.setAdapter(myCompleteServicesAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftLay:
                viewPager.setCurrentItem((viewPager.getCurrentItem() > 0) ? viewPager.getCurrentItem() - 1 : 0);
                break;
            case R.id.rightLay:
                viewPager.setCurrentItem((viewPager.getCurrentItem() < dotsCount)
                        ? viewPager.getCurrentItem() + 1 : 0);
                break;
            case R.id.menuBtn:
                finish();
                break;
            case R.id.historyHeaderLay:
                if (historyLay.getVisibility() == View.VISIBLE) {
                    historyLay.setVisibility(View.GONE);
                    historyArrowBtn.setImageResource(R.drawable.down_arrow_service);
                } else if (historyLay.getVisibility() == View.GONE) {
                    historyLay.setVisibility(View.VISIBLE);
                    historyArrowBtn.setImageResource(R.drawable.up_arrow_service);
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        if (position + 1 == dotsCount) {
            rightFrem.setVisibility(View.GONE);
            leftFrem.setVisibility(View.VISIBLE);

        } else if (position == 0) {
            leftFrem.setVisibility(View.GONE);
            rightFrem.setVisibility(View.VISIBLE);

        } else {
            leftFrem.setVisibility(View.VISIBLE);
            rightFrem.setVisibility(View.VISIBLE);
        }
        if (dots.length == 1) {
            leftFrem.setVisibility(View.GONE);
            rightFrem.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setUiPageViewController() {
        viewPagerCountDots.removeAllViews();
        dotsCount = viewPager.getAdapter().getCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(MyVehicleInfoLayoutActivity.this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            viewPagerCountDots.addView(dots[i], params);
        }
        if (dots.length == 1) {
            leftFrem.setVisibility(View.GONE);
            rightFrem.setVisibility(View.GONE);
            viewPagerCountDots.setVisibility(View.GONE);
        } else {
            leftFrem.setVisibility(View.GONE);
            rightFrem.setVisibility(View.VISIBLE);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }
}
