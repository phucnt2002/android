package com.example.sampleproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sampleproject.sql.DBManager;
import com.example.sampleproject.sql.DataWeatherModel;
import com.google.gson.Gson;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class HistoryActivity extends AppCompatActivity {

    private ListView listViewHis;
    private ArrayList<String> arrayTime;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        id = getIntent().getStringExtra("id");
        listViewHis = (ListView) findViewById(R.id.listViewHis);
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
        ArrayList<DataWeatherModel> datas = new ArrayList<>();
        arrayTime = new ArrayList<>();
        ArrayList<DataWeatherModel> weatherDatas = DBManager.getInstance().getWeather();
        for(DataWeatherModel weatherData : weatherDatas){
            if(id.equals(weatherData.id)){
                arrayTime.add(TimeUtils.formatLongToDateHour(weatherData.time));
                datas.add(weatherData);
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                arrayTime
        );
        listViewHis.setAdapter(adapter);
        listViewHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(HistoryActivity.this, InfoHistoryActivity.class);
                String time = adapterView.getItemAtPosition(position).toString();
                String newTime = Long.toString(TimeUtils.convertTime(time)/1000);

                for(DataWeatherModel data: datas){
                    Log.d("debug", newTime+"*****"+(data.time));
                    Log.d("debug", (Long.toString(data.time)).contains(newTime)+"");

                    if((Long.toString(data.time)).contains(newTime)){
                        intent.putExtra("Current", new Gson().toJson(data));
                        startActivity(intent);
                        return;
                    }
                }
            }
        });
    }

}