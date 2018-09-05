package com.bethena.youtudemo.utils;

import com.bethena.youtudemo.constant.Constants;
import com.bethena.youtudemo.constant.YouTuApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {

    private static Retrofit retrofit;
    private static OkHttpClient httpClient;
    private static YouTuApi api;

    public static YouTuApi getApi() {
        if (api == null) {
            synchronized (YouTuApi.class) {
                if (api == null) {
                    api = getRetrofit().create(YouTuApi.class);
                }
            }
        }
        return api;
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (Retrofit.class) {
                if (retrofit == null) {
                    Retrofit r = new Retrofit.Builder()
                            .baseUrl("http://api.youtu.qq.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(getHttpClient())
                            .build();
                    retrofit = r;
                }
            }
        }
        return retrofit;
    }

    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (OkHttpClient.class) {
                if (httpClient == null) {
                    return new OkHttpClient.Builder()
                            .addNetworkInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request();
                                    StringBuffer mySign = new StringBuffer("");
                                    YoutuSign.appSign(Constants.APP_ID, Constants.SECRET_ID, Constants.SECRET_KEY,
                                            System.currentTimeMillis() / 1000 + Constants.EXPIRED_SECONDS,
                                            "", mySign);
                                    Request nRequest = request.newBuilder()
                                            .header("accept", "*/*")
                                            .header("user-agent", "youtu-android-sdk")
                                            .header("Authorization", mySign.toString())
                                            .header("Content-Type", "text/json")
                                            .build();
                                    return chain.proceed(nRequest);
                                }
                            })
                            .build();
                }
            }
        }

        return httpClient;
    }
}
