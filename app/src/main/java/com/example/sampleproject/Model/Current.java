package com.example.sampleproject.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Current {

    public String id;
    public String name;
    public long createdOn;
    public Attributes attributes;


//    public static Current fromJson(String json) {
//        if (json == null) {
//            return new Current();
//        }
//        Gson gson = new GsonBuilder().create();
//        return gson.fromJson(json, Current.class);
//
//    }

}
