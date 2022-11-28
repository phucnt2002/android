package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sampleproject.Model.Asset;
import com.example.sampleproject.Model.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    APIInterface apiInterface;
    MapInterface mapInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        TextView txttype = (TextView)findViewById(R.id.textView1);

//        txttype.setText("HELLO MY FRIEND");
        //Call AssetAPI
//        apiInterface = APIClient.getClient().create(APIInterface.class);
//
////        System.out.println(apiInterface);
//
////        Call<Asset> call = apiInterface.getAsset("6H4PeKLRMea1L0WsRXXWp9");//, "Bearer "+ token);
////        call.enqueue(new Callback<Asset>() {
////            @Override
////            public void onResponse(Call<Asset> call, Response<Asset> response) {
////                Log.d("API CALL", response.code()+"");
////                //Log.d ("API CALL", response.toString());
////                Asset asset = response.body();
////
////                Log.d("API CALL", asset.type+"");
////                txttype.setText(asset.attributes.toString());
////            }
////            @Override
////            public void onFailure(Call<Asset> call, Throwable t) {
////                Log.d("API CALL", t.getMessage().toString());
////
////                //t.printStackTrace();
////
////            }
////        });
//
//        //Call mapAPI
//        mapInterface = APIClient.getClient().create(MapInterface.class);
//        Call<Map> call = mapInterface.getAllData();
//        call.enqueue(new Callback<Map>() {
//            @Override
//            public void onResponse(Call<Map> call, Response<Map> response) {
//                Log.d("API CALL", response.code()+"");
//                //Log.d ("API CALL", response.toString());
//                Map map = response.body();
//
//                Log.d("API CALL", map.version+"");
//                txttype.setText(map.options.toString());
//            }
//
//            @Override
//            public void onFailure(Call<Map> call, Throwable t) {
//                Log.d("API CALL", t.getMessage().toString());
//            }
//        });

    }
}