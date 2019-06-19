package com.cara2v.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cara2v.Interface.AddEditPromocodeListioner;
import com.cara2v.R;
import com.cara2v.responceBean.GetAllPromoCodeResponce;
import com.cara2v.util.Constant;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.List;

/**
 * Created by chiranjib on 28/12/17.
 */

public class PromoCodeSwipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<GetAllPromoCodeResponce.DataBean> allPromoCodeList;
    private AddEditPromocodeListioner addEditPromocodeListioner;
    private int lastClosedItem=-1;
    private ItemTouchHelperExtension mItemTouchHelperExtension;

    public PromoCodeSwipeAdapter(Context context, List<GetAllPromoCodeResponce.DataBean> allPromoCodeList, AddEditPromocodeListioner addEditPromocodeListioner) {
        this.context = context;
        this.allPromoCodeList = allPromoCodeList;
        this.addEditPromocodeListioner = addEditPromocodeListioner;

    }
    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promocode, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GetAllPromoCodeResponce.DataBean dataBean = allPromoCodeList.get(position);
        final PromoCodeSwipeAdapter.MyViewHolder h = (PromoCodeSwipeAdapter.MyViewHolder) holder;
        h.itemPromocodeTxt.setText(dataBean.getTitle());
        h.itemExpireDate.setText("Expire On - " + Constant.dateFormateChangePromoCode(dataBean.getExpire()));
        if (dataBean.getUseCount() > 0) {
            h.itemUsed.setText("" + dataBean.getUseCount() + " user used this code");
        } else {
            h.itemUsed.setText("No user used this code");
        }
        h.discountTxt.setText(dataBean.getAmount() + "%");
    }

    @Override
    public int getItemCount() {
        return allPromoCodeList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private SwipeRevealLayout swipeRevealLayout;
        private ImageView itemDeleteBtn;
        private ImageView itemEditBtn;
        private TextView itemPromocodeTxt;
        private TextView itemExpireDate;
        private TextView itemUsed;
        private TextView discountTxt;

        public MyViewHolder(View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            itemDeleteBtn = itemView.findViewById(R.id.itemDeleteBtn);
            itemEditBtn = itemView.findViewById(R.id.itemEditBtn);
            itemPromocodeTxt = itemView.findViewById(R.id.itemPromocodeTxt);
            itemExpireDate = itemView.findViewById(R.id.itemExpireDate);
            itemUsed = itemView.findViewById(R.id.itemUsed);
            discountTxt = itemView.findViewById(R.id.discountTxt);

            itemEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addEditPromocodeListioner.onEdit(allPromoCodeList.get(getAdapterPosition()));
                }
            });
            itemDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addEditPromocodeListioner.onDelete(allPromoCodeList.get(getAdapterPosition()));
                }
            });
        }
    }
}