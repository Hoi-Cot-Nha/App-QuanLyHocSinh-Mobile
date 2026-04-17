package com.example.quanlyhocsinhmobile.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class FormatDate {
    public static String formatDateForDisplay(String storageDate) {
        if (storageDate == null || storageDate.isEmpty()) return "";
        Date parsed = parseDate(storageDate, "dd/MM/yyyy");
        if (parsed == null) {
            parsed = parseDate(storageDate, "yyyy-MM-dd");
        }
        if (parsed == null) return storageDate;
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsed);
    }
    public static String normalizeDateToStorage(String value) {
        if (value == null || value.isEmpty()) return null;
        Date parsed = parseDate(value, "dd/MM/yyyy");
        if (parsed == null) {
            parsed = parseDate(value, "yyyy-MM-dd");
        }
        if (parsed == null) return null;
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsed);
    }
    public static Date parseDate(String value, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            sdf.setLenient(false);
            return sdf.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }
}