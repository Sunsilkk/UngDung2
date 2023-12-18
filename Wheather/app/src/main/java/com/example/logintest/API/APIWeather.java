package com.example.logintest.API;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIWeather {
    @POST("/api/master/asset/datapoint/{assetId}/attribute/{attributeName}")
    Call<JsonArray> getAssetDatapointAttribute(@Header("Authorization") String bearerToken,
                                               @Path("assetId") String assetId ,
                                               @Path("attributeName") String attributeName,
                                               @Body Asset rawBody);

}
