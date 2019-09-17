package com.openclassrooms.realestatemanager.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicturesDetailsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_details_video_view)
    VideoView mVideoView;
    @BindView(R.id.fragment_details_photo_img)
    ImageView mImageView;
    @BindView(R.id.fragment_details_photo_title)
    TextView mImageTitle;
    @BindView(R.id.fragment_details_delete_btn)
    ImageView mDeleteBtn;


    public PicturesDetailsViewHolder(@NonNull View itemView, int origin) {
        super(itemView);
        itemView.setTag(this);
        ButterKnife.bind(this, itemView);
        mDeleteBtn.setTag(this);
        if (origin == 1)
            mDeleteBtn.setVisibility(View.GONE);
    }

    /**
     * Checks if the Pictures in parameters is a photo or a movie and bind it in the right view.
     */
    public void bindPicturesInCorrespondingView(Pictures pictures) {
        if (pictures.getUri().toString().contains("MP4") ||
                pictures.getUri().toString().contains("VID")) {
            mImageView.setVisibility(View.INVISIBLE);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setVideoURI(pictures.getUri());
            mVideoView.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
            mVideoView.start();
            mImageTitle.setText(pictures.getDescription());
        } else {
            mImageView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            mImageView.setImageURI(pictures.getUri());
            mImageTitle.setText(pictures.getDescription());
        }
    }

}
