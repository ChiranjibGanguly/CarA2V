package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cara2v.Interface.AddPriceListioner;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.R;
import com.cara2v.bean.QuotedServicesBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chiranjib on 28/12/17.
 */

public class ViewQuotedServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<QuotedServicesBean> serviceList;
    AddPriceListioner addPriceListioner;
    int mRowHeight;
    String color;

    public ViewQuotedServiceAdapter(Context context, ArrayList<QuotedServicesBean> serviceList, String color, AddPriceListioner addPriceListioner) {
        this.context = context;
        this.serviceList = serviceList;
        this.addPriceListioner = addPriceListioner;
        this.color = color;
        mRowHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_quated_list_group, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewQuotedServiceAdapter.MyViewHolder h = (ViewQuotedServiceAdapter.MyViewHolder) holder;
        QuotedServicesBean dataBean = serviceList.get(h.getAdapterPosition());
        if (!TextUtils.isEmpty(dataBean.getServiceImage()))
            Log.e("ServiceImagePay", dataBean.getServiceImage());
            Picasso.with(h.serviceItemImage.getContext()).load(dataBean.getServiceImage()).fit().into(h.serviceItemImage);
        h.serviveHeader.setText(dataBean.getName());
        h.subList.setAdapter(new ViewQuotedSubServiceBaseAdapter(context, dataBean.getQuotedSubServiceBeans(),color, new RemoveImageListioner() {
            @Override
            public void onClick(int position) {
                addPriceListioner.onClick(h.getAdapterPosition(), position);
            }
        }));
        setListViewHeightBasedOnChildren(h.subList);
        if (dataBean.isVisible()) {
            h.subView.setVisibility(View.VISIBLE);
            h.arrowBtn.setBackgroundResource(R.drawable.up_arrow_service);
        } else if (!dataBean.isVisible()) {
            h.subView.setVisibility(View.GONE);
            h.arrowBtn.setBackgroundResource(R.drawable.down_arrow_service);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceItemImage;
        TextView serviveHeader;
        ListView subList;
        LinearLayout subView;
        LinearLayout containerLay;
        ImageButton arrowBtn;


        public MyViewHolder(View itemView) {
            super(itemView);
            serviceItemImage = itemView.findViewById(R.id.serviceItemImage);
            serviveHeader = itemView.findViewById(R.id.serviveHeader);
            subList = itemView.findViewById(R.id.subList);
            subView = itemView.findViewById(R.id.subView);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            containerLay = itemView.findViewById(R.id.containerLay);


            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QuotedServicesBean dataBean = serviceList.get(getAdapterPosition());
                    if (dataBean.isVisible()) {
                        dataBean.setVisible(false);
                    } else if (!dataBean.isVisible()) {
                        dataBean.setVisible(true);
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
            containerLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QuotedServicesBean dataBean = serviceList.get(getAdapterPosition());
                    if (dataBean.isVisible()) {
                        dataBean.setVisible(false);
                    } else if (!dataBean.isVisible()) {
                        dataBean.setVisible(true);
                    }
                    notifyItemChanged(getAdapterPosition());
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
        if (listAdapter.getCount() % 2 == 0) {
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        } else {
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 20;
        }
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
