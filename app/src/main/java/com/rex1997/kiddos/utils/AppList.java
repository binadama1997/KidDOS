package com.rex1997.kiddos.utils;

import android.graphics.drawable.Drawable;

public class AppList {
    public final String name;
    Drawable icon;
    public final String packages;

    public AppList(String name, Drawable icon, String packages) {
        this.name = name;
        this.icon = icon;
        this.packages = packages;
    }

    public String getName() {
        return name;
    }
    public Drawable getIcon() {
        return icon;
    }
    public String getPackages() {
        return packages;
    }
}
