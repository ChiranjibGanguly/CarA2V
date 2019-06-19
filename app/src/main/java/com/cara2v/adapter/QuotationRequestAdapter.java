package com.cara2v.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.responceBean.ServiceDetailsResponce;
import com.cara2v.util.Constant;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class QuotationRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    List<ServiceDetailsResponce.DataBean.OrderdetailsBean> orderdetailsBeans;
    RemoveImageListioner listioner;
    ServicesAddRemoveListioner chatListioner;
    private DecimalFormat df = new DecimalFormat("#.##");

    public QuotationRequestAdapter(Context context, List<ServiceDetailsResponce.DataBean.OrderdetailsBean> orderdetailsBeans, RemoveImageListioner listioner, ServicesAddRemoveListioner chatListioner) {
        this.context = context;
        this.orderdetailsBeans = orderdetailsBeans;
        this.chatListioner = chatListioner;
        this.listioner = listioner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote_lay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceDetailsResponce.DataBean.OrderdetailsBean dataBean = orderdetailsBeans.get(position);
        QuotationRequestAdapter.MyViewHolder h = (QuotationRequestAdapter.MyViewHolder) holder;
        h.estPrcTxt.setText("$ " + Constant.DecimalFormat(Double.parseDouble(dataBean.getTotalAmount())));
        h.itemDistance.setText("" + df.format(Double.parseDouble(dataBean.getDistance())) + " mi");
        if (!TextUtils.isEmpty(dataBean.getUserInfo().getProfileImage())) {
            Picasso.with(h.itemUserImage.getContext()).load(dataBean.getUserInfo().getProfileImage()).placeholder(R.drawable.user_place_holder).fit().into(h.itemUserImage);
            Log.e("ProfilePic", dataBean.getUserInfo().getProfileImage());
        }
        h.itemTimeTxt.setText(Constant.getDayDifference(dataBean.getCrd(), dataBean.getNow()));
        h.itemServiceName.setText(dataBean.getUserInfo().getBusinessName());
        try {
            h.itemRating.setRating(Float.parseFloat(dataBean.getUserInfo().getRating()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        h.itemAddress.setText(dataBean.getUserInfo().getAddress());
    }

    @Override
    public int getItemCount() {
        return orderdetailsBeans.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView estPrcTxt;
        private TextView itemDistance;
        private ImageView itemUserImage;
        private TextView itemTimeTxt;
        private Button chatBtn;
        private Button viewCodeBtn;
        private TextView itemServiceName;
        private AppCompatRatingBar itemRating;
        private TextView itemAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            estPrcTxt = itemView.findViewById(R.id.estPrcTxt);
            itemDistance = itemView.findViewById(R.id.itemDistance);
            itemUserImage = itemView.findViewById(R.id.itemUserImage);
            itemTimeTxt = itemView.findViewById(R.id.itemTimeTxt);
            chatBtn = itemView.findViewById(R.id.chatBtn);
            viewCodeBtn = itemView.findViewById(R.id.viewCodeBtn);
            itemServiceName = itemView.findViewById(R.id.itemServiceName);
            itemRating = itemView.findViewById(R.id.itemRating);
            itemAddress = itemView.findViewById(R.id.itemAddress);
            viewCodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listioner.onClick(getAdapterPosition());
                }
            });
            chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatListioner.onClick(getAdapterPosition());
                }
            });
        }
    }

}