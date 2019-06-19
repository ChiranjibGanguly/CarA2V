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

public class MyJobsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> stringArrayList;
    private ArrayList<GetMyJobsResponce.DataBean> requestArrayList;
    private ServicesAddRemoveListioner servicesAddRemoveListioner;
    private int userType = 0;

    public MyJobsAdapter(Context context, ArrayList<GetMyJobsResponce.DataBean> requestArrayList, ServicesAddRemoveListioner servicesAddRemoveListioner) {
        this.context = context;
        this.requestArrayList = requestArrayList;
        this.servicesAddRemoveListioner = servicesAddRemoveListioner;
        userType = PreferenceConnector.readInteger(context, PreferenceConnector
                .UserType, 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_jobs, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyJobsAdapter.MyViewHolder h = (MyJobsAdapter.MyViewHolder) holder;
        GetMyJobsResponce.DataBean dataBean = requestArrayList.get(h.getAdapterPosition());
        String brandName = dataBean.getCar().getCarName().substring(0, 1).toUpperCase() + dataBean.getCar().getCarName().substring(1);
        String modelName = dataBean.getCar().getModelName().substring(0, 1).toUpperCase() + dataBean.getCar().getModelName().substring(1);
        h.itemCarName.setText(dataBean.getCar().getCarYear() + "  " + brandName + "  " + modelName);
        h.serviceName.setText(dataBean.getService().get(0).getName());
        h.itemVinCodeTxt.setText(dataBean.getCar().getVINCode());
        h.itemCarNo.setText(dataBean.getCar().getPlateNumber());
        h.serviceList.setAdapter(new MyJobSubServiceAdapter(context, dataBean.getService().get(0).getSubService()));
        manageStatus(dataBean.getStatus(), h.itemDateTxt, dataBean.getDateAndTime(),dataBean.getTimeSlot());
        setListViewHeightBasedOnChildren(h.serviceList);
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView itemDateTxt;
        private TextView itemCarName;
        private TextView itemVinCodeTxt;
        private TextView itemCarNo;
        private TextView serviceName;
        private ListView serviceList;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemDateTxt = itemView.findViewById(R.id.itemDateTxt);
            itemCarName = itemView.findViewById(R.id.itemCarName);
            itemVinCodeTxt = itemView.findViewById(R.id.itemVinCodeTxt);
            itemCarNo = itemView.findViewById(R.id.itemCarNo);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceList = itemView.findViewById(R.id.serviceList);
            itemCarNo.setVisibility(View.GONE);
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

    private void manageStatus(int status, TextView itemDateTxt, String date, String timeSlot) {
        // 1:new, 2:accept, 3:start, 4:In Progress, 5:Almost Done,6:End Service,7:Ask for payment,8:payment done, 9:review complete
        switch (status) {
            case 2:
                itemDateTxt.setText("Confirmed - " + Constant.dateTimeFormateChangeForOnlnList(date)+" "+timeSlot);
                break;
            case 3:
                itemDateTxt.setText("Service Start - " + Constant.dateTimeFormateChangeForOnlnList(date)+" "+timeSlot);
                break;
            case 4:
                itemDateTxt.setText("In Progress - " + Constant.dateTimeFormateChangeForOnlnList(date)+" "+timeSlot);
                break;
            case 5:
                itemDateTxt.setText("Almost Done - " + Constant.dateTimeFormateChangeForOnlnList(date)+" "+timeSlot);
                break;
            case 6:
                itemDateTxt.setText("End Service - " + Constant.dateTimeFormateChangeForOnlnList(date)+" "+timeSlot);
                break;
            case 7:
                itemDateTxt.setText("Asked For Payment - " + Constant.dateTimeFormateChangeForOnlnList(date)+" "+timeSlot);
                break;
            case 8:
                itemDateTxt.setText("Payment Done - " + Constant.dateTimeFormateChangeForOnlnList(date)+" "+timeSlot);
                break;
            case 9:
                itemDateTxt.setText("Completed - " + Constant.dateTimeFormateChangeForOnlnList(date)+" "+timeSlot);
                break;
        }
    }
}
