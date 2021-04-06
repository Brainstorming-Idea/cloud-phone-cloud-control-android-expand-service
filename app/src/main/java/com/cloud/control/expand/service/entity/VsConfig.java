package com.cloud.control.expand.service.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wangyou
 * @desc: 虚拟场景用户配置
 * @date :2021/3/18
 */
public class VsConfig implements Parcelable {
    private double[] centerCoords;
    private int sceneType;
    private double[] terminalLoc;
    private int radius;
    private String city;//省+市 防止出现城市名称相同情况
    private String address;//详细地址
    private boolean isStart;//虚拟场景是否启动

    public VsConfig() {
    }

    protected VsConfig(Parcel in) {
        centerCoords = in.createDoubleArray();
        sceneType = in.readInt();
        terminalLoc = in.createDoubleArray();
        radius = in.readInt();
        city = in.readString();
        address = in.readString();
        isStart = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(centerCoords);
        dest.writeInt(sceneType);
        dest.writeDoubleArray(terminalLoc);
        dest.writeInt(radius);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeByte((byte) (isStart ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VsConfig> CREATOR = new Creator<VsConfig>() {
        @Override
        public VsConfig createFromParcel(Parcel in) {
            return new VsConfig(in);
        }

        @Override
        public VsConfig[] newArray(int size) {
            return new VsConfig[size];
        }
    };

    public double[] getCenterCoords() {
        return centerCoords;
    }

    public void setCenterCoords(double[] centerCoords) {
        this.centerCoords = centerCoords;
    }

    public int getSceneType() {
        return sceneType;
    }

    public void setSceneType(int sceneType) {
        this.sceneType = sceneType;
    }

    public double[] getTerminalLoc() {
        return terminalLoc;
    }

    public void setTerminalLoc(double[] terminalLoc) {
        this.terminalLoc = terminalLoc;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }
}