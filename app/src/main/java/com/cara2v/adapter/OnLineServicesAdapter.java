package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cara2v.Interface.OnDeleteListioner;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.responceBean.AddServicesResponce;
import com.cara2v.responceBean.MyServiceRequestResponce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class OnLineServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MyServiceRequestResponce.DataBean> requestArrayList;
    private ServicesAddRemoveListioner servicesAddRemoveListioner;
    private RemoveImageListioner removeImageListioner;
    private OnDeleteListioner onDeleteListioner;
    private int userType = 0;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public OnLineServicesAdapter(Context context, ArrayList<MyServiceRequestResponce.DataBean> requestArrayList, ServicesAddRemoveListioner servicesAddRemoveListioner, RemoveImageListioner removeImageListioner, OnDeleteListioner onDeleteListioner) {
        this.context = context;
        this.requestArrayList = requestArrayList;
        this.servicesAddRemoveListioner = servicesAddRemoveListioner;
        this.removeImageListioner = removeImageListioner;
        this.onDeleteListioner = onDeleteListioner;
        userType = PreferenceConnector.readInteger(context, PreferenceConnector
                .UserType, 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_online_sevices_jobs, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OnLineServicesAdapter.MyViewHolder h = (OnLineServicesAdapter.MyViewHolder) holder;
        MyServiceRequestResponce.DataBean dataBean = requestArrayList.get(h.getAdapterPosition());
        String brandName = dataBean.getCar().getCarName().substring(0, 1).toUpperCase() + dataBean.getCar().getCarName().substring(1);
        String modelName = dataBean.getCar().getModelName().substring(0, 1).toUpperCase() + dataBean.getCar().getModelName().substring(1);
        h.carName.setText(dataBean.getCar().getCarYear() + "  " + brandName + "  " + modelName);
        h.serviceName.setText(dataBean.getService().get(0).getName());
        h.serviceList.setAdapter(new OnLineSubServiceAdapter(context, dataBean.getService().get(0).getSubService()));
        viewBinderHelper.bind(h.swipeRevealLayout, String.valueOf(dataBean.get_id()));
        if (userType == Constant.uerTypeConsumer) {
            if (dataBean.getUsersRequest().equals("")) {
                h.itemQuoteTxt.setText("No Quote");
            } else if (dataBean.getUsersRequest().equals("1")) {
                h.swipeRevealLayout.setLockDrag(true);
                h.itemQuoteTxt.setText(dataBean.getUsersRequest() + " Quote");
            } else {
                h.swipeRevealLayout.setLockDrag(true);
                h.itemQuoteTxt.setText(dataBean.getUsersRequest() + " Quotes");
            }
        } else if (userType == Constant.userTypeOwner) {
            if (dataBean.getStatus() == 1 && dataBean.getMyRequest() == 1) {
                h.itemQuoteTxt.setText("Your request is pending");
            } else if (dataBean.getStatus() == 1 && dataBean.getMyRequest() == 0) {
                h.itemQuoteTxt.setText("Create Quote");
            } else if (dataBean.getStatus() == 2) {
                h.itemQuoteTxt.setText("Request Accepted");
            }
            h.opnRqTxt.setText(Constant.dateTimeFormateChangeForOnlnList(dataBean.getDateAndTime()) + " " + dataBean.getTimeSlot());
            h.swipeRevealLayout.setLockDrag(true);
        }
        setListViewHeightBasedOnChildren(h.serviceList);
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView carName;
        private TextView opnRqTxt;
        private TextView serviceName;
        private TextView itemQuoteTxt;
        private ListView serviceList;
        private ImageView itemEditBtn;
        private ImageView itemDeleteBtn;

        private SwipeRevealLayout swipeRevealLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.carName);
            opnRqTxt = itemView.findViewById(R.id.opnRqTxt);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceList = itemView.findViewById(R.id.serviceList);
            itemQuoteTxt = itemView.findViewById(R.id.itemQuoteTxt);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            itemEditBtn = itemView.findViewById(R.id.itemEditBtn);
            itemDeleteBtn = itemView.findViewById(R.id.itemDeleteBtn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    servicesAddRemoveListioner.onClick(getAdapterPosition());
                }
            });


            serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    servicesAddRemoveListioner.onClick(getAdapterPosition());
                }
            });

            itemEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick(getAdapterPosition());
                }
            });
            itemDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteListioner.onDelete(getAdapterPosition());
                }
            });
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
