package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtilsTest {


    @Test
    public void convertStringToDateTest() {
        String string = null;
        assertNull(Utils.convertStringToDate(string));
        string = "1-1-2019";
        Date date = Utils.convertStringToDate(string);
        assertEquals("Tue Jan 01 00:00:00 CET 2019", date.toString());
    }

    @Test
    public void convertDateToStringTest() {
        Context context = mock(Context.class);
        Date date = null;
        when(context.getString(R.string.unknown_date)).thenReturn("Unknown date");
        assertEquals("Unknown date", Utils.convertDateToString(date, context));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        date = calendar.getTime();

        assertEquals("12-08-2019", Utils.convertDateToString(date, context));
    }

    @Test
    public void convertSquareFeetIntoSquareMetersTest() {
        int surface = 0;
        assertEquals(0, Utils.convertSquareFeetIntoSquareMeters(surface));

        surface = 11;
        assertEquals(1, Utils.convertSquareFeetIntoSquareMeters(surface));

        surface = 100;
        assertEquals(9, Utils.convertSquareFeetIntoSquareMeters(surface));
    }

    @Test
    public void convertSquareMetersIntoSquareFeetTest(){
        int surface = 0;
        assertEquals(0,Utils.convertSquareMetersIntoSquareFeet(surface));

        surface = 1;
        assertEquals(11, Utils.convertSquareMetersIntoSquareFeet(surface));

        surface = 100;
        assertEquals(1076,Utils.convertSquareMetersIntoSquareFeet(surface));
    }

    @Test
    public void displayAreaUnitAccordingToPreferencesTest(){
        Context context = Mockito.mock(Context.class);
        SharedPreferences preferences = Mockito.mock(SharedPreferences.class);
        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(preferences);

        when(preferences.getString("area","sq ft")).thenReturn("sq ft");
        assertEquals("100 sq ft",Utils.displayAreaUnitAccordingToPreferences(context,100));

        when(preferences.getString("area","sq ft")).thenReturn("m²");
        assertEquals("9 m²",Utils.displayAreaUnitAccordingToPreferences(context,100));
    }

    @Test
    public void convertAreaAccordingToPreferencesTest(){
        Context context = Mockito.mock(Context.class);
        SharedPreferences preferences = Mockito.mock(SharedPreferences.class);
        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(preferences);

        when(preferences.getString("area", "sq ft")).thenReturn("sq ft");
        assertEquals(100,Utils.convertAreaAccordingToPreferences(context,"100"));

        when(preferences.getString("area", "sq ft")).thenReturn("m²");
        assertEquals(1076,Utils.convertAreaAccordingToPreferences(context,"100"));
    }

    @Test
    public void convertDollarsToEurosTest(){
        int price = 0;
        assertEquals(0,Utils.convertDollarToEuro(price));

        price = 1;
        assertEquals(1,Utils.convertDollarToEuro(price));

        price = 10;
        assertEquals(9,Utils.convertDollarToEuro(price));

        price = 100;
        assertEquals(90,Utils.convertDollarToEuro(price));
    }

    @Test
    public void convertEurosToDollarsTest(){
        int price = 0;
        assertEquals(0,Utils.convertEurosToDollars(price));

        price = 1;
        assertEquals(1,Utils.convertEurosToDollars(price));

        price = 10;
        assertEquals(11,Utils.convertEurosToDollars(price));

        price = 100;
        assertEquals(111, Utils.convertEurosToDollars(price));
    }

    @Test
    public void displayCurrencyAccordingToPreferencesTest(){
        Context context = Mockito.mock(Context.class);
        SharedPreferences preferences = Mockito.mock(SharedPreferences.class);
        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(preferences);

        when(preferences.getString("currency","$")).thenReturn("$");
        assertEquals("100$",Utils.displayCurrencyAccordingToPreferences(context,100));

        when(preferences.getString("currency","$")).thenReturn("€");
        assertEquals("90€",Utils.displayCurrencyAccordingToPreferences(context,100));
    }

    @Test
    public void convertPriceAccordingToPreferencesTest(){
        Context context = Mockito.mock(Context.class);
        SharedPreferences preferences = Mockito.mock(SharedPreferences.class);
        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(preferences);

        when(preferences.getString("currency", "$")).thenReturn("$");
        assertEquals(100,Utils.convertPriceAccordingToPreferences(context,"100"));

        when(preferences.getString("currency", "$")).thenReturn("€");
        assertEquals(111,Utils.convertPriceAccordingToPreferences(context,"100"));
    }

}
