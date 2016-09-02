package aabrasha.ua.streettranslator;

import aabrasha.ua.streettranslator.sqlite.StreetsOpenHelper;
import android.app.Application;
import android.content.Context;

/**
 * Created by Andrii Abramov on 9/3/16.
 */
public class StreetsApplication extends Application {

    private static StreetsOpenHelper streetsDatabase;
    private static Context applicationContext;

    public static StreetsOpenHelper getStreetsDatabase() {
        return streetsDatabase;
    }

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        streetsDatabase = new StreetsOpenHelper(getApplicationContext());
        applicationContext = getApplicationContext();
    }
}
