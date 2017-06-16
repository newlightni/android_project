package com.bs.teachassistant.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/6/5.
 *
 */
@Entity
public class Score implements Parcelable  {
    @Id
    private Long id;
    private String name; //姓名
    private String stuId;//学号
    private String term;//学期
    private String className;//课程名称
    private Integer score;//分数
    private String groupName;//所属班级

    protected Score(Parcel in) {
        id=in.readLong();
        name = in.readString();
        stuId = in.readString();
        term = in.readString();
        className = in.readString();
        score=in.readInt();
        groupName = in.readString();
    }

    @Generated(hash = 56872988)
    public Score(Long id, String name, String stuId, String term, String className,
            Integer score, String groupName) {
        this.id = id;
        this.name = name;
        this.stuId = stuId;
        this.term = term;
        this.className = className;
        this.score = score;
        this.groupName = groupName;
    }

    @Generated(hash = 226049941)
    public Score() {
    }

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(stuId);
        dest.writeString(term);
        dest.writeString(className);
        dest.writeInt(score);
        dest.writeString(groupName);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStuId() {
        return this.stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getScore() {
        return this.score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stuId='" + stuId + '\'' +
                ", term='" + term + '\'' +
                ", className='" + className + '\'' +
                ", score=" + score +
                '}';
    }
}
