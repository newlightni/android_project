package com.bs.teachassistant.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.StudBean;
import com.bs.teachassistant.utils.FormatUtils;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */

public class ResourceAssetsHander implements IResourceUriHander {
    private Context mContext;

    private String TAG = "ResourceAssetsHander";
    private List<StudBean> studDatas;
    private List<StudBean> students;

    private SharedPreferences userPreference;

    public ResourceAssetsHander(Context context) {
        this.mContext = context;
        studDatas = new ArrayList<>();
        students = new ArrayList<>();
        userPreference = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith("/");
    }

    @Override
    public void hander(String uri, HttpContext httpContext) throws IOException {
        studDatas.clear();
        if (!TextUtils.isEmpty(userPreference.getString("allStud", ""))) {
            studDatas = GsonUtils.StudGsonToBean(userPreference.getString("allStud", ""));
        }
        if (!TextUtils.isEmpty(userPreference.getString("students", ""))) {
            students.clear();
            students = GsonUtils.StudGsonToBean(userPreference.getString("students", ""));
            for (StudBean item : students) {
                LogUtil.d(TAG, "所有学生=" + item.toString());
            }
        }

        String url = URLDecoder.decode(uri, "utf-8");
        Log.w(TAG, "url=" + url);

        int startIndex = url.length();
        String assetsPath = "";
        String classId, status, ip, term, group, course;

        ip = httpContext.getUnderlySocket().getRemoteSocketAddress().toString();
        Log.w(TAG, "ip=" + ip);

        if (startIndex == 1) {
            assetsPath = "html/index.html";
        }
        if (url.startsWith("/sign.do?")) {
            classId = url.substring(9).split("&")[0].split("=")[1];
            term = url.substring(9).split("&")[1].split("=")[1];
            group = url.substring(9).split("&")[2].split("=")[1];
            course = url.substring(9).split("&")[3].split("=")[1];
            status = url.substring(9).split("&")[4].split("=")[1];

            Log.w(TAG, "学号：" + classId + " " + status);
            StudBean stud = new StudBean("", classId, status, FormatUtils.getTime(), ip.split(":")[0],
                    term, group, course);
            stud.setGroup(group);
            stud.setCourse(course);
            stud.setTerm(term);
            boolean isSign = false;
            if (studDatas.size() > 0) {
                for (StudBean item : studDatas) {
                    if (item.getStuId().equals(stud.getStuId())) {
                        isSign = true;
                    }
                }
            }
            if (isSign) {
                assetsPath = "html/error.html";
            } else {
                boolean isExit = false;
                for (StudBean item : students) {
                    for (StudBean item1 : studDatas) {
                        if (item.getStuId().equals(item1.getStuId())) {
                            item1.setName(item.getName());
                        }
                    }
                    if (item.getStuId().equals(stud.getStuId())) {
                        stud.setName(item.getName());
                        isExit = true;
                    }
                }
                //判断学生数据库有没有该签到学生的学号  有则添加签到状态  否则反馈签到为：没有该学生
                if (isExit) {
                    studDatas.add(stud);
                    userPreference.edit().putString("allStud", GsonUtils.GsonString(studDatas)).apply();
                    assetsPath = "html/success.html";
                    Intent intent = new Intent();
                    intent.setAction(Comm.ACTION_UPDATE_ADD_SIGN);
                    intent.putExtra("stud", stud);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                } else {
                    assetsPath = "html/error1.html";
                }

            }
        }

        Log.w(TAG, "assetsPath=" + assetsPath);

        InputStream fis = mContext.getAssets().open(assetsPath);
        byte[] raw = StreamToolkit.readRawFromStream(fis);
        fis.close();

        OutputStream ous = httpContext.getUnderlySocket().getOutputStream();
        PrintStream writer = new PrintStream(ous);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Length:" + raw.length);
        if (assetsPath.endsWith(".html")) {
            writer.println("Content-Type:text/html");
        } else if (assetsPath.endsWith(".js")) {
            writer.println("Content-Type:text/js");
        } else if (assetsPath.endsWith(".css")) {
            writer.println("Content-Type:text/css");
        } else if (assetsPath.endsWith(".jpg")) {
            writer.println("Content-Type:text/jpg");
        } else if (assetsPath.endsWith(".png")) {
            writer.println("Content-Type:text/png");
        }
        writer.println();
        writer.write(raw);
    }
}
