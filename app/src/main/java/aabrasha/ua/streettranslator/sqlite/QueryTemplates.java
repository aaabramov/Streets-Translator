package aabrasha.ua.streettranslator.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Andrii Abramov on 8/29/16.
 */
public final class QueryTemplates {

    public static final String TAG = QueryTemplates.class.getSimpleName();

    public static Cursor getAllQuery(SQLiteDatabase database, String tableName, boolean distinct) {
        Log.d(TAG, "getAllQuery: for " + tableName);
        return database.query(distinct, tableName, null, null, null, null, null, null, null);
    }

    public static Cursor getRawQuery(SQLiteDatabase database, String query) {
        Log.d(TAG, "getRawQuery: from " + query);
        return database.rawQuery(query, null);
    }

    public static long insertQuery(SQLiteDatabase database, String tableName, ContentValues data) {
        Log.d(TAG, "insertQuery: for " + tableName + " inserting " + data);
        if (database.isReadOnly()) {
            Log.e(TAG, "insertQuery: for " + tableName + " you should provide WritableDatabase");
            throw new RuntimeException("To insert data into database you should provide WritableDatabase!");
        }
        return database.insert(tableName, null, data);
    }

}
