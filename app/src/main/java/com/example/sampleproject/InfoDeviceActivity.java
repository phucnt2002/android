package com.example.sampleproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                Intent intent = new Intent(InfoDeviceActivity.this, CommentActivity.class);
                intent.putExtra("Current", new Gson().toJson(current));
                startActivity(intent);
                CustomIntent.customType(InfoDeviceActivity.this, "left-to-right");
            }
        });

        btnReadComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoDeviceActivity.this, ReadCommentActivity.class);
                startActivity(intent);
                CustomIntent.customType(InfoDeviceActivity.this, "left-to-right");
            }
        });

    }

    @SuppressLint("NewApi")
    public static String formatLongToDate(long epochMilliUtc) {
        @SuppressLint({"NewApi", "LocalSuppress"}) Instant instant = Instant.ofEpochMilli(epochMilliUtc);
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

}