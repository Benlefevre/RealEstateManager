package com.openclassrooms.realestatemanager.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.ui.viewholder.PropertyViewHolder;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyViewHolder> {

    private List<Property> mProperties;
    private List<Pictures> mPictures;
    private View.OnClickListener mClickListener;


    public PropertyAdapter(List<Property> properties, List<Pictures> pictures) {
        mProperties = properties;
        mPictures = pictures;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.property_list_item, parent, false);
        PropertyViewHolder propertyViewHolder = new PropertyViewHolder(view, context);
        propertyViewHolder.itemView.setOnClickListener(v -> mClickListener.onClick(v));
        return propertyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        holder.updateUI(mProperties.get(position), mPictures);
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}
