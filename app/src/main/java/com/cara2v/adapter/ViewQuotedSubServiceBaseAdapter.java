package com.cara2v.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cara2v.Interface.RemoveImageListioner;
import com.cara2v.R;
import com.cara2v.bean.QuotedServicesBean;
import com.cara2v.bean.QuotedSubServiceBean;
import com.cara2v.util.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewQuotedSubServiceBaseAdapter extends BaseAdapter {

    private List<QuotedSubServiceBean> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;
    private RemoveImageListioner removeImageListioner;
    private DecimalFormat df = new DecimalFormat("#.##");
    private int mRowHeight;
    private String color;

    public ViewQuotedSubServiceBaseAdapter(Context context, List<QuotedSubServiceBean> objects, String color, RemoveImageListioner removeImageListioner) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
        this.removeImageListioner = removeImageListioner;
        this.color = color;
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
            convertView = layoutInflater.inflate(R.layout.item_sub_service_view_quoted, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((QuotedSubServiceBean) getItem(position), (ViewHolder) convertView.getTag(), position);

        return convertView;
    }

    private void initializeViews(final QuotedSubServiceBean object, final ViewHolder holder, int position) {
        holder.subItemText.setText(object.getName());
        holder.totalPrice.setTag(position);
        holder.totalPrice.setVisibility(View.VISIBLE);

        if (object.getParts().equals("0") && object.getFlatPrice().equals("0") && object.getPrice().equals("0")) {
            holder.partPriceTxt.setText("$ " + Constant.DecimalFormat(Float.parseFloat(object.getParts())));
            holder.laborPriceTxt.setText("$ " + Constant.DecimalFormat(Float.parseFloat(object.getPrice())));
            holder.totalPrice.setText("$ 0.00");
            holder.partLay.setVisibility(View.VISIBLE);
        } else if (Float.parseFloat(object.getFlatPrice()) > 0) {
            holder.totalPrice.setText("$ " + Constant.DecimalFormat((Float.parseFloat(object.getFlatPrice()))));
            holder.partLay.setVisibility(View.GONE);
        } else {
            holder.partPriceTxt.setText("$ " + Constant.DecimalFormat(Float.parseFloat(object.getParts())));
            holder.laborPriceTxt.setText("$ " + Constant.DecimalFormat(Float.parseFloat(object.getPrice())));
            holder.totalPrice.setText("$ " + Constant.DecimalFormat((Float.parseFloat(object.getParts()) + Float.parseFloat(object.getPrice()))));
            holder.partLay.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    protected class ViewHolder {
        private TextView subItemText;
        // private ImageView addChargeBtn;
        private TextView totalPrice;
        private TextView partPriceTxt;
        private TextView laborPriceTxt;
        private LinearLayout partLay;
        private RelativeLayout priceLay;

        public ViewHolder(View view) {
            subItemText = view.findViewById(R.id.subItemText);
            // addChargeBtn = view.findViewById(R.id.addChargeBtn);
            totalPrice = view.findViewById(R.id.totalPrice);
            partLay = view.findViewById(R.id.partLay);
            partPriceTxt = view.findViewById(R.id.partPriceTxt);
            laborPriceTxt = view.findViewById(R.id.LaborPriceTxt);
            priceLay = view.findViewById(R.id.priceLay);
            if (color.equals("blue")) {
                totalPrice.setTextColor(ContextCompat.getColor(context, R.color.colorBlueText));
                partPriceTxt.setTextColor(ContextCompat.getColor(context, R.color.colorBlueText));
                laborPriceTxt.setTextColor(ContextCompat.getColor(context, R.color.colorBlueText));
            }
            /*addChargeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) addChargeBtn.getTag());
                }
            });*/
            /*partPriceTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) totalPrice.getTag());
                }
            });
            laborPriceTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) totalPrice.getTag());
                }
            });
            priceLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) totalPrice.getTag());
                }
            });*/
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeImageListioner.onClick((Integer) totalPrice.getTag());
                }
            });
        }
    }
}