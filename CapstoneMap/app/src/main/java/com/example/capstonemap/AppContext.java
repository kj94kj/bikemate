package com.example.capstonemap;

import android.app.Application;

public class AppContext extends Application {
    private static AppContext appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = (AppContext) getApplicationContext();
    }

    public static AppContext getAppContext() {
        return appContext;
    }
}
