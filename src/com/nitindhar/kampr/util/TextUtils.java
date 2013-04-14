package com.nitindhar.kampr.util;

import java.text.NumberFormat;
import java.util.Locale;

public class TextUtils {

    private static final NumberFormat usFormat = NumberFormat.getIntegerInstance(Locale.US);

    public static String numberToUSFormat(int num) {
        return usFormat.format(num);
    }

}