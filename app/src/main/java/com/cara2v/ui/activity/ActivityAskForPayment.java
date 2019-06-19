package com.cara2v.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.AddPriceListioner;
import com.cara2v.R;
import com.cara2v.adapter.ViewQuotedServiceAdapter;
import com.cara2v.bean.QuotedServicesBean;
import com.cara2v.bean.QuotedSubServiceBean;
import com.cara2v.bean.QuotedSubservice;
import com.cara2v.responceBean.CheckedPromoCodeResponce;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.ui.activity.consumer.StripPaymentActivity;
import com.cara2v.util.Constant;
import com.cara2v.util.MyApplication;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.ProgressDialog;
import com.cara2v.util.Validation;
import com.cara2v.vollyemultipart.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAskForPayment extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;

    private RecyclerView quotedRecycler;
    private LinearLayout cpnUpLay;
    private RelativeLayout extraChargesLay;
    private EditText cupnTxt;
    private LinearLayout bellowLay;
    private TextView partChargeTxt;
    private TextView laborChargeTxt;
    private TextView flateChargeTxt;
    private RelativeLayout taxLay;
    private RelativeLayout extraLay;
    private TextView taxx;
    private TextView percn;
    private TextView taxTxt;
    private TextView adcom;
    private TextView adminComTxt;
    private TextView rewardsTxt;
    private TextView totalTxt;
    private TextView pay;
    private TextView depositeTxt;
    private TextView extraTotalTxt;
    private TextView exPartChargeTxt;
    private TextView exLaborChargeTxt;
    private TextView exFlateChargeTxt;
    private RecyclerView extraRecycler;
    private Button askFrPayBtn;
    private Button applyCupn;
    private ProgressBar smlProgress;

    private View cpnVw;
    private View labView;
    private View adcomView;
    private View totalView;

    private ImageView menuBtn;
    private ImageView extraBtn;
    private TextView titleTxt;

    private int userType = 0;
    private int width = 0, height = 0;
    private String requestDetailsJson = "", orderBean = "";
    private String extraChargesJson = "";

    private ArrayList<QuotedServicesBean> quotedServicesBeanArrayList;
    private ArrayList<QuotedServicesBean> quotedServicesBeanArrayListForEdit;
    private ViewQuotedServiceAdapter viewQuotedServiceAdapter;
    private ServiceDetailsResponce serviceDetailsResponce;
    private ServiceDetailsResponce.DataBean.OrderdetailsBean bean;
    private ArrayList<QuotedSubservice> quotedSubservices = new ArrayList<>();
    private Gson gson = new Gson();

    private float partPrice = 0;
    private float laborPrice = 0;
    private float totalPrice = 0;
    private float flatePrice = 0;
    private float reward = 0;
    private float adminComisioin = 0;
    private float tax = 0;
    private float depositeAmount = 0;

    private float extraPartPrice = 0;
    private float extraLaborPrice = 0;
    private float extraFlatePrice = 0;

    private float extraTotal = 0;
    private float allTotal = 0;
    private float discountPrice = 0;
    private float payPrice = 0;


    private DecimalFormat df = new DecimalFormat("#.##");

    private String myEditedJson = "";
    private String requestId = "";
    private String status = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_payment);
        MyApplication.addActivity(ActivityAskForPayment.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestDetailsJson = bundle.getString("requestDetailsJson");
            // position = bundle.getInt("position");
            orderBean = bundle.getString("orderBean");
            status = bundle.getString("status");
        }
        initializeView();
        if (!requestDetailsJson.equals("")) {
            serviceDetailsResponce = gson.fromJson(requestDetailsJson, ServiceDetailsResponce.class);
            if (userType == Constant.userTypeOwner) {
                setDataOwnerEnd();
                askFrPayBtn.setText("Ask for Payment");
            } else if (userType == Constant.uerTypeConsumer) {
                askFrPayBtn.setText("Pay Now");
                setDataConsumerEnd();
            }
            collectExtraChargesData();
        }
    }


    private void initializeView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        mainLayout = findViewById(R.id.mainLayout);
        quotedRecycler = findViewById(R.id.quotedRecycler);
        cpnUpLay = findViewById(R.id.cpnUpLay);
        cupnTxt = findViewById(R.id.cupnTxt);
        bellowLay = findViewById(R.id.bellowLay);
        partChargeTxt = findViewById(R.id.partChargeTxt);
        laborChargeTxt = findViewById(R.id.laborChargeTxt);
        flateChargeTxt = findViewById(R.id.flateChargeTxt);
        taxLay = findViewById(R.id.taxLay);
        taxx = findViewById(R.id.taxx);
        percn = findViewById(R.id.percn);
        taxTxt = findViewById(R.id.taxTxt);
        adcom = findViewById(R.id.adcom);
        extraBtn = findViewById(R.id.extraBtn);
        adminComTxt = findViewById(R.id.adminComTxt);
        rewardsTxt = findViewById(R.id.rewardsTxt);
        totalTxt = findViewById(R.id.totalTxt);
        pay = findViewById(R.id.pay);
        depositeTxt = findViewById(R.id.depositeTxt);
        applyCupn = findViewById(R.id.applyCupn);
        smlProgress = findViewById(R.id.smlProgress);

        // extraTotalTxt = findViewById(R.id.extraTotalTxt);
        exPartChargeTxt = findViewById(R.id.exPartChargeTxt);
        exLaborChargeTxt = findViewById(R.id.exLaborChargeTxt);
        exFlateChargeTxt = findViewById(R.id.exFlateChargeTxt);
        extraChargesLay = findViewById(R.id.extraChargesLay);
        extraLay = findViewById(R.id.extraLay);
        // extraRecycler = findViewById(R.id.extraRecycler);
        askFrPayBtn = findViewById(R.id.askFrPayBtn);
        askFrPayBtn.setOnClickListener(this);
        applyCupn.setOnClickListener(this);
        extraChargesLay.setOnClickListener(this);

        cpnVw = findViewById(R.id.cpnVw);
        labView = findViewById(R.id.labView);
        adcomView = findViewById(R.id.adcomView);
        totalView = findViewById(R.id.totalView);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);

        menuBtn.setImageResource(R.drawable.back_icon);
        menuBtn.setOnClickListener(this);
        userType = PreferenceConnector.readInteger(ActivityAskForPayment.this, PreferenceConnector.UserType, 0);
        if (status.equals("7")) {
            extraBtn.setImageResource(R.drawable.view);
        }
        if (userType == Constant.uerTypeConsumer) {
            titleTxt.setText("Invoice");
        } else if (userType == Constant.userTypeOwner) {
            titleTxt.setText("Ask For Payment");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;

            case R.id.applyCupn:
                Constant.hideSoftKeyboard(this);
                if (cupnTxt.getText().toString().trim().equals("")) {
                    Constant.snackbar(mainLayout, "Enter promo code");
                } else if (cupnTxt.getText().toString().trim().length() < 6) {
                    Constant.snackbar(mainLayout, "Promo code should be 6 digit in length");
                } else if (cupnTxt.getText().toString().trim().length() == 6) {
                    checkPromoCode();
                }
                break;
            case R.id.askFrPayBtn:
                if (userType == Constant.userTypeOwner) {
                    getEditedJson();
                    updateInvoice();
                } else if (userType == Constant.uerTypeConsumer) {
                    popUpPopupWindow();
                }
                break;
            case R.id.extraChargesLay:
                if (status.equals("7")) {
                    if (quotedSubservices.size() > 0) {
                        Intent intent = new Intent(ActivityAskForPayment.this, ActivityAddExtracharges.class);
                        intent.putExtra("requestDetailsJson", requestDetailsJson);
                        intent.putExtra("extraChargesJson", extraChargesJson);
                        intent.putExtra("status", status);
                        startActivityForResult(intent, 1111);
                    } else {
                        Constant.snackbar(mainLayout, "No extra charges applied.");
                    }
                } else {
                    Intent intent = new Intent(ActivityAskForPayment.this, ActivityAddExtracharges.class);
                    intent.putExtra("requestDetailsJson", requestDetailsJson);
                    intent.putExtra("extraChargesJson", extraChargesJson);
                    intent.putExtra("status", status);
                    startActivityForResult(intent, 1111);
                    break;
                }
        }
    }


    private void setDataOwnerEnd() {
        quotedServicesBeanArrayList = new ArrayList<>();
        quotedServicesBeanArrayListForEdit = new ArrayList<>();

        bean = gson.fromJson(orderBean, ServiceDetailsResponce.DataBean.OrderdetailsBean.class);
        adminComisioin = Float.parseFloat(bean.getCommission());
        requestId = String.valueOf(serviceDetailsResponce.getData().get_id());
        try {
            tax = Float.parseFloat(bean.getTax());
            taxTxt.setText("" + Constant.DecimalFormat(Double.valueOf(tax)));
        } catch (Exception e) {
            tax = 0;
            taxTxt.setText("" + Constant.DecimalFormat(Double.valueOf(tax)));
        }
        for (ServiceDetailsResponce.DataBean.ServiceBean serviceBean : serviceDetailsResponce.getData().getService()) {
            QuotedServicesBean quotedServicesBean = new QuotedServicesBean();
            QuotedServicesBean quotedServicesBeanEdit = new QuotedServicesBean();
            quotedServicesBean.setId(serviceBean.getId());
            quotedServicesBean.setName(serviceBean.getName());
            quotedServicesBean.setServiceImage(serviceBean.getServiceImage());

            quotedServicesBeanEdit.setId(serviceBean.getId());
            quotedServicesBeanEdit.setName(serviceBean.getName());
            quotedServicesBeanEdit.setServiceImage(serviceBean.getServiceImage());

            ArrayList<QuotedSubServiceBean> subServiceBeanArrayList = new ArrayList<>();
            ArrayList<QuotedSubServiceBean> subServiceBeanArrayListEdit = new ArrayList<>();

            for (ServiceDetailsResponce.DataBean.ServiceBean.SubServiceBean subServiceBean : serviceBean.getSubService()) {
                QuotedSubServiceBean quotedSubServiceBean = new QuotedSubServiceBean();
                QuotedSubServiceBean quotedSubServiceBeanEdit = new QuotedSubServiceBean();
                quotedSubServiceBean.setId(subServiceBean.getId());
                quotedSubServiceBean.setName(subServiceBean.getName());
                quotedSubServiceBeanEdit.setId(subServiceBean.getId());
                quotedSubServiceBeanEdit.setName(subServiceBean.getName());
                for (ServiceDetailsResponce.DataBean.OrderdetailsBean.RequestJsonBean jsonBean : bean.getRequestJson()) {
                    if (subServiceBean.getId().equals(jsonBean.getId())) {
                        quotedSubServiceBean.setPrice(jsonBean.getPrice());
                        quotedSubServiceBean.setParts(jsonBean.getParts());
                        quotedSubServiceBean.setFlatPrice(jsonBean.getFlatPrice());
                        quotedSubServiceBean.setFlatRemark(jsonBean.getFlatRemark());
                        quotedSubServiceBeanEdit.setPrice(jsonBean.getPrice());
                        quotedSubServiceBeanEdit.setParts(jsonBean.getParts());
                        quotedSubServiceBeanEdit.setFlatPrice(jsonBean.getFlatPrice());
                        quotedSubServiceBeanEdit.setFlatRemark(jsonBean.getFlatRemark());

                        break;
                    }
                }
                subServiceBeanArrayList.add(quotedSubServiceBean);
                subServiceBeanArrayListEdit.add(quotedSubServiceBeanEdit);
            }
            quotedServicesBean.setQuotedSubServiceBeans(subServiceBeanArrayList);
            quotedServicesBeanEdit.setQuotedSubServiceBeans(subServiceBeanArrayListEdit);
            quotedServicesBeanArrayList.add(quotedServicesBean);
            quotedServicesBeanArrayListForEdit.add(quotedServicesBeanEdit);
        }

        viewQuotedServiceAdapter = new ViewQuotedServiceAdapter(this, quotedServicesBeanArrayListForEdit, "blue", new AddPriceListioner() {
            @Override
            public void onClick(int servicePos, int subServicePos) {
                Log.e("POP", "servicePos: " + servicePos + " subServicePos: " + subServicePos);
                QuotedSubServiceBean quotedSubServiceBeanFrEdit = quotedServicesBeanArrayListForEdit.get(servicePos).getQuotedSubServiceBeans().get(subServicePos);
                popUpAdPrice(servicePos, subServicePos, quotedSubServiceBeanFrEdit);
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
        cpnUpLay.setVisibility(View.GONE);
        cpnVw.setVisibility(View.GONE);
        setBellowLayOutDataOwnerEnd();
        if (status.equals("7")) {
            askFrPayBtn.setVisibility(View.GONE);
        }
    }

    private void setDataConsumerEnd() {
        quotedServicesBeanArrayList = new ArrayList<>();
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
                QuotedSubServiceBean quotedSubServiceBeanFrEdit = quotedServicesBeanArrayList.get(servicePos).getQuotedSubServiceBeans().get(subServicePos);
                popUpAdPrice(servicePos, subServicePos, quotedSubServiceBeanFrEdit);
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
        setBellowLayOutDataConsumerEnd();
    }

    private void collectExtraChargesData() {
        if (serviceDetailsResponce.getData().getOrderdetails().get(0).getExtraJson().size() > 0) {
            quotedSubservices.clear();
            for (ServiceDetailsResponce.DataBean.OrderdetailsBean.ExtraJsonBean extraJsonBean : serviceDetailsResponce.getData().getOrderdetails().get(0).getExtraJson()) {
                QuotedSubservice quotedSubservice = new QuotedSubservice();
                quotedSubservice.setId(extraJsonBean.getId());
                quotedSubservice.setName(extraJsonBean.getName());
                quotedSubservice.setParts(extraJsonBean.getParts());
                quotedSubservice.setPrice(extraJsonBean.getPrice());
                quotedSubservice.setFlatPrice(extraJsonBean.getFlatPrice());
                quotedSubservice.setFlatRemark(extraJsonBean.getFlatRemark());
                quotedSubservices.add(quotedSubservice);
            }
            extraChargesJson = new Gson().toJson(quotedSubservices);
            setExtraChargesData();
        } else {
            extraLay.setVisibility(View.GONE);
        }
    }

    void popUpAdPrice(final int servicePos, final int subServicePos, final QuotedSubServiceBean quotedSubServiceBeanFrEdit) {
        final Dialog dialog = new Dialog(this);
        final QuotedSubServiceBean quotedSubServiceBean = quotedServicesBeanArrayList.get(servicePos).getQuotedSubServiceBeans().get(subServicePos);
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
        checkFlat.setClickable(false);
        final LinearLayout partLay = dialog.findViewById(R.id.partLay);
        final LinearLayout flatPriceLay = dialog.findViewById(R.id.flatPriceLay);
        subServiceTitleTxt.setText(quotedSubServiceBeanFrEdit.getName());
        popTitle.setText("Service Price");
        partEdTxt.setText(quotedSubServiceBeanFrEdit.getParts());
        laborEdTxt.setText(quotedSubServiceBeanFrEdit.getPrice());
        flatPriceEdTxt.setText(quotedSubServiceBeanFrEdit.getFlatPrice());
        if (quotedSubServiceBeanFrEdit.getFlatRemark().equals("")) {
            flatRemarkTxt.setHint("Description not available");
            partRemarkTxt.setHint("Description not available");
        } else {
            flatRemarkTxt.setText(quotedSubServiceBeanFrEdit.getFlatRemark());
            partRemarkTxt.setText(quotedSubServiceBeanFrEdit.getFlatRemark());
        }
        partEdTxt.setSelection(partEdTxt.getText().length());
        laborEdTxt.setSelection(laborEdTxt.getText().length());
        flatPriceEdTxt.setSelection(flatPriceEdTxt.getText().length());
        addPriceBtn.setText("Edit Price");
        if (userType == Constant.uerTypeConsumer) {
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
            addPriceBtn.setVisibility(View.GONE);
        }
        if (Float.parseFloat(quotedSubServiceBeanFrEdit.getFlatPrice()) > 0) {
            checkFlat.setChecked(true);
            checkFlat.setText("   Added Flat Price");
            partLay.setVisibility(View.GONE);
            flatPriceLay.setVisibility(View.VISIBLE);
        } else {
            checkFlat.setVisibility(View.GONE);
        }
        addPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFlat.isChecked()) {
                    if (isPopValidForFlatPrice(flatPriceEdTxt, flatRemarkTxt, bellowLay, quotedSubServiceBeanFrEdit, quotedSubServiceBean)) {
                        quotedSubServiceBeanFrEdit.setFlatPrice(flatPriceEdTxt.getText().toString().trim());
                        quotedSubServiceBeanFrEdit.setFlatRemark(flatRemarkTxt.getText().toString().trim());
                        viewQuotedServiceAdapter.notifyItemChanged(servicePos);
                        dialog.dismiss();
                    }
                } else {
                    if (isPopValid(partEdTxt, laborEdTxt, flatRemarkTxt, bellowLay, quotedSubServiceBeanFrEdit, quotedSubServiceBean)) {
                        quotedSubServiceBeanFrEdit.setParts(partEdTxt.getText().toString().trim());
                        quotedSubServiceBeanFrEdit.setPrice(laborEdTxt.getText().toString().trim());
                        quotedSubServiceBeanFrEdit.setFlatRemark(flatRemarkTxt.getText().toString().trim());
                        viewQuotedServiceAdapter.notifyItemChanged(servicePos);
                        dialog.dismiss();
                    }
                }
                setBellowLayOutDataOwnerEnd();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        laborEdTxt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                String str = laborEdTxt.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 4, 2);
                if (!str2.equals(str)) {
                    laborEdTxt.setText(str2);
                    int pos = laborEdTxt.getText().length();
                    laborEdTxt.setSelection(pos);
                }
            }
        });
        partEdTxt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                String str = partEdTxt.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 4, 2);
                if (!str2.equals(str)) {
                    partEdTxt.setText(str2);
                    int pos = partEdTxt.getText().length();
                    partEdTxt.setSelection(pos);
                }
            }
        });
        flatPriceEdTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = flatPriceEdTxt.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 4, 2);
                if (!str2.equals(str)) {
                    flatPriceEdTxt.setText(str2);
                    int pos = flatPriceEdTxt.getText().length();
                    flatPriceEdTxt.setSelection(pos);
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void setBellowLayOutDataOwnerEnd() {
        partPrice = 0;
        laborPrice = 0;
        flatePrice = 0;
        totalPrice = 0;
        for (QuotedServicesBean servicesBean : quotedServicesBeanArrayListForEdit) {

            for (QuotedSubServiceBean quotedSubServiceBean : servicesBean.getQuotedSubServiceBeans()) {
                partPrice = partPrice + Float.parseFloat(quotedSubServiceBean.getParts());
                laborPrice = laborPrice + Float.parseFloat(quotedSubServiceBean.getPrice());
                flatePrice = flatePrice + Float.parseFloat(quotedSubServiceBean.getFlatPrice());
            }
        }
        calculateAfterAddPriceOwnerEnd();
    }

    private void setBellowLayOutDataConsumerEnd() {
        allTotal = 0;
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
        totalPrice = partPrice + laborPrice + flatePrice;
        allTotal = total;
        laborChargeTxt.setText("$ " + Constant.DecimalFormat(laborPrice));
        partChargeTxt.setText("$ " + Constant.DecimalFormat(partPrice));
        flateChargeTxt.setText("$ " + Constant.DecimalFormat(flatePrice));
        payPrice = (allTotal - depositeAmount);
        totalTxt.setText("$ " + Constant.DecimalFormat(payPrice));
        taxLay.setVisibility(View.GONE);
    }

    private void calculateAfterAddPriceOwnerEnd() {
        allTotal = 0;
        float flatePriceCom = (flatePrice * adminComisioin) / 100;
        float partTax = (partPrice * tax) / 100;
        float laborCom = (laborPrice * adminComisioin) / 100;
        float total = partPrice + laborPrice + flatePrice + flatePriceCom + partTax;
        totalPrice = total + laborCom;
        allTotal = totalPrice;
        laborChargeTxt.setText("$ " + Constant.DecimalFormat(laborPrice + laborCom));
        partChargeTxt.setText("$ " + Constant.DecimalFormat(partPrice + partTax));
        flateChargeTxt.setText("$ " + Constant.DecimalFormat(flatePrice + flatePriceCom));
        adminComTxt.setText("" + df.format(adminComisioin) + " %");
        payPrice = (allTotal - depositeAmount);
        totalTxt.setText("$ " + Constant.DecimalFormat(payPrice));
       // totalTxt.setText("$ " + Constant.DecimalFormat(totalPrice));
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
                if (up > MAX_BEFORE_POINT) return rFinal;
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

    private boolean isPopValid(EditText partEdTxt, EditText laborEdTxt, EditText flatRemarkTxt, LinearLayout bellowLay, QuotedSubServiceBean quotedSubServiceBeanFrEdit, QuotedSubServiceBean quotedSubServiceBean) {
        Validation v = new Validation();
        if (v.isEmpty(partEdTxt)) {
            Constant.snackbar(bellowLay, "Part price can't be empty");
            partEdTxt.requestFocus();
            return false;
        } else if (Float.parseFloat(partEdTxt.getText().toString().trim()) < Float.parseFloat(quotedSubServiceBean.getParts())) {
            Constant.snackbar(bellowLay, "Part price can't less than quoted part price");
            partEdTxt.requestFocus();
            return false;
        } else if (v.isEmpty(laborEdTxt)) {
            Constant.snackbar(bellowLay, "Labor price can't be empty");
            laborEdTxt.requestFocus();
            return false;
        } else if (Float.parseFloat(laborEdTxt.getText().toString().trim()) < Float.parseFloat(quotedSubServiceBean.getPrice())) {
            Constant.snackbar(bellowLay, "Labor price can't less than quoted labor price");
            laborEdTxt.requestFocus();
            return false;
        }/* else if (v.isEmpty(flatRemarkTxt)) {
            Constant.snackbar(bellowLay, "Please add remark");
            flatRemarkTxt.requestFocus();
            return false;
        }*/
        return true;
    }

    private boolean isPopValidForFlatPrice(EditText flatPriceEdTxt, EditText flatRemarkTxt, LinearLayout bellowLay, QuotedSubServiceBean quotedSubServiceBeanFrEdit, QuotedSubServiceBean quotedSubServiceBean) {
        Validation v = new Validation();
        if (v.isEmpty(flatPriceEdTxt)) {
            Constant.snackbar(bellowLay, "Flat price can't be empty");
            flatPriceEdTxt.requestFocus();
            return false;
        } else if (Float.parseFloat(flatPriceEdTxt.getText().toString()) < Float.parseFloat(quotedSubServiceBean.getFlatPrice())) {
            Constant.snackbar(bellowLay, "Flat price can't less than quoted flat price");
            flatPriceEdTxt.requestFocus();
            return false;
        }/* else if (v.isEmpty(flatRemarkTxt)) {
            Constant.snackbar(bellowLay, "Please add remark");
            flatPriceEdTxt.requestFocus();
            return false;
        }*/
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 & resultCode == 1111) {
            //if (userType == Constant.userTypeOwner) {
            extraChargesJson = data.getStringExtra("extraChargeJson");
            quotedSubservices.clear();
            if (!extraChargesJson.equals("")) {
                QuotedSubservice[] quotedSubservice = new Gson().fromJson(extraChargesJson, QuotedSubservice[].class);
                final List<QuotedSubservice> requestServiceBeans = Arrays.asList(quotedSubservice);
                quotedSubservices.addAll(requestServiceBeans);
                setExtraChargesData();
                Log.e("ExtraCharge", extraChargesJson);
            } else {
                collectExtraChargesData();
            }
        }
        //}
    }

    private void setExtraChargesData() {
        extraPartPrice = 0;
        extraLaborPrice = 0;
        extraFlatePrice = 0;
        extraTotal = 0;
        for (QuotedSubservice servicesBean : quotedSubservices) {
            extraPartPrice = extraPartPrice + Float.parseFloat(servicesBean.getParts());
            extraLaborPrice = extraLaborPrice + Float.parseFloat(servicesBean.getPrice());
            extraFlatePrice = extraFlatePrice + Float.parseFloat(servicesBean.getFlatPrice());
        }
        calculateExtracharges();
    }

    private void calculateExtracharges() {

        float flatePriceCom = (extraFlatePrice * adminComisioin) / 100;
        float partTax = (extraPartPrice * tax) / 100;
        float laborCom = extraLaborPrice * (adminComisioin / 100);
        float total = extraPartPrice + extraLaborPrice + extraFlatePrice + flatePriceCom + partTax + laborCom;
        float editedExtraPrice = allTotal - Float.parseFloat(serviceDetailsResponce.getData().getOrderdetails().get(0).getTotalAmount());
        extraTotal = total + editedExtraPrice;
        exLaborChargeTxt.setText("$ " + Constant.DecimalFormat(extraLaborPrice + laborCom));
        exPartChargeTxt.setText("$ " + Constant.DecimalFormat(extraPartPrice + partTax));
        exFlateChargeTxt.setText("$ " + Constant.DecimalFormat(extraFlatePrice + flatePriceCom));
        allTotal = (totalPrice + (extraTotal - editedExtraPrice));
        payPrice = (allTotal - depositeAmount);
        totalTxt.setText("$ " + Constant.DecimalFormat(payPrice));
        if (quotedSubservices.size() > 0) {
            extraLay.setVisibility(View.VISIBLE);
        } else {
            extraLay.setVisibility(View.GONE);
        }
    }

    public void updateInvoice() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.updateInvoiceUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("ServiceDetails", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(ActivityAskForPayment.this, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityAskForPayment.this);
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
                            progressDialog.dismiss();
                            try {
                                Log.e("ActivityAskForPayment", error.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityAskForPayment.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", requestId);
                    params.put("myarray", myEditedJson);
                    if (extraChargesJson.equals("")) {
                        params.put("extraJson", "[]");
                        params.put("extraPrice", "0");
                    } else {
                        params.put("extraJson", extraChargesJson);
                        params.put("extraPrice", String.valueOf(extraTotal));
                    }
                    params.put("totalAmount", String.valueOf(allTotal));
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
            VolleySingleton.getInstance(ActivityAskForPayment.this).addToRequestQueue(stringRequest);
        }
    }

    private void getEditedJson() {
        ArrayList<QuotedSubservice> editedList = new ArrayList<>();
        for (QuotedServicesBean servicesBean : quotedServicesBeanArrayListForEdit) {
            for (QuotedSubServiceBean quotedSubServiceBean : servicesBean.getQuotedSubServiceBeans()) {
                QuotedSubservice quotedSubservice = new QuotedSubservice();
                quotedSubservice.setId(quotedSubServiceBean.getId());
                quotedSubservice.setName(quotedSubServiceBean.getName());
                quotedSubservice.setParts(quotedSubServiceBean.getParts());
                quotedSubservice.setPrice(quotedSubServiceBean.getPrice());
                quotedSubservice.setFlatPrice(quotedSubServiceBean.getFlatPrice());
                quotedSubservice.setFlatRemark(quotedSubServiceBean.getFlatRemark());
                float total = Float.parseFloat(quotedSubServiceBean.getParts()) + Float.parseFloat(quotedSubServiceBean.getPrice()) + Float.parseFloat(quotedSubServiceBean.getFlatPrice());

                editedList.add(quotedSubservice);
            }
        }
        myEditedJson = new Gson().toJson(editedList);
        Log.e("myEditedJson", myEditedJson);

    }

    public void checkPromoCode() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            smlProgress.setVisibility(View.VISIBLE);
            applyCupn.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.checkPromoUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            smlProgress.setVisibility(View.GONE);
                            applyCupn.setVisibility(View.VISIBLE);
                            System.out.println("#" + response);
                            Log.e("ServiceDetails", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    Constant.snackbar(mainLayout, message);
                                    CheckedPromoCodeResponce checkedPromoCodeResponce = new Gson().fromJson(response, CheckedPromoCodeResponce.class);


                                    depositeAmount = (allTotal * Float.parseFloat(checkedPromoCodeResponce.getData().getAmount()) / 100);
                                    rewardsTxt.setText("- $ " + Constant.DecimalFormat(depositeAmount));
                                    payPrice = (allTotal - depositeAmount);
                                    totalTxt.setText("$ " + Constant.DecimalFormat(payPrice));

                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityAskForPayment.this);
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
                            smlProgress.setVisibility(View.GONE);
                            applyCupn.setVisibility(View.VISIBLE);
                            try {
                                Log.e("ActivityAskForPayment", error.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityAskForPayment.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", String.valueOf(serviceDetailsResponce.getData().getOrderdetails().get(0).getUserInfo().get_id()));
                    params.put("promoCode", cupnTxt.getText().toString().trim());
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
            VolleySingleton.getInstance(ActivityAskForPayment.this).addToRequestQueue(stringRequest);
        }
    }

    void popUpPopupWindow() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_payment_window);
        dialog.getWindow().setLayout((width * 10) / 11, (height * 1) / 2);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = dialog.findViewById(R.id.popCancel);
        final TextView popTitle = dialog.findViewById(R.id.popTitle);
        final ImageView stripeImg = dialog.findViewById(R.id.stripeImg);
        final ImageView cardImg = dialog.findViewById(R.id.cardImg);
        final ImageView cashImg = dialog.findViewById(R.id.cashImg);
        final TextView stripeTxt = dialog.findViewById(R.id.stripeTxt);
        final TextView cardTxt = dialog.findViewById(R.id.cardTxt);
        final TextView CashTxt = dialog.findViewById(R.id.CashTxt);
        dialog.setCancelable(true);
        float priceToPay = 0;
        if (payPrice == 0) {
            priceToPay = allTotal;
        } else {
            priceToPay = payPrice;
        }
        final float finalPriceToPay = priceToPay;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        stripeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stripeImg.setImageResource(R.drawable.active_stripe);
                stripeTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.colorCardWindowPopup));

                cardImg.setImageResource(R.drawable.account);
                cardTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.OnlnServiceTxt));

                cashImg.setImageResource(R.drawable.cash);
                CashTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.OnlnServiceTxt));
                popUpPaymentAlart(1, finalPriceToPay);
                dialog.dismiss();
            }
        });
        cardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stripeImg.setImageResource(R.drawable.stripe);
                stripeTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.OnlnServiceTxt));

                cardImg.setImageResource(R.drawable.active_account);
                cardTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.colorCardWindowPopup));

                cashImg.setImageResource(R.drawable.cash);
                CashTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.OnlnServiceTxt));
                popUpPaymentAlart(2, finalPriceToPay);
                dialog.dismiss();
            }
        });

        cashImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stripeImg.setImageResource(R.drawable.stripe);
                stripeTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.OnlnServiceTxt));

                cardImg.setImageResource(R.drawable.account);
                cardTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.OnlnServiceTxt));

                cashImg.setImageResource(R.drawable.active_cash);
                CashTxt.setTextColor(ContextCompat.getColor(ActivityAskForPayment.this, R.color.colorCardWindowPopup));
                popUpPaymentAlart(3, finalPriceToPay);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    void popUpPaymentAlart(int mode, float price) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_payment_alart);
        dialog.getWindow().setLayout((width * 10) / 11, (height * 1) / 2);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final ImageButton cancel = dialog.findViewById(R.id.popCancel);
        final TextView popTitle = dialog.findViewById(R.id.popTitle);
        final TextView alartTxt = dialog.findViewById(R.id.alartTxt);
        final TextView priceTxt = dialog.findViewById(R.id.priceTxt);
        final Button payPopBtn = dialog.findViewById(R.id.payPopBtn);
        priceTxt.setText("$ " + Constant.DecimalFormat(price));
        switch (mode) {
            case 1:
                alartTxt.setText(Html.fromHtml("Are you sure you want to pay<br/>with Stripe?"));
                break;
            case 2:
                alartTxt.setText(Html.fromHtml("Are you sure you want to pay<br/>with Card?"));
                break;
            case 3:
                alartTxt.setText(Html.fromHtml("Are you sure you want to pay<br/>in Cash?"));
                break;
        }

        dialog.setCancelable(true);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        payPopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(ActivityAskForPayment.this, StripPaymentActivity.class);
                intent.putExtra("requestId", String.valueOf(serviceDetailsResponce.getData().get_id()));
                intent.putExtra("type", "second");
                intent.putExtra("userId", String.valueOf(serviceDetailsResponce.getData().getOrderdetails().get(0).getUserInfo().get_id()));
                startActivity(intent);
            }
        });
    }
}
