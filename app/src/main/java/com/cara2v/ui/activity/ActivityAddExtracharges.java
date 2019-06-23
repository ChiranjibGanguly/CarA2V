package com.cara2v.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.cara2v.Interface.AddPriceListioner;
import com.cara2v.R;
import com.cara2v.adapter.SendQuotedServiceAdapter;
import com.cara2v.bean.QuotedServicesBean;
import com.cara2v.bean.QuotedSubServiceBean;
import com.cara2v.bean.QuotedSubservice;
import com.cara2v.responceBean.AddServicesResponce;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.cara2v.util.Validation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityAddExtracharges extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private RelativeLayout bellowLay;
    private RelativeLayout taxComsnLay;
    private RecyclerView quotedRecycler;
    private ImageView menuBtn;
    private ImageView tabRightIcon;
    private TextView titleTxt;
    private TextView laborChargeTxt;
    private TextView partChargeTxt;
    private TextView comissionTxt;
    private TextView flateChargeTxt;
    private TextView taxTxt;

    private String requestDetailsJson = "", extraChargesJson = "";
    private String myArrayJson = "";
    private String status = "";
    private ServiceDetailsResponce serviceDetailsResponce;
    private ArrayList<QuotedServicesBean> quotedServicesBeanArrayList;
    private SendQuotedServiceAdapter sendQuotedServiceAdapter;
    private ArrayList<QuotedSubservice> quotedSubservices;
    private ArrayList<QuotedSubservice> prevQuotedSubservices = new ArrayList<>();
    private float partPrice = 0;
    private float laborPrice = 0;
    private float flatePrice = 0;
    private float tax = 0;
    private float adminComision = 0;
    private int userType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extracharges);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestDetailsJson = bundle.getString("requestDetailsJson");
            extraChargesJson = bundle.getString("extraChargesJson");
            status = bundle.getString("status");
        }
        initializeView();
        userType = PreferenceConnector.readInteger(this, PreferenceConnector.UserType, 0);
        if (userType == Constant.uerTypeConsumer) {
            tabRightIcon.setVisibility(View.GONE);
            setDataForConsumerEnd();
        } else if (userType == Constant.userTypeOwner) {
            setDataForOwnerEnd();
        }
    }


    private void initializeView() {
        mainLayout = findViewById(R.id.mainLayout);
        bellowLay = findViewById(R.id.bellowLay);
        taxComsnLay = findViewById(R.id.taxComsnLay);
        quotedRecycler = findViewById(R.id.quotedRecycler);
        menuBtn = findViewById(R.id.menuBtn);
        tabRightIcon = findViewById(R.id.tabRightIcon);
        titleTxt = findViewById(R.id.titleTxt);
        laborChargeTxt = findViewById(R.id.laborChargeTxt);
        partChargeTxt = findViewById(R.id.partChargeTxt);
        comissionTxt = findViewById(R.id.comissionTxt);
        flateChargeTxt = findViewById(R.id.flateChargeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        menuBtn.setImageResource(R.drawable.back_icon);
        tabRightIcon.setImageResource(R.drawable.ic_check_icon);
        titleTxt.setText("Add Extra Charges");
        menuBtn.setOnClickListener(this);
        tabRightIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tabRightIcon:
                if (isValidQuotetion()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("extraChargeJson", myArrayJson);
                    setResult(1111, returnIntent);
                    finish();
                }
                break;
            case R.id.menuBtn:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("extraChargeJson", "");
                setResult(1111, returnIntent);
                finish();
                break;
        }
    }


    private void setDataForConsumerEnd() {
        quotedServicesBeanArrayList = new ArrayList<>();
        serviceDetailsResponce = new Gson().fromJson(requestDetailsJson, ServiceDetailsResponce.class);
        if (!extraChargesJson.equals("")) {
            QuotedSubservice[] quotedSubservice = new Gson().fromJson(extraChargesJson, QuotedSubservice[].class);
            final List<QuotedSubservice> requestServiceBeans = Arrays.asList(quotedSubservice);
            prevQuotedSubservices.clear();
            prevQuotedSubservices.addAll(requestServiceBeans);
        }
        tax = Float.parseFloat(serviceDetailsResponce.getData().getOrderdetails().get(0).getTax());
        adminComision = Float.parseFloat(serviceDetailsResponce.getAdminDetail().getPercentage());
        AddServicesResponce addServicesResponce = new Gson().fromJson(PreferenceConnector.readString(this, PreferenceConnector.myServiceListJson, ""), AddServicesResponce.class);

        for (ServiceDetailsResponce.UpdateDataBean serviceBean : serviceDetailsResponce.getUpdateData()) {

            QuotedServicesBean quotedServicesBean = new QuotedServicesBean();
            quotedServicesBean.setId(String.valueOf(serviceBean.get_id()));
            quotedServicesBean.setName(serviceBean.getServiceName());
            quotedServicesBean.setServiceImage(serviceBean.getServiceImage());

            ArrayList<QuotedSubServiceBean> subServiceBeanArrayList = new ArrayList<>();
            for (ServiceDetailsResponce.UpdateDataBean.SubServiceBeanX subServiceBean : serviceBean.getSubService()) {
                QuotedSubServiceBean quotedSubServiceBean = new QuotedSubServiceBean();
                for (ServiceDetailsResponce.DataBean.OrderdetailsBean.ExtraJsonBean jsonBean : serviceDetailsResponce.getData().getOrderdetails().get(0).getExtraJson()) {
                    if (String.valueOf(subServiceBean.get_id()).equals(jsonBean.getId())) {
                        quotedSubServiceBean.setId(String.valueOf(subServiceBean.get_id()));
                        quotedSubServiceBean.setName(String.valueOf(subServiceBean.getSubServiceName()));

                        for (QuotedSubservice quotedSubservice : prevQuotedSubservices) {
                            if (quotedSubServiceBean.getId().equals(quotedSubservice.getId())) {
                                float flatePriceCom = (Float.parseFloat(quotedSubservice.getFlatPrice()) * adminComision) / 100;
                                float partTax = (Float.parseFloat(quotedSubservice.getParts()) * tax) / 100;
                                float laborCom = (Float.parseFloat(quotedSubservice.getPrice()) * adminComision) / 100;

                                float flatPrice = Float.parseFloat(quotedSubservice.getFlatPrice()) + flatePriceCom;
                                float partPrice = Float.parseFloat(quotedSubservice.getParts()) + partTax;
                                float price = Float.parseFloat(quotedSubservice.getPrice()) + laborCom;
                                if (price > 0) {
                                    quotedSubServiceBean.setPrice(String.valueOf(price));
                                } else {
                                    quotedSubServiceBean.setPrice("0");
                                }
                                if (partPrice > 0) {
                                    quotedSubServiceBean.setParts(String.valueOf(partPrice));
                                } else {
                                    quotedSubServiceBean.setParts("0");
                                }
                                if (flatPrice > 0) {
                                    quotedSubServiceBean.setFlatPrice(String.valueOf(flatPrice));
                                } else {
                                    quotedSubServiceBean.setFlatPrice("0");
                                }
                                quotedSubServiceBean.setFlatRemark(quotedSubservice.getFlatRemark());
                                subServiceBeanArrayList.add(quotedSubServiceBean);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            quotedServicesBean.setQuotedSubServiceBeans(subServiceBeanArrayList);
            if (quotedServicesBean.getQuotedSubServiceBeans().size() > 0) {
                quotedServicesBeanArrayList.add(quotedServicesBean);
            }

        }
        sendQuotedServiceAdapter = new SendQuotedServiceAdapter(this, quotedServicesBeanArrayList, new AddPriceListioner() {
            @Override
            public void onClick(int servicePos, int subServicePos) {
                Log.e("POP", "servicePos: " + servicePos + " subServicePos: " + subServicePos);
                popUpAdPrice(servicePos, subServicePos);
            }
        });
        quotedRecycler.setAdapter(sendQuotedServiceAdapter);
        setBellowLayOutData();
    }

    private void setDataForOwnerEnd() {
        quotedServicesBeanArrayList = new ArrayList<>();
        serviceDetailsResponce = new Gson().fromJson(requestDetailsJson, ServiceDetailsResponce.class);
        if (!extraChargesJson.equals("")) {
            QuotedSubservice[] quotedSubservice = new Gson().fromJson(extraChargesJson, QuotedSubservice[].class);
            final List<QuotedSubservice> requestServiceBeans = Arrays.asList(quotedSubservice);
            prevQuotedSubservices.clear();
            prevQuotedSubservices.addAll(requestServiceBeans);
        }
        tax = Float.parseFloat(serviceDetailsResponce.getData().getOrderdetails().get(0).getTax());
        adminComision = Float.parseFloat(serviceDetailsResponce.getAdminDetail().getPercentage());
        AddServicesResponce addServicesResponce = new Gson().fromJson(PreferenceConnector.readString(this, PreferenceConnector.myServiceListJson, ""), AddServicesResponce.class);

        for (AddServicesResponce.DataBean serviceBean : addServicesResponce.getData()) {
            if (serviceBean.getStatus().equals("1")) {
                QuotedServicesBean quotedServicesBean = new QuotedServicesBean();
                quotedServicesBean.setId(String.valueOf(serviceBean.get_id()));
                quotedServicesBean.setName(serviceBean.getServiceName());
                quotedServicesBean.setServiceImage(serviceBean.getServiceImage());

                ArrayList<QuotedSubServiceBean> subServiceBeanArrayList = new ArrayList<>();
                for (AddServicesResponce.DataBean.SubServiceBean subServiceBean : serviceBean.getSubService()) {

                        QuotedSubServiceBean quotedSubServiceBean = new QuotedSubServiceBean();
                        boolean isExist = false;
                        for (ServiceDetailsResponce.DataBean.OrderdetailsBean.RequestJsonBean jsonBean : serviceDetailsResponce.getData().getOrderdetails().get(0).getRequestJson()) {
                            if (String.valueOf(subServiceBean.get_id()).equals(jsonBean.getId())) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            quotedSubServiceBean.setId(String.valueOf(subServiceBean.get_id()));
                            quotedSubServiceBean.setName(String.valueOf(subServiceBean.getSubServiceName()));
                            quotedSubServiceBean.setPrice("0");
                            quotedSubServiceBean.setParts("0");
                            quotedSubServiceBean.setFlatPrice("0");
                            quotedSubServiceBean.setFlatRemark("");
                            if (status.equals("7")) {
                                for (QuotedSubservice quotedSubservice : prevQuotedSubservices) {
                                    if (quotedSubServiceBean.getId().equals(quotedSubservice.getId())) {
                                        quotedSubServiceBean.setPrice(quotedSubservice.getPrice());
                                        quotedSubServiceBean.setParts(quotedSubservice.getParts());
                                        quotedSubServiceBean.setFlatPrice(quotedSubservice.getFlatPrice());
                                        quotedSubServiceBean.setFlatRemark(quotedSubservice.getFlatRemark());
                                        subServiceBeanArrayList.add(quotedSubServiceBean);
                                        break;
                                    }
                                }
                            } else {
                                for (QuotedSubservice quotedSubservice : prevQuotedSubservices) {
                                    if (quotedSubServiceBean.getId().equals(quotedSubservice.getId())) {
                                        quotedSubServiceBean.setPrice(quotedSubservice.getPrice());
                                        quotedSubServiceBean.setParts(quotedSubservice.getParts());
                                        quotedSubServiceBean.setFlatPrice(quotedSubservice.getFlatPrice());
                                        quotedSubServiceBean.setFlatRemark(quotedSubservice.getFlatRemark());
                                        break;
                                    }
                                }

                                if (subServiceBean.getStatus()==1){
                                        subServiceBeanArrayList.add(quotedSubServiceBean);
                                    }

                            }
                        /*for (QuotedSubservice quotedSubservice : prevQuotedSubservices) {
                            if (quotedSubServiceBean.getId().equals(quotedSubservice.getId())) {
                                quotedSubServiceBean.setPrice(quotedSubservice.getPrice());
                                quotedSubServiceBean.setParts(quotedSubservice.getParts());
                                quotedSubServiceBean.setFlatPrice(quotedSubservice.getFlatPrice());
                                quotedSubServiceBean.setFlatRemark(quotedSubservice.getFlatRemark());
                                //subServiceBeanArrayList.add(quotedSubServiceBean);
                                break;
                            }
                        }*/
                            // subServiceBeanArrayList.add(quotedSubServiceBean);
                        }
                    }

                quotedServicesBean.setQuotedSubServiceBeans(subServiceBeanArrayList);
                if (quotedServicesBean.getQuotedSubServiceBeans().size() > 0) {
                    quotedServicesBeanArrayList.add(quotedServicesBean);
                }
            }

        }
        sendQuotedServiceAdapter = new SendQuotedServiceAdapter(this, quotedServicesBeanArrayList, new AddPriceListioner() {
            @Override
            public void onClick(int servicePos, int subServicePos) {
                Log.e("POP", "servicePos: " + servicePos + " subServicePos: " + subServicePos);
                popUpAdPrice(servicePos, subServicePos);
            }
        });
        quotedRecycler.setAdapter(sendQuotedServiceAdapter);
        setBellowLayOutData();
    }

    void popUpAdPrice(final int servicePos, final int subServicePos) {
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
        final EditText flatRemarkTxt = dialog.findViewById(R.id.flatRemarkTxt);
        final EditText partRemarkTxt = dialog.findViewById(R.id.partRemarkTxt);
        final EditText laborEdTxt = dialog.findViewById(R.id.laborEdTxt);
        final EditText flatPriceEdTxt = dialog.findViewById(R.id.flatPriceEdTxt);
        final TextView subServiceTitleTxt = dialog.findViewById(R.id.subServiceTitleTxt);
        final Button addPriceBtn = dialog.findViewById(R.id.addPriceBtn);
        final LinearLayout bellowLay = dialog.findViewById(R.id.bellowLay);
        final LinearLayout partLay = dialog.findViewById(R.id.partLay);
        final LinearLayout flatPriceLay = dialog.findViewById(R.id.flatPriceLay);
        final LinearLayout parttLay = dialog.findViewById(R.id.parttLay);
        final LinearLayout laborrLay = dialog.findViewById(R.id.laborrLay);
        final LinearLayout flattPriceLay = dialog.findViewById(R.id.flattPriceLay);
        final AppCompatCheckBox checkFlat = dialog.findViewById(R.id.checkFlat);
        subServiceTitleTxt.setText(quotedServicesBeanArrayList.get(servicePos).getQuotedSubServiceBeans().get(subServicePos).getName());
        partEdTxt.setText(quotedSubServiceBean.getParts());
        laborEdTxt.setText(quotedSubServiceBean.getPrice());
        flatPriceEdTxt.setText(quotedSubServiceBean.getFlatPrice());
        if (quotedSubServiceBean.getFlatRemark().equals("")) {
            flatRemarkTxt.setHint("Description not available");
            partRemarkTxt.setHint("Description not available");
        } else {
            flatRemarkTxt.setText(quotedSubServiceBean.getFlatRemark());
            partRemarkTxt.setText(quotedSubServiceBean.getFlatRemark());
        }
        partEdTxt.setSelection(quotedSubServiceBean.getParts().length());
        laborEdTxt.setSelection(quotedSubServiceBean.getPrice().length());
        flatPriceEdTxt.setSelection(quotedSubServiceBean.getFlatPrice().length());

        flatRemarkTxt.setSelection(quotedSubServiceBean.getFlatRemark().length());
        partRemarkTxt.setSelection(quotedSubServiceBean.getFlatRemark().length());

         if (userType == Constant.uerTypeConsumer||status.equals("7")) {
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

        if (Float.parseFloat(quotedSubServiceBean.getFlatPrice()) > 0) {
            checkFlat.setChecked(true);
            partLay.setVisibility(View.GONE);
            flatPriceLay.setVisibility(View.VISIBLE);
        }
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
        checkFlat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    partLay.setVisibility(View.GONE);
                    flatPriceLay.setVisibility(View.VISIBLE);
                    try {
                        if (Float.parseFloat(partEdTxt.getText().toString()) > 0 || Float.parseFloat(laborEdTxt.getText().toString()) > 0) {
                            flatPriceEdTxt.setText("0");
                        }
                    } catch (Exception e) {
                        flatPriceEdTxt.setText("0");
                    }
                } else {
                    flatPriceLay.setVisibility(View.GONE);
                    partLay.setVisibility(View.VISIBLE);
                    try {
                        if (Float.parseFloat(flatPriceEdTxt.getText().toString()) > 0) {
                            partEdTxt.setText("0");
                            laborEdTxt.setText("0");
                        }
                    } catch (Exception e) {
                        partEdTxt.setText("0");
                        laborEdTxt.setText("0");
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        addPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFlat.isChecked()) {
                    if (isPopValidForFlatPrice(flatPriceEdTxt, flatRemarkTxt, bellowLay)) {
                        if (Float.parseFloat(flatPriceEdTxt.getText().toString().trim()) == 0) {
                            quotedSubServiceBean.setFlatPrice("0");
                            quotedSubServiceBean.setFlatRemark("");
                        } else {
                            quotedSubServiceBean.setFlatPrice(flatPriceEdTxt.getText().toString().trim());
                            quotedSubServiceBean.setFlatRemark(flatRemarkTxt.getText().toString().trim());
                        }

                        quotedSubServiceBean.setPrice("0");
                        quotedSubServiceBean.setParts("0");
                        sendQuotedServiceAdapter.notifyDataSetChanged();
                        setBellowLayOutData();
                        try {
                            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        sendQuotedServiceAdapter.notifyItemChanged(servicePos);
                    }

                } else {
                    if (isPopValid(partEdTxt, laborEdTxt, bellowLay)) {
                        if (Float.parseFloat(partEdTxt.getText().toString().trim()) == 0) {
                            quotedSubServiceBean.setParts("0");
                        } else {
                            quotedSubServiceBean.setParts(partEdTxt.getText().toString().trim());
                        }
                        if (Float.parseFloat(laborEdTxt.getText().toString().trim()) == 0) {
                            quotedSubServiceBean.setPrice("0");
                        } else {
                            quotedSubServiceBean.setPrice(laborEdTxt.getText().toString().trim());
                        }
                        quotedSubServiceBean.setFlatPrice("0");
                        if (Float.parseFloat(partEdTxt.getText().toString().trim()) == 0 && Float.parseFloat(laborEdTxt.getText().toString().trim()) == 0) {
                            quotedSubServiceBean.setFlatRemark("");
                        } else {
                            quotedSubServiceBean.setFlatRemark(partRemarkTxt.getText().toString().trim());
                        }
                        sendQuotedServiceAdapter.notifyDataSetChanged();
                        setBellowLayOutData();
                        try {
                            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        sendQuotedServiceAdapter.notifyItemChanged(servicePos);
                    }
                }

            }

        });

        parttLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (partEdTxt.getText().toString().equals("0")) {
                    partEdTxt.setText("");
                }
                partEdTxt.requestFocus();
            }
        });
        laborrLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (laborEdTxt.getText().toString().equals("0")) {
                    laborEdTxt.setText("");
                }
                laborEdTxt.requestFocus();
            }
        });
        flattPriceLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flatPriceEdTxt.getText().toString().equals("0")) {
                    flatPriceEdTxt.setText("");
                }
                flatPriceEdTxt.requestFocus();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private boolean isPopValid(EditText partEdTxt, EditText laborEdTxt, LinearLayout bellowLay) {
        Validation v = new Validation();
        if (v.isEmpty(partEdTxt)) {
            Constant.snackbar(bellowLay, "Part price can't be empty");
            partEdTxt.requestFocus();
            return false;
        } else if (Float.parseFloat(partEdTxt.getText().toString().trim()) == 0) {
            Constant.snackbar(bellowLay, "Part price can't be zero");
            partEdTxt.requestFocus();
            return false;
        } else if (v.isEmpty(laborEdTxt)) {
            Constant.snackbar(bellowLay, "Labor price can't be empty");
            laborEdTxt.requestFocus();
            return false;
        } else if (Float.parseFloat(laborEdTxt.getText().toString().trim()) == 0) {
            Constant.snackbar(bellowLay, "Labor price can't be zero");
            laborEdTxt.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPopValidForFlatPrice(EditText flatPriceEdTxt, EditText flatRemarkTxt, LinearLayout bellowLay) {
        Validation v = new Validation();
        if (v.isEmpty(flatPriceEdTxt)) {
            Constant.snackbar(bellowLay, "Flat price can't be empty");
            flatPriceEdTxt.requestFocus();
            return false;
        } else if (Float.parseFloat(flatPriceEdTxt.getText().toString()) == 0) {
            Constant.snackbar(bellowLay, "Flat price can't be zero");
            flatPriceEdTxt.requestFocus();
            return false;
        }/* else if (v.isEmpty(flatRemarkTxt)) {
            Constant.snackbar(bellowLay, "Please add remark");
            flatPriceEdTxt.requestFocus();
            return false;
        }*/
        return true;
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

    private void setBellowLayOutData() {
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
        if (userType == Constant.uerTypeConsumer) {
            calculateAfterAddPriceUserEnd();
        } else if (userType == Constant.userTypeOwner) {
            calculateAfterAddPriceOwnerEnd();
        }
    }

    private void calculateAfterAddPriceOwnerEnd() {
        float flatePriceCom = (flatePrice * adminComision) / 100;
        float partTax = (partPrice * tax) / 100;
        float total = partPrice + laborPrice + flatePrice + flatePriceCom + partTax;
        float laborCom = laborPrice * (adminComision / 100);
        // totalPrice = total + laborCom;

        laborChargeTxt.setText("$ " + Constant.DecimalFormat(laborPrice + laborCom));
        partChargeTxt.setText("$ " + Constant.DecimalFormat(partPrice + partTax));
        flateChargeTxt.setText("$ " + Constant.DecimalFormat(flatePrice + flatePriceCom));
        comissionTxt.setText("" + Constant.DecimalFormat(adminComision) + " %");
        taxTxt.setText("" + Constant.DecimalFormat(tax) + " %");
        //  laborChargeTxt.setText("$ " + df.format(laborPrice));
        // totalTxt.setText("$ " + Constant.DecimalFormat(totalPrice));
        if (total > 0) {
            bellowLay.setVisibility(View.VISIBLE);
            tabRightIcon.setVisibility(View.VISIBLE);
            //  calculateAfterAddPriceOwnerEnd();
        } else {
            bellowLay.setVisibility(View.GONE);
            tabRightIcon.setVisibility(View.GONE);
            //  calculateAfterAddPriceOwnerEnd();
        }

        if (status.equals("7")){
            tabRightIcon.setVisibility(View.GONE);
        }


       /* partChargeTxt.setText("$ " + df.format(partPrice));
        laborChargeTxt.setText("$ " + df.format(laborPrice));
        float tx = 0;
        if (!taxTxt.getText().toString().equals("")) {
            tx = Float.parseFloat(taxTxt.getText().toString());
        }
        Float laborTax = (laborPrice * tx) / 100;
        totalPrice = partPrice + laborPrice + laborTax;
        laborChargeTxt.setText("$ " + df.format(laborPrice + laborTax));
        totalTxt.setText("$ " + df.format(totalPrice));*/
    }

    private void calculateAfterAddPriceUserEnd() {
       /* float flatePriceCom = (flatePrice * adminComision) / 100;
        float partTax = (partPrice * tax) / 100;
        float total = partPrice + laborPrice + flatePrice + flatePriceCom + partTax;
        float laborCom = laborPrice * (adminComision / 100);*/
        // totalPrice = total + laborCom;
        float total = partPrice + laborPrice + flatePrice;
        laborChargeTxt.setText("$ " + Constant.DecimalFormat(laborPrice /*+ laborCom*/));
        partChargeTxt.setText("$ " + Constant.DecimalFormat(partPrice /*+ partTax*/));
        flateChargeTxt.setText("$ " + Constant.DecimalFormat(flatePrice /*+ flatePriceCom*/));
        comissionTxt.setText("" + Constant.DecimalFormat(adminComision) + " %");
        taxTxt.setText("" + Constant.DecimalFormat(tax) + " %");
        //  laborChargeTxt.setText("$ " + df.format(laborPrice));
        // totalTxt.setText("$ " + Constant.DecimalFormat(totalPrice));
        if (total > 0) {
            bellowLay.setVisibility(View.VISIBLE);
            tabRightIcon.setVisibility(View.VISIBLE);
            //  calculateAfterAddPriceOwnerEnd();
        } else {
            bellowLay.setVisibility(View.GONE);
            tabRightIcon.setVisibility(View.GONE);
            //  calculateAfterAddPriceOwnerEnd();
        }
        taxComsnLay.setVisibility(View.GONE);
        tabRightIcon.setVisibility(View.GONE);

       /* partChargeTxt.setText("$ " + df.format(partPrice));
        laborChargeTxt.setText("$ " + df.format(laborPrice));
        float tx = 0;
        if (!taxTxt.getText().toString().equals("")) {
            tx = Float.parseFloat(taxTxt.getText().toString());
        }
        Float laborTax = (laborPrice * tx) / 100;
        totalPrice = partPrice + laborPrice + laborTax;
        laborChargeTxt.setText("$ " + df.format(laborPrice + laborTax));
        totalTxt.setText("$ " + df.format(totalPrice));*/
    }

    private boolean isValidQuotetion() {
        myArrayJson = "";
        quotedSubservices = new ArrayList<>();
        for (QuotedServicesBean servicesBean : quotedServicesBeanArrayList) {
            for (QuotedSubServiceBean quotedSubServiceBean : servicesBean.getQuotedSubServiceBeans()) {
                QuotedSubservice quotedSubservice = new QuotedSubservice();
                quotedSubservice.setId(quotedSubServiceBean.getId());
                quotedSubservice.setName(quotedSubServiceBean.getName());
                quotedSubservice.setParts(quotedSubServiceBean.getParts());
                quotedSubservice.setPrice(quotedSubServiceBean.getPrice());
                quotedSubservice.setFlatPrice(quotedSubServiceBean.getFlatPrice());
                quotedSubservice.setFlatRemark(quotedSubServiceBean.getFlatRemark());
                float total = Float.parseFloat(quotedSubServiceBean.getParts()) + Float.parseFloat(quotedSubServiceBean.getPrice()) + Float.parseFloat(quotedSubServiceBean.getFlatPrice());
                if (total > 0) {
                    quotedSubservices.add(quotedSubservice);
                }
            }
        }
        myArrayJson = new Gson().toJson(quotedSubservices);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("extraChargeJson", "");
        setResult(1111, returnIntent);
        finish();
    }
}
