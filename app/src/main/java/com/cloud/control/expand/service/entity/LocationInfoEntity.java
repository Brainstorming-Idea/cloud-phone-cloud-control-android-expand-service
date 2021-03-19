package com.cloud.control.expand.service.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：abin
 * Date：2020/10/17
 * Description： 传给H5的位置信息
 */
public class LocationInfoEntity implements Parcelable {
    private String longitude; //经度
    private String latitude; //维度
    private String ip; //IP
    private String city; //城市
    private String detail;

    public LocationInfoEntity() {
    }

    protected LocationInfoEntity(Parcel in) {
        longitude = in.readString();
        latitude = in.readString();
        ip = in.readString();
        city = in.readString();
        detail = in.readString();
    }

    public static final Creator<LocationInfoEntity> CREATOR = new Creator<LocationInfoEntity>() {
        @Override
        public LocationInfoEntity createFromParcel(Parcel in) {
            return new LocationInfoEntity(in);
        }

        @Override
        public LocationInfoEntity[] newArray(int size) {
            return new LocationInfoEntity[size];
        }
    };

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "LocationInfoEntity{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", ip='" + ip + '\'' +
                ", city='" + city + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(ip);
        dest.writeString(city);
        dest.writeString(detail);
    }
}
