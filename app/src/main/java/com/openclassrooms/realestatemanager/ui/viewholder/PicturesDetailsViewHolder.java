package com.openclassrooms.realestatemanager.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicturesDetailsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_details_photo_img)
    ImageView mImageView;
    @BindView(R.id.fragment_details_photo_title)
    TextView mImageTitle;
    @BindView(R.id.fragment_details_delete_btn)
    ImageView mDeleteBtn;


    public PicturesDetailsViewHolder(@NonNull View itemView, int origin) {
        super(itemView);
        itemView.setTag(this);
        ButterKnife.bind(this,itemView);
        mDeleteBtn.setTag(this);
        if (origin == 1)
            mDeleteBtn.setVisibility(View.GONE);
    }

    public void bindPhotoInImgView(Pictures pictures){
        mImageView.setImageURI(pictures.getUri());
        mImageTitle.setText(pictures.getDescription());
    }

}
