package com.nitindhar.kampr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.text.format.DateUtils;

public class TimeUtils {
    
    protected static final SimpleDateFormat _dbFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static String getPostDate(String timestamp) {
        try {
            long inMillis = _dbFormat.parse(timestamp).getTime();
            return DateUtils.formatDateTime(null, inMillis, DateUtils.FORMAT_ABBREV_ALL);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing post date", e);
        }
    }
    
    public static String getPostDate(long timeAsLong){
        return DateUtils.formatDateTime(null, timeAsLong, DateUtils.FORMAT_ABBREV_ALL);
    }

}
