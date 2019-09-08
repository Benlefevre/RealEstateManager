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

    /**
     * Converts a price expressed in Euros in Dollars.
     */
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

    /**
     * Verifies if the device has a network access and return a boolean.
     */
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

    /**
     * Converts a given string in a date with defined format.
     */
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

    /**
     * Converts a date in a string. Needs a context to retrieve a string resource.
     */
    public static String convertDateToString(Date date, Context context) {
        if (date == null)
            return context.getString(R.string.unknown_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        return simpleDateFormat.format(date);

    }

    /**
     * Converts a surface expressed in Sq ft into m²
     */
    public static int convertSquareFeetIntoSquareMeters(int surface) {
        return (int) Math.round(surface / 10.764);
    }

    /**
     * Converts an area expressed in m² into Sq ft
     */
    public static int convertSquareMetersIntoSquareFeet(int surface) {
        return (int) Math.round(surface * 10.764);
    }

    /**
     * Returns a surface with a suffix according to the user's preferences.
     * Converts the surface if needed.
     */
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

    /**
     * Converts a surface in Sq ft according to user's preferences. Needed to save surface in db with
     * only one measurement unit.
     */
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

    /**
     * Converts a surface with the correct measurement unit according to the user's preferences. Needed
     * to display the surface when user need to edit the real estate.
     */
    public static int convertAreaAccordingToPreferencesToEdit(Context context, int surface){
        if (context == null)
            return 0;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String area = preferences.getString("area", "sq ft");
        if (!area.equals("sq ft"))
            return convertSquareFeetIntoSquareMeters(surface);
        else
            return surface;
    }

    /**
     * Returns a price with a suffix according to the user's preferences.
     * Converts the price if needed.
     */
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

    /**
     * Converts a price in Dollars according to user's preferences. Needed to save surface in db with
     * only one currency.
     */
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

    /**
     * Converts a price with the correct currency according to the user's preferences. Needed
     * to display the price when user need to edit the real estate.
     */
    public static int convertPriceAccordingToPreferenceToEdit(Context context, int price){
        if (context == null)
            return 0;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String currency = preferences.getString("currency", "$");
        if (!currency.equals("$"))
            return convertDollarToEuro(price);
        else
            return price;
    }

}
