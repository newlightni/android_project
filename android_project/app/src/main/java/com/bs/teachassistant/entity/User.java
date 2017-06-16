package com.bs.teachassistant.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by limh on 2017/4/25.
 * 用户信息
 */

public class User implements Parcelable {
    private String userName;
    private String password;
    private String phone;
    private String sex;

    public User(String userName, String password, String phone, String sex) {
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.sex = sex;
    }


    protected User(Parcel in) {
        userName = in.readString();
        password = in.readString();
        phone = in.readString();
        sex = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(password);
        parcel.writeString(phone);
        parcel.writeString(sex);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getSex() {
        return sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
