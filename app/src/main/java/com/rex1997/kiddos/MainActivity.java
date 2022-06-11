package com.rex1997.kiddos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rex1997.kiddos.oauth2library.OAuthResponse;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;

public class MainActivity extends AppCompatActivity {

    private OAuthResponse oAuth2Response;
    private TextView textToken;
    private ImageView latestImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getToken();
        textToken= findViewById(R.id.textView);
        latestImage = findViewById(R.id.imageView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLatestImage();
    }

    private String getToken(){
        String token = oAuth2Response.getAccessToken();
        String token2 = oAuth2Response.getRefreshToken();
        textToken.setText(token + token2);
        return token;
    }

    private void getLatestImage (){
        File imagePath = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        FileFilter imageFilter = new WildcardFileFilter("*.jpg");
        File[] images = imagePath.listFiles(imageFilter);

        assert images != null;
        int imagesCount = images.length; // get the list of images from folder
        Bitmap getImage = BitmapFactory.decodeFile(images[imagesCount - 1].getAbsolutePath());
        latestImage.setImageBitmap(getImage); // set bitmap in imageview
    }
}