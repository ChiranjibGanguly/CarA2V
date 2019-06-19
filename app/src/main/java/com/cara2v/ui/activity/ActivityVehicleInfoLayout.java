package com.cara2v.ui.activity;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cara2v.R;
import com.cara2v.adapter.ViewPagerAdapter;
import com.cara2v.animation.ZoomOutPageTransformer;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.google.gson.Gson;

public class ActivityVehicleInfoLayout extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {


    private ViewPager viewPager;
    private RelativeLayout viewPagerIndicator;
    private LinearLayout viewPagerCountDots;
    private LinearLayout leftLay;
    private LinearLayout rightLay;
    private TextView vinCodeTxt;
    private TextView brandName;
    private TextView vehicleNo;
    private TextView plateNo;
    private TextView brandNameTxt;
    private TextView modelName;
    private TextView mileageTxt;
    private TextView fulTypeTxt;
    private TextView vehicleTypeTxt;
    private TextView vehicleYearTxt;
    private FrameLayout rightFrem;
    private FrameLayout leftFrem;

    private ImageView menuBtn;
    private TextView titleTxt;

    private String requestDetailsJson = "";
    private int dotsCount;
    private ImageView[] dots;
    private int userType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_info_layout);
        Bundle bundle = getIntent().getExtras();
        userType = PreferenceConnector.readInteger(ActivityVehicleInfoLayout.this, PreferenceConnector.UserType, 0);
        if (bundle != null) {
            requestDetailsJson = bundle.getString("requestDetailsJson");
            Log.e("requestDetailsJson", requestDetailsJson);
        }
        initializeView();

        if (!requestDetailsJson.equals("")) {
            setData();
        }
    }

    private void initializeView() {
        viewPager = findViewById(R.id.viewPager);
        viewPagerIndicator = findViewById(R.id.viewPagerIndicator);
        viewPagerCountDots = findViewById(R.id.viewPagerCountDots);
        rightFrem = findViewById(R.id.rightFrem);
        //rightFrem.setOnClickListener(this);
        leftFrem = findViewById(R.id.leftFrem);
        //leftFrem.setOnClickListener(this);

        leftLay = findViewById(R.id.leftLay);
        rightLay = findViewById(R.id.rightLay);
        leftLay.setOnClickListener(this);
        rightLay.setOnClickListener(this);

        vinCodeTxt = findViewById(R.id.vinCodeTxt);
        brandName = findViewById(R.id.brandName);
        vehicleNo = findViewById(R.id.vehicleNo);
        plateNo = findViewById(R.id.plateNo);
        brandNameTxt = findViewById(R.id.brandNameTxt);
        modelName = findViewById(R.id.modelName);
        mileageTxt = findViewById(R.id.mileageTxt);
        fulTypeTxt = findViewById(R.id.fulTypeTxt);
        vehicleTypeTxt = findViewById(R.id.vehicleTypeTxt);
        vehicleYearTxt = findViewById(R.id.vehicleYearTxt);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Vehicle Details");
        menuBtn.setOnClickListener(this);
        if (userType == Constant.userTypeOwner) {
            vehicleNo.setVisibility(View.GONE);
            plateNo.setVisibility(View.GONE);
        }
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

    private void setData() {
        Gson gson = new Gson();
        ServiceDetailsResponce serviceDetailsResponce = gson.fromJson(requestDetailsJson, ServiceDetailsResponce.class);
        String carImages = serviceDetailsResponce.getData().getCar().getMyCarImage();
        String[] carImagesArray = carImages.split(",");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(ActivityVehicleInfoLayout.this, carImagesArray);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        setUiPageViewController();
        brandName.setText(serviceDetailsResponce.getData().getCar().getBrand().getBrandName().toUpperCase());
        brandNameTxt.setText(serviceDetailsResponce.getData().getCar().getBrand().getBrandName());
        modelName.setText(serviceDetailsResponce.getData().getCar().getModel().getModelName());
        if (serviceDetailsResponce.getData().getCar().getPlateNumberStatus().equals("0")) {
            vehicleNo.setText(serviceDetailsResponce.getData().getCar().getPlateNumber());
        }
        vinCodeTxt.setText(serviceDetailsResponce.getData().getCar().getVINCode());
        mileageTxt.setText(serviceDetailsResponce.getData().getCar().getMileage());
        fulTypeTxt.setText(serviceDetailsResponce.getData().getCar().getFuelType());
        vehicleTypeTxt.setText(serviceDetailsResponce.getData().getCar().getCarType());
        if (!TextUtils.isEmpty(serviceDetailsResponce.getData().getCar().getCarYear())) {
            vehicleYearTxt.setText(serviceDetailsResponce.getData().getCar().getCarYear());
        }
    }

    private void setUiPageViewController() {
        viewPagerCountDots.removeAllViews();
        dotsCount = viewPager.getAdapter().getCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(ActivityVehicleInfoLayout.this);
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
