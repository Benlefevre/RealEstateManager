package com.openclassrooms.realestatemanager.ui.viewholder;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_photo_img)
    ImageView mImageView;
    @BindView(R.id.item_type_property_txt)
    TextView mTypeProperty;
    @BindView(R.id.item_rooms_bedrooms)
    TextView mNbRoomsBedrooms;
    @BindView(R.id.item_surface)
    TextView mSurface;
    @BindView(R.id.item_city_txt)
    TextView mCity;
    @BindView(R.id.item_price_txt)
    TextView mPrice;

    private Context mContext;

    public PropertyViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
        mContext = context;
    }

    public void updateUI(Property property, List<Pictures> pictures) {
        for (Pictures pictures1 : pictures) {
            if (pictures1.getRealEstateId() == property.getId())
                mImageView.setImageURI(pictures1.getUri());
        }
        mTypeProperty.setText(property.getTypeProperty());
        mNbRoomsBedrooms.setText(mContext.getString(R.string.rooms_and_bedrooms, property.getNbRooms(), property.getNbBedrooms()));
        mSurface.setText(Utils.displayAreaUnitAccordingToPreferences(mContext, property.getSurface()));
        mCity.setText(property.getCity());
        mPrice.setText(Utils.displayCurrencyAccordingToPreferences(mContext, property.getPrice()));
    }
}
