package com.monch.network;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pools;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by monch on 2017/5/4.
 */

public final class ApiResponse {

    private static final Pools.Pool<ApiResponse> POOL = new Pools.SynchronizedPool<>(30);

    public static ApiResponse obtain() {
        ApiResponse instance = POOL.acquire();
        if (instance == null) {
            instance = new ApiResponse();
        }
        return instance;
    }

    public static void release(ApiResponse response) {
        if (response == null) return;
        response.code = 0;
        if (response.headers != null) {
            response.headers.clear();
            response.headers = null;
        }
        response.body = null;
        response.tag = null;
        POOL.release(response);
    }

    // 请求码
    private int code;
    // 响应头数据
    private ArrayMap<String, String> headers;
    // 响应数据体
    private byte[] body;
    // 标记
    private Object tag;
    // 编码
    private Charset charset;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = ArrayFactory.createArrayMap();
        }
        headers.put(key, value);
    }

    public String getHeader(String key) {
        if (headers != null && headers.containsKey(key)) {
            return headers.get(key);
        }
        return null;
    }

    public byte[] getBody() {
        return body;
    }

    public String getString() {
        try {
            return new String(getBody(), getCharset().name());
        } catch (UnsupportedEncodingException e) {
            return new String(getBody());
        }
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset == null ? Charset.forName("UTF-8") : charset;
    }

}
