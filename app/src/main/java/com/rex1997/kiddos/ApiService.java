package com.rex1997.kiddos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.rex1997.kiddos.connection.Post;
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

public class ApiService extends AppCompatActivity {
    private static final String TAG = "API Service";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar loadingBar = findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.VISIBLE);
        apiService(getLatestImage());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void apiService(File getImage){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "data",
                        getImage.getName(),
                        RequestBody.create(new File(getImage.getAbsolutePath()), MediaType.parse("text/plain")))
                .build();

        Call<Post> call = RetrofitClient
                .getInstance()
                .apiInterface()
                .uploadImage(requestBody)
                ;

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String data = response.body().getFaces().toString();
                    data = data.replaceAll("\\[", "").replaceAll("\\]", "");
                    double result = Double.parseDouble(data);
                    Log.i(TAG, "Get age from API: " + result);
                    startActivity(new Intent(ApiService.this, ModeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("Age", result));
                } else {
                    Log.e(TAG, "onEmptyResponse => Returned empty response");
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    Toast.makeText(ApiService.this, "Request was aborted", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Request was aborted");
                } else {
                    Toast.makeText(ApiService.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, t.getMessage());
                }
                finish();
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