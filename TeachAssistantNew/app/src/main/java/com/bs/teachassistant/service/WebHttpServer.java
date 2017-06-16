package com.bs.teachassistant.service;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/4.
 * App服务器
 */

public class WebHttpServer {

    private final ExecutorService threadPool;
    private final WebConfig config;

    private boolean isEnable = false;
    private ServerSocket socket;

    private Set<IResourceUriHander> resourceHanders;

    private final static String TAG = "WebHttpServer";

    public WebHttpServer(WebConfig config) {
        this.config = config;
        threadPool = Executors.newCachedThreadPool();
        resourceHanders = new HashSet<>();
    }

    /**
     * 启动服务器
     */
    public void startServer() {
        isEnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcSync();
            }
        }).start();
    }


    /**
     * 停止服务器
     */
    public void stopServer() {
        if (!isEnable)
            return;
        isEnable = false;
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doProcSync() {
        try {
            InetSocketAddress socketAddr = new InetSocketAddress(config.getPort());
            socket = new ServerSocket();
            socket.bind(socketAddr);
            while (isEnable) {
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.w(TAG, "建立一个链接:" + remotePeer.getRemoteSocketAddress().toString());
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
            Log.w(TAG, "socket异常:" + e.toString());
        }
    }

    public void registerResourceHander(IResourceUriHander hander) {
        resourceHanders.add(hander);
    }

    /**
     * @param remotePeer
     */
    private void onAcceptRemotePeer(Socket remotePeer) {
        try {
            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(remotePeer);
            InputStream nis = remotePeer.getInputStream();
            String headerline;
            String resourceUri = StreamToolkit.readLine(nis).split(" ")[1];
            Log.d(TAG,"resourceUri="+resourceUri);
            while ((headerline = StreamToolkit.readLine(nis)) != null) {
                if (headerline.equals("\r\n")) {
                    break;
                }
                String[] pair = headerline.split(":");
                httpContext.addRequestHeader(pair[0], pair[1]);
                Log.w(TAG, "headerLine=" + headerline);
            }

            for (IResourceUriHander item : resourceHanders) {
                if (!item.accept(resourceUri)) {
                    continue;
                }
                item.hander(resourceUri, httpContext);
            }
        } catch (IOException e) {
            Log.w(TAG, "输出异常:" + e.toString());
        }finally {
            try {
                remotePeer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
