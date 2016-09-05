package aabrasha.ua.streettranslator.util;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.StreetsApplication;
import aabrasha.ua.streettranslator.model.StreetEntry;
import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrii Abramov on 8/27/16.
 */
public class StreetsLoader {

    private static final String TAG = StreetsLoader.class.getSimpleName();
    private static final String PARTS_DELIMITER = ":";

    public List<StreetEntry> getDefaultStreetEntries() {
        Context appContext = StreetsApplication.getContext();
        InputStream is = appContext.getResources().openRawResource(R.raw.streets);
        List<String> lines = IOUtils.readLines(is);
        return fromStringList(lines);
    }

    private List<StreetEntry> fromStringList(List<String> entries) {
        List<StreetEntry> result = new ArrayList<>(entries.size());
        int lastId = 0;
        for (String line : entries) {
            result.add(parseStreetEntry(line, lastId));
            lastId++;
        }
        return result;
    }

    private StreetEntry parseStreetEntry(String from, int withId) {
        String[] parts = from.split(PARTS_DELIMITER);
        StreetEntry result = parseFromParts(parts);
        result.setId(withId);
        return result;
    }

    private StreetEntry parseFromParts(String[] parts) {
        final int numberOfParts = parts.length;
        StreetEntry result;
        if (numberOfParts == 2) {
            result = parseWithNoDescription(parts);
        } else if (numberOfParts == 3) {
            result = parseWithDescription(parts);
        } else {
            Log.e(TAG, "parseStreetEntry: broken String: " + Arrays.toString(parts));
            throw new RuntimeException("parseStreetEntry: broken String:"); // TODO
        }
        return result;
    }

    private StreetEntry parseWithDescription(String[] parts) {
        StreetEntry result = new StreetEntry();
        result.setOldName(parts[0]);
        result.setNewName(parts[1]);
        result.setDescription(parts[2]);
        return result;
    }

    private StreetEntry parseWithNoDescription(String[] parts) {
        StreetEntry result = new StreetEntry();
        result.setOldName(parts[0]);
        result.setNewName(parts[1]);
        return result;
    }

}
