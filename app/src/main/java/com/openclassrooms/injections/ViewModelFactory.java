package com.openclassrooms.injections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.repositories.RealEstateDataRepository;
import com.openclassrooms.viewmodel.RealEstateViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RealEstateDataRepository mRealEstateDataRepository;
    private final Executor mExecutor;

    public ViewModelFactory(RealEstateDataRepository realEstateDataRepository, Executor executor) {
        mRealEstateDataRepository = realEstateDataRepository;
        mExecutor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateViewModel.class)){
            return (T) new RealEstateViewModel(mRealEstateDataRepository,mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
