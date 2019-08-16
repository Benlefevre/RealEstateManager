package com.openclassrooms.realestatemanager.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class FullScreenViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Uri> mUriList;


    public FullScreenViewPagerAdapter(Context context, List<Uri> uriList) {
        mContext = context;
        mUriList = uriList;
    }

    @Override
    public int getCount() {
        return mUriList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageURI(mUriList.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
