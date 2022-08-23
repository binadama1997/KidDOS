package com.rex1997.kiddos.connection;

import com.rex1997.kiddos.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String ID = BuildConfig.clientID;
    private static final String SECRET = BuildConfig.clientSecret;
    private static final String BASE_URL = "https://api.everypixel.com/";
    private static final String BASIC_AUTH =
            "Basic " + android.util.Base64.encodeToString((ID + ":" + SECRET).getBytes(),
                    android.util.Base64.NO_WRAP);
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;

    public RetrofitClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        chain -> {
                            Request auth = chain.request();

                            Request.Builder requestBuilder = auth.newBuilder()
                                    .addHeader("Authorization", BASIC_AUTH)
                                    .method(auth.method(), auth.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                )
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public ApiInterface apiInterface(){
        return retrofit.create(ApiInterface.class);
    }
}
