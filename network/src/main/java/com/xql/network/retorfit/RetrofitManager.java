package com.xql.network.retorfit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ClassName: RetrofitManager
 * @Description: 网络类
 * @CreateDate: 2021/9/10 14:50
 * @UpdateUser: RyanLiu
 */

public class RetrofitManager {
    // 请求的URL前缀  要以"/"结尾
    private static final String BASEURL = "http://web.juhe.cn:8080/constellation/";
    private static Retrofit retrofit;
    private static RetrofitManager retrofitManager;

    //提供共有的方法供外界访问
    public static RetrofitManager newInstance() {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                retrofitManager = new RetrofitManager();
            }
        }
        return retrofitManager;
    }

    //通过动态代理生成相应的Http请求
    public <T> T creat(Class<T> t) {
        return retrofit.create(t);
    }

    //构造方法私有化
    private RetrofitManager() {
        retrofit = getRetrofit();
    }

    //构建Ok请求
    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    //构建Retrofit
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl(BASEURL )
                //设置数据解析器
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
