package com.rex1997.kiddos.connection;

import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiService extends AppCompatActivity {
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startApiService(){
        RequestBody requestFile = RequestBody.create(getLatestImage(), MediaType.parse("multipart/form-data"));
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .apiInterface()
                .uploadImage(requestFile);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                DefaultResponse dr =  response.body();
                if (response.code() == 200) {
                    assert dr != null;
                    Toast.makeText(ApiService.this, dr.getMsg(), Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(ApiService.this, "UNAUTHORIZE", Toast.LENGTH_LONG).show();
                } else if (response.code() == 429){
                    Toast.makeText(ApiService.this, "Subscription limit has reached", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ApiService.this, "Something wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                Toast.makeText(ApiService.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public File getLatestImage(){
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
    */
}
