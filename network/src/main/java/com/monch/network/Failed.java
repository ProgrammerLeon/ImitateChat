package com.monch.network;

/**
 * Created by monch on 2017/5/4.
 */

public enum Failed {

    TIMEOUT_ERROR("连接超时"),
    NETWORK_ERROR("网络连接失败"),
    PARSE_ERROR("数据解析错误"),
    DOWNLOAD_ERROR("下载失败"),
    ACCOUNT_ERROR("帐号异常"),
    OTHER("未知错误");

    private String error;
    Failed(String error) {
        this.error = error;
    }
    public String error() {
        return error;
    }

}
