package com.openclassrooms.realestatemanager.utils;

import java.sql.SQLTransactionRollbackException;

public class Constants {

    public static final int READ_AND_WRITE_EXTERNAL_STORAGE = 1;
    public static final int ACCESS_LOCATION = 2;
    public static final int IMAGE_CAPTURE_CODE = 3;
    public static final int IMAGE_PICK_CODE = 4;

    public static final String ESTATE_LIST_FRAGMENT = "estateListFragment";
    public static final String DETAILS_FRAGMENT = "DetailsFragment";
    public static final String FULL_SCREEN_FRAGMENT = "FullScreenFragment";
    public static final String AGENT_LOCATION_FRAGMENT = "AgentLocationFragment";

    public static final String LOCATION_EXTRA = "locationDataExtra";
    public static final String RECEIVER_EXTRA = "receiverDataExtra";
    public static final String RESULT_EXTRA = "resultExtra";
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAILURE = 1;
}
