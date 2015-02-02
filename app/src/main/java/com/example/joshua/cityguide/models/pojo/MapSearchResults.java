package com.example.joshua.cityguide.models.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by joshua on 1/30/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapSearchResults {

    public List<Place> results;
    public String next_page_token;
    public String status;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Place {
        public String name;
        public Double rating;
        public Geometry geometry;
        public String distance;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        public Location location;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        public double lat;
        public double lng;
    }
}
