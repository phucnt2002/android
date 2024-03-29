package com.example.sampleproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sampleproject.Model.Current;
import com.example.sampleproject.sql.DBManager;
import com.example.sampleproject.sql.DataWeatherModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;


public class InfoDeviceActivity extends AppCompatActivity implements LocationListener {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_device);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvTemp = findViewById(R.id.tvTemp);
        TextView tvLat = findViewById(R.id.tvLat);
        TextView tvLong = findViewById(R.id.tvLong);
        TextView tvHumidity = findViewById(R.id.tvHumidity);
        TextView tvDeg = findViewById(R.id.tvDeg);
        TextView tvSpeed = findViewById(R.id.tvSpeed);
        TextView tvTimeDeg = findViewById(R.id.tvTimeDeg);
        TextView tvTimeSpeed = findViewById(R.id.tvTimeSpeed);
        TextView tvTimeHumidity = findViewById(R.id.tvTimeHumidity);
        TextView tvTimeTemp = findViewById(R.id.tvTimeTemp);
        TextView tvTimeLatLong = findViewById(R.id.tvTimeLatLong);
        TextView tvSunset = findViewById(R.id.tvSunset);
        TextView tvSunrise = findViewById(R.id.tvSunrises);
        TextView tvPressure = findViewById(R.id.tvPressure);
        TextView tvTimePressure = findViewById(R.id.tvTimePressure);
        TextView tvTempMax = findViewById(R.id.tvTempMax);
        TextView tvTempMin = findViewById(R.id.tvTempMin);
        TextView tvCloud = findViewById(R.id.tvCloud);
        TextView tvTimeCloud = findViewById(R.id.tvTimeCloud);
        Button btnComment = findViewById(R.id.btnComment);
        Button btnReadComment = findViewById(R.id.btnReadComment);

        findViewById(R.id.btnBack).setOnClickListener(view -> finish());

        String json = getIntent().getStringExtra("Current");
        Gson gson = new GsonBuilder().create();
//        KLog.json(json);
        Current current = gson.fromJson(json, Current.class);

        tvName.setText(current.name);

        ArrayList<DataWeatherModel> weatherDatas = DBManager.getInstance().getWeatherByDate(Calendar.getInstance().getTimeInMillis(), current.id);


        float minTemp = Float.parseFloat(weatherDatas.get(0).temp);
        float maxTemp = 0;
        for(int i = 0; i<weatherDatas.size(); i++){
            if(Float.parseFloat(weatherDatas.get(i).temp)<minTemp){
                minTemp = Float.parseFloat(weatherDatas.get(i).temp);
            }
            if(Float.parseFloat(weatherDatas.get(i).temp)>maxTemp){
                maxTemp = Float.parseFloat(weatherDatas.get(i).temp);
            }
        }

        tvHumidity.setText(current.attributes.humidity.value + "%");
        tvTemp.setText(current.attributes.temperature.value + " °C");
        tvDeg.setText((current.attributes.windDirection.value)+" °");
        tvSpeed.setText(current.attributes.windSpeed.value + " km/h");
        tvPressure.setText(current.attributes.weatherData.value.main.pressure+ " N/m²");
//        tvTempMax.setText("Cao nhất: "+current.attributes.weatherData.value.main.temp_max + " °C");
//        tvTempMin.setText("Thấp nhất: "+current.attributes.weatherData.value.main.temp_min + " °C");
        tvTempMax.setText("Cao nhất: "+maxTemp + " °C");
        tvTempMin.setText("Thấp nhất: "+minTemp + " °C");
        tvCloud.setText((current.attributes.weatherData.value.weather.get(0).description));

        tvTimeDeg.setText(formatLongToDate(current.attributes.windDirection.timestamp));
        tvTimeSpeed.setText(formatLongToDate(current.attributes.windSpeed.timestamp));
        tvTimeHumidity.setText(formatLongToDate(current.attributes.humidity.timestamp));
        tvTimeTemp.setText(formatLongToDate(current.attributes.temperature.timestamp));
        tvTimeLatLong.setText(formatLongToDate(current.attributes.location.timestamp));
        tvSunset.setText("Mặt trời lặn: "+formatLongToDate(current.attributes.weatherData.value.sys.sunset*1000));
        tvSunrise.setText("Mặt trời mọc: "+formatLongToDate(current.attributes.weatherData.value.sys.sunrise*1000));
        tvTimePressure.setText(formatLongToDate(current.attributes.humidity.timestamp));
        tvTimeCloud.setText(formatLongToDate(current.attributes.humidity.timestamp));

        double lat = current.attributes.location.value.coordinates.get(1);
        double lon = current.attributes.location.value.coordinates.get(0);
        tvLat.setText("Lat: " + lat);
        tvLong.setText("Long: " + lon);

        findViewById(R.id.btnChart).setOnClickListener(view -> {
            Intent intent = new Intent(this, ChartActivity.class);
            intent.putExtra("id", current.id);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(InfoDeviceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(InfoDeviceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                KLog.d("Location: ", location);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                Log.d("location: ", longitude+"###"+latitude);
                float[] results = new float[1];
                location.distanceBetween(current.attributes.location.value.coordinates.get(1), current.attributes.location.value.coordinates.get(0), latitude, longitude, results);
                Log.d("khoangcach", results[0]+"");
                if(results[0]<= 90000){
                    Intent intent = new Intent(InfoDeviceActivity.this, CommentActivity.class);
                    intent.putExtra("Current", new Gson().toJson(current));
                    startActivity(intent);
                    CustomIntent.customType(InfoDeviceActivity.this, "left-to-right");
                }else{
                    Toast.makeText(InfoDeviceActivity.this, "Bạn ở quá xa so với maker", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        btnReadComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoDeviceActivity.this, ReadCommentActivity.class);
                intent.putExtra("id", current.id);
                startActivity(intent);
                CustomIntent.customType(InfoDeviceActivity.this, "left-to-right");
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("NewApi")
    public static String formatLongToDate(long epochMilliUtc) {
        @SuppressLint({"NewApi", "LocalSuppress"}) Instant instant = Instant.ofEpochMilli(epochMilliUtc);
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

}