package com.openclassrooms.realestatemanager.ui.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.databinding.PicturesDetailsItemBinding;

public class PicturesDetailsViewHolder extends RecyclerView.ViewHolder {

    public PicturesDetailsItemBinding mBinding;
    Context mContext;
    private SimpleExoPlayer mPlayer;

    public PicturesDetailsViewHolder(PicturesDetailsItemBinding binding, int origin, Context context) {
        super(binding.getRoot());
        mBinding = binding;
        mBinding.getRoot().setTag(this);
        mBinding.fragmentDetailsDeleteBtn.setTag(this);
        mBinding.fragmentDetailsExoplayer.findViewById(R.id.exo_fullscreen_button).setTag(this);
        if (origin == 1)
            mBinding.fragmentDetailsDeleteBtn.setVisibility(View.GONE);
        mContext = context;
    }

    /**
     * Checks if the Pictures in parameters is a photo or a movie and bind it in the right view.
     */
    public void bindPicturesInCorrespondingView(Pictures pictures) {
        Log.i("player", "uri : " + pictures.getUri().toString());
        if (pictures.getUri().toString().contains("mp4") ||
                pictures.getUri().toString().contains("VID")) {
            preparePlayer(pictures);
        } else {
            mBinding.fragmentDetailsPhotoImg.setVisibility(View.VISIBLE);
            mBinding.fragmentDetailsExoplayer.setVisibility(View.GONE);
            Glide.with(mContext).load(pictures.getUri().toString()).into(mBinding.fragmentDetailsPhotoImg);
        }
        mBinding.fragmentDetailsPhotoTitle.setText(pictures.getDescription());
        mBinding.fragmentDetailsPhotoTitle.setVisibility(View.GONE);
    }

    private void preparePlayer(Pictures picture){
        mPlayer = new SimpleExoPlayer.Builder(mContext).build();
        mBinding.fragmentDetailsPhotoImg.setVisibility(View.INVISIBLE);
        mBinding.fragmentDetailsExoplayer.setVisibility(View.VISIBLE);
        mBinding.fragmentDetailsExoplayer.setPlayer(mPlayer);
        MediaItem item = MediaItem.fromUri(picture.getUri());
        mPlayer.setMediaItem(item);
        mPlayer.setVolume(0f);
        mPlayer.prepare();
    }

    public void releasePlayer(){
        if(mPlayer != null){
            mPlayer.release();
        }
    }

}
