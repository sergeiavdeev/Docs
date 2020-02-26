package com.avdeev.docs.core.network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService instance;
    private static String baseUrl;
    private Retrofit retrofit;

    private NetworkService() {

        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                Request request = chain.request();
                Request.Builder builder = request.newBuilder()
                        .addHeader("X-Auth-Key", "3d5670c50623d2ed")
                        .addHeader("X-Auth-Timestamp", "1574250835161")
                        .addHeader("X-Auth-Token", "200f03928f3d90f31dbefdc01a4cff1f3e616de8");

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance(String baseUrl) {

        NetworkService.baseUrl = baseUrl;

        if (instance == null) {
            instance = new NetworkService();
        }

        return instance;
    }

    public JsonDocApi getApi() {
        return retrofit.create(JsonDocApi.class);
    }
}
