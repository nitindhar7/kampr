package com.nitindhar.kampr.util;

import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;

public class SpanUtils extends URLSpan {


    public SpanUtils(String url) {
        super(url);
    }

    @Override
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

}