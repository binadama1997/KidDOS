package com.rex1997.kiddos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;

import com.rex1997.kiddos.connection.ApiService;

public class MainActivity extends AppCompatActivity {

    private ImageView latestImage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        latestImage = findViewById(R.id.imageView);
        getLatestImage();

        ApiService post = new ApiService();
        post.postMethod();
    }

    private void getLatestImage (){
        File imagePath = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getPackageName()
                + "/Files");
        FileFilter imageFilter = new WildcardFileFilter("*.jpg");
        File[] images = imagePath.listFiles(imageFilter);

        assert images != null;
        int imagesCount = images.length; // get the list of images from folder
        Bitmap getImage = BitmapFactory.decodeFile(images[imagesCount - 1].getAbsolutePath());
        latestImage.setImageBitmap(getImage); // set bitmap in imageview
    }
}