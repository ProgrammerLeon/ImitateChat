package com.monch.network;

import org.json.JSONException;

/**
 * Created by monch on 2017/5/4.
 */

public interface ApiCallback {

    /**
     * 请求开始回调，运行在当前线程
     * @param request 请求数据
     */
    void onStart(ApiRequest request);

    /**
     * 请求响应回调，运行在子线程，主要用于数据解析。
     * 在解析过程中，如果发现后台返回的错误为帐户异常，
     * 可直接抛出AccountException，在onAccountError回调中统一处理
     * @param response 响应数据
     * @return
     * @throws JSONException JSON解析异常
     * @throws AccountException 帐户登录异常
     */
    ApiResult onResponse(ApiResponse response) throws JSONException, AccountException;

    /**
     * 请求完成，运行在主线程，将解析后的结果返回
     * @param result 解析结果
     */
    void onComplete(ApiResult result);

    /**
     * 请求失败回调，运行在主线程
     * @param failed 错误类型
     * @param throwable 异常信息
     */
    void onFailure(Failed failed, Throwable throwable);

    /**
     * 帐户异常回调，运行在主线程，在onResponse回调中，如果接收到AccountException异常，会直接回调到这里
     */
    void onAccountError();

    /**
     * 请求取消回调，运行在主线程
     */
    void onCancel();

}
