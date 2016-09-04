package aabrasha.ua.streettranslator;

import aabrasha.ua.streettranslator.sqlite.StreetsDatabase;
import android.app.Application;
import android.content.Context;

/**
 * Created by Andrii Abramov on 9/3/16.
 */
public class StreetsApplication extends Application {

    private static StreetsDatabase streetsDatabase;
    private static Context applicationContext;

    public static StreetsDatabase getStreetsDatabase() {
        return streetsDatabase;
    }

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        streetsDatabase = new StreetsDatabase(getApplicationContext());
        applicationContext = getApplicationContext();
    }
}
