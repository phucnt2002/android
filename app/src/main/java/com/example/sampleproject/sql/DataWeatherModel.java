package com.example.sampleproject.sql;

public class DataWeatherModel {

    public String id;
    public long time;
    public String temp;
    public String humidity;
    public String speed;
    public String deg;
    //cloud
    //pressure
    //sunset


    public DataWeatherModel(String id,long time, String temp, String humidity, String speed, String deg) {
        this.id = id;
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
        this.speed = speed;
        this.deg = deg;
    }
}
