package com.example.sampleproject;

import android.view.Menu;
import android.view.MenuItem;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sampleproject.Model.Asset;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {
    private GoogleMap mMap;
    APIInterface apiInterface;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Asset> call = apiInterface.getAsset("6H4PeKLRMea1L0WsRXXWp9");//, "Bearer "+ token);
        call.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                Log.d("API CALL F", response.code()+"");
                Asset asset = response.body();
                Log.d("API CALL F", asset.attributes.weatherData.type+"");
                Toast.makeText(getContext(), "SUCCESS CALL API", Toast.LENGTH_SHORT).show();
                SupportMapFragment supportMapFragment=(SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.map);

                // Async map
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap map) {
                        mMap = map;

                        //Add maker
                        double lat = Double.parseDouble(asset.attributes.weatherData.value.coord.lat);
                        double lon = Double.parseDouble(asset.attributes.weatherData.value.coord.lon);
                        LatLng latLng = new LatLng(lat, lon);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(asset.attributes.weatherData.value.name);
                        markerOptions.position(latLng);
                        mMap.addMarker(markerOptions);


                        //move camera to marker
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        mMap.animateCamera(cameraUpdate);

                        //zoom map
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                        mMap.getUiSettings().setZoomGesturesEnabled(true);
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                        mMap.getUiSettings().setRotateGesturesEnabled(true);

                        //set camera
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
                                mMap.addMarker(new MarkerOptions().title("DH CNTT")
                                        .position(new LatLng(10.870346,106.802480)));
                                mMap.addMarker(new MarkerOptions().title("DH Nong Lam")
                                        .position(new LatLng(10.871142949778433,106.7915703367933)));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,zoomWidth,
                                        zoomHeight,zoomPadding));
                            }
                        });

                        //GPS
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

                        //event click marker
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                String markerName = marker.getTitle();

//                                AssetsFragment assetsFragment = new AssetsFragment();
//                                FragmentManager fragmentManager = getChildFragmentManager();
//                                Bundle data = new Bundle();
//                                data.putSerializable("asset", asset);
//                                assetsFragment.setArguments(data);
//                                Log.d("API CALL", asset.attributes.weatherData.name+"1");
//                                fragmentManager.beginTransaction().replace(R.id.fragment_container_view_tag, assetsFragment).commit();
//                                Log.d("API CALL", asset.attributes.weatherData.name+"2");
//                                Log.d("API CALL", asset.attributes.weatherData.name+"3");

                                Toast.makeText(getActivity(), "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                    }

                });

            }
            @Override
            public void onFailure(Call<Asset> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString());
                Toast.makeText(getContext(), "ERROR CALL API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        Log.d("Fragment", "onCreateView");

        View view=inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment

        // Return view
        return view;
    }
}