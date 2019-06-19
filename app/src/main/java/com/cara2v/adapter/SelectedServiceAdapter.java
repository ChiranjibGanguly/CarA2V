package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cara2v.R;
import com.cara2v.bean.RequestServiceBean;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chiranjib on 28/12/17.
 */

public class SelectedServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<RequestServiceBean> serviceList;


    public SelectedServiceAdapter(Context context, ArrayList<RequestServiceBean> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_service, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RequestServiceBean dataBean = serviceList.get(position);
        SelectedServiceAdapter.MyViewHolder h = (SelectedServiceAdapter.MyViewHolder) holder;
        h.itemServiceTxt.setText(dataBean.name);
        h.subList.setAdapter(new ArrayAdapter<RequestServiceBean.Subservice>(context, R.layout.item_sublist_onln_service, R.id.subItemText, (ArrayList) dataBean.subService));
        setListViewHeightBasedOnChildren(h.subList);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemServiceTxt;
        ListView subList;

        public MyViewHolder(View itemView) {
            super(itemView);
            subList = itemView.findViewById(R.id.subList);
            itemServiceTxt = itemView.findViewById(R.id.itemServiceTxt);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
