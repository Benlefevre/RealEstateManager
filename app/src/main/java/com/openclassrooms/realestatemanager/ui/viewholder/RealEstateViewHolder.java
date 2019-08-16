package com.openclassrooms.realestatemanager.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.ui.controllers.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_photo_img)
    ImageView mImageView;
    @BindView(R.id.item_type_property_txt)
    TextView mTypeProperty;
    @BindView(R.id.item_city_txt)
    TextView mCity;
    @BindView(R.id.item_price_txt)
    TextView mPrice;

    public RealEstateViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setTag(this);
    }

    public void updateUI(RealEstate realEstate, List<Pictures> pictures){
        for (Pictures pictures1 : pictures){
            if (pictures1.getRealEstateId() == realEstate.getId())
                mImageView.setImageURI(pictures1.getUri());
        }
        mTypeProperty.setText(realEstate.getTypeProperty());
        mCity.setText(realEstate.getCity());
        mPrice.setText(String.valueOf(realEstate.getPrice()));
    }
}
