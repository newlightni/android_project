package com.bs.teachassistant.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by zt on 2017/6/6.
 */

@Entity
public class Course implements Parcelable {

    @Id
    private Long id;

    //课程名
    private String name;

    //学年
    private String year;

    //学期
    private String term;

    //教学地点
    private String address;

    //描述
    private String description;

    public Course(String name, String year, String term,
                  String address, String description) {
        this.name = name;
        this.year = year;
        this.term = term;
        this.address = address;
        this.description = description;
    }

    protected Course(Parcel in) {
        id = in.readLong();
        name = in.readString();
        year = in.readString();
        term = in.readString();
        address = in.readString();
        description = in.readString();
    }

    @Generated(hash = 806885865)
    public Course(Long id, String name, String year, String term, String address,
            String description) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.term = term;
        this.address = address;
        this.description = description;
    }

    @Generated(hash = 1355838961)
    public Course() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(year);
        dest.writeString(term);
        dest.writeString(address);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
