package com.example.joshua.cityguide.models.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by joshua on 1/31/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceMatrixResult {

    public List<Row> rows;
    public String status;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Row {
        public List<Element> elements;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Element {
        public Distance distance;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Distance {
        public double value;
        public String text;
    }
}
