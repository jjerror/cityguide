package com.example.joshua.cityguide.events;

import com.example.joshua.cityguide.models.pojo.MapSearchResults;

public class DistanceNeededEvent {
    public MapSearchResults.Place destination;

    public DistanceNeededEvent(MapSearchResults.Place destination) {
        this.destination = destination;
    }
}
