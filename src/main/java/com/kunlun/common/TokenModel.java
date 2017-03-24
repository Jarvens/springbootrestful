package com.kunlun.common;

import java.io.Serializable;

/**
 * token 对象模型
 * Created by kunlun on 2017/3/24.
 */
public class TokenModel implements Serializable {

    private static final long serialVersionUID = 241009729792865326L;

    //用户ID
    private Long id;

    //用户唯一token
    private String token;

    public TokenModel(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public TokenModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
