package com.example.habitree.autocomplete;

import com.google.android.gms.location.Geofence;

import java.util.function.Function;

public class GeofenceAutocompleteDelegate extends AbstractAutocompleteDelegate {
    Geofence geofence;

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
    public void setEnabled(boolean e) {

    }
}
