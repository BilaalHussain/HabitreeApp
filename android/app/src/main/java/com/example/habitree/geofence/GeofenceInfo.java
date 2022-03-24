package com.example.habitree.geofence;

import java.util.function.Function;

public class GeofenceInfo {
    public boolean enabled;
    public float lat, lng, radius;
    public GeofenceInfo(String id,
                 float lat,
                 float lng,
                 float radius // Radius in meters
    ) {
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
        this.enabled = true;
    }
    public GeofenceInfo() {
        this.enabled = false;
    }
}
