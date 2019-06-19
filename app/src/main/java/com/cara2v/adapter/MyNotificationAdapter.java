package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.R;
import com.cara2v.responceBean.GetMyNotifcationResponce;
import com.cara2v.util.Constant;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by chiranjib on 28/12/17.
 */

public class MyNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<GetMyNotifcationResponce.DataBean> myNotificationList;
    private String now;
    private MyOnCheckListioner myOnCheckListioner;

    public MyNotificationAdapter(Context context, ArrayList<GetMyNotifcationResponce.DataBean> myNotificationList, String now, MyOnCheckListioner myOnCheckListioner) {
        this.context = context;
        this.myNotificationList = myNotificationList;
        this.now = now;
        this.myOnCheckListioner = myOnCheckListioner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetMyNotifcationResponce.DataBean dataBean = myNotificationList.get(position);
        final MyNotificationAdapter.MyViewHolder h = (MyNotificationAdapter.MyViewHolder) holder;
        h.userName.setText(dataBean.getUserDetail().get(0).getUserName());
        h.notifyMsg.setText(dataBean.getNotification().getBody());
        h.dateTime.setText(Constant.getDayDifference(dataBean.getCrd(), now));
        String imgPath = dataBean.getUserDetail().get(0).getProfileImage();
        if (!TextUtils.isEmpty(imgPath)) {
            try {
                Picasso.with(h.profileImage.getContext())
                        .load(imgPath)
                        .placeholder(R.drawable.user_place_holder)
                        .fit()
                        .into(h.profileImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
            } catch (Exception e) {

            }
        } else if (TextUtils.isEmpty(imgPath)) {
            h.profileImage.setImageResource(R.drawable.user_place_holder);
        }
    }

    @Override
    public int getItemCount() {
        return myNotificationList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout container;
        private ImageView profileImage;
        private TextView dateTime;
        private TextView userName;
        private TextView notifyMsg;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            profileImage = itemView.findViewById(R.id.profileImage);
            dateTime = itemView.findViewById(R.id.dateTime);
            userName = itemView.findViewById(R.id.userName);
            notifyMsg = itemView.findViewById(R.id.notifyMsg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myOnCheckListioner.OnCheck(getAdapterPosition());
                }
            });
        }
    }
}
