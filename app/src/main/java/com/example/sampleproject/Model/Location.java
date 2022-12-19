package com.example.sampleproject.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Location {
    public Value value;
    public long timestamp;
    public class Value {
        public ArrayList<Double> coordinates;
    }
}
