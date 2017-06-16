package com.bs.teachassistant.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/12/4.
 * 网络请求操作类
 */

public class StreamToolkit {
    public static final String readLine(InputStream nis) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c1 = 0;
        int c2 = 0;
        while (c2 != -1 && !(c1 == '\r' && c2 == '\n')) {
            c1 = c2;
            c2 = nis.read();
            sb.append((char) c2);
        }
        if (sb.length() == 0) {
            return null;
        } else {
            return sb.toString();
        }
    }

    public static byte[] readRawFromStream(InputStream fis) throws IOException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int read;
        while((read=fis.read(buffer))>0){
            bos.write(buffer,0,read);
        }
        return bos.toByteArray();
    }
}
