package com.example.habitree.geofence;

import java.util.function.Function;

public class GeofenceInfo {
    boolean enabled;
    float lat, lng, radius;
    GeofenceInfo(Function<String,Boolean> onComplete,
                 String id,
                 float lat,
                 float lng,
                 float radius // Radius in meters
    ) {
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
    }

}
