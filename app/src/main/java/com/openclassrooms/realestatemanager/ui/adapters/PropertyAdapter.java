package com.openclassrooms.realestatemanager.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.Property;
import com.openclassrooms.realestatemanager.databinding.PropertyListItemBinding;
import com.openclassrooms.realestatemanager.ui.viewholder.PropertyViewHolder;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyViewHolder> {

    private final List<Property> mProperties;
    private final List<Pictures> mPictures;
    private View.OnClickListener mClickListener;
    public int index = -1;


    public PropertyAdapter(List<Property> properties, List<Pictures> pictures) {
        mProperties = properties;
        mPictures = pictures;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        PropertyListItemBinding binding = PropertyListItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new PropertyViewHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        holder.mBinding.getRoot().setOnClickListener(v -> {
            mClickListener.onClick(v);
            index = position;
            notifyDataSetChanged();
        });
        Log.i("player", " index : " + index);
        holder.updateUI(mProperties.get(position), mPictures);
        if(index == position){
            holder.changeBackground(R.color.listSelectedColor);
        }else{
            holder.changeBackground(R.color.colorOnPrimary);
        }
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}
