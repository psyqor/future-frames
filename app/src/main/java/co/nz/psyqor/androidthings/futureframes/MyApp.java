package co.nz.psyqor.androidthings.futureframes;

import android.app.Application;
import android.content.Context;

/**
 * Created by psyqor on 15/01/17.
 */


public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}