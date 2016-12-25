package aabrasha.ua.streettranslator.model;

import aabrasha.ua.streettranslator.R;
import android.content.Context;

/**
 * @author Andrii Abramov on 12/25/16.
 */
public enum SortMethod {


    BY_OLD_NAME(R.string.sort_method_old_name),
    BY_NEW_NAME(R.string.sort_method_new_name),
    BY_INSERTION_DATE(R.string.sort_method_insertion_date);


    public static final String KEY_SORT_METHOD = "KEY_SORT_METHOD";

    private final int stringId;

    SortMethod(int stringId) {
        this.stringId = stringId;
    }

    public String getLocalizedString(Context context) {
        return context.getString(stringId);
    }
}
