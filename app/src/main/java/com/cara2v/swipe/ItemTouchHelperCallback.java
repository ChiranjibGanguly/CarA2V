/*
package com.cara2v.swipe;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.cara2v.adapter.PromoCodeSwipeAdapter;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof PromoCodeSwipeAdapter.ItemNoSwipeViewHolder) {
            return 0;
        }
        return makeMovementFlags(ItemTouchHelper.UP| ItemTouchHelper.DOWN, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        PromoCodeSwipeAdapter adapter = (PromoCodeSwipeAdapter) recyclerView.getAdapter();
        adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (dY != 0 && dX == 0) super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        PromoCodeSwipeAdapter.ItemBaseViewHolder holder = (PromoCodeSwipeAdapter.ItemBaseViewHolder) viewHolder;
        if (viewHolder instanceof PromoCodeSwipeAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) {
            if (dX < -holder.mActionContainer.getWidth()) {
                dX = -holder.mActionContainer.getWidth();
            }
            holder.mViewContent.setTranslationX(dX);
            return;
        }
        if (viewHolder instanceof PromoCodeSwipeAdapter.ItemBaseViewHolder)
            holder.mViewContent.setTranslationX(dX);
    }
}*/
