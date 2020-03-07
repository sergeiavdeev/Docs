package com.avdeev.docs.core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.avdeev.docs.core.network.pojo.Action;
import com.avdeev.docs.core.network.pojo.Document;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class AppUser {

    private static boolean auth;
    private static String passwordHash;
    private static String apiUrl;
    private static String deviceKey;
    private static AppUser instance;

    private AppUser() {
        auth = false;
        passwordHash = "";
        apiUrl = "";
        deviceKey = "";
    }

    public static AppUser getInstance() {
        if (instance == null) {
            synchronized (AppUser.class) {
                if (instance == null) {
                    instance = new AppUser();
                }
            }
        }
        return instance;
    }

    public static void setAuth(boolean auth) {
        AppUser.auth = auth;
    }

    public static boolean isAuth() {
        return auth;
    }

    public static void setHash(String passwordHash) {
        instance.passwordHash = passwordHash;
    }

    public static void setApiUrl(String url) {
        instance.apiUrl = url;
    }

    public static void setDeviceKey(String deviceKey) {
        instance.deviceKey = deviceKey;
    }

    public static String getPasswordHash() {
        return passwordHash;
    }

    public static String getApiUrl() {
        return apiUrl;
    }

    public static String getDeviceKey() {
        return deviceKey;
    }
}