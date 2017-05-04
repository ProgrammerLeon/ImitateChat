package com.monch.network;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pools;

import com.monch.network.executor.IExecutor;
import com.monch.network.executor.OkHttpExecutor;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by monch on 2017/5/4.
 */

public final class ApiRequest {

    // 请求实例池
    private static final Pools.Pool<ApiRequest> POOL = new Pools.SynchronizedPool<>(30);

    // 生成ApiRequest实例
    public static ApiRequest obtain(Builder builder) {
        ApiRequest instance = POOL.acquire();
        if (instance == null) {
            instance = new ApiRequest(builder);
        }
        return instance;
    }

    // 释放ApiRequest实例
    public static void release(ApiRequest request) {
        if (request == null) return;
        IExecutor executor = request.executor;
        if (executor != null) {
            // 释放请求缓存
            executor.releaseCache(request);
        }
        request.url = null;
        // 在这里，我们将所有的ArrayMap都置为null，
        // 是因为之前的请求有可能已经将ArrayMap的空间增加的足够大
        // 为了避免浪费多余分配的空间，之后的每次使用我们都重新创建
        if (request.parameters != null) {
            request.parameters.clear();
            request.parameters = null;
        }
        if (request.headers != null) {
            request.headers.clear();
            request.headers = null;
        }
        if (request.files != null) {
            request.files.clear();
            request.files = null;
        }
        request.callback = null;
        request.charset = null;
        request.tag = null;
        POOL.release(request);
    }

    // 默认执行器
    private static IExecutor mDefaultExecutor = new OkHttpExecutor();
    // 默认编码方式
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /* 请求类型 */
    public static final int GET = 0;
    public static final int POST = GET + 1;
    public static final int UPLOAD = POST + 1;
    public static final int DOWNLOAD = UPLOAD + 1;

    private String url;     // 请求URL
    private int type;       // 请求类型
    private ArrayMap<String, String> parameters;    // 参数集合
    private ArrayMap<String, String> headers;       // 请求头集合
    private ArrayMap<String, File> files;           // 上传文件集合
    private ApiCallback callback;                   // 请求回调
    private Charset charset;                        // 编码
    private Object tag;                             // 标记
    private IExecutor executor;                     // 执行器

    private ApiRequest(Builder builder) {
        this.url = builder.url;
        this.type = builder.type;
        this.parameters = builder.parameters;
        this.headers = builder.headers;
        this.files = builder.files;
        this.callback = builder.callback;
        this.charset = builder.charset != null ? builder.charset : DEFAULT_CHARSET;
        this.tag = builder.tag;
        this.executor = builder.executor != null ? builder.executor : mDefaultExecutor;
    }

    // 开始请求
    private void request() {
        switch (type) {
            case POST:
                executor.doPost(this);
                break;
            case UPLOAD:
                executor.doUpload(this);
                break;
            case DOWNLOAD:
                executor.doDownload(this);
                break;
            default:
                executor.doGet(this);
                break;
        }
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }

    public ArrayMap<String, String> getParameters() {
        return parameters;
    }

    public ArrayMap<String, String> getHeaders() {
        return headers;
    }

    public ArrayMap<String, File> getFiles() {
        return files;
    }

    public ApiCallback getCallback() {
        return callback;
    }

    public Charset getCharset() {
        return charset;
    }

    public Object getTag() {
        return tag;
    }

    public IExecutor getExecutor() {
        return executor;
    }

    public void cancel() {
        if (executor != null) {
            executor.cancel(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String url;
        private int type = GET;
        private ArrayMap<String, String> parameters;
        private ArrayMap<String, String> headers;
        private ArrayMap<String, File> files;
        private ApiCallback callback;
        private Charset charset;
        private Object tag;
        private IExecutor executor;
        private Builder(){}
        public Builder url(String url) {
            this.url = url;
            return this;
        }
        public Builder addParameter(String key, String value) {
            if (parameters == null) {
                parameters = ArrayFactory.createArrayMap();
            }
            parameters.put(key, value);
            return this;
        }
        public Builder addHeader(String key, String value) {
            if (headers == null) {
                headers = ArrayFactory.createArrayMap(1);
            }
            headers.put(key, value);
            return this;
        }
        public Builder addFile(String key, File file) {
            if (files == null) {
                files = ArrayFactory.createArrayMap(1);
            }
            files.put(key, file);
            return this;
        }
        public Builder callback(ApiCallback callback) {
            this.callback = callback;
            return this;
        }
        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }
        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }
        public Builder executor(IExecutor executor) {
            this.executor = executor;
            return this;
        }
        public ApiRequest get() {
            this.type = GET;
            ApiRequest request = obtain(this);
            request.request();
            return request;
        }
        public ApiRequest post() {
            this.type = POST;
            ApiRequest request = obtain(this);
            request.request();
            return request;
        }
        public ApiRequest upload() {
            this.type = UPLOAD;
            ApiRequest request = obtain(this);
            request.request();
            return request;
        }
        public ApiRequest download() {
            this.type = DOWNLOAD;
            ApiRequest request = obtain(this);
            request.request();
            return request;
        }
    }

}
