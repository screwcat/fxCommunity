package com.taiji.fxsqjw.views.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/29.
 */

public class SerializableHashMap<String,UserPolygons> extends HashMap implements Parcelable {

    public SerializableHashMap(){

    }

    protected SerializableHashMap(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SerializableHashMap> CREATOR = new Creator<SerializableHashMap>() {
        @Override
        public SerializableHashMap createFromParcel(Parcel in) {
            return new SerializableHashMap(in);
        }

        @Override
        public SerializableHashMap[] newArray(int size) {
            return new SerializableHashMap[size];
        }
    };
}
