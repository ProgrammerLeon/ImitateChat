package com.monch.network.executor;

import com.monch.network.ApiRequest;

/**
 * Created by monch on 2017/5/4.
 */

public interface IExecutor {

    /**
     * 拉取请求
     * @param request
     */
    void doGet(ApiRequest request);

    /**
     * 提交请求
     * @param request
     */
    void doPost(ApiRequest request);

    /**
     * 上传请求
     * @param request
     */
    void doUpload(ApiRequest request);

    /**
     * 下载请求
     * @param request
     */
    void doDownload(ApiRequest request);

    /**
     * 释放缓存
     * @param request
     */
    void releaseCache(ApiRequest request);

    /**
     * 取消请求
     * @param request
     */
    void cancel(ApiRequest request);

    /**
     * 取消所有请求
     */
    void cancelAll();

}
