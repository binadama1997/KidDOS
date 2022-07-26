package com.rex1997.kiddos.connection;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    @SerializedName("faces")
    @Expose
    private List<Face> faces = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Face> getFaces() {
        return faces;
    }

    public void setFaces(List<Face> faces) {
        this.faces = faces;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Post(List<Face> faces, String status) {
        this.faces = faces;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Post{" +
                "faces=" + faces +
                ", status='" + status + '\'' +
                '}';
    }
}
