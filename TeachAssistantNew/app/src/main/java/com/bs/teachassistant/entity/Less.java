package com.bs.teachassistant.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Spinner;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by limh on 2017/4/27.
 * 备课
 */
@Entity
public class Less implements Parcelable {
    @Id
    private Long id;
    private String name;//课程名称
    private String chapter;//章节
    private String time;//授课时间
    private String year;//学年
    private String term;//第几学期
    private String local;//上课地点
    private String content;//备课内容
    private Long courseId;//关联的课程Id
    private String pert;//节数

    public Less(String name, String time, String chapter, String content) {
        this.name = name;
        this.time = time;
        this.chapter = chapter;
        this.content = content;
    }

    protected Less(Parcel in) {
        id = in.readLong();
        name = in.readString();
        chapter = in.readString();
        time = in.readString();
        term = in.readString();
        local = in.readString();
        content = in.readString();
        courseId = in.readLong();
        year = in.readString();
        pert = in.readString();
    }

    @Generated(hash = 1455652383)
    public Less(Long id, String name, String chapter, String time, String year,
            String term, String local, String content, Long courseId, String pert) {
        this.id = id;
        this.name = name;
        this.chapter = chapter;
        this.time = time;
        this.year = year;
        this.term = term;
        this.local = local;
        this.content = content;
        this.courseId = courseId;
        this.pert = pert;
    }

    @Generated(hash = 442254384)
    public Less() {
    }

    public static final Creator<Less> CREATOR = new Creator<Less>() {
        @Override
        public Less createFromParcel(Parcel in) {
            return new Less(in);
        }

        @Override
        public Less[] newArray(int size) {
            return new Less[size];
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
        dest.writeString(chapter);
        dest.writeString(time);
        dest.writeString(term);
        dest.writeString(local);
        dest.writeString(content);
        dest.writeLong(courseId);
        dest.writeString(year);
        dest.writeString(pert);
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

    public String getChapter() {
        return this.chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLocal() {
        return this.local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPert() {
        return pert;
    }

    public void setPert(String pert) {
        this.pert = pert;
    }
}
