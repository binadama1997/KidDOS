package com.rex1997.kiddos;

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "API Service";
    private ProgressBar loadingBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingBar = findViewById(R.id.loadingBar);

        apiService(getLatestImage());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void apiService(File getImage){
        loadingBar.setVisibility(View.VISIBLE);
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

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String result = response.body().getFaces().toString();
                    Toast.makeText(MainActivity.this, "SUCCESS.", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Get data from API." + result);
                } else {
                    Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onEmptyResponse => Returned empty response");
                    finish();
                }
                loadingBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                if(call.isCanceled()) {
                    Toast.makeText(MainActivity.this, "Request was aborted", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Request was aborted");
                } else {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, t.getMessage());
                }
                finish();
                loadingBar.setVisibility(View.GONE);
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