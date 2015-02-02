package com.example.joshua.cityguide.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.joshua.cityguide.CityGuideApplication;
import com.example.joshua.cityguide.common.BusProvider;

import rx.subscriptions.CompositeSubscription;

/**
 * Base Activity for this application
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected final CompositeSubscription mSubscriptions = new CompositeSubscription();
    CityGuideApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = CityGuideApplication.from(this);
        mApplication.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.setResumedActivity(this);
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        BusProvider.getInstance().unregister(this);
        mApplication.setResumedActivity(null);
        mSubscriptions.clear();
        super.onPause();
    }

}
