package com.nitindhar.kampr.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.widget.Button;
import android.widget.TextView;

public class SpanUtils extends URLSpan {
    
    public static final String FONT_REGULAR = "Roboto-Condensed.ttf";
    public static final String FONT_BOLD = "Roboto-BoldCondensed.ttf";
    public static final String FONT_ITALIC = "Roboto-Italic.ttf";
    
    public SpanUtils(String url) {
        super(url);
    }
 
    public void updateDrawState(TextPaint drawState) {
        super.updateDrawState(drawState);
        drawState.setUnderlineText(false);
    }
    
    /**
     * Removes URL underlines in a string by replacing URLSpan occurrences by
     * URLSpanUtils objects.
     *
     * @param text A Spannable object. For example, a TextView casted as Spannable.
     */
    public static void removeUnderlines(Spannable text) {
        URLSpan[] spans = text.getSpans(0, text.length(), URLSpan.class);
     
        for(URLSpan span:spans) {
            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);
            text.removeSpan(span);
            span = new SpanUtils(span.getURL());
            text.setSpan(span, start, end, 0);
        }
     }
    
    /**
     * Set the font on a textview using the regular font as default 
     * @param context
     * @param textview
     */
    public static void setFont(Context context, TextView textview) {
        if (textview != null) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), FONT_REGULAR);
            textview.setTypeface(font);
        }
    }
    
    /**
     * Set the font on a textview
     * @param context
     * @param button
     * @param asset name of the font to use
     */
    public static void setFont(Context context, TextView textview, String asset) {
        if (textview != null) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), asset);
            textview.setTypeface(font);
        }
    }
    
    /**
     * Set the font on a textview using the regular font as default 
     * @param context
     * @param button
     */
    public static void setFont(Context context, Button button) {
        if (button != null) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), FONT_REGULAR);
            button.setTypeface(font);
        }
    }
    
    /**
     * Set the font on a button
     * @param context
     * @param button
     * @param asset name of the font to use
     */
    public static void setFont(Context context, Button button, String asset) {
        if (button != null) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), asset);
            button.setTypeface(font);
        }
    }

}
