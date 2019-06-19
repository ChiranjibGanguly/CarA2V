package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.responceBean.GetMyJobsResponce;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class MyCompletedJobsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    private ArrayList<GetMyJobsResponce.DataBean> requestArrayList;
    private ServicesAddRemoveListioner servicesAddRemoveListioner;
    private int userType = 0;

    public MyCompletedJobsAdapter(Context context, ArrayList<GetMyJobsResponce.DataBean> requestArrayList, ServicesAddRemoveListioner servicesAddRemoveListioner) {
        this.context = context;
        this.requestArrayList = requestArrayList;
        this.servicesAddRemoveListioner = servicesAddRemoveListioner;
        userType = PreferenceConnector.readInteger(context, PreferenceConnector
                .UserType, 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cm_job_service, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyCompletedJobsAdapter.MyViewHolder h = (MyCompletedJobsAdapter.MyViewHolder) holder;
        GetMyJobsResponce.DataBean dataBean = requestArrayList.get(h.getAdapterPosition());
        String brandName = dataBean.getCar().getCarName().substring(0, 1).toUpperCase() + dataBean.getCar().getCarName().substring(1);
        String modelName = dataBean.getCar().getModelName().substring(0, 1).toUpperCase() + dataBean.getCar().getModelName().substring(1);
        h.itemCarBrand.setText(dataBean.getCar().getCarYear() + "  " + brandName + "  " + modelName);
        h.itemDateTxt.setText(Constant.dateTimeFormateChangeForOnlnList(dataBean.getDateAndTime()) + " " + dataBean.getTimeSlot());
        h.serviceName.setText(dataBean.getService().get(0).getName());
        h.itemVinCodeTxt.setText(dataBean.getCar().getVINCode());
        h.itemCarNoTxt.setText(dataBean.getCar().getPlateNumber());
        settotalPrice(h.itemPrice, dataBean.getInvoice().get(0));
        h.serviceList.setAdapter(new MyJobSubServiceAdapter(context, dataBean.getService().get(0).getSubService()));
        setListViewHeightBasedOnChildren(h.serviceList);
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView itemDateTxt;
        private TextView itemCarBrand;
        private TextView itemVinCodeTxt;
        private TextView itemCarNoTxt;
        private TextView itemPrice;
        private TextView serviceName;
        private ListView serviceList;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemDateTxt = itemView.findViewById(R.id.itemDateTxt);
            itemCarBrand = itemView.findViewById(R.id.itemCarBrand);
            itemVinCodeTxt = itemView.findViewById(R.id.itemVinCodeTxt);
            itemCarNoTxt = itemView.findViewById(R.id.itemCarNoTxt);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceList = itemView.findViewById(R.id.serviceList);
            itemCarNoTxt.setVisibility(View.GONE);
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

    private void settotalPrice(TextView itemPrice, GetMyJobsResponce.DataBean.InvoiceBean invoiceBean) {
        float totalPrice = Float.parseFloat(invoiceBean.getTotalAmount());
        float extrPrice = Float.parseFloat(invoiceBean.getExtraPrice());
        float total = totalPrice + extrPrice;
        itemPrice.setText("$ " + Constant.DecimalFormat(total));
    }
}
