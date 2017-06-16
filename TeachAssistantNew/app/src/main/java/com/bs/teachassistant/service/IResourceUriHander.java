package com.bs.teachassistant.service;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/4.
 *
 */

public interface IResourceUriHander {
    boolean accept(String uri);
    void hander(String uri, HttpContext httpContext) throws IOException;
}
