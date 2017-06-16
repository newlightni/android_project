package com.bs.teachassistant.service;

/**
 * Created by Administrator on 2016/12/4.
 * 服务器配置类
 */

public class WebConfig {
    /**
     * 端口
     */
    private int port;

    /**
     * 最大监听数
     */
    private int maxParallels;

    public int getMaxParallels() {
        return maxParallels;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
