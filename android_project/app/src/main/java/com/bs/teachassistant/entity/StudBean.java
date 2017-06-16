package com.bs.teachassistant.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by limh on 2017/4/26.
 * 学生签到实体类
 */

public class StudBean implements Parcelable {
    private String name;
    private String stuId;
    private String status;
    private String signTime;
    private String signIp;
    private String term;
    private String group;
    private String course;

    public StudBean(String name, String stuId, String status, String signTime, String signIp,
                    String term, String group, String course) {
        this.name = name;
        this.stuId = stuId;
        this.status = status;
        this.signTime=signTime;
        this.signIp = signIp;
        this.term = term;
        this.group = group;
        this.course = course;
    }

    protected StudBean(Parcel in) {
        name = in.readString();
        stuId = in.readString();
        status = in.readString();
        signIp = in.readString();
        signTime = in.readString();
        course = in.readString();
        group = in.readString();
        term = in.readString();
    }

    public static final Creator<StudBean> CREATOR = new Creator<StudBean>() {
        @Override
        public StudBean createFromParcel(Parcel in) {
            return new StudBean(in);
        }

        @Override
        public StudBean[] newArray(int size) {
            return new StudBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignIp() {
        return signIp;
    }

    public void setSignIp(String signIp) {
        this.signIp = signIp;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(stuId);
        parcel.writeString(status);
        parcel.writeString(signIp);
        parcel.writeString(signTime);
        parcel.writeString(term);
        parcel.writeString(course);
        parcel.writeString(group);
    }

    @Override
    public String toString() {
        return "StudBean{" +
                "name='" + name + '\'' +
                ", stuId='" + stuId + '\'' +
                ", status='" + status + '\'' +
                ", signIp='" + signIp + '\'' +
                '}';
    }
}
