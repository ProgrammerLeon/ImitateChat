package com.monch.network.executor;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.monch.network.AccountException;
import com.monch.network.ApiCallback;
import com.monch.network.ApiRequest;
import com.monch.network.ApiResponse;
import com.monch.network.ApiResult;
import com.monch.network.Failed;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by monch on 2017/5/4.
 */

public class OkHttpCallback implements Callback {

    /** 主线程执行器 **/
    private static Handler mResponseHandler = new Handler(Looper.getMainLooper());
    private static Executor mResponsePoster = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            mResponseHandler.post(command);
        }
    };

    private ApiRequest apiRequest;

    public OkHttpCallback(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        // 请求失败，将任务抛回主线程执行
        mResponsePoster.execute(new FailedRunnable(apiRequest, call, e));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (call.isCanceled()) {
            // 请求已终止
            mResponsePoster.execute(new FailedRunnable(apiRequest, call, null));
            return;
        }
        if (response == null) {
            // 请求未响应
            mResponsePoster.execute(new FailedRunnable(apiRequest, call,
                    new IOException("Response is null.")));
            return;
        }
        if (!response.isSuccessful()) {
            // 请求失败
            mResponsePoster.execute(new FailedRunnable(apiRequest, call,
                    new IOException("Unexpected code " + response)));
            return;
        }
        ApiCallback apiCallback = apiRequest.getCallback();
        if (apiCallback == null) return;
        ResponseBody body = response.body();
        try {
            // 填充请求响应数据
            ApiResponse apiResponse = ApiResponse.obtain();
            apiResponse.setCode(response.code());
            apiResponse.setCharset(apiRequest.getCharset());
            apiResponse.setBody(body.bytes());
            Headers headers = response.headers();
            for (String key : headers.names()) {
                apiResponse.addHeader(key, headers.get(key));
            }
            apiResponse.setTag(apiRequest.getTag());
            // 回调响应，运行在子线程，用于数据解析
            ApiResult apiResult = apiCallback.onResponse(apiResponse);
            // 释放响应数据
            ApiResponse.release(apiResponse);
            // 抛回主线程执行完成
            mResponsePoster.execute(new SuccessRunnable(apiRequest, call, apiResult));
        } catch (Exception e) {
            // 抛回主线程执行失败
            mResponsePoster.execute(new FailedRunnable(apiRequest, call, e));
        } finally {
            if (body != null) body.close();
        }
    }

    // 请求错误处理
    private static class FailedRunnable implements Runnable {
        private ApiRequest apiRequest;
        private Call call;
        private Throwable throwable;
        FailedRunnable(ApiRequest apiRequest, Call call, Throwable throwable) {
            this.apiRequest = apiRequest;
            this.call = call;
            this.throwable = throwable;
        }
        @Override
        public void run() {
            ApiCallback apiCallback = apiRequest.getCallback();
            if (apiCallback != null) {
                if (call.isCanceled()) {
                    apiCallback.onCancel();
                } else {
                    Failed f = Failed.OTHER;
                    if (throwable != null) {
                        if (throwable instanceof IOException) {
                            f = Failed.NETWORK_ERROR;       // 网络异常
                        } else if (throwable instanceof TimeoutException) {
                            f = Failed.TIMEOUT_ERROR;       // 请求超时
                        } else if (throwable instanceof JSONException) {
                            f = Failed.PARSE_ERROR;         // 数据解析异常
                        } else if (throwable instanceof AccountException) {
                            apiCallback.onAccountError();   // 帐户信息异常
                            return;
                        }
                    }
                    apiCallback.onFailure(f, throwable);
                }
            }
            ApiRequest.release(apiRequest);
        }
    }

    // 请求成功处理
    private static class SuccessRunnable implements Runnable {
        private ApiRequest apiRequest;
        private Call call;
        private ApiResult apiResult;
        SuccessRunnable(ApiRequest apiRequest, Call call, ApiResult apiResult) {
            this.apiRequest = apiRequest;
            this.call = call;
            this.apiResult = apiResult;
        }
        @Override
        public void run() {
            ApiCallback callback = apiRequest.getCallback();
            if (callback != null) {
                if (call.isCanceled()) {
                    callback.onCancel();
                } else {
                    callback.onComplete(apiResult);
                }
            }
            ApiResult.release(apiResult);
            ApiRequest.release(apiRequest);
        }
    }

}
