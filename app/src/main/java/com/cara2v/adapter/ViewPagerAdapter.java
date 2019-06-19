package com.cara2v.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cara2v.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by android-5 on 15/7/17.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    String[] pagerImageList;
    //PagerAdapterClickListner pagerAdapterClickListner;


    public ViewPagerAdapter(Context mContext, String[] pagerImageList/*, PagerAdapterClickListner pagerAdapterClickListner*/) {
        this.mContext = mContext;
        this.pagerImageList = pagerImageList;
       // this.pagerAdapterClickListner = pagerAdapterClickListner;
    }

    @Override
    public int getCount() {
        return pagerImageList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_view_pager, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.pagerImageItem);
        final ProgressBar smlProgress = (ProgressBar) itemView.findViewById(R.id.smlProgress);
        String profileImg = pagerImageList[position];
        if (!TextUtils.isEmpty(profileImg)) {
            try {
                Log.e("CarImage", "http://"+profileImg );
                Picasso.with(imageView.getContext())
                        .load("http://"+profileImg)
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                smlProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                smlProgress.setVisibility(View.GONE);
                                imageView.setImageResource(R.drawable.place_holder);
                            }
                        });
            } catch (Exception e) {

            }
        }
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerAdapterClickListner.clickedItem(position);
            }
        });*/
            //imageView.setImageResource(mResources[position]);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem (ViewGroup container,int position, Object object){
            container.removeView((LinearLayout) object);
        }
    }
