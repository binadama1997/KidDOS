package com.rex1997.kiddos.connection;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Face {
    @SerializedName("bbox")
    @Expose
    private final List<Object> bbox;
    @SerializedName("score")
    @Expose
    private final Double score;
    @SerializedName("age")
    @Expose
    private final Double age;
    @SerializedName("class")
    @Expose
    private final String _class;

    public List<Object> getBbox() {
        return bbox;
    }

    public Double getScore() {
        return score;
    }

    public Double getAge() {
        return age;
    }

    public String get_class() {
        return _class;
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
        return String.valueOf(getAge());
    }

    /*
    @NonNull
    @Override
    public String toString() {
        return "{\"bbox\"=\"" + getBbox() + "\"" +
                ", \"score\"=\"" + getScore() + "\"" +
                ", \"age\"=\"" + getAge() + "\"" +
                ", \"_class\"=\"" + get_class() + "\"" +
                "}";
    }



    @NonNull
    @Override
    public String toString() {
        return "{" +
                "bbox:\'" + bbox +
                "\',\'score:\'" + score +
                "\',\'age:\'" + age +
                "\',\'_class:\'" + _class +
                "}";
    }

     */
}

