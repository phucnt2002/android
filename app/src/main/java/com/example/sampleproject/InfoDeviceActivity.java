package com.example.sampleproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sampleproject.Model.Current;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import maes.tech.intentanim.CustomIntent;


public class InfoDeviceActivity extends AppCompatActivity {

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

        findViewById(R.id.btnBack).setOnClickListener(view -> finish());

        String json = getIntent().getStringExtra("Current");
        Gson gson = new GsonBuilder().create();
        KLog.json(json);
        Current current = gson.fromJson(json, Current.class);

        tvName.setText(current.name);

        tvHumidity.setText(current.attributes.humidity.value + "%");
        tvTemp.setText(current.attributes.temperature.value + " °C");
        tvDeg.setText(String.valueOf(current.attributes.windDirection.value));
        tvSpeed.setText(current.attributes.windSpeed.value + " km/h");
        tvPressure.setText(current.attributes.weatherData.value.main.pressure+ " N/m²");
        tvTempMax.setText("Cao nhất: "+current.attributes.weatherData.value.main.temp_max + " °C");
        tvTempMin.setText("Thấp nhất: "+current.attributes.weatherData.value.main.temp_min + " °C");

        tvTimeDeg.setText(formatLongToDate(current.attributes.windDirection.timestamp));
        tvTimeSpeed.setText(formatLongToDate(current.attributes.windSpeed.timestamp));
        tvTimeHumidity.setText(formatLongToDate(current.attributes.humidity.timestamp));
        tvTimeTemp.setText(formatLongToDate(current.attributes.temperature.timestamp));
        tvTimeLatLong.setText(formatLongToDate(current.attributes.location.timestamp));
        tvSunset.setText("Mặt trời mọc: "+formatLongToDate(current.attributes.weatherData.value.sys.sunset*1000));
        tvSunrise.setText("Mặt trời lặn: "+formatLongToDate(current.attributes.weatherData.value.sys.sunrise*1000));
        tvTimePressure.setText(formatLongToDate(current.attributes.location.timestamp));

        double lat = current.attributes.location.value.coordinates.get(1);
        double lon = current.attributes.location.value.coordinates.get(0);
        tvLat.setText("Lat: " + lat);
        tvLong.setText("Long: " + lon);

        findViewById(R.id.btnChart).setOnClickListener(view -> {
            Intent intent = new Intent(this, ChartActivity.class);
            intent.putExtra("id", current.id);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");
            finish();
        });


    }

    @SuppressLint("NewApi")
    public static String formatLongToDate(long epochMilliUtc) {
        @SuppressLint({"NewApi", "LocalSuppress"}) Instant instant = Instant.ofEpochMilli(epochMilliUtc);
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

}