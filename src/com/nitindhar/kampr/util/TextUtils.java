package com.nitindhar.kampr.util;

import java.text.NumberFormat;
import java.util.Locale;

import android.text.Html;

public class TextUtils {
    
    private static final NumberFormat _usFormat = NumberFormat.getIntegerInstance(Locale.US);
    
    /**
     * Synonymous to innerHTML in Javascript
     * @param text HTML to extract text from
     * @return
     */
    public static String convertHtmlToText(String text) {
        return Html.fromHtml(text).toString().trim();
    }
    
    public static String stripHtmlTag(String text, String htmlTag) {
        return text.replaceAll(htmlTag, "");
    }
    
    public static String cleanseText(String text) {
        return convertHtmlToText(text);
    }
    
    public static String numberToUSFormat(int num) {
        return _usFormat.format(num);
    }

}
