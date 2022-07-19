package com.rex1997.kiddos;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.rex1997.kiddos.connection.DefaultResponse;
import com.rex1997.kiddos.connection.RetrofitClient;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView latestImage;
    private TextView textView;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);

        apiServicePost();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void apiServicePost(){
        /* POST */
        progressBar.setVisibility(View.VISIBLE);
        RequestBody requestFile = RequestBody.create
                (getLatestImage(), MediaType.parse("multipart/form-data"));

        MultipartBody.Part body = MultipartBody.Part.createFormData
                ("data", getLatestImage().getName(), requestFile);

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .apiInterface()
                .uploadImage(body)
                ;

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                assert dr != null;
                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "SUCCESS! Ok.", Toast.LENGTH_LONG).show();
                    //Log.d("SUCCESS! Ok.", dr.getMsg());

                    apiServiceGet();

                } else if (response.code() == 401) {
                    Toast.makeText(MainActivity.this, "UNAUTHORIZE", Toast.LENGTH_LONG).show();
                    //Log.d("FAIL! User unauthorize.", dr.getMsg());
                } else if (response.code() == 429){
                    Toast.makeText(MainActivity.this, "Subscription limit has reached", Toast.LENGTH_LONG).show();
                    //Log.d("FAIL! Limit reached.", dr.getMsg());
                } else {
                    Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_LONG).show();
                    //Log.e("FAIL! Something wrong.", dr.getMsg());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.e("Retrofit POST image : ", t.toString());
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void apiServiceGet(){
        //DefaultResponse defaultResponse = new DefaultResponse(_age, )
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .apiInterface()
                .apiServicePost()
                ;

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this,"Data retrieved",Toast.LENGTH_LONG).show();

                        //Log.i("onSuccess", response.body());
                        //String jsonresponse = response.body().toString();
                    } else {
                        Toast.makeText(MainActivity.this,"Nothing returned",Toast.LENGTH_LONG).show();
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                Log.e("Retrofit Get image : ", t.toString());
            }
        });
    }

    private File getLatestImage(){
        File imagePath = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getPackageName()
                + "/Files");
        FileFilter imageFilter = new WildcardFileFilter("*.jpg");
        File[] images = imagePath.listFiles(imageFilter);

        assert images != null;
        int imagesCount = images.length; // get the list of images from folder
        return new File(images[imagesCount - 1].getAbsolutePath());
    }
}