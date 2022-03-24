package com.example.habitree.autocomplete;

import com.google.android.gms.location.Geofence;

import java.util.function.Function;

public class GeofenceAutocompleteDelegate extends AbstractAutocompleteDelegate {
    Geofence geofence;

    GeofenceAutocompleteDelegate() {
        super(x -> true);
        geofence = new Geofence.Builder()
                .setRequestId("GF1")
                .setCircularRegion(
                        43.464256,
                        -80.520409,
                        100 // Meteres
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }
    GeofenceAutocompleteDelegate(Function<String,Boolean> onComplete,
                                 Geofence geofence) {
        super(onComplete);
        this.geofence = geofence;
    }
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void cleanup() {

    }
}
