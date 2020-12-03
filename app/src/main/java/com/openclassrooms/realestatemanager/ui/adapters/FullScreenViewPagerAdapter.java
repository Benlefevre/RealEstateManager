package com.openclassrooms.realestatemanager.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.openclassrooms.realestatemanager.R;

import java.util.List;
import java.util.Objects;

public class FullScreenViewPagerAdapter extends PagerAdapter {

    private final Context mContext;
    private final List<Uri> mUriList;
    private SimpleExoPlayer mPlayer;


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
        View itemView = Objects.requireNonNull(inflater).inflate(R.layout.viewpager_item, container, false);
        StyledPlayerView playerView = itemView.findViewById(R.id.viewpager_exo_player);
        ImageView imageView = itemView.findViewById(R.id.viewpager_img_view);
//        Depending on whether the pictures is an image or a video, it is bound in an ImageView or a StyledPlayerView (ExoPlayer).
        if (mUriList.get(position).toString().contains("Movies") ||
                mUriList.get(position).toString().contains("VID")) {
            imageView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            mPlayer = new SimpleExoPlayer.Builder(mContext).build();
            playerView.setPlayer(mPlayer);
            MediaItem item = MediaItem.fromUri(mUriList.get(position));
            mPlayer.setMediaItem(item);
            mPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            mPlayer.setVolume(0f);
            mPlayer.prepare();
            mPlayer.play();
            playerView.hideController();
        } else {
            imageView.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            Glide.with(mContext).load(mUriList.get(position)).into(imageView);
//            imageView.setImageURI(mUriList.get(position));
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (mPlayer != null) {
            mPlayer.release();
        }
        container.removeView((View) object);
    }
}
