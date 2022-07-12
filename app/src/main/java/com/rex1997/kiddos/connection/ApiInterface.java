package com.rex1997.kiddos.connection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    String BASE_URL = "https://api.everypixel.com/v1/faces";

    @FormUrlEncoded
    @POST(BASE_URL)
    Call<ResponseBody> uploadClass (

    );
}
