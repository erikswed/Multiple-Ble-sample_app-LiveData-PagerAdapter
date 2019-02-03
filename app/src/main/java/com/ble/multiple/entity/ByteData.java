package com.ble.multiple.entity;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ByteData {

    // [START Firestore keys  ]

    @Exclude
    public static final String DATA = "data";

    // [END Firestore keys]

    private String data;

    public ByteData() {
    }

    public ByteData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Exclude
    public Map<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put(DATA, data);
        return result;
    }
}
