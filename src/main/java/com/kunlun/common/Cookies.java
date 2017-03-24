package com.kunlun.common;

import org.n3r.diamond.client.Miner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by kunlun on 2017/3/24.
 */
@Component
public class Cookies implements Serializable {
    private static final long serialVersionUID = 4164557424856074390L;

    //allowed for javascript or not  防止跨站攻击 XSS
    public static enum HttpOnly {
        ON, OFF
    }

    //allowed for https or not    防止https  攻击
    public static enum Secure4Https {
        ON, OFF
    }

    Logger logger = LoggerFactory.getLogger(Cookies.class);

    public String getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return "";
        }
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param name         cookie名称
     * @param value        cookie值
     * @param maxSeconds   cookie最大有效时间
     * @param httpOnly     是否开启防止跨站攻击  XSS
     * @param secure4Https
     * @return
     */
    public Cookie addCookie(HttpServletResponse response, String name, String value, int maxSeconds, HttpOnly httpOnly, Secure4Https secure4Https) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxSeconds);
        cookie.setHttpOnly(httpOnly == HttpOnly.ON);
        cookie.setSecure(secure4Https == Secure4Https.ON);
        String domain = new Miner().getMiner("hcon.base", "domain").getString("root");
        if (isNotEmpty(domain))
            cookie.setDomain(domain);
        response.addCookie(cookie);
        logger.info("add cookie - success");
        return cookie;
    }

    public Cookie addCookie(HttpServletResponse response, String name, String value) {
        return addCookie(response, name, value, -1, HttpOnly.OFF, Secure4Https.OFF);
    }

    public Cookie addCookie(HttpServletResponse response, String name, String value, int expire, TimeUnit timeUnit) {
        return addCookie(response, name, value, (int) timeUnit.toSeconds(expire), HttpOnly.OFF, Secure4Https.OFF);
    }

    public void delCookie(HttpServletResponse response, String name) {

    }

}
