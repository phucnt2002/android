package com.example.sampleproject.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Asset implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("createdOn")
    public String createdOn;
    @SerializedName("name")
    public String name;
    @SerializedName("accessPublicRead")
    public String accessPublicRead;
    @SerializedName("parentID")
    public String parentID;
    @SerializedName("realm")
    public String realm;
    @SerializedName("type")
    public String type;
    @SerializedName("path")
    public String path[];
    @SerializedName("attributes")
    public Attributes attributes;

}
