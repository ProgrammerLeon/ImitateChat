package com.monch.network.executor;

import android.text.TextUtils;

import com.monch.network.ApiCallback;
import com.monch.network.ApiRequest;
import com.monch.network.ArrayFactory;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by monch on 2017/5/4.
 */

public class OkHttpExecutor implements IExecutor {

    private OkHttpClient mOkHttpClient;
    private Map<ApiRequest, Call> mRequestCache = ArrayFactory.createConcurrentHashMap();

    public OkHttpExecutor() {
        mOkHttpClient = OkHttpClientFactory.getClient();
    }

    @Override
    public void doGet(ApiRequest request) {
        OkHttpClient client = mOkHttpClient;
        Charset charset = request.getCharset();
        String url = RequestUtils.makeUrl(request.getUrl(), request.getParameters(), charset);
        ApiCallback apiCallback = request.getCallback();
        if (apiCallback != null) {
            apiCallback.onStart(request);
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.tag(request.getTag());
        builder.get();
        RequestUtils.makeHeader(builder, request.getHeaders(), charset);
        Call call = client.newCall(builder.build());
        call.enqueue(new OkHttpCallback(request));
        mRequestCache.put(request, call);
    }

    @Override
    public void doPost(ApiRequest request) {
        OkHttpClient client = mOkHttpClient;
        Charset charset = request.getCharset();
        ApiCallback callback = request.getCallback();
        if (callback != null) {
            callback.onStart(request);
        }
        Request.Builder builder = new Request.Builder();
        builder.url(request.getUrl());
        builder.tag(request.getTag());
        builder.post(formBody(request.getParameters(), charset));
        RequestUtils.makeHeader(builder, request.getHeaders(), charset);
        Call call = client.newCall(builder.build());
        call.enqueue(new OkHttpCallback(request));
        mRequestCache.put(request, call);
    }

    private RequestBody formBody(Map<String, String> params, Charset charset) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = RequestUtils.encode(entry.getKey(), charset);
                String value = RequestUtils.encode(entry.getValue(), charset);
                if (!TextUtils.isEmpty(key) && value != null) {
                    builder.addEncoded(key, value);
                }
            }
        }
        return builder.build();
    }

    @Override
    public void doUpload(ApiRequest request) {
        OkHttpClient client = mOkHttpClient;
        Charset charset = request.getCharset();
        ApiCallback callback = request.getCallback();
        if (callback != null) {
            callback.onStart(request);
        }
        Request.Builder builder = new Request.Builder();
        builder.url(request.getUrl());
        builder.tag(request.getTag());
        builder.post(formBody(request.getParameters(), request.getFiles(), charset));
        RequestUtils.makeHeader(builder, request.getHeaders(), charset);
        Call call = client.newCall(builder.build());
        call.enqueue(new OkHttpCallback(request));
        mRequestCache.put(request, call);
    }

    private static final String DEFAULT_CONTENT = "Content-Disposition";

    private RequestBody formBody(Map<String, String> params, Map<String, File> files, Charset charset) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = RequestUtils.encode(entry.getKey(), charset);
                byte[] content = entry.getValue().getBytes(charset);
                builder.addPart(Headers.of(DEFAULT_CONTENT, getParamValue(key)),
                        RequestBody.create(MediaType.parse(charset.name()), content));
            }
        }
        if (files != null && !files.isEmpty()) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                String name = RequestUtils.encode(entry.getKey(), charset);
                File file = entry.getValue();
                String fileName = file.getName();
                builder.addPart(Headers.of(DEFAULT_CONTENT, getFileValue(name, fileName)),
                        RequestBody.create(MediaType.parse(guessMimeType(fileName)), file));
            }
        }
        return builder.build();
    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private static String getParamValue(String name) {
        return "form-data; name=\"" + name + "\"";
    }

    private static String getFileValue(String name, String fileName) {
        return "form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"";
    }

    @Override
    public void doDownload(ApiRequest request) {
        doGet(request);
    }

    @Override
    public void releaseCache(ApiRequest request) {
        if (request == null) return;
        if (mRequestCache.containsKey(request)) {
            mRequestCache.remove(request);
        }
    }

    @Override
    public void cancel(ApiRequest request) {
        if (request == null) return;
        if (mRequestCache.containsKey(request)) {
            Call call = mRequestCache.get(request);
            if (call != null && !call.isCanceled()) {
                call.cancel();
            }
            mRequestCache.remove(request);
        }
    }

    @Override
    public void cancelAll() {
        if (!mRequestCache.isEmpty()) {
            for (Call call : mRequestCache.values()) {
                if (call != null && !call.isCanceled()) {
                    call.cancel();
                }
            }
            mRequestCache.clear();
        }
    }

}
