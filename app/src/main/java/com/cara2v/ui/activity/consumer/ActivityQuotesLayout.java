package com.cara2v.ui.activity.consumer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.adapter.QuotationRequestAdapter;
import com.cara2v.chat.ChatActivity;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.ui.activity.ActivityServiceQuote;
import com.cara2v.ui.activity.owner.ActivityCreateQuote;
import com.cara2v.util.Constant;
import com.cara2v.util.MyApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityQuotesLayout extends Activity implements View.OnClickListener {

    private RelativeLayout mainLayout;
    private LinearLayout visibleLayout;
    private RecyclerView quotesRecycler;
    private TextView titleTxt, noDataTxt;
    private ImageView menuBtn, tabRightIcon;
    private ProgressBar progressBar;

    private String requestDetailsJson = "", requestId = "";
    private Gson gson = new Gson();

    private QuotationRequestAdapter quotationRequestAdapter;
    private List<ServiceDetailsResponce.DataBean.OrderdetailsBean> orderdetailsBeanList;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes_layout);
        MyApplication.addActivity(ActivityQuotesLayout.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestDetailsJson = bundle.getString("requestDetailsJson");
        }
        initializeView();

        if (!requestDetailsJson.equals("")) {
            setData();
        }

    }

    private void initializeView() {
        mainLayout = findViewById(R.id.mainLayout);
        noDataTxt = findViewById(R.id.noDataTxt);
        progressBar = findViewById(R.id.progressBar);
        quotesRecycler = findViewById(R.id.quotesRecycler);
        menuBtn = findViewById(R.id.menuBtn);
        tabRightIcon = findViewById(R.id.tabRightIcon);
        titleTxt = findViewById(R.id.titleTxt);

        menuBtn.setImageResource(R.drawable.back_icon);
        tabRightIcon.setImageResource(R.drawable.filter);
        titleTxt.setText("Quotes");
        menuBtn.setOnClickListener(this);
        tabRightIcon.setVisibility(View.VISIBLE);
        tabRightIcon.setOnClickListener(this);
        popUpAdPrice();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuBtn:
                finish();
                break;
            case R.id.tabRightIcon:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(tabRightIcon, 150, 0);
                }
                break;
        }
    }

    private void setData() {
        ServiceDetailsResponce serviceDetailsResponce = gson.fromJson(requestDetailsJson, ServiceDetailsResponce.class);
        requestId = String.valueOf(serviceDetailsResponce.getData().get_id());
        orderdetailsBeanList = new ArrayList<>();
        orderdetailsBeanList.addAll(serviceDetailsResponce.getData().getOrderdetails());
        quotationRequestAdapter = new QuotationRequestAdapter(ActivityQuotesLayout.this, orderdetailsBeanList, new RemoveImageListioner() {
            @Override
            public void onClick(int position) {
                ServiceDetailsResponce.DataBean.OrderdetailsBean bean = orderdetailsBeanList.get(position);
                String jsonBean = gson.toJson(bean);
                Intent intent = new Intent(ActivityQuotesLayout.this, ActivityServiceQuote.class);
                intent.putExtra("requestDetailsJson", requestDetailsJson);
                //  intent.putExtra("position", position);
                intent.putExtra("orderBean", jsonBean);
                startActivity(intent);
            }
        }, new ServicesAddRemoveListioner() {
            @Override
            public void onClick(int position) {
                ServiceDetailsResponce.DataBean.OrderdetailsBean bean = orderdetailsBeanList.get(position);
                Intent intent = new Intent(ActivityQuotesLayout.this, ChatActivity.class);
                intent.putExtra(Constant.FROM, "");
                intent.putExtra(Constant.rcvUId, String.valueOf(bean.getUserInfo().get_id()));
                intent.putExtra(Constant.rcvName, bean.getUserInfo().getUserName());
                intent.putExtra(Constant.rcvFireBaseToken, bean.getUserInfo().getDeviceToken());
                intent.putExtra(Constant.rcvPrflImg, bean.getUserInfo().getProfileImage());
                intent.putExtra(Constant.requestId, requestId);
                intent.putExtra(Constant.requestType, Constant.reqPending);
                startActivity(intent);
            }
        });
        quotesRecycler.setAdapter(quotationRequestAdapter);
    }

    void popUpAdPrice() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.pop_up_filter, null);
        popupWindow = new PopupWindow(customView,
                (int) getResources().getDimension(R.dimen._109sdp),
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        TextView popPrice = customView.findViewById(R.id.popPrice);
        TextView popMiles = customView.findViewById(R.id.popMiles);
        TextView popReview = customView.findViewById(R.id.popReview);

        // Set a click listener for the popup window close button
        popPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                shortListByTotalPrice();
            }
        });
        popMiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                popupWindow.dismiss();
                shortListByDistance();
            }
        });
        popReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shortListByReview();
                popupWindow.dismiss();
                shortListByTotalPrice();
            }
        });
    }

    private void shortListByTotalPrice() {
        Collections.sort(orderdetailsBeanList, new Comparator<ServiceDetailsResponce.DataBean.OrderdetailsBean>() {
            @Override
            public int compare(ServiceDetailsResponce.DataBean.OrderdetailsBean a1, ServiceDetailsResponce.DataBean.OrderdetailsBean a2) {
                int ret = 0;
                if (a1.getTotalAmount() == null || a2.getTotalAmount() == null) return -1;
                else {
                    float total1 = Float.parseFloat(String.valueOf(a1.getTotalAmount()));
                    float total2 = Float.parseFloat(String.valueOf(a2.getTotalAmount()));
                    if (total1 > total2) {
                        ret = 1;

                    } else if (total1 < total2) {
                        ret = -1;
                    } else if (total1 == total2) {
                        ret = 0;
                    }
                }
                return ret;
            }
        });
        quotationRequestAdapter.notifyDataSetChanged();
    }

    private void shortListByDistance() {
        Collections.sort(orderdetailsBeanList, new Comparator<ServiceDetailsResponce.DataBean.OrderdetailsBean>() {
            @Override
            public int compare(ServiceDetailsResponce.DataBean.OrderdetailsBean a1, ServiceDetailsResponce.DataBean.OrderdetailsBean a2) {
                int ret = 0;
                if (a1.getDistance() == null || a2.getDistance() == null) return -1;
                else {
                    double distance1 = Double.parseDouble(String.valueOf(a1.getDistance()));
                    double distance2 = Double.parseDouble(String.valueOf(a2.getDistance()));
                    if (distance1 > distance2) {
                        ret = 1;

                    } else if (distance1 < distance2) {
                        ret = -1;
                    } else if (distance1 == distance2) {
                        ret = 0;
                    }
                }
                return ret;
            }
        });
        quotationRequestAdapter.notifyDataSetChanged();
    }

    private void shortListByReview() {
        Collections.sort(orderdetailsBeanList, new Comparator<ServiceDetailsResponce.DataBean.OrderdetailsBean>() {
            @Override
            public int compare(ServiceDetailsResponce.DataBean.OrderdetailsBean a1, ServiceDetailsResponce.DataBean.OrderdetailsBean a2) {
                int ret = 0;
                if (a1.getTotalAmount() == null || a2.getTotalAmount() == null) return -1;
                else {
                    float total1 = Float.parseFloat(String.valueOf(a1.getUserInfo().getRating()));
                    float total2 = Float.parseFloat(String.valueOf(a2.getUserInfo().getRating()));
                    if (total1 > total2) {
                        ret = 1;

                    } else if (total1 < total2) {
                        ret = -1;
                    } else if (total1 == total2) {
                        ret = 0;
                    }
                }
                return ret;
            }
        });
        quotationRequestAdapter.notifyDataSetChanged();
    }
}
