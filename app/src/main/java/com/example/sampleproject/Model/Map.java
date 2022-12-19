package com.example.sampleproject.Model;

import com.google.gson.annotations.SerializedName;

public class Map {
    @SerializedName("options")
    public Object options;

    @SerializedName("version")
    public String version;

    @SerializedName("sources")
    public Object sources;

    @SerializedName("sprite")
    public String sprite;

    @SerializedName("glyphs")
    public String glyphs;

    @SerializedName("layers")
    public Object layers;
}
