package com.openclassrooms.realestatemanager.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.entities.Pictures;
import com.openclassrooms.realestatemanager.ui.viewholder.PicturesDetailsViewHolder;

import java.util.List;

public class DetailsPhotoAdapter extends RecyclerView.Adapter<PicturesDetailsViewHolder> {

    private List<Pictures> mPictures;
    private int mOrigin;

    private View.OnClickListener mClickListener;

    public DetailsPhotoAdapter(List<Pictures> pictures, int origin) {
        mPictures = pictures;
        mOrigin = origin;
    }

    @NonNull
    @Override
    public PicturesDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.pictures_details_item, parent, false);
        PicturesDetailsViewHolder picturesDetailsViewHolder = new PicturesDetailsViewHolder(view, mOrigin);
//        According to the origin, the OnClickListener is set on the entire item or only on the delete button.
        if (mOrigin == 1)
            picturesDetailsViewHolder.itemView.setOnClickListener(view1 -> mClickListener.onClick(view1));
        else
            picturesDetailsViewHolder.itemView.findViewById(R.id.fragment_details_delete_btn).setOnClickListener(view1 -> mClickListener.onClick(view1));
        return picturesDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PicturesDetailsViewHolder holder, int position) {
        holder.bindPicturesInCorrespondingView(mPictures.get(position));
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}
