package com.rex1997.kiddos.facedetection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.rex1997.kiddos.connection.ApiService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveImage extends FaceDetectionActivity{
    private static final String TAG = "Save Image";

    public void saveImage(Bitmap image){
    File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Toast.makeText(this, "ERROR! : Error creating media file, check storage permissions", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error creating media file, check storage permissions");
        } else {
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Log.i(TAG, "Image file has been saved");
                fos.close();
                new Handler().postDelayed(() -> {
                    Intent result = new Intent(this, ApiService.class);
                    startActivity(result);
                }, 1000);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "ERROR! : File not found =>" + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Toast.makeText(this, "ERROR! : Error accessing file =>" + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    }

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getPackageName()
                + "/Files");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm", Locale.getDefault()).format(new Date());
        File mediaFile;
        String mImageName="KD_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
