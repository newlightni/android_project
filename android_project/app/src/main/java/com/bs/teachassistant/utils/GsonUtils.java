package com.bs.teachassistant.utils;

import com.bs.teachassistant.entity.Less;
import com.bs.teachassistant.entity.Note;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.entity.Score;
import com.bs.teachassistant.entity.StudBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtils() {
    }

    /**
     * Bean转String
     *
     * @param object Bean
     * @return String Result
     */
    public static String GsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * String转Bean
     *
     * @param gsonString 要转换的jsonString
     * @param cls        Bean
     * @return 转换结果
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * Bean转String
     *
     * @param allObject Bean
     * @return String Result
     */
    public static String GsonString(List<Object> allObject) {
        JSONArray array = new JSONArray();
        for (Object item : allObject) {
            array.put(GsonString(item));
        }
        return array.toString();
    }

    public static List<ScheBean> ScheGsonToBean(String str) {
        List<ScheBean> datas = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(str);
            for (int i = 0; i < array.length(); i++) {
                datas.add(GsonToBean(array.getString(i), ScheBean.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static List<StudBean> StudGsonToBean(String str) {
        List<StudBean> datas = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(str);
            for (int i = 0; i < array.length(); i++) {
                datas.add(GsonToBean(array.getString(i), StudBean.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static List<Less> LessGsonToBean(String str) {
        List<Less> datas = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(str);
            for (int i = 0; i < array.length(); i++) {
                datas.add(GsonToBean(array.getString(i), Less.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static List<Note> NoteGsonToBean(String str) {
        List<Note> datas = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(str);
            for (int i = 0; i < array.length(); i++) {
                datas.add(GsonToBean(array.getString(i), Note.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }
    public static List<Score> ScoreGsonToBean(String str) {
        List<Score> datas = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(str);
            for (int i = 0; i < array.length(); i++) {
                datas.add(GsonToBean(array.getString(i), Score.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

}
