package aabrasha.ua.streettranslator.service;

import aabrasha.ua.streettranslator.model.SortMethod;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Andrii Abramov on 12/25/16.
 */
public class SortingManager {

    private final Context context;

    public SortingManager(Context applicationContext) {
        this.context = applicationContext;
    }

    public SortMethod getCurrentSortingMethod() {
        String savedValue = getSortingPreferences()
                .getString(SortMethod.KEY_SORT_METHOD, SortMethod.BY_INSERTION_DATE.name());

        return SortMethod.valueOf(savedValue);
    }

    public void setCurrentSortingMethod(SortMethod sortMethod) {
        getSortingPreferences()
                .edit()
                .putString(SortMethod.KEY_SORT_METHOD, sortMethod.name())
                .apply();
    }


    private SharedPreferences getSortingPreferences() {
        return context.getSharedPreferences("sorting.preferences", MODE_PRIVATE);
    }


}
