package com.example.joshua.cityguide.api;


import android.location.Location;

import com.example.joshua.cityguide.BuildConfig;
import com.example.joshua.cityguide.models.pojo.DistanceMatrixResult;
import com.example.joshua.cityguide.models.pojo.MapSearchResults;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import rx.Observable;

/**
 * Entry point for all requests to **My Project** API.
 * Uses Retrofit library to abstract the actual REST API into a service.
 */

public class MyProjectApi {


    private static MyProjectApi sInstance;
    private GooglePlacesService mSearchService;

    /**
     * Private singleton constructor.
     */
    private MyProjectApi() {
        RestAdapter restAdapter = buildRestAdapter();
        this.mSearchService = restAdapter.create(GooglePlacesService.class);
    }

    /**
     * Returns the instance of this singleton.
     */
    public static MyProjectApi getInstance() {
        if (sInstance == null) {
            sInstance = new MyProjectApi();
        }
        return sInstance;
    }

    /**
     * Creates the RestAdapter by setting custom HttpClient.
     */
    public static RestAdapter buildRestAdapter() {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }

        return builder.setEndpoint(Constants.API_URL)
                .setConverter(new JacksonConverter())
                .setClient(getHttpClient())
                .build();
    }

    /**
     * Custom Http Client to define connection timeouts.
     */
    private static Client getHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(Constants.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        return new OkClient(httpClient);
    }

    /**
     * Does a place search for a given location and type, and returns an Observable of the results.
     *
     * @param location location to search
     * @param type     type of place to search
     * @return an Observable with the results
     */
    public Observable<MapSearchResults> getSearchEngineResults(Location location, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY);
        params.put("location", String.format("%f,%f", location.getLatitude(), location.getLongitude()));
        params.put("type", type);
        params.put("rankby", "distance");
        return this.mSearchService.search(params);
    }

    /**
     * Does a place search for a given location and type, and returns an Observable of the results.
     *
     * @param nextToken the nextToken
     * @return an Observable with the results
     */
    public Observable<MapSearchResults> getSearchEngineResults(String nextToken) {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY);
        params.put("pagetoken", nextToken);
        return this.mSearchService.search(params);
    }

    /**
     * Does a distance matrix look up for 2 given locations, and returns an Observable of the results.
     *
     * @return an Observable with the results
     */
    public Observable<DistanceMatrixResult> getDistance(double latitude, double longitude, Location location) {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY);
        params.put("units", "imperial");
        params.put("origins", String.format("%f,%f", location.getLatitude(), location.getLongitude()));
        params.put("destinations", String.format("%f,%f", latitude, longitude));
        return this.mSearchService.getDistance(params);
    }
}
