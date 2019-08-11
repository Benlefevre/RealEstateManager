package com.openclassrooms.controllers.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.controllers.R;
import com.openclassrooms.injections.Injection;
import com.openclassrooms.injections.ViewModelFactory;
import com.openclassrooms.models.RealEstate;
import com.openclassrooms.utils.Utils;
import com.openclassrooms.viewmodel.RealEstateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_activity_text_view_main)
    TextView textViewMain;
    @BindView(R.id.activity_main_activity_text_view_quantity)
    TextView textViewQuantity;
    @BindView(R.id.ID)
    TextView mId;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.surface)
    TextView mSurface;
    @BindView(R.id.nbPiece)
    TextView mNbPiece;
    @BindView(R.id.nbBedrooms)
    TextView mNbBedrooms;
    @BindView(R.id.nbBathroom)
    TextView mNbBathroom;
    @BindView(R.id.photo_btn)
    Button mPhotoBtn;


    private RealEstateViewModel mRealEstateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
        configureViewModel();
        getRealEstate(1);
    }

    private void configureTextViewMain() {
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText("" + quantity);
    }


    //    Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.providerViewModelFactory(this);
        mRealEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void getRealEstate(long id) {
        mRealEstateViewModel.getRealEstate(id).observe(this, this::configureTest);
    }

    private void configureTest(RealEstate realEstate) {
        mId.setText(realEstate.getId() + "");
        mAddress.setText(realEstate.getAddress());
        mSurface.setText(realEstate.getSurface() + "");
        mNbPiece.setText(realEstate.getNbPieces() + "");
        mNbBedrooms.setText(realEstate.getNbBedrooms() + "");
        mNbBathroom.setText(realEstate.getNbBathrooms() + "");
    }

    @OnClick(R.id.photo_btn)
    public void onViewClicked() {

        
    }
}
