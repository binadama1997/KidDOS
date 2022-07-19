package com.rex1997.kiddos.connection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiServiceTest2 extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postMethod() {
        String baseURL = "https://api.everypixel.com/v1/faces";
        String clientID = "6aywCResze0K9kh8u70Ky8WG";
        String clientSecret = "crD8hhONleq3AGHB7fPuIKdv9Iln8M3iQeOwx0Qz8gvlHcyv";
        String result="";

        try{
            Map<String,String> map = new LinkedHashMap<>();
            map.put("key", "value");
            StringBuilder postdata = new StringBuilder();
            for(Map.Entry<String,String> ignored :map.entrySet()){
                if(postdata.length() != 0) postdata.append('&');
                postdata.append("data");
                postdata.append('=');
                postdata.append(getImage());
            }
            byte[] data = postdata.toString().getBytes(StandardCharsets.UTF_8);
            String val = clientID +":"+ clientSecret;
            byte[] authEncoder = Base64.getEncoder().encode(val.getBytes());
            String authString = new String(authEncoder);
            URL url = new URL(baseURL);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Basic " + authString);
            urlConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data");
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(data);
            Reader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                    StandardCharsets.UTF_8));
            StringBuilder str = new StringBuilder();
            for(int i;(i = in.read()) >= 0;)
                str.append((char)i);
            result = str.toString();
        }
        catch(Exception e){
            //handle the exception
            e.printStackTrace();
        }
        System.out.println(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getImageToString(){
        File imagePath = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getPackageName()
                + "/Files");
        FileFilter imageFilter = new WildcardFileFilter("*.jpg");
        File[] images = imagePath.listFiles(imageFilter);

        assert images != null;
        int imagesCount = images.length; // get the list of images from folder
        Bitmap getImage = BitmapFactory.decodeFile(images[imagesCount - 1].getAbsolutePath());

        String encodeString = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            getImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bt = bos.toByteArray();
            encodeString = Base64.getEncoder().encodeToString (bt);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return encodeString;
    }

    private File getImage (){
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

    private JSONObject getData (String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        JSONObject data = obj.getJSONObject("data");
        JSONArray child = (JSONArray)data.get("children");
        data = (JSONObject)child.get(0);
        data = (JSONObject)data.get("data");

        return data;
    }

}
