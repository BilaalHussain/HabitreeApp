package com.example.habitree.ui.editing;


import static com.example.habitree.ui.editing.EditHabitFragment.GEOFENCE_CREATE;
import static com.example.habitree.ui.editing.EditHabitFragment.GEOFENCE_CREATE_DATA;
import static com.example.habitree.ui.editing.EditHabitFragment.GEOFENCE_FRAGMENT_REQUEST_KEY;
import static com.example.habitree.ui.editing.EditHabitFragment.GEOFENCE_RESULT_TYPE_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.habitree.R;
import com.example.habitree.geofence.GeofenceHelper;
import com.example.habitree.geofence.GeofenceInfo;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


import java.util.Collections;


public class MapsFragment extends Fragment {

    private static final String TAG = "GEOFENCE";
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    private LatLng center;
    private String id;
    private double radius;
    private boolean enabled;
    private GoogleMap gMap;
    private Gson gson = new Gson();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            getUserPermission();
            gMap.setMyLocationEnabled(true);
            final LatLng mcBuilding = new LatLng(43.4720, -80.544);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mcBuilding, 16));

            if(enabled) {
                gMap.addMarker(new MarkerOptions().position(center));
                gMap.addCircle(new CircleOptions()
                        .center(center)
                        .radius(radius)
                        .strokeColor(Color.argb(255,0,255,0))
                        .strokeWidth(4f)
                        .fillColor(Color.argb(81, 0,255,0))
                );
            }

            gMap.setOnMapLongClickListener(latLng -> {
                center = latLng;
                gMap.clear();
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                gMap.addMarker(markerOptions);
            });

            gMap.setOnMapClickListener(latLng -> {
                if(center == null) {
                    return;
                }
                float[] dist = {0};
                Location.distanceBetween(center.latitude, center.longitude, latLng.latitude, latLng.longitude, dist);

                radius = dist[0];

                CircleOptions c = new CircleOptions()
                        .center(center)
                        .radius(dist[0])
                        .strokeColor(Color.argb(255,0,255,0))
                        .strokeWidth(4f)
                        .fillColor(Color.argb(81, 0,255,0));

                gMap.clear();
                gMap.addMarker(new MarkerOptions().position(center));
                gMap.addCircle(c);
            });
        }
    };
    private GeofenceHelper geofenceHelper;
    private GeofencingClient geofencingClient;


    private void getUserPermission() {
        if(
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "getUserPermission called, already have all permissions");
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            //Display rationalle
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
        else {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(getContext(), "Geofence feature enabled", Toast.LENGTH_SHORT).show();
            } else {
                //We do not have the permission..
                Toast.makeText(getContext(), "Background location access forbidden. Geofence feature disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
//    public MapsFragment(String ID, GeofenceInfo geofenceInfo) {
//        id = ID;
//        if (geofenceInfo == null) {
//            geofenceInfo = new GeofenceInfo(ID);
//        }
//        this.center = new LatLng(geofenceInfo.lat, geofenceInfo.lng);
//        this.radius = geofenceInfo.radius;
//        this.enabled = geofenceInfo.enabled;
//    }
//    public MapsFragment(String ID) {
//        id = ID;
//        this.enabled = false;
//    }
//    public static MapsFragment newInstance(String ID, GeofenceInfo geofenceInfo) {
//        MapsFragment fragment = new MapsFragment(ID, geofenceInfo);
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//    public static MapsFragment newInstance(String ID) {
//        MapsFragment fragment = new MapsFragment(ID);
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("MAPS", "OnCreateView called");
        View root = inflater.inflate(R.layout.fragment_maps, container, false);

        Bundle b = getArguments();

        Log.w(TAG, "For some reason, the saved instance state (bundle) is null>?");
        String uuid = b.getString("uuid");

        GeofenceInfo geofenceInfo =
                gson.fromJson(b.getString("geofence"),
                        GeofenceInfo.class);

        id = uuid;
        if (geofenceInfo == null) {
            geofenceInfo = new GeofenceInfo(uuid);
        }
        this.center = new LatLng(geofenceInfo.lat, geofenceInfo.lng);
        this.radius = geofenceInfo.radius;
        this.enabled = geofenceInfo.enabled;


        final Button confirmFence = root.findViewById(R.id.confirm_geofence_button);
        final Button cancelFence = root.findViewById(R.id.cancel_geofence_button);
        final Button eraseFence = root.findViewById(R.id.erase_geofence_button);
        Context ctx = requireContext();
        confirmFence.setOnClickListener(x -> createFence(ctx, getParentFragmentManager()));
        cancelFence.setOnClickListener(x -> cancelFence(ctx));
        eraseFence.setOnClickListener(x -> deleteFence(ctx));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("MAPS", "OnViewCreated called");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        geofenceHelper = new GeofenceHelper(getContext());
        geofencingClient = LocationServices.getGeofencingClient(requireContext());
    }

    @SuppressLint("MissingPermission")
    private void createFence(Context c, FragmentManager f) {
        Geofence fence = geofenceHelper.getGeofence(id,
                center,
                radius,
                GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL);
        GeofencingRequest request = geofenceHelper.getGeofencingRequest(fence);
        PendingIntent intent = geofenceHelper.getPendingIntent();
        getUserPermission();


        geofencingClient.addGeofences(request, intent)
                .addOnSuccessListener(x -> {
                    Toast.makeText(c, "Geofence added",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Add success id:" + id );

                    Bundle bundle = new Bundle();
                    bundle.putString(GEOFENCE_RESULT_TYPE_KEY, GEOFENCE_CREATE);
                    bundle.putString(GEOFENCE_CREATE_DATA, gson.toJson(
                            new GeofenceInfo(id, center.latitude, center.longitude, radius)
                    ));
                    f.setFragmentResult(
                            GEOFENCE_FRAGMENT_REQUEST_KEY,
                            bundle);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(c, geofenceHelper.getErrorString(e), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Add failure id:" + id + " msg: " + geofenceHelper.getErrorString(e));
                });

        getParentFragmentManager().popBackStack();
    }
    private void cancelFence(Context c) {
        getParentFragmentManager().popBackStack();
    }
    private void deleteFence(Context c) {
        gMap.clear();
        geofencingClient
                .removeGeofences(Collections.singletonList(id))
                .addOnFailureListener(e -> Log.w(TAG,"Delete fail"))
                .addOnSuccessListener(s -> Log.w(TAG, "Delete Success"));
        getParentFragmentManager().popBackStack();
    }

}