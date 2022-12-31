package com.example.sampleproject;

import com.example.sampleproject.Model.Asset;
import com.example.sampleproject.Model.Current;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface APIInterface {

    @GET("api/master/asset/{assetID}")
    Call<Asset> getAsset(@Path("assetID") String assetID);//, @Header("Authorization") String auth);

    @GET("api/master/asset/user/current")
    Observable<ArrayList<Current>> getCurrent();
}
