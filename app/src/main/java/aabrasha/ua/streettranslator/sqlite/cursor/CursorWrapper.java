package aabrasha.ua.streettranslator.sqlite.cursor;

import android.database.Cursor;

/**
 * @author Andrii Abramov on 9/15/16.
 */
public abstract class CursorWrapper<T> {

    private final Cursor cursor;

    public CursorWrapper(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public abstract T parseModel();

}
