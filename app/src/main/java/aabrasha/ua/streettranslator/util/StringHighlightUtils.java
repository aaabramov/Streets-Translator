package aabrasha.ua.streettranslator.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Andrii Abramov on 9/8/16.
 */
public class StringHighlightUtils {

    private static final int HIGHLIGHT_COLOR = Color.RED;
    private static final ForegroundColorSpan FOREGROUND = new ForegroundColorSpan(HIGHLIGHT_COLOR);

    public static Spannable highlight(String s, String pattern) {

        if (StringUtils.isEmptyOrNull(s))
            return createSpannable("");

        Spannable result = createSpannable(s);

        int start = getCaseInsensitiveIndexOf(s, pattern);
        if (start != -1) {
            int finish = start + pattern.length();
            result.setSpan(FOREGROUND, start, finish, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return result;
    }

    private static int getCaseInsensitiveIndexOf(String where, String what) {
        return where.toLowerCase().indexOf(what.toLowerCase());
    }

    private static Spannable createSpannable(String from) {
        return Spannable.Factory.getInstance().newSpannable(from);
    }

}
