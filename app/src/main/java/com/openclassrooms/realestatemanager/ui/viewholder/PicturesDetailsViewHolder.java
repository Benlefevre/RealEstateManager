package com.openclassrooms.realestatemanager.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.ui.controllers.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicturesDetailsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_details_photo_img)
    ImageView mImageView;
    @BindView(R.id.fragment_details_photo_title)
    TextView mImageTitle;

    public PicturesDetailsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setTag(this);
        ButterKnife.bind(this,itemView);
    }

    public void bindPhotoInImgView(Pictures pictures){
        mImageView.setImageURI(pictures.getUri());
        mImageTitle.setText(pictures.getDescription());
    }

}
