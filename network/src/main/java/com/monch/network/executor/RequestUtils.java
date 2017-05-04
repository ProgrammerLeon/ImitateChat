package com.monch.network.executor;

import android.support.v4.util.ArrayMap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by monch on 2017/5/4.
 */

public class RequestUtils {

    public static String makeUrl(String source, ArrayMap<String, String> parameter, Charset charset) {
        if (parameter == null || parameter.isEmpty()) return source;
        StringBuilder sb = new StringBuilder(source);
        if (source.contains("?")) sb.append("&");
        else sb.append("?");
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            String key = encode(entry.getKey(), charset);
            String value = encode(entry.getValue(), charset);
            sb.append(key).append("=").append(value).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void makeHeader(Request.Builder builder, ArrayMap<String, String> headers, Charset charset) {
        if (builder == null || headers == null || headers.isEmpty()) return;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String name = encode(entry.getKey(), charset);
            String value = encode(entry.getValue(), charset);
            builder.addHeader(name, value);
        }
    }

    public static String encode(String string, Charset charset) {
        if (string == null) return null;
        try {
            return URLEncoder.encode(string, charset.name());
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

    public static String decode(String string, Charset charset) {
        if (string == null) return null;
        try {
            return URLDecoder.decode(string, charset.name());
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

}
