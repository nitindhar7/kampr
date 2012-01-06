package com.kampr.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.Html;

public class TextUtils {
    
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
    
    public static String toMD5(String toConvert) {
        String hashedString = null;

        try{
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.update(toConvert.getBytes());
            byte messageDigest[] = algorithm.digest();
                    
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            hashedString = messageDigest.toString();
        } catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Could not convert string [" + toConvert + "] to MD5");
        }
        
        return hashedString;
    }

}
