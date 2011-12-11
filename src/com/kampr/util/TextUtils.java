package com.kampr.util;

import android.text.Html;

public class TextUtils {
    
    public static String convertHtmlToText(String text) {
        return Html.fromHtml(text).toString();
    }
    
    public static String stripHtmlTag(String text, String htmlTag) {
        return text.replaceAll(htmlTag, "");
    }
    
    public static String cleanseText(String text) {
        return convertHtmlToText(text);
    }

}
