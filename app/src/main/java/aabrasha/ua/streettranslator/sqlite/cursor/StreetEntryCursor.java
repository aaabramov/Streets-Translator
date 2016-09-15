package aabrasha.ua.streettranslator.sqlite.cursor;

import aabrasha.ua.streettranslator.model.StreetEntry;
import android.database.Cursor;

/**
 * Created by Andrii Abramov on 9/15/16.
 */
public class StreetEntryCursor extends CursorWrapper<StreetEntry> {

    public StreetEntryCursor(Cursor dbCursor) {
        super(dbCursor);
    }

    @Override
    public StreetEntry parseModel() {
        Cursor cursor = getCursor();
        return fillInModel(cursor);
    }

    private StreetEntry fillInModel(Cursor cursor) {
        int id = cursor.getInt(0);
        String oldName = cursor.getString(1);
        String newName = cursor.getString(2);
        String description = cursor.getString(3);
        StreetEntry result = StreetEntry.from(oldName, newName, description);
        result.setId(id);
        return result;
    }
}
