package com.example.joshua.cityguide;

import android.app.Activity;
import android.app.Application;

import com.example.joshua.cityguide.common.ProdAppModule;

import dagger.ObjectGraph;

public class CityGuideApplication extends Application {

    private ObjectGraph objectGraph;
    private Activity resumedActivity;

    public static CityGuideApplication from(Activity activity) {
        return (CityGuideApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Object prodModule = new ProdAppModule(this);
        objectGraph = ObjectGraph.create(prodModule);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    /**
     * May return null
     */
    @SuppressWarnings("UnusedDeclaration")
    public Activity getResumedActivity() {
        return resumedActivity;
    }

    public void setResumedActivity(Activity activity) {
        this.resumedActivity = activity;
    }
}
