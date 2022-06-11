package com.rex1997.kiddos.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.rex1997.kiddos.R;
import com.rex1997.kiddos.facedetection.FaceDetectionActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        int delay = 3000;
        new Handler().postDelayed(() -> {
            Intent start=new Intent(SplashScreenActivity.this, FaceDetectionActivity.class);
            startActivity(start);
            finish();

        }, delay);
    }
}