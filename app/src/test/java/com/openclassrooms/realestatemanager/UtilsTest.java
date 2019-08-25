package com.openclassrooms.realestatemanager;

import android.content.Context;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtilsTest {



    @Test
    public void convertStringToDateTest(){
        String string = null;
        assertNull(null);
        string = "2019-1-1";
        Date date = Utils.convertStringToDate(string);
        assertEquals("Tue Jan 01 00:00:00 CET 2019",date.toString());
    }

    @Test
    public void convertDateToStringTest(){
        Context context = mock(Context.class);
        Date date = null;
        when(context.getString(R.string.unknown_date)).thenReturn("Unknown date");
        assertEquals("Unknown date",Utils.convertDateToString(date,context));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        date = calendar.getTime();

        assertEquals("12-08-2019",Utils.convertDateToString(date,context));
    }
}
