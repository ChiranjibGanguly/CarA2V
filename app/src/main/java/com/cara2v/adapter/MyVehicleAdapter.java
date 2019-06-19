package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.R;
import com.cara2v.responceBean.GetMyCarResponce;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chiranjib on 28/12/17.
 */

public class MyVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<GetMyCarResponce.DataBean> mycarlist;
    private RemoveImageListioner itemClickListioner;

    public MyVehicleAdapter(Context context, List<GetMyCarResponce.DataBean> mycarlist, RemoveImageListioner itemClickListioner) {
        this.context = context;
        this.mycarlist = mycarlist;
        this.itemClickListioner = itemClickListioner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_vehicle, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetMyCarResponce.DataBean dataBean = mycarlist.get(position);
        final MyVehicleAdapter.MyViewHolder h = (MyVehicleAdapter.MyViewHolder) holder;
        if (dataBean.getPlateNumberStatus().equals("0")){
            h.carNoTxt.setText(dataBean.getPlateNumber());
        }else if (dataBean.getPlateNumberStatus().equals("1")){
            h.carNoTxt.setText("");
        }
        h.carModelTxt.setText(dataBean.getBrand().getBrandName().toUpperCase());
        h.carBrandTxt.setText(dataBean.getModel().getModelName());
        h.carVinTxt.setText(dataBean.getVINCode());
        h.carYearTxt.setText(dataBean.getCarYear());

        String imgPaths[] = dataBean.getMyCarImage().split(",");
        String imgPath = "http://" + imgPaths[0];
        if (!TextUtils.isEmpty(imgPath))
            Picasso.with(h.carImg.getContext()).load(imgPath).fit().placeholder(R.drawable.place_holder).into(h.carImg);
    }

    @Override
    public int getItemCount() {
        return mycarlist.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView carNoTxt;
        private TextView carModelTxt;
        private TextView carBrandTxt;
        private TextView carVinTxt;
        private TextView carYearTxt;
        private ImageView carImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            carImg = itemView.findViewById(R.id.carImg);
            carNoTxt = itemView.findViewById(R.id.carNoTxt);
            carModelTxt = itemView.findViewById(R.id.carModelTxt);
            carBrandTxt = itemView.findViewById(R.id.carBrandTxt);
            carVinTxt = itemView.findViewById(R.id.carVinTxt);
            carYearTxt = itemView.findViewById(R.id.carYearTxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListioner.onClick(getAdapterPosition());
                }
            });
        }

    }

}
