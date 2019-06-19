package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
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

import com.cara2v.responceBean.GetNearestOwnerResponce;
import com.cara2v.util.Constant;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chiranjib on 28/12/17.
 */

public class NearByOwnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<GetNearestOwnerResponce.DataBean> myOwnerList;
    private MyOnCheckListioner myOnCheckListioner;
    private LatLng currentLatLng;

    public NearByOwnerAdapter(Context context, ArrayList<GetNearestOwnerResponce.DataBean> myOwnerList, LatLng currentLatLng, MyOnCheckListioner myOnCheckListioner) {
        this.context = context;
        this.myOwnerList = myOwnerList;
        this.currentLatLng = currentLatLng;
        this.myOnCheckListioner = myOnCheckListioner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetNearestOwnerResponce.DataBean dataBean = myOwnerList.get(position);
        final NearByOwnerAdapter.MyViewHolder h = (NearByOwnerAdapter.MyViewHolder) holder;
        h.itemUserName.setText(dataBean.getBusinessName());
        h.notifyMsg.setText(dataBean.getAddress());
        if (!dataBean.getRating().equals("")) {
            h.itemUserRating.setRating(Float.parseFloat(dataBean.getRating()));
        }
        h.itemDistance.setText(Constant.DecimalFormat(Constant.distanceBetween(currentLatLng, new LatLng(dataBean.getAddressLatLong().get(0), dataBean.getAddressLatLong().get(1)))) + " miles");
        String imgPath = dataBean.getProfileImage();
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
        return myOwnerList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout container;
        private ImageView profileImage;
        private TextView itemUserName;
        private TextView itemDistance;
        private AppCompatRatingBar itemUserRating;
        private TextView notifyMsg;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            profileImage = itemView.findViewById(R.id.profileImage);
            itemUserName = itemView.findViewById(R.id.itemUserName);
            itemDistance = itemView.findViewById(R.id.itemDistance);
            itemUserRating = itemView.findViewById(R.id.itemUserRating);
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
