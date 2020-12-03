package com.openclassrooms.realestatemanager.injections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.data.repositories.PicturesDataRepository;
import com.openclassrooms.realestatemanager.data.repositories.PropertyDataRepository;
import com.openclassrooms.realestatemanager.ui.viewmodel.RealEstateViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PropertyDataRepository mPropertyDataRepository;
    private final PicturesDataRepository mPicturesDataRepository;
    private final Executor mExecutor;

    ViewModelFactory(PropertyDataRepository propertyDataRepository, Executor executor,
                     PicturesDataRepository picturesDataRepository) {
        mPropertyDataRepository = propertyDataRepository;
        mExecutor = executor;
        mPicturesDataRepository = picturesDataRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateViewModel.class))
            return (T) new RealEstateViewModel(mPropertyDataRepository, mExecutor, mPicturesDataRepository);
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
