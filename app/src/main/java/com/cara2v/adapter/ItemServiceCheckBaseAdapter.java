package com.cara2v.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cara2v.R;
import com.cara2v.responceBean.AddServicesResponce;

public class ItemServiceCheckBaseAdapter extends BaseAdapter {

    private List<AddServicesResponce.DataBean.SubServiceBean> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemServiceCheckBaseAdapter(Context context, List<AddServicesResponce.DataBean.SubServiceBean> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public AddServicesResponce.DataBean.SubServiceBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_service_check, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((AddServicesResponce.DataBean.SubServiceBean) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final AddServicesResponce.DataBean.SubServiceBean object, final ViewHolder holder) {
        holder.subItemText.setText(object.getSubServiceName());
        if (object.isChecked()) {
            holder.checkbox.setChecked(object.isChecked());
        } else if (!object.isChecked()) {
            holder.checkbox.setChecked(object.isChecked());
        }
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = holder.checkbox.isChecked();
                object.setChecked(checked);
            }
        });
        holder.subItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = holder.checkbox.isChecked();
                if (checked){
                    holder.checkbox.setChecked(false);
                    object.setChecked(holder.checkbox.isChecked());
                }else {
                    holder.checkbox.setChecked(true);
                    object.setChecked(holder.checkbox.isChecked());
                }
            }
        });
        notifyDataSetChanged();
    }

    protected class ViewHolder {
        private CheckBox checkbox;
        private TextView subItemText;

        public ViewHolder(View view) {
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            subItemText = (TextView) view.findViewById(R.id.subItemText);
        }
    }
}
