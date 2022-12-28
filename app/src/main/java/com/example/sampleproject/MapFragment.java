package com.example.sampleproject;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sampleproject.Model.Asset;
import com.example.sampleproject.Model.Current;
import com.example.sampleproject.sql.DBManager;
import com.example.sampleproject.sql.DataWeatherModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends Fragment {
    private GoogleMap mMap;
    APIInterface apiInterface;
    SupportMapFragment supportMapFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        Log.d("Fragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

//        GPS
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling   ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
            return;
        }

        setViewMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setViewMap();
        }
    }

    @SuppressLint("MissingPermission")
    private void setViewMap() {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap = map;

                mMap.setMyLocationEnabled(true);

                //event click marker
                mMap.setOnMarkerClickListener(marker -> {
                    Current current = (Current) marker.getTag();

                    Intent intent = new Intent(getContext(), InfoDeviceActivity.class);
                    KLog.json(new Gson().toJson(current));
                    intent.putExtra("Current", new Gson().toJson(current));
                    startActivity(intent);
//                    CustomIntent.customType(getContext(), "fadein-to-fadeout");
                    return false;
                });

                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(true);

                APIClient.getClient().create(APIInterface.class).getCurrent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(currentArrayList -> {
                            for (Current current : currentArrayList) {
                                if (current.attributes.weatherData != null) {
                                    Log.e("add Marker Device", " " + current.id + " " + current.attributes.location.value.coordinates.get(1) + " " + current.attributes.location.value.coordinates.get(0));

                                    //Add maker thiết bị
                                    double lat = current.attributes.location.value.coordinates.get(1);
                                    double lon = current.attributes.location.value.coordinates.get(0);
                                    LatLng latLng = new LatLng(lat, lon);
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.title(current.name);
                                    markerOptions.position(latLng);

                                    Marker marker = mMap.addMarker(markerOptions);
                                    marker.setTag(current);


                                    DataWeatherModel dataWeatherModel = new DataWeatherModel(current.id, Calendar.getInstance().getTimeInMillis(),
                                            current.attributes.temperature.value,
                                            current.attributes.humidity.value,
                                            current.attributes.windSpeed.value,
                                            current.attributes.windDirection.value);
                                    DBManager.getInstance().addWeather(dataWeatherModel);

                                    //move camera to marker
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                    mMap.animateCamera(cameraUpdate);
                                }
                            }

                            KLog.json(new Gson().toJson(DBManager.getInstance().getWeather()));
                        }, throwable -> {
                            Log.e("getCurrent Error", " " + throwable.getMessage());
                        });


            }

        });

    }

    public static long getTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getDefault());

        Date date = new Date();
        try {
            date = sdf.parse(getDateNow());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getDateNow() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String time = df.format(c.getTime());
        return time;
    }


}