package com.example.joshua.cityguide.events;

import android.location.Location;

public class LocationChangedEvent {
    public Location location;

    public LocationChangedEvent(Location location) {
        this.location = location;
    }
}
