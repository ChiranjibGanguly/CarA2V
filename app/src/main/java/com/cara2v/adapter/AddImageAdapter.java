package com.cara2v.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.cara2v.Interface.AddImageListioner;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.R;

import java.util.ArrayList;


public class AddImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Uri> addProductImageList;
    static int NO_IMAGE = 1;
    static int IMAGE = 2;
    AddImageListioner addImageListioner;
    RemoveImageListioner removeImageListioner;

    public AddImageAdapter(Context context, ArrayList<Uri> addProductImageList,
                           AddImageListioner addImageListioner, RemoveImageListioner removeImageListioner) {
        this.context = context;
        this.addProductImageList = addProductImageList;
        this.addImageListioner = addImageListioner;
        this.removeImageListioner = removeImageListioner;

    }

    public void notifyMyAdapter(ArrayList<Uri> addProductImageList) {
        this.addProductImageList = addProductImageList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (addProductImageList.get(position) != null) ? IMAGE : NO_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_image_layout, parent, false);
            return new MyViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_image_layout, parent, false);
            return new NoImageViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Uri image = addProductImageList.get(position);
        if (image == null) {
            NoImageViewHolder h = (NoImageViewHolder) holder;
            h.addProductImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            MyViewHolder h = (MyViewHolder) holder;
            h.addProductImage.setImageURI(image);
            h.addProductImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public int getItemCount() {
        return addProductImageList == null ? 0 : addProductImageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView addProductImage, crossBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            addProductImage = itemView.findViewById(R.id.uploadedImg);
            crossBtn = itemView.findViewById(R.id.crossBtn);
            crossBtn.setVisibility(View.VISIBLE);
            crossBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick(getAdapterPosition());
                    /*addProductImageList.remove(getAdapterPosition());
                    notifyDataSetChanged();*/
                }
            });
        }
    }

    public class NoImageViewHolder extends RecyclerView.ViewHolder {
        ImageView addProductImage;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            addProductImage = itemView.findViewById(R.id.uploadedImg);
            addProductImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addImageListioner.onClick();
                }
            });
        }
    }

}
