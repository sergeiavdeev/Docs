package com.avdeev.docs.core.network;


import com.avdeev.docs.core.AppUser;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;
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
    private String authKey;
    private String passwordHash;

    private NetworkService() {

        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                String date = String.valueOf(new Date().getTime());

                Request request = chain.request();
                Request.Builder builder = request.newBuilder()
                        .addHeader("X-Auth-Key", authKey)
                        .addHeader("X-Auth-Timestamp", date)
                        .addHeader("X-Auth-Token", generateApiToken(date));

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

    public static NetworkService getInstance() {
        return instance;
    }

    public NetworkService setAuthKey(String authKey) {
        this.authKey = authKey;
        return this;
    }

    public NetworkService setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public NetworkService setPasswordHashFromPassword(String password) {
        MessageDigest pHash;
        try {
            pHash = MessageDigest.getInstance("SHA-1");
            pHash.update(password.getBytes("UTF-8"));
            this.passwordHash = getHex(pHash.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppUser.setHash(this.passwordHash);
        return this;
    }

    private String generateApiToken(String date) {
        String token = "";
        MessageDigest hash;
        try {
            hash = MessageDigest.getInstance("SHA-1");
            hash.update(authKey.getBytes("UTF-8"));
            hash.update(date.getBytes("UTF-8"));
            hash.update(passwordHash.getBytes("UTF-8"));
            token = getHex(hash.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    @NotNull
    private String getHex(@NotNull byte [] bytes) {
        StringBuilder bTtoken = new StringBuilder();
        for (byte b : bytes)
        {
            bTtoken.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return bTtoken.toString();
    }

    public JsonDocApi getApi() {
        return retrofit.create(JsonDocApi.class);
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
