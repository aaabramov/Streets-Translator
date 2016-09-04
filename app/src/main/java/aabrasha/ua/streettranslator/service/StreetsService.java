package aabrasha.ua.streettranslator.service;

import aabrasha.ua.streettranslator.StreetsApplication;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.sqlite.StreetsDatabase;
import aabrasha.ua.streettranslator.util.StreetsLoader;
import android.util.Log;

import java.util.List;

/**
 * Created by Andrii Abramov on 8/29/16.
 */
public class StreetsService {

    private static final String TAG = StreetsService.class.getSimpleName();

    private static final StreetsService INSTANCE = new StreetsService();

    private StreetsDatabase streetsDatabase;

    private StreetsService() {
        this.streetsDatabase = StreetsApplication.getStreetsDatabase();
    }

    public static StreetsService getInstance() {
        return INSTANCE;
    }

    public List<StreetEntry> getAll() {
        return streetsDatabase.getAll();
    }

    public List<StreetEntry> getByNameLike(String nameLike) {
        return streetsDatabase.getStreetsByNameLike(nameLike);
    }

    public long addNewStreetEntry(StreetEntry entry) {
        Log.d(TAG, "addNewStreetEntry: adding " + entry);
        long id = streetsDatabase.insertStreetEntry(entry);
        Log.d(TAG, "addNewStreetEntry: added with id " + id);
        return id;
    }

    public int deleteById(long id){
        Log.d(TAG, "deleteById: id = " + id);
        int numOfDeletedRows = streetsDatabase.deleteById(id);
        Log.d(TAG, String.format("deleteById: deleted %d rows", numOfDeletedRows));
        return numOfDeletedRows;
    }

    public int cleanDatabase() {
        Log.d(TAG, "cleanDatabase: ");
        int numOfDeletedRows = streetsDatabase.deleteAllStreets();
        Log.d(TAG, String.format("cleanDatabase: deleted %d rows", numOfDeletedRows));
        return numOfDeletedRows;
    }

    public int fillWithSampleData() {
        Log.d(TAG, "fillWithSampleData: starting to populate data");
        StreetsLoader loader = new StreetsLoader();

        List<StreetEntry> items = loader.getDefaultStreetEntries();
        for (StreetEntry item : items) {
            Log.d(TAG, "fillWithSampleData: adding " + item);
            streetsDatabase.insertStreetEntry(item);
        }

        int numOfAddedRows = items.size();
        Log.d(TAG, String.format("fillWithSampleData: added %d items", numOfAddedRows));

        return numOfAddedRows;
    }

}
