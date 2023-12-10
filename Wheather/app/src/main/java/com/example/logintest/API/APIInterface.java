package com.example.logintest.API;

import com.example.logintest.Model.token;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {
    // Login API method
    @FormUrlEncoded
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<token> login(@Field("client_id") String client_id, @Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);

    @GET("api/master/asset/{assetID}")
    Call<token> getAsset(@Path("assetID") String assetID);

}
