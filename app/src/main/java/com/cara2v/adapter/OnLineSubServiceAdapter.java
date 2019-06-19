package com.cara2v.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cara2v.R;
import com.cara2v.responceBean.AddServicesResponce;
import com.cara2v.responceBean.MyServiceRequestResponce;

import java.util.ArrayList;
import java.util.List;

public class OnLineSubServiceAdapter extends BaseAdapter {

    private List<MyServiceRequestResponce.DataBean.ServiceBean.SubServiceBean> objects = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public OnLineSubServiceAdapter(Context context, List<MyServiceRequestResponce.DataBean.ServiceBean.SubServiceBean> subService) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = subService;
        if (objects.size()==1){
           objects.add(null);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public MyServiceRequestResponce.DataBean.ServiceBean.SubServiceBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_sublist_onln_service, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((MyServiceRequestResponce.DataBean.ServiceBean.SubServiceBean) getItem(position),position, (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final MyServiceRequestResponce.DataBean.ServiceBean.SubServiceBean object, int position, final ViewHolder holder) {
        if (object==null){
            holder.containerLay.setVisibility(View.INVISIBLE);
        }else {
            holder.containerLay.setVisibility(View.VISIBLE);
            holder.subItemText.setText(object.getName());
        }
    }

    protected class ViewHolder {

        private TextView subItemText;
        private LinearLayout containerLay;

        public ViewHolder(View view) {

            subItemText = view.findViewById(R.id.subItemText);
            containerLay =view.findViewById(R.id.containerLay);
        }
    }
}
