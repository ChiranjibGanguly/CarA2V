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
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.Interface.ServicesAddRemoveListioner;
import com.cara2v.R;
import com.cara2v.responceBean.AllPostResponce;
import com.cara2v.responceBean.GetUserProfileInfoResponce;
import com.cara2v.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class AllPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AllPostResponce.DataBean> socialPostBeanList;
    private RemoveImageListioner likeListioner;
    private ServicesAddRemoveListioner commentListioner;
    private MyOnCheckListioner shareListioner;
    private String now;

    public AllPostAdapter(Context context, List<AllPostResponce.DataBean> socialPostBeanList, String now, RemoveImageListioner likeListioner, ServicesAddRemoveListioner commentListioner, MyOnCheckListioner shareListioner) {
        this.context = context;
        this.socialPostBeanList = socialPostBeanList;
        this.likeListioner = likeListioner;
        this.commentListioner = commentListioner;
        this.shareListioner = shareListioner;
        this.now = now;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_feed_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AllPostResponce.DataBean socialPostBean = socialPostBeanList.get(position);
        //  postType:1 // 1:status 2:image 3:image with status
        //  postShowType:1 // 1:public 2:private

        AllPostResponce.DataBean.UserDetailBean userDetailBean = socialPostBean.getUserDetail().get(0);
        final AllPostAdapter.MyViewHolder h = (AllPostAdapter.MyViewHolder) holder;
        String prflImgPath = userDetailBean.getProfileImage();
        if (!TextUtils.isEmpty(prflImgPath)) {
            Picasso.with(h.itemUserImage.getContext()).load(prflImgPath).placeholder(R.drawable.place_holder).fit().into(h.itemUserImage);
        }
        h.itemUserName.setText(userDetailBean.getUserName());
        h.itemLikeCount.setText("" + socialPostBean.getLikeCount());
        h.itemCommentCount.setText("" + socialPostBean.getCommentCount());
        if (socialPostBean.getIsLike() == 0) {
            h.isLikeImg.setImageResource(R.drawable.dislike_light);
        } else if (socialPostBean.getIsLike() == 1) {
            h.isLikeImg.setImageResource(R.drawable.like_light);
        }
        h.iteAgoTxt.setText(Constant.getDayDifference(socialPostBean.getCrd(), now));
        switch (socialPostBean.getPostType()) {
            case 1:
                h.itemCaption.setVisibility(View.GONE);
                h.itemPostImg.setVisibility(View.GONE);
                h.itemPostTxt.setVisibility(View.VISIBLE);
                h.itemPostTxt.setText(socialPostBean.getPostStatus());
                break;
            case 2:
                h.itemCaption.setVisibility(View.GONE);
                h.itemPostImg.setVisibility(View.VISIBLE);
                h.itemPostTxt.setVisibility(View.GONE);
                String imgPath = socialPostBean.getPostImage();
                if (!TextUtils.isEmpty(imgPath)) {
                    Picasso.with(h.itemPostImg.getContext()).load(imgPath).placeholder(R.drawable.place_holder).fit().into(h.itemPostImg);
                }
                break;
            case 3:
                h.itemCaption.setVisibility(View.VISIBLE);
                h.itemPostImg.setVisibility(View.VISIBLE);
                h.itemPostTxt.setVisibility(View.GONE);
                h.itemCaption.setText(socialPostBean.getPostStatus());
                String imgPathh = socialPostBean.getPostImage();
                if (!TextUtils.isEmpty(imgPathh)) {
                    Picasso.with(h.itemPostImg.getContext()).load(imgPathh).placeholder(R.drawable.place_holder).fit().into(h.itemPostImg);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return socialPostBeanList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout topLay;
        private TextView itemUserName;
        private TextView iteAgoTxt;
        private LinearLayout feedLay;
        private TextView itemCaption;
        private TextView itemPostTxt;
        private ImageView itemPostImg;
        private ImageView isLikeImg;
        private ImageView itemUserImage;
        private TextView itemLikeCount;
        private TextView itemCommentCount;
        private LinearLayout likeLay;
        private LinearLayout commentLay;
        private LinearLayout shareLay;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.topLay = itemView.findViewById(R.id.topLay);
            this.itemUserImage = itemView.findViewById(R.id.itemUserImage);
            this.itemUserName = itemView.findViewById(R.id.itemUserName);
            this.iteAgoTxt = itemView.findViewById(R.id.iteAgoTxt);
            this.feedLay = itemView.findViewById(R.id.feedLay);
            this.itemCaption = itemView.findViewById(R.id.itemCaption);
            this.itemPostTxt = itemView.findViewById(R.id.itemPostTxt);
            this.itemPostImg = itemView.findViewById(R.id.itemPostImg);
            this.itemLikeCount = itemView.findViewById(R.id.itemLikeCount);
            this.itemCommentCount = itemView.findViewById(R.id.itemCommentCount);
            this.likeLay = itemView.findViewById(R.id.likeLay);
            this.commentLay = itemView.findViewById(R.id.commentLay);
            this.shareLay = itemView.findViewById(R.id.shareLay);
            this.isLikeImg = itemView.findViewById(R.id.isLikeImg);
            likeLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeListioner.onClick(getAdapterPosition());
                }
            });
            commentLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentListioner.onClick(getAdapterPosition());
                }
            });
            shareLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareListioner.OnCheck(getAdapterPosition());
                }
            });
        }
    }

}
