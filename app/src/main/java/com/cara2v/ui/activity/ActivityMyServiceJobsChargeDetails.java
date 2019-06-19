package com.cara2v.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;
import android.widget.LinearLayout;

import com.cara2v.Interface.AddPriceListioner;
import com.cara2v.R;
import com.cara2v.adapter.ViewQuotedServiceAdapter;
import com.cara2v.bean.QuotedServicesBean;
import com.cara2v.bean.QuotedSubServiceBean;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ActivityMyServiceJobsChargeDetails extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private RecyclerView quotedRecycler;
   // private RelativeLayout cpnUpLay;
   // private TextView cupnTxt;
    private LinearLayout bellowLay;
    private TextView partChargeTxt;
    private TextView laborChargeTxt;
    private TextView flateChargeTxt;
    private RelativeLayout taxLay;
    private TextView taxx;
    private TextView percn;
    private TextView taxTxt;
    private TextView adcom;
    private TextView adminComTxt;
    private TextView rewardsTxt;
    private TextView totalTxt;
    private TextView pay;
    private TextView depositeTxt;
   // private View cpnVw;
    private View labView;
    private View taxView;
    private View adcomView;
    private View totalView;
    private Button payBtn;

    private ImageView menuBtn;
    private TextView titleTxt;

    private int userType = 0;
    private DecimalFormat df = new DecimalFormat("#.##");

    private int width = 0, height = 0;

    private float partPrice = 0;
    private float laborPrice = 0;
    private float totalPrice = 0;
    private float flatePrice = 0;
    private float reward = 0;
    private float adminComisioin = 0;
    private float tax = 0;
    private float depositeAmount = 0;

    private String requestDetailsJson = "", orderBean = "";

    private ServiceDetailsResponce serviceDetailsResponce;
    private ServiceDetailsResponce.DataBean.OrderdetailsBean bean;

    private Gson gson = new Gson();
    private ArrayList<QuotedServicesBean> quotedServicesBeanArrayList;
    private ViewQuotedServiceAdapter viewQuotedServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_jobs_charge_details);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestDetailsJson = bundle.getString("requestDetailsJson");
            // position = bundle.getInt("position");
            orderBean = bundle.getString("orderBean");
        }
        initializeView();
        if (!requestDetailsJson.equals("")) {
            if (userType == Constant.userTypeOwner) {
                payBtn.setVisibility(View.GONE);
                setDataOwnerEnd();
            } else if (userType == Constant.uerTypeConsumer) {
                payBtn.setVisibility(View.GONE);
                setDataConsumerEnd();
            }

        }
    }

    private void initializeView() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        mainLayout = findViewById(R.id.mainLayout);
        quotedRecycler = findViewById(R.id.quotedRecycler);
       // cpnUpLay = findViewById(R.id.cpnUpLay);
       // cupnTxt = findViewById(R.id.cupnTxt);
        bellowLay = findViewById(R.id.bellowLay);
        partChargeTxt = findViewById(R.id.partChargeTxt);
        laborChargeTxt = findViewById(R.id.laborChargeTxt);
        flateChargeTxt = findViewById(R.id.flateChargeTxt);
        taxLay = findViewById(R.id.taxLay);
        taxx = findViewById(R.id.taxx);
        percn = findViewById(R.id.percn);
        taxTxt = findViewById(R.id.taxTxt);
        adcom = findViewById(R.id.adcom);
        adminComTxt = findViewById(R.id.adminComTxt);
        rewardsTxt = findViewById(R.id.rewardsTxt);
        totalTxt = findViewById(R.id.totalTxt);
        pay = findViewById(R.id.pay);
        depositeTxt = findViewById(R.id.depositeTxt);

      //  cpnVw = findViewById(R.id.cpnVw);
        labView = findViewById(R.id.labView);
        taxView = findViewById(R.id.taxView);
        adcomView = findViewById(R.id.adcomView);
        totalView = findViewById(R.id.totalView);
        payBtn = findViewById(R.id.payBtn);
        payBtn.setOnClickListener(this);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);


        menuBtn.setImageResource(R.drawable.back_icon);

        titleTxt.setText("Service Charge");
        menuBtn.setOnClickListener(this);
        userType = PreferenceConnector.readInteger(ActivityMyServiceJobsChargeDetails.this, PreferenceConnector.UserType, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;
            case R.id.payBtn:
                Constant.snackbar(mainLayout, getString(R.string.development_mode));
                break;
        }
    }

    private void setDataOwnerEnd() {
        quotedServicesBeanArrayList = new ArrayList<>();
        serviceDetailsResponce = gson.fromJson(requestDetailsJson, ServiceDetailsResponce.class);
        bean = gson.fromJson(orderBean, ServiceDetailsResponce.DataBean.OrderdetailsBean.class);
        adminComisioin = Float.parseFloat(bean.getCommission());
        try {
            tax = Float.parseFloat(bean.getTax());
            taxTxt.setText("" + Constant.DecimalFormat(Double.valueOf(tax)));
        } catch (Exception e) {
            tax = 0;
            taxTxt.setText("" + Constant.DecimalFormat(Double.valueOf(tax)));
        }
        for (ServiceDetailsResponce.DataBean.ServiceBean serviceBean : serviceDetailsResponce.getData().getService()) {
            QuotedServicesBean quotedServicesBean = new QuotedServicesBean();
            quotedServicesBean.setId(serviceBean.getId());
            quotedServicesBean.setName(serviceBean.getName());
            quotedServicesBean.setServiceImage(serviceBean.getServiceImage());
            ArrayList<QuotedSubServiceBean> subServiceBeanArrayList = new ArrayList<>();
            for (ServiceDetailsResponce.DataBean.ServiceBean.SubServiceBean subServiceBean : serviceBean.getSubService()) {
                QuotedSubServiceBean quotedSubServiceBean = new QuotedSubServiceBean();
                quotedSubServiceBean.setId(subServiceBean.getId());
                quotedSubServiceBean.setName(subServiceBean.getName());
                for (ServiceDetailsResponce.DataBean.OrderdetailsBean.RequestJsonBean jsonBean : bean.getRequestJson()) {
                    if (subServiceBean.getId().equals(jsonBean.getId())) {
                        quotedSubServiceBean.setPrice(jsonBean.getPrice());
                        quotedSubServiceBean.setParts(jsonBean.getParts());
                        quotedSubServiceBean.setFlatPrice(jsonBean.getFlatPrice());
                        quotedSubServiceBean.setFlatRemark(jsonBean.getFlatRemark());
                        break;
                    }
                }
                subServiceBeanArrayList.add(quotedSubServiceBean);
            }
            quotedServicesBean.setQuotedSubServiceBeans(subServiceBeanArrayList);
            quotedServicesBeanArrayList.add(quotedServicesBean);
        }
        viewQuotedServiceAdapter = new ViewQuotedServiceAdapter(this, quotedServicesBeanArrayList, "blue", new AddPriceListioner() {
            @Override
            public void onClick(int servicePos, int subServicePos) {
                Log.e("POP", "servicePos: " + servicePos + " subServicePos: " + subServicePos);
                popUpAdPrice(servicePos, subServicePos);
            }
        });
        quotedRecycler.setAdapter(viewQuotedServiceAdapter);

        ///setDataOwnerEnd
        try {
            reward = Float.parseFloat(bean.getDisAmount());
            rewardsTxt.setText("" + Constant.DecimalFormat(reward));
        } catch (Exception e) {
            rewardsTxt.setText("" + Constant.DecimalFormat(reward));
        }
        try {
            depositeAmount = Float.parseFloat(bean.getDepositPrice());
        } catch (Exception e) {

        }
        if (depositeAmount > 0) {
            depositeTxt.setText("Deposit $ " + Constant.DecimalFormat(depositeAmount));
        } else {
            depositeTxt.setText("Pay After Repair");
        }
        /*if (TextUtils.isEmpty(bean.getCoupon())) {
            cpnUpLay.setVisibility(View.GONE);
            cpnVw.setVisibility(View.GONE);
        }*/
        setBellowLayOutDataOwnerEnd();
    }

    private void setDataConsumerEnd() {
        quotedServicesBeanArrayList = new ArrayList<>();
        serviceDetailsResponce = gson.fromJson(requestDetailsJson, ServiceDetailsResponce.class);
        bean = gson.fromJson(orderBean, ServiceDetailsResponce.DataBean.OrderdetailsBean.class);
        try {
            tax = Float.parseFloat(bean.getTax());
            //    taxTxt.setText("" + Constant.DecimalFormat(Double.valueOf(tax)));
        } catch (Exception e) {
            tax = 0;
            //    taxTxt.setText("" + Constant.DecimalFormat(Double.valueOf(tax)));
        }
        adminComisioin = Float.parseFloat(bean.getCommission());

        for (ServiceDetailsResponce.DataBean.ServiceBean serviceBean : serviceDetailsResponce.getData().getService()) {
            QuotedServicesBean quotedServicesBean = new QuotedServicesBean();
            quotedServicesBean.setId(serviceBean.getId());
            quotedServicesBean.setName(serviceBean.getName());
            quotedServicesBean.setServiceImage(serviceBean.getServiceImage());
            ArrayList<QuotedSubServiceBean> subServiceBeanArrayList = new ArrayList<>();
            for (ServiceDetailsResponce.DataBean.ServiceBean.SubServiceBean subServiceBean : serviceBean.getSubService()) {
                QuotedSubServiceBean quotedSubServiceBean = new QuotedSubServiceBean();
                quotedSubServiceBean.setId(subServiceBean.getId());
                quotedSubServiceBean.setName(subServiceBean.getName());
                for (ServiceDetailsResponce.DataBean.OrderdetailsBean.RequestJsonBean jsonBean : bean.getRequestJson()) {
                    if (subServiceBean.getId().equals(jsonBean.getId())) {
                        float flatePriceCom = (Float.parseFloat(jsonBean.getFlatPrice()) * adminComisioin) / 100;
                        float partTax = (Float.parseFloat(jsonBean.getParts()) * tax) / 100;
                        float laborCom = (Float.parseFloat(jsonBean.getPrice()) * adminComisioin) / 100;

                        quotedSubServiceBean.setPrice(String.valueOf(Float.parseFloat(jsonBean.getPrice()) + laborCom));
                        quotedSubServiceBean.setParts(String.valueOf(Float.parseFloat(jsonBean.getParts()) + partTax));
                        quotedSubServiceBean.setFlatPrice(String.valueOf(Float.parseFloat(jsonBean.getFlatPrice()) + flatePriceCom));
                        quotedSubServiceBean.setFlatRemark(jsonBean.getFlatRemark());
                        break;
                    }
                }
                subServiceBeanArrayList.add(quotedSubServiceBean);
            }
            quotedServicesBean.setQuotedSubServiceBeans(subServiceBeanArrayList);
            quotedServicesBeanArrayList.add(quotedServicesBean);
        }
        viewQuotedServiceAdapter = new ViewQuotedServiceAdapter(this, quotedServicesBeanArrayList, "blue", new AddPriceListioner() {
            @Override
            public void onClick(int servicePos, int subServicePos) {
                Log.e("POP", "servicePos: " + servicePos + " subServicePos: " + subServicePos);
                popUpAdPrice(servicePos, subServicePos);
            }
        });
        quotedRecycler.setAdapter(viewQuotedServiceAdapter);

        ///setDataOwnerEnd
        try {
            reward = Float.parseFloat(bean.getDisAmount());
            rewardsTxt.setText("" + Constant.DecimalFormat(reward));
        } catch (Exception e) {
            rewardsTxt.setText("" + Constant.DecimalFormat(reward));
        }
        try {
            depositeAmount = Float.parseFloat(bean.getDepositPrice());
        } catch (Exception e) {

        }
        if (depositeAmount > 0) {
            depositeTxt.setText("Deposit $ " + Constant.DecimalFormat(depositeAmount));
        } else {
            depositeTxt.setText("Pay After Repair");
        }

        /*if (TextUtils.isEmpty(bean.getCoupon())) {
            cpnUpLay.setVisibility(View.GONE);
            cpnVw.setVisibility(View.GONE);
        }*/
        setBellowLayOutDataConsumerEnd();
    }

    void popUpAdPrice(final int servicePos, final int subServicePos) {
        final Dialog dialog = new Dialog(this);
        QuotedSubServiceBean quotedSubServiceBean = quotedServicesBeanArrayList.get(servicePos).getQuotedSubServiceBeans().get(subServicePos);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_add_service_quote_price);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = dialog.findViewById(R.id.popCancel);
        final EditText partEdTxt = dialog.findViewById(R.id.partEdTxt);
        final EditText laborEdTxt = dialog.findViewById(R.id.laborEdTxt);
        final EditText flatRemarkTxt = dialog.findViewById(R.id.flatRemarkTxt);
        final EditText partRemarkTxt = dialog.findViewById(R.id.partRemarkTxt);
        final TextView popTitle = dialog.findViewById(R.id.popTitle);
        final TextView subServiceTitleTxt = dialog.findViewById(R.id.subServiceTitleTxt);
        final Button addPriceBtn = dialog.findViewById(R.id.addPriceBtn);
        final LinearLayout bellowLay = dialog.findViewById(R.id.bellowLay);
        final EditText flatPriceEdTxt = dialog.findViewById(R.id.flatPriceEdTxt);
        final AppCompatCheckBox checkFlat = dialog.findViewById(R.id.checkFlat);
        final LinearLayout partLay = dialog.findViewById(R.id.partLay);
        final LinearLayout flatPriceLay = dialog.findViewById(R.id.flatPriceLay);
        subServiceTitleTxt.setText(quotedServicesBeanArrayList.get(servicePos).getQuotedSubServiceBeans().get(subServicePos).getName());
        popTitle.setText("Service Price");
        partEdTxt.setText(Constant.DecimalFormat(Double.valueOf(quotedSubServiceBean.getParts())));
        laborEdTxt.setText(Constant.DecimalFormat(Double.valueOf(quotedSubServiceBean.getPrice())));
        flatPriceEdTxt.setText(Constant.DecimalFormat(Double.valueOf(quotedSubServiceBean.getFlatPrice())));
        if (quotedSubServiceBean.getFlatRemark().equals("")) {
            flatRemarkTxt.setHint("Description not available");
            partRemarkTxt.setHint("Description not available");
        } else {
            flatRemarkTxt.setText(quotedSubServiceBean.getFlatRemark());
            partRemarkTxt.setText(quotedSubServiceBean.getFlatRemark());
        }
        partEdTxt.setSelection(partEdTxt.getText().length());
        laborEdTxt.setSelection(laborEdTxt.getText().length());
        flatPriceEdTxt.setSelection(flatPriceEdTxt.getText().length());
        laborEdTxt.setClickable(false);
        laborEdTxt.setFocusable(false);
        partEdTxt.setClickable(false);
        partEdTxt.setFocusable(false);
        flatPriceEdTxt.setClickable(false);
        flatPriceEdTxt.setFocusable(false);
        checkFlat.setFocusable(false);
        checkFlat.setClickable(false);
        partRemarkTxt.setClickable(false);
        flatRemarkTxt.setClickable(false);
        partRemarkTxt.setFocusable(false);
        flatRemarkTxt.setFocusable(false);

        if (Float.parseFloat(quotedSubServiceBean.getFlatPrice()) > 0) {
            checkFlat.setChecked(true);
            checkFlat.setText("   Added Flat Price");
            partLay.setVisibility(View.GONE);
            flatPriceLay.setVisibility(View.VISIBLE);
        } else {
            checkFlat.setVisibility(View.GONE);
        }
        addPriceBtn.setVisibility(View.GONE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private void setBellowLayOutDataOwnerEnd() {
        partPrice = 0;
        laborPrice = 0;
        flatePrice = 0;
        for (QuotedServicesBean servicesBean : quotedServicesBeanArrayList) {

            for (QuotedSubServiceBean quotedSubServiceBean : servicesBean.getQuotedSubServiceBeans()) {
                partPrice = partPrice + Float.parseFloat(quotedSubServiceBean.getParts());
                laborPrice = laborPrice + Float.parseFloat(quotedSubServiceBean.getPrice());
                flatePrice = flatePrice + Float.parseFloat(quotedSubServiceBean.getFlatPrice());
            }
        }
        calculateAfterAddPriceOwnerEnd();
    }

    private void setBellowLayOutDataConsumerEnd() {
        partPrice = 0;
        laborPrice = 0;
        flatePrice = 0;
        for (QuotedServicesBean servicesBean : quotedServicesBeanArrayList) {

            for (QuotedSubServiceBean quotedSubServiceBean : servicesBean.getQuotedSubServiceBeans()) {
                partPrice = partPrice + Float.parseFloat(quotedSubServiceBean.getParts());
                laborPrice = laborPrice + Float.parseFloat(quotedSubServiceBean.getPrice());
                flatePrice = flatePrice + Float.parseFloat(quotedSubServiceBean.getFlatPrice());
            }
        }
        float total = partPrice + laborPrice + flatePrice;
        laborChargeTxt.setText("$ " + Constant.DecimalFormat(laborPrice));
        partChargeTxt.setText("$ " + Constant.DecimalFormat(partPrice));
        flateChargeTxt.setText("$ " + Constant.DecimalFormat(flatePrice));
        totalTxt.setText("$ " + Constant.DecimalFormat(total));
        taxLay.setVisibility(View.GONE);
    }

    private void calculateAfterAddPriceOwnerEnd() {

        float flatePriceCom = (flatePrice * adminComisioin) / 100;
        float partTax = (partPrice * tax) / 100;
        float laborCom = (laborPrice * adminComisioin) / 100;
        float total = partPrice + laborPrice + flatePrice + flatePriceCom + partTax;

        totalPrice = total + laborCom;
        laborChargeTxt.setText("$ " + Constant.DecimalFormat(laborPrice + laborCom));
        partChargeTxt.setText("$ " + Constant.DecimalFormat(partPrice + partTax));
        flateChargeTxt.setText("$ " + Constant.DecimalFormat(flatePrice + flatePriceCom));
        adminComTxt.setText("" + df.format(adminComisioin) + " %");
        totalTxt.setText("$ " + Constant.DecimalFormat(totalPrice));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityMyServiceJobsChargeDetails.this.registerReceiver(receiver, new IntentFilter("FILTERSERVICEPROGRESS"));

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterWifiReceiver();
    }

    private void unregisterWifiReceiver() {
        ActivityMyServiceJobsChargeDetails.this.unregisterReceiver(receiver);
    }
}
