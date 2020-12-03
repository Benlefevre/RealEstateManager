package com.openclassrooms.realestatemanager.ui.viewholder;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.openclassrooms.realestatemanager.databinding.FullscreenItemBinding;

public class FullScreenViewHolder extends RecyclerView.ViewHolder {

    public FullscreenItemBinding mBinding;
    public SimpleExoPlayer mPlayer;
    Context mContext;

    public FullScreenViewHolder(FullscreenItemBinding binding, Context context) {
        super(binding.getRoot());
        mBinding = binding;
        mContext = context;
        Log.i("player", "new holder");
    }

    /**
     * Checks if the Pictures in parameters is a photo or a movie and bind it in the right view.
     */
    public void bindPicturesInCorrespondingView(Uri uri) {
        Log.i("player", "uri :" + uri.toString());
        if (uri.toString().contains("mp4") ||
                uri.toString().contains("VID")) {
            preparePlayer(uri);
            Log.i("player", "new player");
        } else {
            mBinding.fullscreenPhotoImg.setVisibility(View.VISIBLE);
            mBinding.fullscreenExoplayer.setVisibility(View.GONE);
            Glide.with(mContext).load(uri.toString()).centerInside().into(mBinding.fullscreenPhotoImg);
        }
    }

    public void preparePlayer(Uri uri) {
        mPlayer = new SimpleExoPlayer.Builder(mContext).build();
        mBinding.fullscreenPhotoImg.setVisibility(View.INVISIBLE);
        mBinding.fullscreenExoplayer.setVisibility(View.VISIBLE);
        mBinding.fullscreenExoplayer.setPlayer(mPlayer);
        mBinding.fullscreenExoplayer.setKeepContentOnPlayerReset(true);
        MediaItem item = MediaItem.fromUri(uri);
        mPlayer.setMediaItem(item);
        mPlayer.setVolume(0f);
        mPlayer.prepare();
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            Log.i("player","release player");
            mPlayer.release();
        }
    }
}
