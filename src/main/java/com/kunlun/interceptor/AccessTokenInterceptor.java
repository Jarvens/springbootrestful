package com.kunlun.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Created by kunlun on 2017/3/24.
 */
public class AccessTokenInterceptor extends HandlerInterceptorAdapter implements Serializable {

    private static final long serialVersionUID = -61573114790200412L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }
}
