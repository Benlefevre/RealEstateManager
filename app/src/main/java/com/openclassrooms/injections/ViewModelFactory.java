package com.openclassrooms.injections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.data.repositories.RealEstateDataRepository;
import com.openclassrooms.data.repositories.PicturesDataRepository;
import com.openclassrooms.viewmodel.RealEstateViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RealEstateDataRepository mRealEstateDataRepository;
    private final PicturesDataRepository mPicturesDataRepository;
    private final Executor mExecutor;

    public ViewModelFactory(RealEstateDataRepository realEstateDataRepository, Executor executor,
                            PicturesDataRepository picturesDataRepository) {
        mRealEstateDataRepository = realEstateDataRepository;
        mExecutor = executor;
        mPicturesDataRepository = picturesDataRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateViewModel.class)){
            return (T) new RealEstateViewModel(mRealEstateDataRepository,mExecutor, mPicturesDataRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
