package com.example.joshua.cityguide.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.joshua.cityguide.R;
import com.example.joshua.cityguide.adapters.TabPagerAdapter;
import com.example.joshua.cityguide.common.BusProvider;
import com.example.joshua.cityguide.events.DistanceNeededEvent;
import com.example.joshua.cityguide.events.DistanceUpdatedEvent;
import com.example.joshua.cityguide.events.LocationChangedEvent;
import com.example.joshua.cityguide.models.GooglePlacesModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


public class MainActivity extends BaseActivity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    protected static final String TAG = "MAIN";
    @InjectView(R.id.pager) ViewPager mPager;
    @Inject GooglePlacesModel googlePlacesModel;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.main_layout);
        ButterKnife.inject(this);
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        BusProvider.getInstance().post(new LocationChangedEvent(mCurrentLocation));
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Produce
    public LocationChangedEvent produceAnswer() {
        return new LocationChangedEvent(mCurrentLocation);
    }

    @Subscribe
    public void onDistanceNeededEvent(final DistanceNeededEvent event) {
        if (mCurrentLocation == null) {
            return;
        }
        mSubscriptions.add(
                googlePlacesModel.getDistance(
                        event.destination.geometry.location.lat,
                        event.destination.geometry.location.lng,
                        mCurrentLocation).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        event.destination.distance = s;
                        BusProvider.getInstance().post(new DistanceUpdatedEvent(event.destination));
                    }
                })
        );
    }

}

