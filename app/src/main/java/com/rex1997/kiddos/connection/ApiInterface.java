package com.rex1997.kiddos.connection;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("v1/faces")
    Call<Post> uploadImage(@Body RequestBody data);
}
