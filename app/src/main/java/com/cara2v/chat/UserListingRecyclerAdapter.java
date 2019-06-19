package com.cara2v.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cara2v.Interface.MyOnCheckListioner;
import com.cara2v.R;
import com.cara2v.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chiranjib on 15/3/18.
 */

public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<ChatHistoryBeanForList> mChatHistorylist;
    private MyOnCheckListioner myOnCheckListioner;
    private Context mContext;

    public UserListingRecyclerAdapter(Context context, List<ChatHistoryBeanForList> users, MyOnCheckListioner myOnCheckListioner) {
        this.mContext = context;
        this.mChatHistorylist = users;
        this.myOnCheckListioner = myOnCheckListioner;
    }

    public void add(ChatHistoryBeanForList user) {
        mChatHistorylist.add(user);
        notifyItemInserted(mChatHistorylist.size() - 1);
    }

    @Override
    public UserListingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListingRecyclerAdapter.ViewHolder holder, final int position) {
        final ChatHistoryBeanForList user = mChatHistorylist.get(position);
        String imgPath = "";

        imgPath = user.profilePic;
        holder.userName.setText(user.name);
        if (user.image==1) {
            holder.lastMsg.setText("Send image");
        } else {
            holder.lastMsg.setText(user.message);
        }

        holder.dateTime.setText(Constant.getDayDifference(Constant.ConvertMilliSecondsToFormattedDate(String.valueOf(user.timestamp)), Constant.getCurrentDate() + " " + Constant.getCurrentTime()));


        if (!TextUtils.isEmpty(imgPath)) {
            try {
                Picasso.with(holder.profileImage.getContext())
                        .load(imgPath)
                        .placeholder(R.drawable.user_place_holder)
                        .fit()
                        .into(holder.profileImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
            } catch (Exception e) {

            }
        }else if (TextUtils.isEmpty(imgPath)){
            holder.profileImage.setImageResource(R.drawable.user_place_holder);
        }
    }

    @Override
    public int getItemCount() {
        if (mChatHistorylist != null) {
            return mChatHistorylist.size();
        }
        return 0;
    }

    public ChatHistoryBeanForList getUser(int position) {
        return mChatHistorylist.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, lastMsg, product, dateTime;
        private LinearLayout container;
        private ImageView profileImage;

        ViewHolder(View itemView) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.userName);
            this.lastMsg = itemView.findViewById(R.id.lastMsg);
            this.container = itemView.findViewById(R.id.container);
            this.profileImage = itemView.findViewById(R.id.profileImage);
            this.dateTime = itemView.findViewById(R.id.dateTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myOnCheckListioner.OnCheck(getAdapterPosition());
                }
            });
        }
    }
}