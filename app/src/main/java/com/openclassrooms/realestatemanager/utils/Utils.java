package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import androidx.preference.PreferenceManager;

import com.openclassrooms.realestatemanager.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.9045);
    }


    public static int convertEurosToDollars(int euros) {
        return (int) Math.round(euros * 1.1059);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     */
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     */
    public static Boolean isInternetAvailable(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return Objects.requireNonNull(wifi).isWifiEnabled();
    }

    public static boolean isNetworkAccessEnabled(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static Date convertStringToDate(String string) {
        if (string == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertDateToString(Date date, Context context) {
        if (date == null)
            return context.getString(R.string.unknown_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        return simpleDateFormat.format(date);

    }

    public static int convertSquareFeetIntoSquareMeters(int surface) {
        return (int) Math.round(surface / 10.764);
    }

    public static int convertSquareMetersIntoSquareFeet(int surface) {
        return (int) Math.round(surface * 10.764);
    }

    public static String displayAreaUnitAccordingToPreferences(Context context, int surface) {
        if (context == null)
            return null;
        String surfaceText;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String area = preferences.getString("area", "sq ft");
        if (!area.equals("sq ft"))
            surfaceText = convertSquareFeetIntoSquareMeters(surface) + " m²";
        else
            surfaceText = surface + " sq ft";
        return surfaceText;
    }

    public static int convertAreaAccordingToPreferences(Context context, String surfaceText) {
        if (context == null)
            return 0;
        int surface;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String area = preferences.getString("area", "sq ft");
        if (!area.equals("sq ft"))
            surface = convertSquareMetersIntoSquareFeet(Integer.parseInt(surfaceText));
        else
            surface = Integer.parseInt(surfaceText);
        return surface;
    }

    public static String displayCurrencyAccordingToPreferences(Context context, int price){
        if (context == null)
            return null;
        String priceText;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String currency = preferences.getString("currency","$");
        if (!currency.equals("$"))
            priceText = convertDollarToEuro(price) + "€";
        else
            priceText = String.format(Locale.US,"%,d",price) + "$";
        return priceText;
    }

    public static int convertPriceAccordingToPreferences(Context context, String priceText){
        if (context == null)
            return 0;
        int price;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String currency = preferences.getString("currency","$");
        if (!currency.equals("$"))
            price = convertEurosToDollars(Integer.parseInt(priceText));
        else
            price = Integer.parseInt(priceText);
        return price;
    }


}
