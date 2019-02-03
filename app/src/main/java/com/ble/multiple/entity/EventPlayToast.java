package com.ble.multiple.entity;

import android.content.Context;

public class EventPlayToast {

    public final String text;
    public int length;
    public final Context context;

    public EventPlayToast(String text, int length, Context context) {
        this.text = text;
        this.length = length;
        this.context = context;
    }
}


