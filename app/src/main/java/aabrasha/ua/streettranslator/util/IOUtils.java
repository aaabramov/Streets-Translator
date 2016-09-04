package aabrasha.ua.streettranslator.util;

import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrii Abramov on 8/27/16.
*/
public final class IOUtils {

    private static final String TAG = IOUtils.class.getSimpleName();
    public static final String EMPTY_STRING = "";

    /**
     * The stream will be closed after is has been read
     *
     * @param is InputStream from which to read
     * @return contents of the file
     */
    public static String readAll(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();

        String line;
        while ((line = readLine(reader)) != null) {
            result.append(line).append('\n');
        }

        closeStream(is);
        return result.toString();
    }

    /**
     * The stream will be closed after is has been read
     *
     * @param is InputStream from which to read
     * @return contents of the file in List<String>
     */
    public static List<String> readLines(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<String> result = new ArrayList<>();

        String line;
        while ((line = readLine(reader)) != null) {
            result.add(line);
        }

        closeStream(is);
        return result;
    }


    private static String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            Log.e(TAG, "readLine: error reading line", e);
            return EMPTY_STRING;
        }
    }

    private static void closeStream(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "closeStream: error", e);
        }
    }

    private static void closeStream(OutputStream os) {
        try {
            os.close();
        } catch (IOException e) {
            Log.e(TAG, "closeStream: error", e);
        }
    }

}
