package com.example.joshua.cityguide.common;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public final class BusProvider {
    private static final Bus BUS = new Bus(ThreadEnforcer.MAIN);

    private BusProvider() {
        // No instances.
    }

    public static Bus getInstance() {
        return BUS;
    }
}
