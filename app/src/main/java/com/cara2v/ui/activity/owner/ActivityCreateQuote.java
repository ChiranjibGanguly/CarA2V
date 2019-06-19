package com.cara2v.ui.activity.owner;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cara2v.Interface.AddPriceListioner;
import com.cara2v.R;
import com.cara2v.adapter.CuponAdapter;
import com.cara2v.adapter.SendQuotedServiceAdapter;
import com.cara2v.bean.QuotedServicesBean;
import com.cara2v.bean.QuotedSubServiceBean;
import com.cara2v.bean.QuotedSubservice;
import com.cara2v.responceBean.ServiceDetailsResponce;
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
import java.util.HashMap;
import java.util.Map;

public class ActivityCreateQuote extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private RecyclerView quotedRecycler;
    ///  private RecyclerView cuponRecycler;
    //private LinearLayout cpnUpLay;
    private ImageView cupnArwImg;
    //  private LinearLayout innerLay;
    private RelativeLayout bellowLay;
    private TextView partChargeTxt;
    private TextView laborChargeTxt;
    private TextView flateChargeTxt;
    private TextView comissionTxt;
    private TextView tax;
    private TextView txt;
    private TextView rewardsTxt;
    private TextView totalTxt;
    private EditText depositeTxt;
    private EditText taxTxt;
    private TextView pay;
    private RadioGroup payRdGr;
    private RadioButton rdBtnDeposite;
    private RadioButton rdBtnAfter;
    private LinearLayout payLay;
    private LinearLayout taxLay;
    private View topView;

    private ImageView menuBtn;
    private TextView titleTxt;
    private String requestDetailsJson = "", requestId = "", myArrayJson = "";
    private Gson gson = new Gson();
    private int width = 0, height = 0;

    ///
    float partPrice = 0;
    float laborPrice = 0;
    float flatePrice = 0;
    float totalPrice = 0;
    float depositePrice = 0;
    float sentDepositePrice = 0;
    float quotedTax = 0;
    float adminComision = 0;

    private DecimalFormat df = new DecimalFormat("#.##");

    private ArrayList<QuotedServicesBean> quotedServicesBeanArrayList;
    private ArrayList<QuotedSubservice> quotedSubservices;
    private SendQuotedServiceAdapter sendQuotedServiceAdapter;
    //private CuponAdapter cuponAdapter;
    private ProgressDialog progressDialog;
    private boolean isdepositeChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quote);
        MyApplication.addActivity(ActivityCreateQuote.this);
        Bundle bundle = getIntent().getExtras();
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
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        mainLayout = findViewById(R.id.mainLayout);
        quotedRecycler = findViewById(R.id.quotedRecycler);
        //  cuponRecycler = findViewById(R.id.cuponRecycler);
        //cpnUpLay = findViewById(R.id.cpnUpLay);
        //  cupnArwImg = findViewById(R.id.cupnArwImg);
        //   innerLay = findViewById(R.id.innerLay);
        bellowLay = findViewById(R.id.bellowLay);
        partChargeTxt = findViewById(R.id.partChargeTxt);
        laborChargeTxt = findViewById(R.id.laborChargeTxt);
        flateChargeTxt = findViewById(R.id.flateChargeTxt);
        comissionTxt = findViewById(R.id.comissionTxt);
        tax = findViewById(R.id.tax);
        txt = findViewById(R.id.txt);
        rewardsTxt = findViewById(R.id.rewardsTxt);
        totalTxt = findViewById(R.id.totalTxt);
        taxLay = findViewById(R.id.taxLay);
        pay = findViewById(R.id.pay);
        payRdGr = findViewById(R.id.payRdGr);
        rdBtnDeposite = findViewById(R.id.rdBtnDeposite);
        rdBtnAfter = findViewById(R.id.rdBtnAfter);
        payLay = findViewById(R.id.payLay);
        taxTxt = findViewById(R.id.taxTxt);
        depositeTxt = findViewById(R.id.depositeTxt);
        topView = findViewById(R.id.topView);

        menuBtn = findViewById(R.id.menuBtn);
        titleTxt = findViewById(R.id.titleTxt);

        menuBtn.setImageResource(R.drawable.back_icon);
        titleTxt.setText("Create Quote");
        menuBtn.setOnClickListener(this);
        //cpnUpLay.setOnClickListener(this);

        findViewById(R.id.sendQuoteBtn).setOnClickListener(this);
        /*taxLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (taxTxt.getText().toString().equals("0")) {
                    taxTxt.setText("");
                    taxTxt.requestFocus();
                }
            }
        });*/
        taxTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*if (taxTxt.getText().toString().equals("0")) {
                    taxTxt.setText("");
                    taxTxt.requestFocus();
                }*/
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = taxTxt.getText().toString();
                if (str.isEmpty()) {
                    calculateAfterAddPrice();
                    return;
                }
                String str2 = PerfectDecimal(str, 2, 2);

                if (!str2.equals(str)) {
                    taxTxt.setText(str2);
                    int pos = taxTxt.getText().length();
                    taxTxt.setSelection(pos);
                }
                calculateAfterAddPrice();
            }
        });
        payRdGr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rdBtnDeposite.getId()) {
                    payLay.setVisibility(View.VISIBLE);
                    // depositeTxt.requestFocus();
                    if (TextUtils.isEmpty(depositeTxt.getText().toString())) {
                        sentDepositePrice = 0;
                        depositeTxt.setText("0");
                    } else {
                        sentDepositePrice = (Float.parseFloat(depositeTxt.getText().toString()));
                    }
                    //depositeTxt.setText(Constant.DecimalFormat(sentDepositePrice));
                    isdepositeChecked = true;
                } else if (i == rdBtnAfter.getId()) {
                    payLay.setVisibility(View.GONE);
                    sentDepositePrice = 0;
                    // depositeTxt.setText("");
                    isdepositeChecked = false;
                }

            }
        });
        depositeTxt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                String str = depositeTxt.getText().toString();
                if (str.isEmpty()) {
                    sentDepositePrice = 0;
                    return;
                }
                String str2 = PerfectDecimal(str, 4, 2);
                if (!str2.equals(str)) {
                    depositeTxt.setText(str2);
                    int pos = depositeTxt.getText().length();
                    depositeTxt.setSelection(pos);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendQuoteBtn:
                if (depositeTxt.getText().toString().equals("")) {
                    sentDepositePrice = 0;
                } else {
                    sentDepositePrice = Float.parseFloat(depositeTxt.getText().toString());
                }
                if (taxTxt.getText().toString().equals("")) {
                    quotedTax = 0;
                } else {
                    quotedTax = Float.parseFloat(taxTxt.getText().toString());
                }

                if (sentDepositePrice > totalPrice) {
                    Constant.snackbar(mainLayout, "Deposit price should be less or equal to total amount");
                } else if (sentDepositePrice > depositePrice) {
                    Constant.snackbar(mainLayout, "Deposit price should be less or equal to " + depositePrice+" $");
                } else if (taxTxt.getText().toString().equals("")) {
                    Constant.snackbar(mainLayout, "Please enter tax amount");
                } else if (quotedTax < 6) {
                    Constant.snackbar(mainLayout, "Tax amount should be more then 5%");
                } else {
                    if (isValidQuotetion()) {
                        sendRequestQuote();
                    } else {
                        Constant.snackbar(mainLayout, "Please add all services amount");
                    }
                }
                break;
            case R.id.menuBtn:
                finish();
                break;
            /*case R.id.cpnUpLay:
                if (innerLay.getVisibility() == View.VISIBLE) {
                    innerLay.setVisibility(View.GONE);
                    cupnArwImg.setImageResource(R.drawable.down);
                } else {
                    innerLay.setVisibility(View.VISIBLE);
                    cupnArwImg.setImageResource(R.drawable.up);
                }
                break;*/
        }
    }

    private void setData() {
        quotedServicesBeanArrayList = new ArrayList<>();
        ServiceDetailsResponce serviceDetailsResponce = gson.fromJson(requestDetailsJson, ServiceDetailsResponce.class);
        requestId = "" + serviceDetailsResponce.getData().get_id();
        try {
            adminComision = Float.parseFloat(serviceDetailsResponce.getAdminDetail().getPercentage());
        } catch (Exception e) {
            e.printStackTrace();
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
                quotedSubServiceBean.setParts("0");
                quotedSubServiceBean.setPrice("0");
                quotedSubServiceBean.setFlatPrice("0");
                quotedSubServiceBean.setFlatRemark("");
                subServiceBeanArrayList.add(quotedSubServiceBean);
            }
            quotedServicesBean.setQuotedSubServiceBeans(subServiceBeanArrayList);
            quotedServicesBeanArrayList.add(quotedServicesBean);
        }
        sendQuotedServiceAdapter = new SendQuotedServiceAdapter(this, quotedServicesBeanArrayList, new AddPriceListioner() {
            @Override
            public void onClick(int servicePos, int subServicePos) {
                Log.e("POP", "servicePos: " + servicePos + " subServicePos: " + subServicePos);
                popUpAdPrice(servicePos, subServicePos);
            }
        });
        quotedRecycler.setAdapter(sendQuotedServiceAdapter);
       /* if (serviceDetailsResponce.getMyPromoCode().size() > 0) {
            cuponRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            cuponAdapter = new CuponAdapter(this, serviceDetailsResponce.getMyPromoCode());
            cuponRecycler.setAdapter(cuponAdapter);
        } else {
            cpnUpLay.setVisibility(View.GONE);
        }*/

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
        flatRemarkTxt.setText(quotedSubServiceBean.getFlatRemark());
        partRemarkTxt.setText(quotedSubServiceBean.getFlatRemark());
        partEdTxt.setSelection(quotedSubServiceBean.getParts().length());
        laborEdTxt.setSelection(quotedSubServiceBean.getPrice().length());
        flatPriceEdTxt.setSelection(quotedSubServiceBean.getFlatPrice().length());
        flatRemarkTxt.setSelection(quotedSubServiceBean.getFlatRemark().length());
        partRemarkTxt.setSelection(quotedSubServiceBean.getFlatRemark().length());

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
        calculateAfterAddPrice();
    }

    private void calculateAfterAddPrice() {
        float tx = 0;
        if (!taxTxt.getText().toString().equals("")) {
            tx = Float.parseFloat(taxTxt.getText().toString());
        }
        float flatePriceCom = (flatePrice * adminComision) / 100;
        float partTax = (partPrice * tx) / 100;
        float total = partPrice + laborPrice + flatePrice + flatePriceCom + partTax;
        float laborCom = laborPrice * (adminComision / 100);
        totalPrice = total + laborCom;

        laborChargeTxt.setText("$ " + Constant.DecimalFormat(laborPrice + laborCom));
        partChargeTxt.setText("$ " + Constant.DecimalFormat(partPrice + partTax));
        flateChargeTxt.setText("$ " + Constant.DecimalFormat(flatePrice + flatePriceCom));
        comissionTxt.setText("" + Constant.DecimalFormat(adminComision) + " %");
        //  laborChargeTxt.setText("$ " + df.format(laborPrice));
        totalTxt.setText("$ " + Constant.DecimalFormat(totalPrice));
        if (total > 0) {
            bellowLay.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);

            // deposite Ammount
            depositePrice = (totalPrice * Constant.ADMIN_DEPOSITE) / 100;
            /*sentDepositePrice = (totalPrice * Float.parseFloat(depositeTxt.getText().toString())) / 100;
           if (sentDepositePrice==0){
               depositeTxt.setText(Constant.DecimalFormat(depositePrice));
               sentDepositePrice=depositePrice;
           }else{
               depositeTxt.setText(Constant.DecimalFormat(sentDepositePrice));
            }
           if (isdepositeChecked) {
                sentDepositePrice = (totalPrice * Float.parseFloat(depositeTxt.getText().toString())) / 100;
               // depositeTxt.setText(Constant.DecimalFormat(sentDepositePrice));
            } else {
              //  depositePrice=0;
                sentDepositePrice=0;
              //  depositeTxt.setText("");
            }*/
        } else {
            bellowLay.setVisibility(View.GONE);
            topView.setVisibility(View.GONE);
            //  calculateAfterAddPrice();
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

    private void sendRequestQuote() {
        if (Constant.isNetworkAvailable(this, mainLayout)) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.sendRequestQuoteUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("#" + response);
                            Log.e("BRAND", "onResponse: " + response);
                            JSONObject result = null;
                            try {
                                result = new JSONObject(response);
                                String status = result.getString("status");
                                String message = result.getString("message");
                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(ActivityCreateQuote.this, message, Toast.LENGTH_LONG);
                                    MyApplication.finishAllActivity();
                                } else if (status.equalsIgnoreCase("authFail")) {
                                    Constant.showLogOutDialog(ActivityCreateQuote.this);
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
                            Constant.errorHandle(error, ActivityCreateQuote.this);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("authToken", PreferenceConnector.readString(ActivityCreateQuote.this, PreferenceConnector.AuthToken, ""));
                    return header;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("requestId", requestId);
                    params.put("myarray", myArrayJson);
                    params.put("disCode", "");
                    params.put("disAmount", "" + 0);
                    params.put("tax", "" + quotedTax);
                    params.put("depositPrice", "" + sentDepositePrice);
                    params.put("totalAmount", "" + totalPrice);
                    params.put("commission", "" + adminComision);
                    String string = new JSONObject(params).toString();
                    return string.getBytes();
                }

                /*requestId:1
                myarray:[{"id":"10","name":"AMIT","parts":"50","price":"12"},{"id":"11","name":"AKASH","parts":"50","price":"12"},{"id":"15","name":"tesst","parts":"50","price":"12"}]
                disCode:SUV15K
                disAmount:12
                tax:10
                depositPrice:50
                totalAmount:500*/

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=" + getParamsEncoding();
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(ActivityCreateQuote.this).addToRequestQueue(stringRequest);
        }
    }

    private boolean isValidQuotetion() {
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
                if (total == 0) {
                    return false;
                }
                quotedSubservices.add(quotedSubservice);
            }
        }
        myArrayJson = new Gson().toJson(quotedSubservices);
        Log.e("myArrayJson", myArrayJson);
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
}
