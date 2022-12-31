package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sampleproject.Model.Current;
import com.example.sampleproject.sql.DataWeatherModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;

public class InfoHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_history);

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
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());

        String json = getIntent().getStringExtra("Current");
        Gson gson = new GsonBuilder().create();
        KLog.json(json);
        DataWeatherModel current = gson.fromJson(json, DataWeatherModel.class);

        tvHumidity.setText(current.humidity + "%");
        tvTemp.setText(current.temp + " °C");
        tvDeg.setText(String.valueOf(current.deg)+ " °");
        tvSpeed.setText(current.speed + " km/h");

    }
}