package com.rex1997.kiddos.connection;

import com.google.gson.annotations.SerializedName;

public class ImageClass {
    @SerializedName("data")
    private String Image;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }

    /*
    public String getImage() {
        return Image;
    }*/
}
