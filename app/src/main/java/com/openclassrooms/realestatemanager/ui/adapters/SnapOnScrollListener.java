package com.openclassrooms.realestatemanager.ui.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


public class SnapOnScrollListener extends RecyclerView.OnScrollListener {

    SnapHelper mSnapHelper;
    Behavior mBehavior;
    int mSnapPosition = RecyclerView.NO_POSITION;
    OnSnapPositionChangeListener mOnSnapPositionChangeListener;
    public SnapOnScrollListener(SnapHelper snapHelper, Behavior behavior, OnSnapPositionChangeListener onSnapPositionChangeListener) {
        mSnapHelper = snapHelper;
        mBehavior = behavior;
        mOnSnapPositionChangeListener = onSnapPositionChangeListener;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (mBehavior == Behavior.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView);
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (mBehavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE && newState == RecyclerView.SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange(recyclerView);
        }
    }

    private void maybeNotifySnapPositionChange(RecyclerView recyclerView) {
        int snapPosition = getSnapPosition(recyclerView);
        boolean snapPositionChanged = mSnapPosition != snapPosition;
        if (snapPositionChanged) {
            mOnSnapPositionChangeListener.onSnapPositionChange(mSnapPosition,snapPosition);
            mSnapPosition = snapPosition;
        }
    }

    private int getSnapPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        View snapView = mSnapHelper.findSnapView(layoutManager);
        return layoutManager.getPosition(snapView);
    }

    public enum Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

}
