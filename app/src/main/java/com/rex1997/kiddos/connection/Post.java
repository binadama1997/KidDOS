package com.rex1997.kiddos.connection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    @SerializedName("faces")
    @Expose
    private final List<Face> faces;
    @SerializedName("status")
    @Expose
    private final String status;

    public List<Face> getFaces() {
        return faces;
    }

    public String getStatus() {
        return status;
    }

    public Post(List<Face> faces, String status) {
        this.faces = faces;
        this.status = status;
    }
}
