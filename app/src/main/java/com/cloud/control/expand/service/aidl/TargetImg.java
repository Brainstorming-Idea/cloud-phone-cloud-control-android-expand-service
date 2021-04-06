package com.cloud.control.expand.service.aidl;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wangyou
 * @desc:
 * @date :2021/2/4
 */
public class TargetImg implements Parcelable {
    private Bitmap target;

    protected TargetImg(Parcel in) {
        target = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<TargetImg> CREATOR = new Creator<TargetImg>() {
        @Override
        public TargetImg createFromParcel(Parcel in) {
            return new TargetImg(in);
        }

        @Override
        public TargetImg[] newArray(int size) {
            return new TargetImg[size];
        }
    };

    public Bitmap getTarget() {
        return target;
    }

    public void setTarget(Bitmap target) {
        this.target = target;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(target, flags);
    }
}