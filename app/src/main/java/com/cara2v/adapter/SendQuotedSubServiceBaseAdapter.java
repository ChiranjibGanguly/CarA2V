package com.cara2v.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.R;
import com.cara2v.bean.QuotedServicesBean;
import com.cara2v.bean.QuotedSubServiceBean;
import com.cara2v.responceBean.AddServicesResponce;
import com.cara2v.util.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SendQuotedSubServiceBaseAdapter extends BaseAdapter {

    private List<QuotedSubServiceBean> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;
    private RemoveImageListioner removeImageListioner;
    private DecimalFormat df = new DecimalFormat("#.##");
    private int mRowHeight;

    public SendQuotedSubServiceBaseAdapter(Context context, List<QuotedSubServiceBean> objects, RemoveImageListioner removeImageListioner) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
        this.removeImageListioner = removeImageListioner;
        mRowHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public QuotedSubServiceBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_sub_service_sent_quoted, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((QuotedSubServiceBean) getItem(position), (ViewHolder) convertView.getTag(), position);

        return convertView;
    }

    private void initializeViews(final QuotedSubServiceBean object, final ViewHolder holder, int position) {
        holder.subItemText.setText(object.getName());
        holder.addChargeBtn.setTag(position);
        if (object.getParts().equals("0") && object.getPrice().equals("0")&&object.getFlatPrice().equals("0")) {
            holder.partLay.setVisibility(View.GONE);
            holder.addChargeBtn.setVisibility(View.VISIBLE);
            holder.totalPrice.setVisibility(View.GONE);
        } else {
            if (!object.getFlatPrice().equals("0")){
                holder.totalPrice.setText("$ " + Constant.DecimalFormat((Float.parseFloat(object.getFlatPrice()))));
                holder.addChargeBtn.setVisibility(View.GONE);
                holder.partLay.setVisibility(View.GONE);
                holder.totalPrice.setVisibility(View.VISIBLE);
            }else if (!object.getParts().equals("0")||!object.getPrice().equals("0")) {
                holder.partLay.setVisibility(View.VISIBLE);
                holder.addChargeBtn.setVisibility(View.GONE);
                holder.totalPrice.setVisibility(View.VISIBLE);
                holder.totalPrice.setText("$ " + Constant.DecimalFormat((Float.parseFloat(object.getParts()) + Float.parseFloat(object.getPrice()))));
                holder.partPriceTxt.setText("$ " + Constant.DecimalFormat(Float.parseFloat(object.getParts())));
                holder.laborPriceTxt.setText("$ " + Constant.DecimalFormat(Float.parseFloat(object.getPrice())));
            }
        }
        notifyDataSetChanged();
    }

    protected class ViewHolder {
        private TextView subItemText;
        private ImageView addChargeBtn;
        private TextView totalPrice;
        private TextView partPriceTxt;
        private TextView laborPriceTxt;
        private LinearLayout partLay;
        private RelativeLayout priceLay;

        public ViewHolder(View view) {
            subItemText = view.findViewById(R.id.subItemText);
            addChargeBtn = view.findViewById(R.id.addChargeBtn);
            totalPrice = view.findViewById(R.id.totalPrice);
            partLay = view.findViewById(R.id.partLay);
            partPriceTxt = view.findViewById(R.id.partPriceTxt);
            laborPriceTxt = view.findViewById(R.id.LaborPriceTxt);
            priceLay = view.findViewById(R.id.priceLay);
            /*addChargeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) addChargeBtn.getTag());
                }
            });*/
            partPriceTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) addChargeBtn.getTag());
                }
            });
            laborPriceTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) addChargeBtn.getTag());
                }
            });
            priceLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) addChargeBtn.getTag());
                }
            });
        }
    }
}