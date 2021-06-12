package com.ingenieriasoftware.sefitness.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String[] DAYS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    public static String getFormattedDate(long dateInMilliseconds){
        Date date = new Date(dateInMilliseconds);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        return df.format(date);
    }

    public static String getDaysFromFirebaseFormat(String days){
        String formattedDays = "";
        for(int i = 0; i < days.length(); i++){
            switch (days.charAt(i)){
                case '1':
                    formattedDays = formattedDays.concat(DAYS[i]+",");
                    break;
            }
        }
        return removeLastChars(formattedDays, 1);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }
}
