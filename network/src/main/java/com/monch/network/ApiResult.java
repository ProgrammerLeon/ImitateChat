package com.monch.network;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pools;

/**
 * Created by monch on 2017/5/4.
 */

public final class ApiResult {

    // 缓存池
    private static final Pools.Pool<ApiResult> POOL = new Pools.SynchronizedPool<>(30);

    public static ApiResult obtain() {
        ApiResult instance = POOL.acquire();
        if (instance == null) {
            instance = new ApiResult();
        }
        return instance;
    }

    public static void release(ApiResult apiResult) {
        if (apiResult == null) return;
        apiResult.code = 0;
        apiResult.errorMessage = null;
        if (apiResult.parameters != null) {
            apiResult.parameters.clear();
            apiResult.parameters = null;
        }
        POOL.release(apiResult);
    }

    // 错误码
    private int code;
    // 错误消息
    private String errorMessage;
    // 结果参数
    private ArrayMap<String, Object> parameters;

    private ApiResult() {
        parameters = ArrayFactory.createArrayMap();
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String error) {
        this.errorMessage = error;
    }

    public final <T> void put(String key, T value) {
        parameters.put(key, value);
    }

    public <T> T get(String key) {
        if (parameters.containsKey(key)) {
            Object o = parameters.get(key);
            try {
                return (T) o;
            } catch (Throwable t) {
                return null;
            }
        }
        return null;
    }

    public Integer getInteger(String key) {
        if (parameters.containsKey(key)) {
            Object o = parameters.get(key);
            if (o != null && o instanceof Integer) {
                return (Integer) o;
            }
        }
        return 0;
    }

    public Long getLong(String key) {
        if (parameters.containsKey(key)) {
            Object o = parameters.get(key);
            if (o != null && o instanceof Long) {
                return (Long) o;
            }
        }
        return 0L;
    }

    public Float getFloat(String key) {
        if (parameters.containsKey(key)) {
            Object o = parameters.get(key);
            if (o != null && o instanceof Float) {
                return (Float) o;
            }
        }
        return 0F;
    }

    public Double getDouble(String key) {
        if (parameters.containsKey(key)) {
            Object o = parameters.get(key);
            if (o != null && o instanceof Double) {
                return (Double) o;
            }
        }
        return 0D;
    }

    public Boolean getBoolean(String key) {
        if (parameters.containsKey(key)) {
            Object o = parameters.get(key);
            if (o != null && o instanceof Boolean) {
                return (Boolean) o;
            }
        }
        return false;
    }

    public String getString(String key) {
        if (parameters.containsKey(key)) {
            Object o = parameters.get(key);
            if (o != null && o instanceof String) {
                return (String) o;
            }
        }
        return null;
    }

}
