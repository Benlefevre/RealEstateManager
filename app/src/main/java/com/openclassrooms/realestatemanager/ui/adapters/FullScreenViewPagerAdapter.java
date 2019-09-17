package com.openclassrooms.realestatemanager.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.openclassrooms.realestatemanager.R;

import java.util.List;
import java.util.Objects;

public class FullScreenViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Uri> mUriList;
    private VideoView mVideoView;
    private MediaController mMediaController;


    public FullScreenViewPagerAdapter(Context context, List<Uri> uriList) {
        mContext = context;
        mUriList = uriList;
    }

    @Override
    public int getCount() {
        return mUriList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = Objects.requireNonNull(inflater).inflate(R.layout.viewpager_item,container,false);
        mVideoView = itemView.findViewById(R.id.viewpager_video_view);
        ImageView imageView = itemView.findViewById(R.id.viewpager_img_view);
//        Depending on whether the pictures is an image or a video, it is bound in an ImageView or a VideoView.
        if (mUriList.get(position).toString().contains("Movies") ||
                mUriList.get(position).toString().contains("VID")) {
            imageView.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            mMediaController = new MediaController(mContext);
            mMediaController.setAnchorView(container);
            mVideoView.setVideoURI(mUriList.get(position));
//            mVideoView.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
            mVideoView.setMediaController(mMediaController);
            mVideoView.start();
            container.addView(itemView);
        } else {
            imageView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            imageView.setImageURI(mUriList.get(position));
            container.addView(itemView);
        }
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mVideoView.setMediaController(null);
        container.removeView((View) object);
    }
}
