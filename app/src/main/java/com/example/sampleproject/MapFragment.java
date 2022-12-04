package com.example.sampleproject;

import android.view.Menu;
import android.view.MenuItem;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view=inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap = map;

                //Add maker
                LatLng latLng = new LatLng(10.87, 106.8);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("Cac em gai");
                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);

                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(true);


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLng latLng1 = new LatLng( 10.86815,106.79931 );
                LatLng latLng2 = new LatLng(10.87304, 106.80524);

                //the include method will calculate the min and max bound.
                builder.include(latLng1);
                builder.include(latLng2);
                final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
                final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
                final int zoomPadding = (int) (zoomWidth * 0.3);
                final LatLngBounds bounds = builder.build();
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
//                mMap.addMarker(new MarkerOptions().title("your title")
//                        .snippet("your desc")
//                        .position(new LatLng(10.87,106.8)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,zoomWidth,
                                zoomHeight,zoomPadding));
                    }
                });
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);

            }
        });
        // Return view
        return view;
    }
}