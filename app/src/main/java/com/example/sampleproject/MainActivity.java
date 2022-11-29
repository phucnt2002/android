package com.example.sampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sampleproject.Model.Asset;
import com.example.sampleproject.Model.Map;
import com.example.sampleproject.databinding.ActivityMapsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private UiSettings mUiSettings;

    private CheckBox mMyLocationButtonCheckbox;

    private CheckBox mMyLocationLayerCheckbox;

    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final int LOCATION_LAYER_PERMISSION_REQUEST_CODE = 2;
    private boolean mLocationPermissionDenied = false;
    private boolean isPermisstionGranter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.



//        checkPermission();
        //đang lỗi hàm check quyền truy cập nên ko check mà sd luôn

        if(isPermisstionGranter){
            if(checkGooglePlayServices()){
                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
                mapFragment.getMapAsync(this);
                //Toast.makeText(this, "Google Playservices Available", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Google Playservices Not Available", Toast.LENGTH_LONG).show();
            }
        }

    }


    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(result == ConnectionResult.SUCCESS){
            return true;
        }else if(googleApiAvailability.isUserResolvableError(result)){
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(MainActivity.this, "User Cancelked Dialoge", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        }
        return false;
    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermisstionGranter=true;
                Toast.makeText(MainActivity.this, "Permisstion Granter", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("asdasssssss", "****");
        mMap = map;

        //Add maker
//        LatLng latLng = new LatLng(10.87, 106.8);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.title("Cac em gai");
//        markerOptions.position(latLng);
//        mMap.addMarker(markerOptions);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//        mMap.animateCamera(cameraUpdate);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Change type of map
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.nonemap){
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
        if(item.getItemId()==R.id.NormalMap){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if(item.getItemId()==R.id.SatelliteMap){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if(item.getItemId()==R.id.MapHybird){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        if(item.getItemId()==R.id.MapTerrain){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        return super.onOptionsItemSelected(item);
    }
}