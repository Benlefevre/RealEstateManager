package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.controllers.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.viewholder.PicturesDetailsViewHolder;

import java.util.List;

public class DetailsPhotoAdapter extends RecyclerView.Adapter<PicturesDetailsViewHolder> {

    private List<Pictures> mPictures;

    private View.OnClickListener mClickListener;

    public DetailsPhotoAdapter(List<Pictures> pictures) {
        mPictures = pictures;
    }

    @NonNull
    @Override
    public PicturesDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.pictures_details_item, parent, false);
        PicturesDetailsViewHolder picturesDetailsViewHolder = new PicturesDetailsViewHolder(view);
        picturesDetailsViewHolder.itemView.setOnClickListener(view1 -> mClickListener.onClick(view1));
        return picturesDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PicturesDetailsViewHolder holder, int position) {
        holder.bindPhotoInImgView(mPictures.get(position));
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public void setOnClickListener(View.OnClickListener clickListener){
        mClickListener = clickListener;
    }
}
