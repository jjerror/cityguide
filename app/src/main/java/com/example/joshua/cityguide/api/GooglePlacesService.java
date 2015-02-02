package com.example.joshua.cityguide.api;

import com.example.joshua.cityguide.models.pojo.DistanceMatrixResult;
import com.example.joshua.cityguide.models.pojo.MapSearchResults;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Endpoint for the Google Map REST API.
 * Using Retrofit annotations.
 */
public interface GooglePlacesService {

    @GET("/place/nearbysearch/json")
    public Observable<MapSearchResults> search(
            @QueryMap Map<String, String> params
    );

    @GET("/distancematrix/json")
    public Observable<DistanceMatrixResult> getDistance(
            @QueryMap Map<String, String> params
    );
}
