package com.rex1997.kiddos.connection;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Face {
    @SerializedName("bbox")
    @Expose
    private List<Object> bbox;
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("age")
    @Expose
    private Double age;
    @SerializedName("class")
    @Expose
    private String _class;

    public List<Object> getBbox() {
        return bbox;
    }

    public void setBbox(List<Object> bbox) {
        this.bbox = bbox;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getAge() { return age; }

    public void setAge(Double age) {
        this.age = age;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public Face(List<Object> bbox, Double score, Double age, String _class) {
        this.bbox = bbox;
        this.score = score;
        this.age = age;
        this._class = _class;
    }


    @NonNull
    @Override
    public String toString() {
        return String.valueOf(this.age);
    }

}
