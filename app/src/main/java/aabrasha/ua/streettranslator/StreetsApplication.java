package aabrasha.ua.streettranslator;

import aabrasha.ua.streettranslator.service.SortingManager;
import aabrasha.ua.streettranslator.sqlite.StreetsDatabase;
import android.app.Application;

/**
 * @author Andrii Abramov on 9/3/16.
 */
public class StreetsApplication extends Application {

    private StreetsDatabase streetsDatabase;
    private SortingManager sortingManager;

    private static StreetsApplication application;

    public StreetsDatabase getStreetsDatabase() {
        return streetsDatabase;
    }

    public SortingManager getSortingManager() {
        return sortingManager;
    }

    public static StreetsApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        streetsDatabase = new StreetsDatabase(getApplicationContext());
        sortingManager = new SortingManager(getApplicationContext());
        application = this;
    }
}
