package com.monch.network.executor;

import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by monch on 2017/5/4.
 */

final class OkHttpClientFactory {

    private static final String TAG = "OkHttpClientFactory";

    private static final int TIMEOUT = 60;
    private volatile static OkHttpClient mOkHttpClient;
    static OkHttpClient getClient() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpClientFactory.class) {
                if (mOkHttpClient == null) {
                    try {
                        X509TrustManager trustManager = createInsecureTrustManager();
                        SSLSocketFactory sslSocketFactory = createInsecureSslSocketFactory(trustManager);
                        mOkHttpClient = new OkHttpClient.Builder()
                                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                                .sslSocketFactory(sslSocketFactory, trustManager)
                                .hostnameVerifier(createInsecureHostnameVerifier())
                                .build();
                    } catch (Exception e) {
                        Log.e(TAG, "Get client error", e);
                        mOkHttpClient = new OkHttpClient.Builder()
                                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                                .build();
                    }
                }
            }
        }
        return mOkHttpClient;
    }

    private static HostnameVerifier createInsecureHostnameVerifier() {
        return new HostnameVerifier() {
            @Override public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
    }

    private static SSLSocketFactory createInsecureSslSocketFactory(TrustManager trustManager) {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[] {trustManager}, new SecureRandom());
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 信任所有证书
     */
    private static X509TrustManager createInsecureTrustManager() {
        return new X509TrustManager() {
            @Override public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

}
