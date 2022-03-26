package com.example.habitree.geofence;

import java.io.Serializable;
import java.util.function.Function;

public class GeofenceInfo implements Serializable {
    public String uuid;
    public boolean enabled;
    public double lat, lng, radius;
    public GeofenceInfo(String id,
                        double lat,
                        double lng,
                        double radius // Radius in meters
    ) {
        uuid = id;
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
        this.enabled = true;
    }
    public GeofenceInfo(String uuid) {
        this.enabled = false;
        this.uuid = uuid;
    }
}
