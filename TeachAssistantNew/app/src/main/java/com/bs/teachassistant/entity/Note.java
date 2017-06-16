package com.bs.teachassistant.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by limh on 2017/5/3.
 * 日志实体类
 */
@Entity
public class Note implements Parcelable {
    @Id
    private Long id;
    private String term;//学期
    private String calssName; //课程名称
    private String local;    //授课地点
    private String calssify;//分类
    private String title;   //标题
    private String remark;  //备注
    private String Content; //内容
    private String time;    //时间
    private String pert; //节数
    private Long courseId; //课程Id

    public Note(String calssify, String title, String remark, String content, String time) {
        this.calssify = calssify;
        this.title = title;
        this.remark = remark;
        Content = content;
        this.time = time;
    }

    protected Note(Parcel in) {
        id = in.readLong();
        term=in.readString();
        calssName = in.readString();
        local = in.readString();
        calssify = in.readString();
        title = in.readString();
        remark = in.readString();
        Content = in.readString();
        time = in.readString();
        pert = in.readString();
        courseId = in.readLong();
    }

    public Note(Long id, String calssName, String local, String calssify, String title,
                String remark, String Content, String time) {
        this.id = id;
        this.calssName = calssName;
        this.local = local;
        this.calssify = calssify;
        this.title = title;
        this.remark = remark;
        this.Content = Content;
        this.time = time;
    }
    
    public Note() {
    }

    @Generated(hash = 1907590364)
    public Note(Long id, String term, String calssName, String local, String calssify,
            String title, String remark, String Content, String time, String pert,
            Long courseId) {
        this.id = id;
        this.term = term;
        this.calssName = calssName;
        this.local = local;
        this.calssify = calssify;
        this.title = title;
        this.remark = remark;
        this.Content = Content;
        this.time = time;
        this.pert = pert;
        this.courseId = courseId;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(term);
        dest.writeString(calssName);
        dest.writeString(local);
        dest.writeString(calssify);
        dest.writeString(title);
        dest.writeString(remark);
        dest.writeString(Content);
        dest.writeString(time);
        dest.writeString(pert);
        dest.writeLong(courseId);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCalssName() {
        return this.calssName;
    }

    public void setCalssName(String calssName) {
        this.calssName = calssName;
    }

    public String getLocal() {
        return this.local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCalssify() {
        return this.calssify;
    }

    public void setCalssify(String calssify) {
        this.calssify = calssify;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return this.Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPert() {
        return pert;
    }

    public void setPert(String pert) {
        this.pert = pert;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", term='" + term + '\'' +
                ", calssName='" + calssName + '\'' +
                ", local='" + local + '\'' +
                ", calssify='" + calssify + '\'' +
                ", title='" + title + '\'' +
                ", remark='" + remark + '\'' +
                ", Content='" + Content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
