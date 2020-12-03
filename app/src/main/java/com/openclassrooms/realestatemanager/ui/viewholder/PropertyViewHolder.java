package com.openclassrooms.realestatemanager.ui.viewholder;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.databinding.PropertyListItemBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    private final Context mContext;
    public PropertyListItemBinding mBinding;

    public PropertyViewHolder(PropertyListItemBinding binding, Context context) {
        super(binding.getRoot());
        mBinding = binding;
        mBinding.getRoot().setTag(this);
        mContext = context;
    }

    /**
     * Checks that one pictures in the pictures list has the property's id in its attributes and bind
     * the selected picture in the ImageView.
     * Gets the property's details and bind them in the corresponding fields.
     */
    public void updateUI(Property property, List<Pictures> pictures) {
        for (Pictures pictures1 : pictures) {
            if (pictures1 != null) {
                if (pictures1.getRealEstateId() == property.getId()) {
                    Glide.with(mContext).load(pictures1.getUri().toString()).into(mBinding.itemPhotoImg);
                }
            }
        }
        mBinding.itemTypePropertyTxt.setText(property.getTypeProperty());
        mBinding.itemRoomsBedrooms.setText(mContext.getString(R.string.rooms_and_bedrooms, property.getNbRooms(), property.getNbBedrooms()));
        mBinding.itemSurface.setText(Utils.displayAreaUnitAccordingToPreferences(mContext, property.getSurface()));
        mBinding.itemCityTxt.setText(property.getCity());
        mBinding.itemPriceTxt.setText(Utils.displayCurrencyAccordingToPreferences(mContext, property.getPrice()));
    }

    public void changeBackground(int color){
        mBinding.getRoot().setBackgroundColor(mContext.getResources().getColor(color));
    }
}
