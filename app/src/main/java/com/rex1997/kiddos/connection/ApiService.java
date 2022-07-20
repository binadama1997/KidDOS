package com.rex1997.kiddos.connection;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiService extends AppCompatActivity {

    /*
    * Using Full okhttp3 library
    *
    * Use this strict mode code if you use this full okhttp3 request for quick use.
    *
    * StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    * StrictMode.setThreadPolicy(policy);
    */

    private static final String clientID = "6aywCResze0K9kh8u70Ky8WG";
    private static final String clientSecret = "crD8hhONleq3AGHB7fPuIKdv9Iln8M3iQeOwx0Qz8gvlHcyv";
    private static final String BASE_URL = "https://api.everypixel.com/v1/faces";
    private static final String BASIC_AUTH = "Basic " +
            android.util.Base64.encodeToString((clientID + ":" + clientSecret).getBytes(),
                    android.util.Base64.NO_WRAP);
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    public void apiPostService(File getImage){

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", getImage.getName(),
                        RequestBody.create(new File(getImage.getAbsolutePath()), MEDIA_TYPE))
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", BASIC_AUTH)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()){
                String responseBody = response.body().string();
                Log.i("Response", responseBody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
