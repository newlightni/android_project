package com.bs.teachassistant.service;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/4.
 * Http内容
 */

public class HttpContext {
    private Socket underlySocket;

    private final HashMap<String, String> requsetHeader;

    public HttpContext() {
        requsetHeader = new HashMap<>();
    }

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public void setUnderlySocket(Socket underlySocket) {
        this.underlySocket = underlySocket;
    }

    public void addRequestHeader(String headerName, String headerValue) {
        requsetHeader.put(headerName, headerValue);
    }

    public String getRequestHeaderValue(String headerName) {
        return requsetHeader.get(headerName);
    }
}
