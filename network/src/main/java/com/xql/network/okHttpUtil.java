package com.xql.network;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okHttpUtil {
    /**
     * 网络相关
     */
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String repose = new String();
    public static void getOkhttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postOkhttpRequest(String address, String data, Callback callback) {
        RequestBody requestBody = RequestBody.create(JSON, data);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String postJsonRequest(String address, String data) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, data);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }
}