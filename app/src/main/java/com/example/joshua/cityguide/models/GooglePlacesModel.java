package com.example.joshua.cityguide.models;

import android.location.Location;

import com.example.joshua.cityguide.api.MyProjectApi;
import com.example.joshua.cityguide.models.pojo.DistanceMatrixResult;
import com.example.joshua.cityguide.models.pojo.MapSearchResults;

import rx.Observable;
import rx.functions.Func1;

public class GooglePlacesModel {

    public GooglePlacesModel() {
    }

    public Observable<MapSearchResults> searchPlaces(Location location, String type) {
        return MyProjectApi.getInstance().getSearchEngineResults(location, type);
    }

    public Observable<MapSearchResults> searchPlaces(String nextToken) {
        return MyProjectApi.getInstance().getSearchEngineResults(nextToken);
    }

    public Observable<String> getDistance(double lag, double lng, Location location) {
        return MyProjectApi.getInstance().getDistance(lag, lng, location).map(convertSearchResultsToDistance());
    }

    private Func1<DistanceMatrixResult, String> convertSearchResultsToDistance() {
        return new Func1<DistanceMatrixResult, String>() {
            @Override
            public String call(DistanceMatrixResult results) {
                if (results == null) {
                    return "";
                }

                // When the api is over daily limit
                if (results.status.equals("OVER_QUERY_LIMIT")) {
                    return "Over M :)";
                }

                try {
                    return results.rows.get(0).elements.get(0).distance.text;
                } catch (Exception ignore) {
                    return " ";
                }
            }
        };
    }

}
