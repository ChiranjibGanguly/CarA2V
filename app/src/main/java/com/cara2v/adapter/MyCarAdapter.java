package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.R;
import com.cara2v.responceBean.GetMyCarResponce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class MyCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<GetMyCarResponce.DataBean> mycarlist;
    private MyOnCheckListioner myOnCheckListioner;

    public MyCarAdapter(Context context, ArrayList<GetMyCarResponce.DataBean> mycarlist, MyOnCheckListioner myOnCheckListioner) {
        this.context = context;
        this.mycarlist = mycarlist;
        this.myOnCheckListioner = myOnCheckListioner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_car_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetMyCarResponce.DataBean dataBean=mycarlist.get(position);
        final MyCarAdapter.MyViewHolder h = (MyCarAdapter.MyViewHolder) holder;
        h.itemModelName.setText(dataBean.getBrand().getBrandName()+"("+dataBean.getModel().getModelName()+")");
        //   h.itemVinCode.setText(dataBean.getVINCode());
            h.itemPlateNo.setText(dataBean.getPlateNumber());
        if (h.getAdapterPosition()==mycarlist.size()-1){
            h.divider.setVisibility(View.GONE);
        }else {
            h.divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mycarlist.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView itemModelName;
        private TextView itemPlateNo;
        private View divider;
        // private TextView itemVinCode;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemModelName = itemView.findViewById(R.id.itemModelName);
            itemPlateNo = itemView.findViewById(R.id.itemPlateNo);
            divider = itemView.findViewById(R.id.divider);
            // itemVinCode = itemView.findViewById(R.id.itemVinCode);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    for (GetMyCarResponce.DataBean dataBean : mycarlist) {
                        dataBean.setChecked(false);
                    }

                    GetMyCarResponce.DataBean dataBean = mycarlist.get(pos);
                    dataBean.setChecked(true);
                    notifyDataSetChanged();
                    myOnCheckListioner.OnCheck(pos);
                }
            });
        }
    }
}