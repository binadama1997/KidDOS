package com.rex1997.kiddos.connection;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse {

    /** POST **/
    @SerializedName("status")
    private final boolean _error;

    @SerializedName("message")
    private final String _message;

    /** GET **/
    @SerializedName("age")
    private final String _age;
    @SerializedName("score")
    private final String _score;
    @SerializedName("bbox")
    private final String _bbox;
    @SerializedName("class")
    private final String _class;

    public DefaultResponse(boolean _error, String _message, String _age, String _score, String _bbox, String _class) {
        this._error = _error;
        this._message = _message;
        this._age = _age;
        this._score = _score;
        this._bbox = _bbox;
        this._class = _class;
    }

    /** Getter POST **/
    public boolean isErr() {
        return _error;
    }

    public String getMsg() {
        return _message;
    }

    /** Getter GET **/
    public String get_age() {
        return _age;
    }

    public String get_score() {
        return _score;
    }

    public String get_bbox() {
        return _bbox;
    }

    public String get_class() {
        return _class;
    }
}
