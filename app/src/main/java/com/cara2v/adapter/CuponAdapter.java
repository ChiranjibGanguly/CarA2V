package com.cara2v.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cara2v.R;
import com.cara2v.responceBean.ServiceDetailsResponce;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class CuponAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ServiceDetailsResponce.MyPromoCodeBean> promoCodeBeans;

    public CuponAdapter(Context context, List<ServiceDetailsResponce.MyPromoCodeBean> promoCodeBeans) {
        this.context = context;
        this.promoCodeBeans = promoCodeBeans;
    }
    //This is comment ot cjec'

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpn_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceDetailsResponce.MyPromoCodeBean dataBean = promoCodeBeans.get(position);
        CuponAdapter.MyViewHolder h = (CuponAdapter.MyViewHolder) holder;
        h.itemCpneTxt.setText(dataBean.getTitle());
        if (dataBean.isSelected()) {
            h.mainLayout.setBackgroundResource(R.drawable.edittxt_fill_bg);
            h.itemCpneTxt.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (!dataBean.isSelected()) {
            h.mainLayout.setBackgroundResource(R.drawable.edittxt_bg);
            h.itemCpneTxt.setTextColor(ContextCompat.getColor(context, R.color.colorOrange));
        }
    }

    @Override
    public int getItemCount() {
        return promoCodeBeans.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mainLayout;
        private TextView itemCpneTxt;

        public MyViewHolder(View itemView) {
            super(itemView);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            itemCpneTxt = (TextView) itemView.findViewById(R.id.itemCpneTxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (ServiceDetailsResponce.MyPromoCodeBean dataBean : promoCodeBeans) {
                        dataBean.setSelected(false);
                    }
                    ServiceDetailsResponce.MyPromoCodeBean dataBean = promoCodeBeans.get(getAdapterPosition());
                    if (dataBean.isSelected()) {
                        dataBean.setSelected(false);
                    } else if (!dataBean.isSelected()) {
                        dataBean.setSelected(true);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

}