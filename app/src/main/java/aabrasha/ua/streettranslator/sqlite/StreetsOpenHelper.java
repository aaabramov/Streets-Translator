package aabrasha.ua.streettranslator.sqlite;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.util.IOUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrii Abramov on 8/28/16.
 */
public class StreetsOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = StreetsOpenHelper.class.getSimpleName();

    private static final int VERSION = 4;
    private static final String DB_NAME = "street_entries.db";
    private static final String STREETS_TABLE_NAME = "streets";

    private final String CREATE_SCRIPT;
    private final String UPDATE_SCRIPT;

    public StreetsOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        Log.d(TAG, "StreetsOpenHelper: Instantiating Streets database");
        CREATE_SCRIPT = IOUtils.readAll(context.getResources().openRawResource(R.raw.streets_init));
        UPDATE_SCRIPT = IOUtils.readAll(context.getResources().openRawResource(R.raw.streets_update));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Creating database: " + DB_NAME);
        Log.d(TAG, "onCreate: Database version: " + VERSION);
        Log.d(TAG, "onCreate: executing script: " + CREATE_SCRIPT);
        db.execSQL(CREATE_SCRIPT);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d(TAG, "onOpen: opening database: " + DB_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Updating database: " + DB_NAME);
        Log.d(TAG, String.format("onUpgrade: from %d to %d", oldVersion, newVersion));
        Log.d(TAG, "onCreate: executing script: " + UPDATE_SCRIPT);
        onCreate(db);
    }

    public List<StreetEntry> getAll() {
        Log.d(TAG, "getAll: getting all streets");
        Cursor result = getAllStreetsCursor();
        return parseCursor(result);
    }

    private Cursor getAllStreetsCursor() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        return QueryTemplates.getAllQuery(readableDatabase, STREETS_TABLE_NAME, false);
    }

    public List<StreetEntry> getStreetsByNameLike(String nameLike) {
        Log.d(TAG, "getStreetsByNameLike: Searching by name like: " + nameLike);
        String query = getByNameLikeQuery(nameLike);
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor result = QueryTemplates.getRawQuery(readableDatabase, query);
        return parseCursor(result);
    }

    private String getByNameLikeQuery(String nameLike) {
        return "SELECT * FROM streets where " +
                "old_name_cmp like '%" + nameLike.toUpperCase() + "%' " +
                "OR " +
                "new_name_cmp like '%" + nameLike.toUpperCase() + "%'";
    }

    public long insertStreetEntry(StreetEntry streetEntry) {
        Log.d(TAG, "insertStreetEntry: inserting " + streetEntry);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        return QueryTemplates.insertQuery(writableDatabase, STREETS_TABLE_NAME, fromStreetEntry(streetEntry));
    }


    private ContentValues fromStreetEntry(StreetEntry item) {
        ContentValues result = new ContentValues();
        result.put("old_name", item.getOldName());
        result.put("old_name_cmp", item.getOldName().toUpperCase());
        result.put("new_name", item.getNewName());
        result.put("new_name_cmp", item.getNewName().toUpperCase());
        result.put("description", item.getDescription());
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<StreetEntry> parseCursor(Cursor cursor) {
        List<StreetEntry> result = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    StreetEntry added = extractStreetFromCursor(cursor);
                    result.add(added);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return result; // TODO
        } else {
            return (ArrayList<StreetEntry>) Collections.EMPTY_LIST;
        }
    }

    private StreetEntry extractStreetFromCursor(Cursor cursor) {
        int id = cursor.getInt(0);
        String oldName = cursor.getString(1);
        String newName = cursor.getString(3);
        String description = cursor.getString(5);
        StreetEntry result = new StreetEntry();
        result.setId(id);
        result.setOldName(oldName);
        result.setNewName(newName);
        result.setDescription(description);
        return result;

    }

    public int deleteAllStreets() {
        return QueryTemplates.deleteAll(getWritableDatabase(), STREETS_TABLE_NAME);
    }
}
