package aabrasha.ua.streettranslator.model;

import aabrasha.ua.streettranslator.sqlite.StreetsOpenHelper;
import aabrasha.ua.streettranslator.util.StreetsLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Andrii Abramov on 8/29/16.
 */
public class StreetsService {

    public static final String TAG = StreetsService.class.getSimpleName();

    private StreetsOpenHelper streetsDatabase;
    private Context context;

    public StreetsService(Context context) {
        this.context = context;
        this.streetsDatabase = new StreetsOpenHelper(context);
    }

    public List<StreetEntry> getAll() {
        return streetsDatabase.getAll();
    }

    public List<StreetEntry> getByNameLike(String nameLike) {
        return streetsDatabase.getStreetsByNameLike(nameLike);
    }

    public void fillWithSampleData() {
        Log.d(TAG, "fillWithSampleData: starting to populate data");
        StreetsLoader loader = new StreetsLoader(context);
        final List<StreetEntry> items = loader.getDefaultStreetEntries();
        for (StreetEntry item : items) {
            Log.d(TAG, "fillWithSampleData: adding " + item);
            streetsDatabase.insertStreetEntry(item);
        }
    }

}
