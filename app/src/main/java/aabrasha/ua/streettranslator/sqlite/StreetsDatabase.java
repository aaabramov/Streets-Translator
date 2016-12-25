package aabrasha.ua.streettranslator.sqlite;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.sqlite.cursor.StreetEntryCursor;
import aabrasha.ua.streettranslator.sqlite.cv.StreetEntryContentValues;
import aabrasha.ua.streettranslator.util.IOUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrii Abramov on 8/28/16.
 */
public class StreetsDatabase extends SQLiteOpenHelper {

    private static final String TAG = StreetsDatabase.class.getSimpleName();

    public static final String STREETS_TABLE_NAME = "streets";
    public static final String STREETS_COLUMN_ID = "_id";
    public static final String STREETS_COLUMN_OLD_NAME = "old_name";
    public static final String STREETS_COLUMN_OLD_NAME_CMP = "old_name_cmp";
    public static final String STREETS_COLUMN_NEW_NAME = "new_name";
    public static final String STREETS_COLUMN_NEW_NAME_CMP = "new_name_cmp";
    public static final String STREETS_COLUMN_DESCRIPTION = "description";
    public static final String STREETS_COLUMN_INSERTION_DATE = "insertion_date";

    private static final int VERSION = 5;
    private static final String DB_NAME = "street_entries.db";

    private static final String[] SELECT_COLUMN_LIST = {
            STREETS_COLUMN_ID, STREETS_COLUMN_OLD_NAME, STREETS_COLUMN_NEW_NAME, STREETS_COLUMN_DESCRIPTION, STREETS_COLUMN_INSERTION_DATE
    };
    private static final String SELECT_COLUMNS = StringUtils.join(SELECT_COLUMN_LIST, ',');

    private final Context context;

    public StreetsDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
        Log.d(TAG, "StreetsDatabase: Instantiating Streets database");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Creating database: " + DB_NAME);
        Log.d(TAG, "onCreate: Database version: " + VERSION);
        String script = IOUtils.readAll(context.getResources().openRawResource(R.raw.streets_init));
        Log.d(TAG, "onCreate: executing script: " + script);
        db.execSQL(script);
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
        String script = IOUtils.readAll(context.getResources().openRawResource(R.raw.streets_update));
        Log.d(TAG, "onCreate: executing script: " + script);
        onCreate(db);
    }

    public List<StreetEntry> getAll() {
        Log.d(TAG, "getAll: getting all streets");
        Cursor result = getAllStreetsCursor();
        return parseCursor(result);
    }

    private Cursor getAllStreetsCursor() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        return QueryTemplates.getAllQuery(readableDatabase, STREETS_TABLE_NAME, SELECT_COLUMN_LIST);
    }

    public List<StreetEntry> getStreetsByNameLike(String nameLike) {
        Log.d(TAG, "getStreetsByNameLike: Searching by name like: " + nameLike);
        String query = getByNameLikeQuery(nameLike);
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor result = QueryTemplates.getRawQuery(readableDatabase, query);
        return parseCursor(result);
    }

    private String getByNameLikeQuery(String nameLike) {
        return "SELECT " + SELECT_COLUMNS + " FROM " + STREETS_TABLE_NAME + " where " +
                "old_name_cmp like '%" + nameLike.toUpperCase() + "%' " +
                "OR " +
                "new_name_cmp like '%" + nameLike.toUpperCase() + "%'";
    }

    public long insertStreetEntry(StreetEntry streetEntry) {
        Log.d(TAG, "insertStreetEntry: inserting " + streetEntry);
        streetEntry.setInsertionDate(new Date());
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues data = StreetEntryContentValues.toContentValues(streetEntry);
        return QueryTemplates.insert(writableDatabase, STREETS_TABLE_NAME, data);
    }

    public int updateStreetEntry(StreetEntry streetEntry) {
        Log.d(TAG, "updateStreetEntry: updating " + streetEntry);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues data = StreetEntryContentValues.toContentValues(streetEntry);
        return QueryTemplates.updateByIdQuery(writableDatabase, STREETS_TABLE_NAME, streetEntry.getId(), data);
    }


    @SuppressWarnings("unchecked")
    private List<StreetEntry> parseCursor(Cursor cursor) {
        List<StreetEntry> result = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    StreetEntry added = new StreetEntryCursor(cursor).parseModel();
                    result.add(added);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        } else {
            return (ArrayList<StreetEntry>) Collections.EMPTY_LIST;
        }
    }

    public int deleteById(long id) {
        return QueryTemplates.deleteById(getWritableDatabase(), STREETS_TABLE_NAME, id);
    }

    public int deleteAllStreets() {
        return QueryTemplates.deleteAll(getWritableDatabase(), STREETS_TABLE_NAME);
    }
}
