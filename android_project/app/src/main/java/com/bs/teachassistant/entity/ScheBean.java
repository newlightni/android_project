package com.bs.teachassistant.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by limh on 2017/4/25.
 */

public class ScheBean implements Parcelable {
    private String title;
    private String local;
    private String startTime;
    private String endTime;
    private String remark;
    private boolean isFinish;

    public ScheBean(String title, String local, String startTime, String endTime, String remark, boolean isFinish) {
        this.title = title;
        this.local = local;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remark = remark;
        this.isFinish = isFinish;
    }

    protected ScheBean(Parcel in) {
        title = in.readString();
        local = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        remark = in.readString();
        isFinish = in.readInt() == 1;
    }

    public static final Creator<ScheBean> CREATOR = new Creator<ScheBean>() {
        @Override
        public ScheBean createFromParcel(Parcel in) {
            return new ScheBean(in);
        }

        @Override
        public ScheBean[] newArray(int size) {
            return new ScheBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(local);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(remark);
        parcel.writeInt(isFinish ? 1 : 0);
    }

    @Override
    public String toString() {
        return "ScheBean{" +
                "title='" + title + '\'' +
                ", local='" + local + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", remark='" + remark + '\'' +
                ", isFinish=" + isFinish +
                '}';
    }
}
