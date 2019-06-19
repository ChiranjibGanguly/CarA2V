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

import com.cara2v.R;
import com.cara2v.responceBean.AllPostResponce;
import com.cara2v.responceBean.GetUserProfileInfoResponce;
import com.cara2v.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<AllPostResponce.DataBean.CommentBean> commentBeanArrayList;
    private String now;

    public CommentAdapter(Context context, ArrayList<AllPostResponce.DataBean.CommentBean> commentBeanArrayList, String now) {
        this.context = context;
        this.commentBeanArrayList = commentBeanArrayList;
        this.now = now;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AllPostResponce.DataBean.CommentBean commentBean = commentBeanArrayList.get(position);
        final CommentAdapter.MyViewHolder h = (CommentAdapter.MyViewHolder) holder;
        h.itemUserName.setText(commentBean.getUserName());
        String imgPath = commentBean.getUserImage();
        if (!TextUtils.isEmpty(imgPath)) {
            Picasso.with(h.itemUserImage.getContext()).load(imgPath).placeholder(R.drawable.user_place_holder).fit().into(h.itemUserImage);
        }
        h.itemTimeAgo.setText(Constant.getDayDifference(commentBean.getCrd(), now));
        h.itemComment.setText(commentBean.getPostComment());
    }

    @Override
    public int getItemCount() {
        return commentBeanArrayList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemUserImage;
        private TextView itemTimeAgo;
        private TextView itemUserName;
        private TextView itemComment;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemUserImage = itemView.findViewById(R.id.itemUserImage);
            this.itemTimeAgo = itemView.findViewById(R.id.itemTimeAgo);
            this.itemUserName = itemView.findViewById(R.id.itemUserName);
            this.itemComment = itemView.findViewById(R.id.itemComment);

        }

    }

}