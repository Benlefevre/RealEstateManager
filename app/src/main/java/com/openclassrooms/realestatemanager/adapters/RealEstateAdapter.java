package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.controllers.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.data.entities.RealEstate;
import com.openclassrooms.realestatemanager.viewholder.RealEstateViewHolder;

import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateViewHolder> {

    private List<RealEstate> mRealEstates;
    private List<Pictures> mPictures;
    private View.OnClickListener mClickListener;


    public RealEstateAdapter(List<RealEstate> realEstates, List<Pictures> pictures) {
        mRealEstates = realEstates;
        mPictures = pictures;
    }

    @NonNull
    @Override
    public RealEstateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.real_estate_list_item, parent,false);
        RealEstateViewHolder realEstateViewHolder = new RealEstateViewHolder(view);
        realEstateViewHolder.itemView.setOnClickListener(v -> mClickListener.onClick(v));
        return new RealEstateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RealEstateViewHolder holder, int position) {
        holder.updateUI(mRealEstates.get(position),mPictures);
    }

    @Override
    public int getItemCount() {
        return mRealEstates.size();
    }

    public void setOnItemClickListener(View.OnClickListener clickListener){
        mClickListener = clickListener;
    }
}
