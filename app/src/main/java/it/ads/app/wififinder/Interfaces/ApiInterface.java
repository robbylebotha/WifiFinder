package it.ads.app.wififinder.Interfaces;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/api/")
    Call<JSONArray> sendInfo(@Body JSONArray deviceData);


}
