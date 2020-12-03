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
import com.openclassrooms.realestatemanager.databinding.PicturesDetailsItemBinding;
import com.openclassrooms.realestatemanager.ui.viewholder.PicturesDetailsViewHolder;

import java.util.List;

public class DetailsPhotoAdapter extends RecyclerView.Adapter<PicturesDetailsViewHolder> {

    private final List<Pictures> mPictures;
    private final int mOrigin;

    private View.OnClickListener mClickListener;

    public DetailsPhotoAdapter(List<Pictures> pictures, int origin) {
        mPictures = pictures;
        mOrigin = origin;
    }

    @NonNull
    @Override
    public PicturesDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        PicturesDetailsItemBinding binding = PicturesDetailsItemBinding.inflate(LayoutInflater.from(context),parent,false);
        PicturesDetailsViewHolder picturesDetailsViewHolder = new PicturesDetailsViewHolder(binding, mOrigin, context);
//        According to the origin, the OnClickListener is set on the entire item or only on the delete button.
        if (mOrigin == 1) {
            picturesDetailsViewHolder.mBinding.getRoot().setOnClickListener(view1 -> mClickListener.onClick(view1));
            picturesDetailsViewHolder.mBinding.fragmentDetailsExoplayer.findViewById(R.id.exo_fullscreen_button).setVisibility(View.VISIBLE);
            picturesDetailsViewHolder.mBinding.fragmentDetailsExoplayer.findViewById(R.id.exo_fullscreen_button).setOnClickListener(view1 -> mClickListener.onClick(view1));
        }
        else{
            picturesDetailsViewHolder.mBinding.fragmentDetailsExoplayer.findViewById(R.id.exo_fullscreen_button).setVisibility(View.GONE);
            picturesDetailsViewHolder.mBinding.fragmentDetailsDeleteBtn.setOnClickListener(view1 -> mClickListener.onClick(view1));
        }
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

    @Override
    public void onViewRecycled(@NonNull PicturesDetailsViewHolder holder) {
        super.onViewRecycled(holder);
        Log.i("player","release player");
        holder.releasePlayer();
    }
}
