package com.cara2v.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cara2v.Interface.ChatAdapterClickListner;
import com.cara2v.R;
import com.cara2v.util.Constant;
import com.cara2v.util.PreferenceConnector;
import com.google.firebase.auth.FirebaseAuth;
import com.loopeer.shadow.ShadowView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by android-5 on 5/9/17.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;
    private ChatAdapterClickListner chatAdapterClickListner;
    private Context mContext;
    private List<ChatBean> mChatBeen;
    private String mUid;

    public ChatRecyclerAdapter(Context mContext, List<ChatBean> chatBeen, ChatAdapterClickListner chatAdapterClickListner) {
        this.mContext = mContext;
        this.chatAdapterClickListner = chatAdapterClickListner;
        this.mChatBeen = chatBeen;
        this.mUid = PreferenceConnector.readString(mContext, PreferenceConnector.Id, "");
    }

    public void add(ChatBean chatBean) {
        mChatBeen.add(chatBean);
        notifyDataSetChanged();
        notifyItemInserted(mChatBeen.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (TextUtils.equals(mChatBeen.get(position).uid, mUid)) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, final int position) {
        final ChatBean chatBean = mChatBeen.get(position);

        final String message = chatBean.message;
        myChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
        myChatViewHolder.chatImageview.setVisibility(View.VISIBLE);
        myChatViewHolder.smlProgress.setVisibility(View.VISIBLE);
        if (chatBean.image==1) {
            myChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            Picasso.with(myChatViewHolder.chatImageview.getContext()).load(chatBean.imageUrl).fit().into(myChatViewHolder.chatImageview);
        } else {
            myChatViewHolder.chatImageview.setVisibility(View.GONE);
            myChatViewHolder.smlProgress.setVisibility(View.GONE);
            myChatViewHolder.txtChatMessage.setText(message);
        }
        //   chatBean.timeForDelete
        myChatViewHolder.dateTime.setText(Constant.ConvertMilliSecondsToFormattedDateToTime(String.valueOf(chatBean.timestamp)));
        myChatViewHolder.chatImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatAdapterClickListner.clickedItemPosition(chatBean.imageUrl);
            }
        });
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, final int position) {
        final ChatBean chatBean = mChatBeen.get(position);

        final String message = mChatBeen.get(position).message;
        otherChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
        otherChatViewHolder.chatImageview.setVisibility(View.VISIBLE);
        otherChatViewHolder.smlProgress.setVisibility(View.VISIBLE);

        if (chatBean.image==1) {
            otherChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            Picasso.with(otherChatViewHolder.chatImageview.getContext()).load(chatBean.imageUrl).fit().into(otherChatViewHolder.chatImageview);
        } else {
            otherChatViewHolder.chatImageview.setVisibility(View.GONE);
            otherChatViewHolder.smlProgress.setVisibility(View.GONE);
            otherChatViewHolder.txtChatMessage.setText(message);
        }
        otherChatViewHolder.dateTime.setText(Constant.ConvertMilliSecondsToFormattedDateToTime(String.valueOf(chatBean.timestamp)));
        otherChatViewHolder.chatImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatAdapterClickListner.clickedItemPosition(chatBean.imageUrl);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mChatBeen != null) {
            return mChatBeen.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChatBeen.get(position).uid, mUid)) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, dateTime;
        private ImageView chatImageview;
        private ProgressBar smlProgress;
        ///private ShadowView myside;


        MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = itemView.findViewById(R.id.text_view_chat_message);
            dateTime = itemView.findViewById(R.id.dateTime);
            chatImageview = itemView.findViewById(R.id.chat_imageview);
            smlProgress = itemView.findViewById(R.id.smlProgress);
            /*myside = itemView.findViewById(R.id.myside);
            myside.setShadowDx(mContext.getResources().getDimension(R.dimen._5sdp));
            myside.setShadowDy(mContext.getResources().getDimension(R.dimen._5sdp));*/
        }
    }

    private class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, dateTime;
        private ImageView chatImageview;
        private ProgressBar smlProgress;
        //  private ShadowView myside;

        OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = itemView.findViewById(R.id.text_view_chat_message);
            dateTime = itemView.findViewById(R.id.dateTime);
            chatImageview = itemView.findViewById(R.id.chat_imageview);
            smlProgress = itemView.findViewById(R.id.smlProgress);
           /* myside = itemView.findViewById(R.id.myside);
            myside.setShadowDx(mContext.getResources().getDimension(R.dimen._minus5sdp));
            myside.setShadowDy(mContext.getResources().getDimension(R.dimen._5sdp));*/
        }
    }
}
