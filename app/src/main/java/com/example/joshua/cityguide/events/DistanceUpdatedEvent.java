package com.example.joshua.cityguide.events;

import com.example.joshua.cityguide.models.pojo.MapSearchResults;

public class DistanceUpdatedEvent {
    MapSearchResults.Place place;

    public DistanceUpdatedEvent(MapSearchResults.Place place) {
        this.place = place;
    }
}
