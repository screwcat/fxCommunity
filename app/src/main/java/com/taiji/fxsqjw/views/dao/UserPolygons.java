package com.taiji.fxsqjw.views.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/29.
 */

public class UserPolygons implements Parcelable {
    private List<LatLng> latlons;
    private String zrqdm;
    private String zrqmc;

    public UserPolygons(){

    }

    protected UserPolygons(Parcel in) {
        latlons = in.createTypedArrayList(LatLng.CREATOR);
        zrqmc = in.readString();
        zrqdm = in.readString();
    }

    public static final Creator<UserPolygons> CREATOR = new Creator<UserPolygons>() {
        @Override
        public UserPolygons createFromParcel(Parcel in) {
            return new UserPolygons(in);
        }

        @Override
        public UserPolygons[] newArray(int size) {
            return new UserPolygons[size];
        }
    };

    public List<LatLng> getLatlons() {
        return latlons;
    }

    public void setLatlons(List<LatLng> latlons) {
        this.latlons = latlons;
    }

    public String getZrqdm() {
        return zrqdm;
    }

    public void setZrqdm(String zrqdm) {
        this.zrqdm = zrqdm;
    }

    public String getZrqmc() {
        return zrqmc;
    }

    public void setZrqmc(String zrqmc) {
        this.zrqmc = zrqmc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(latlons);
        dest.writeString(zrqmc);
        dest.writeString(zrqdm);
    }
}
