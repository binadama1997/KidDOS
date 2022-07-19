package com.rex1997.kiddos.connection;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @Multipart
    @POST("v1/faces")
    Call<DefaultResponse> uploadImage(@Part MultipartBody.Part data);

    /*
    * @GET("v1/faces")
    * Call<ResponseBody> getApiData(@Field("age") String age_data, @Field("score") String score_data, @Field("bbox") String bbox_data, @Field("class") String class_data);
    */

    @GET("v1/faces")
    Call <DefaultResponse> apiServicePost();
}
