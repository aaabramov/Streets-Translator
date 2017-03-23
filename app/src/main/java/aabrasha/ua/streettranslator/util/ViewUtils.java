package aabrasha.ua.streettranslator.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * @author Andrii Abramov on 9/15/16.
 */
public final class ViewUtils {

    public static String getText(TextView v) {
        return v.getText().toString();
    }

    public static void focusWithKeyboard(View v) {
        Context context = v.getContext();
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        v.requestFocusFromTouch();
    }

}
