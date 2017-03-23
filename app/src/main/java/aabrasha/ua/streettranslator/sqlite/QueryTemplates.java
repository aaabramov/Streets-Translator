package aabrasha.ua.streettranslator.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Andrii Abramov on 8/29/16.
 */
public final class QueryTemplates {

    private static final String TAG = QueryTemplates.class.getSimpleName();

    public static Cursor getAllQuery(SQLiteDatabase database, String tableName) {
        Log.d(TAG, "getAllQuery: for " + tableName);
        return getAllQuery(database, tableName, (String[]) null);
    }

    public static Cursor getAllQuery(SQLiteDatabase database, String tableName, String... columns) {
        Log.d(TAG, "getAllQuery: for " + tableName);
        return database.query(false, tableName, columns, null, null, null, null, null, null);
    }

    public static Cursor getRawQuery(SQLiteDatabase database, String query) {
        Log.d(TAG, "getRawQuery: from " + query);
        return database.rawQuery(query, null);
    }

    public static int deleteAll(SQLiteDatabase database, String tableName) {
        Log.d(TAG, "deleteAll: from table " + tableName);
        return database.delete(tableName, null, null);
    }

    public static long insert(SQLiteDatabase database, String tableName, ContentValues data) {
        Log.d(TAG, "insert: for " + tableName + " inserting " + data);
        checkDatabaseIsWritable(database, tableName);
        return database.insert(tableName, null, data);
    }

    public static int deleteById(SQLiteDatabase database, String tableName, long id) {
        Log.d(TAG, "deleteById: deleting by id = " + id);
        checkDatabaseIsWritable(database, tableName);
        return database.delete(tableName, "_id = " + id, null);
    }

    public static int updateByIdQuery(SQLiteDatabase database, String tableName, Integer id, ContentValues data) {
        Log.d(TAG, "updateByIdQuery: for " + tableName + " updating " + data);
        checkDatabaseIsWritable(database, tableName);
        return database.update(tableName, data, "_id = " + id, null);
    }

    private static void checkDatabaseIsWritable(SQLiteDatabase database, String tableName) {
        if (database.isReadOnly()) {
            Log.e(TAG, "checkDatabaseIsWritable: for " + tableName + " you should provide WritableDatabase");
            throw new RuntimeException("To insert data into database you should provide WritableDatabase!");
        }
    }
}
