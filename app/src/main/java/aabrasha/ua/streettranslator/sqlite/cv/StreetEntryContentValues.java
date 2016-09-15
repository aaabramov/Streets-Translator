package aabrasha.ua.streettranslator.sqlite.cv;

import aabrasha.ua.streettranslator.model.StreetEntry;
import android.content.ContentValues;

import static aabrasha.ua.streettranslator.sqlite.StreetsDatabase.*;

/**
 * Created by Andrii Abramov on 9/15/16.
 */
public class StreetEntryContentValues {

    public static ContentValues toContentValues(StreetEntry item) {
        ContentValues result = new ContentValues();

        String oldName = item.getOldName();
        result.put(STREETS_COLUMN_OLD_NAME, oldName);
        result.put(STREETS_COLUMN_OLD_NAME_CMP, oldName.toUpperCase());

        String newName = item.getNewName();
        if (newName != null) {
            result.put(STREETS_COLUMN_NEW_NAME, newName);
            result.put(STREETS_COLUMN_NEW_NAME_CMP, newName.toUpperCase());
        } else {
            result.putNull(STREETS_COLUMN_NEW_NAME);
            result.putNull(STREETS_COLUMN_NEW_NAME_CMP);
        }

        result.put(STREETS_COLUMN_DESCRIPTION, item.getDescription());
        return result;
    }

}
