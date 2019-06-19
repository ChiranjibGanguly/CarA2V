package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.R;
import com.cara2v.responceBean.GetMyCarResponce;
import com.cara2v.responceBean.GetUserProfileInfoResponce;
import com.cara2v.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class UserReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<GetUserProfileInfoResponce.ReviewBean> reviewBeanList;
    private String now;

    public UserReviewAdapter(Context context, List<GetUserProfileInfoResponce.ReviewBean> reviewBeanList, String now) {
        this.context = context;
        this.reviewBeanList = reviewBeanList;
        this.now = now;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_review_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetUserProfileInfoResponce.ReviewBean reviewBean = reviewBeanList.get(position);
        final UserReviewAdapter.MyViewHolder h = (UserReviewAdapter.MyViewHolder) holder;
        h.itemUserRating.setRating((float) reviewBean.getRating());
        h.itemUserName.setText(reviewBean.getUserDetail().get(0).getUserName());
        h.itemUserReview.setText(reviewBean.getReview());
        h.itemTimeAgo.setText(Constant.getDayDifference(reviewBean.getCrd(), now));
        String imgPath = reviewBean.getUserDetail().get(0).getProfileImage();
        if (!TextUtils.isEmpty(imgPath)) {
            Picasso.with(h.itemUserImage.getContext()).load(imgPath).placeholder(R.drawable.user_place_holder).fit().into(h.itemUserImage);
        }
    }

    @Override
    public int getItemCount() {
        return reviewBeanList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemUserImage;
        private TextView itemUserName;
        private AppCompatRatingBar itemUserRating;
        private TextView itemUserReview;
        private TextView itemTimeAgo;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemUserImage = itemView.findViewById(R.id.itemUserImage);
            itemUserName = itemView.findViewById(R.id.itemUserName);
            itemUserRating = itemView.findViewById(R.id.itemUserRating);
            itemUserReview = itemView.findViewById(R.id.itemUserReview);
            itemTimeAgo = itemView.findViewById(R.id.itemTimeAgo);
        }
    }
}