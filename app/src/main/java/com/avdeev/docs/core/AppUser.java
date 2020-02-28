package com.avdeev.docs.core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.format.DateFormat;
import android.util.Log;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class AppUser {

    private static boolean auth;
    private static AppUser instance;
    private static String apiUrl;
    private static String hash;
    private static String key;
    private Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase dbR;
    private SQLiteDatabase dbW;
    private String passwordHash;
    private String apiPath;
    private String deviceKey;
    private ArrayList<Document> docsIn;
    private ArrayList<Document> docsOut;
    private ArrayList<Document> docsInner;
    private ArrayList<Task> taskList;

    public AppUser(Context context) {

        this.context = context;
        dbHelper = new DbHelper(context);
        dbR = dbHelper.getReadableDatabase();
        auth = false;
        deviceKey = "dfb2304e-67d0-4d06-a8b0-a136dc562103";
        docsIn = new ArrayList<>();
        docsOut = new ArrayList<>();
        docsInner = new ArrayList<>();
        taskList = new ArrayList<>();

        apiPath = "https://sed.rudn.ru/BGU_DEMO/hs/DGU_APP_Mobile_Client/";

        Cursor cursor = dbR.rawQuery("SELECT * FROM User", null);

        if (cursor.moveToFirst()) {

            passwordHash = cursor.getString(cursor.getColumnIndex("token"));
            apiPath = cursor.getString(cursor.getColumnIndex("apiPath"));
            auth = true;
        }

        if (dbR.isOpen()) {
            dbR.close();
        }
    }

    private AppUser() {
        this.auth = false;
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

    public static void setApiUrl(String apiUrl) {
        AppUser.apiUrl = apiUrl;
    }

    public static void setHash(String hash) {
        AppUser.hash = hash;
    }

    public static void setKey(String key) {
        AppUser.key = key;
    }

    public static String getApiUrl() {
        return apiUrl;
    }

    public static String getHash() {
        return hash;
    }

    public static String getKey() {
        return key;
    }

    public static void setAuth(boolean auth) {
        AppUser.auth = auth;
    }

    public boolean isAuth() {

        return auth;
    }

    public boolean auth(String login, String password) {

        boolean result = false;

        try {

            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create("{\"login\": \""+ login + "\"}", JSON);

            //String key = "dfb2304e-67d0-4d06-a8b0-a136dc562103";

            String date = String.valueOf(new Date().getTime());

            MessageDigest hash = MessageDigest.getInstance("SHA-1");
            MessageDigest pHash = MessageDigest.getInstance("SHA-1");

            pHash.update(password.getBytes("UTF-8"));
            String pHex = getHex(pHash.digest());

            hash.update(deviceKey.getBytes("UTF-8"));
            hash.update(date.getBytes("UTF-8"));
            hash.update(pHex.getBytes("UTF-8"));

            String token = getHex(hash.digest());

            Request request = new Request.Builder()
                    .url(apiPath + "auth")
                    .post(body)
                    .addHeader("X-Auth-Key", deviceKey)
                    .addHeader("X-Auth-Timestamp", date)
                    .addHeader("X-Auth-Token", token)
                    .build();

            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();

            Log.d("Код", String.valueOf(response.code()));
            Log.d("Ответ", jsonData);
            Log.d("X-Auth-Token", token);

            JSONObject jsonObject = new JSONObject(jsonData);

            if (jsonObject.getInt("success") == 1 && saveUserData(pHex, apiPath)){

                auth = true;
                passwordHash = pHex;
                result = true;

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<Document> getDocInList() {

        if (docsIn.size() > 0) {
            return docsIn;
        }

        dbR = dbHelper.getReadableDatabase();
        Cursor c = dbR.rawQuery("SELECT * FROM DocIn", null);

        docsIn = new ArrayList<>();

        if (c.moveToFirst()) {

            do {

                docsIn.add(new Document(c));
            }
            while (c.moveToNext());
        }

        if (dbR.isOpen()) {
            dbR.close();
        }

        return docsIn;
    }

    public ArrayList<Document> getDocOutList() {

        if (docsOut.size() > 0) {
            return docsOut;
        }

        dbR = dbHelper.getReadableDatabase();
        Cursor c = dbR.rawQuery("SELECT * FROM DocOut", null);

        docsOut = new ArrayList<>();

        if (c.moveToFirst()) {

            do {

                docsOut.add(new Document(c));
            }
            while (c.moveToNext());
        }

        if (dbR.isOpen()) {
            dbR.close();
        }

        return docsOut;
    }

    public ArrayList<Document> getDocInnerList() {

        if (docsInner.size() > 0) {
            return docsInner;
        }

        dbR = dbHelper.getReadableDatabase();
        Cursor c = dbR.rawQuery("SELECT * FROM DocInner", null);

        docsInner = new ArrayList<>();

        if (c.moveToFirst()) {

            do {

                docsInner.add(new Document(c));
            }
            while (c.moveToNext());
        }

        if (dbR.isOpen()) {
            dbR.close();
        }

        return docsInner;
    }

    public ArrayList<Task> getTaskList() throws Exception {

        if (taskList.size() == 0) {

            dbR = dbHelper.getReadableDatabase();
            Cursor c = dbR.rawQuery("SELECT * FROM Task", null);
            Cursor cFiles;

            if (c.moveToFirst()) {

                do {

                    cFiles = dbR.rawQuery(
                    "SELECT * FROM TaskFile WHERE task='" +
                            c.getString(c.getColumnIndex("id")) + "'",
                            null)
                    ;
                    taskList.add(new Task(c, cFiles));
                }
                while (c.moveToNext());
            }

            if (dbR.isOpen()) {
                dbR.close();
            }
        }

        return taskList;
    }

    public int updateDocList(String type) {

        int addRows = 0;

        try {

            OkHttpClient client = new OkHttpClient();

            long maxTime = getMaxDocTime(type);

            String query = "documents?chapter=" + type;

            if (maxTime > 0) {
                query = query + "&timestamp=" + maxTime;
            }

            ArrayList<Header> headers = getHeaders();

            Request request = new Request.Builder()
                    .url(apiPath + query)
                    .get()
                    .addHeader(headers.get(0).name.utf8(), headers.get(0).value.utf8())
                    .addHeader(headers.get(1).name.utf8(), headers.get(1).value.utf8())
                    .addHeader(headers.get(2).name.utf8(), headers.get(2).value.utf8())
                    .build();

            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonData);

            JSONArray docs = jsonObject.getJSONArray("documents");

            String tableName = getTableName(type);

            dbW = dbHelper.getWritableDatabase();

            dbW.beginTransaction();
            String sql = "INSERT OR REPLACE INTO " + tableName + " VALUES(?, ?, ?, ?, ?, ?, ?)";
            SQLiteStatement insert = dbW.compileStatement(sql);

            try {

                for (int i = 0; i < docs.length(); i++) {

                    JSONObject doc = docs.getJSONObject(i);

                    insert.bindString(1, doc.getString("id"));
                    insert.bindString(2, doc.getString("title"));
                    insert.bindString(3, doc.getString("author"));
                    insert.bindString(4, doc.getString("type"));
                    insert.bindString(5, doc.getString("number"));
                    insert.bindLong(6, doc.getLong("updated_at"));
                    insert.bindLong(7, 0);
                    insert.execute();

                    addRows++;
                }
                dbW.setTransactionSuccessful();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                dbW.endTransaction();
            }

            if (dbW.isOpen()) {
                dbW.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return addRows;
    }

    public Document updateDocument(Document document, String type) {

        Document doc = new Document(document);

        OkHttpClient client = new OkHttpClient();


        String query = "documents/details?chapter=" + type + "&id=" + document.getId();

        ArrayList<Header> headers = getHeaders();

        Request request = new Request.Builder()
                .url(apiPath + query)
                .get()
                .addHeader(headers.get(0).name.utf8(), headers.get(0).value.utf8())
                .addHeader(headers.get(1).name.utf8(), headers.get(1).value.utf8())
                .addHeader(headers.get(2).name.utf8(), headers.get(2).value.utf8())
                .build();

        try {
            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonData).getJSONObject("document");

            doc.fromJson(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }

    public int updateTaskList() {

        int addRows = 0;

        try {

            OkHttpClient client = new OkHttpClient();

            long maxTime = getMaxTaskTime();

            String query = "tasks";

            if (maxTime > 0) {
                query = query + "?timestamp=" + maxTime;
            }

            ArrayList<Header> headers = getHeaders();

            Request request = new Request.Builder()
                    .url(apiPath + query)
                    .get()
                    .addHeader(headers.get(0).name.utf8(), headers.get(0).value.utf8())
                    .addHeader(headers.get(1).name.utf8(), headers.get(1).value.utf8())
                    .addHeader(headers.get(2).name.utf8(), headers.get(2).value.utf8())
                    .build();

            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonData);

            JSONArray docs = jsonObject.getJSONArray("tasks");

            dbW = dbHelper.getWritableDatabase();

            dbW.beginTransaction();
            String sql = "INSERT OR REPLACE INTO Task VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            SQLiteStatement insert = dbW.compileStatement(sql);

            String sql_files = "INSERT OR REPLACE INTO TaskFile VALUES(?, ?, ?, ?, ?)";
            SQLiteStatement insert_files = dbW.compileStatement(sql_files);

            try {

                for (int i = 0; i < docs.length(); i++) {

                    JSONObject doc = docs.getJSONObject(i);

                    insert.bindString(1, doc.getString("id"));
                    insert.bindString(2, doc.getString("title"));
                    insert.bindString(3, doc.getString("author"));
                    insert.bindLong(4, doc.getLong("priority"));
                    insert.bindString(5, doc.getString("type"));
                    insert.bindString(6, doc.getString("number"));
                    insert.bindString(7, doc.getString("assignee"));
                    insert.bindString(8, doc.getString("document_type"));
                    insert.bindString(9, doc.getString("description"));
                    insert.bindLong(10, doc.getLong("date_due"));
                    if (doc.has("date")) {
                        insert.bindLong(11, doc.getLong("date"));
                    } else {
                        insert.bindLong(11, 0);
                    }
                    insert.bindLong(12, doc.getLong("updated_at"));
                    insert.execute();

                    JSONArray files = doc.getJSONArray("files");

                    for (int j = 0; j < files.length(); j++) {

                        JSONObject file = files.getJSONObject(j);

                        insert_files.bindString(1, file.getString("id"));
                        insert_files.bindString(2, doc.getString("id"));
                        insert_files.bindString(3, file.getString("name"));
                        insert_files.bindString(4, file.getString("type"));
                        insert_files.bindLong(5, file.getLong("size"));
                        insert_files.execute();
                    }


                    addRows++;
                }
                dbW.setTransactionSuccessful();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                dbW.endTransaction();
            }

            if (dbW.isOpen()) {
                dbW.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (dbW.isOpen()) {
                dbW.close();
            }
        }

        return addRows;
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

    private boolean saveUserData(String token, String apiPath) {

        boolean result = false;

        try {
            dbW = dbHelper.getWritableDatabase();
            dbR = dbHelper.getReadableDatabase();
            dbW.execSQL("DELETE FROM User");
            dbW.execSQL("INSERT INTO User VALUES('"+ token + "', '" + apiPath + "')");

            Cursor cursor = dbR.rawQuery("SELECT * FROM ApiHistory WHERE apiPath='" + apiPath + "'", null);

            if (!cursor.moveToFirst()) {

                dbW.execSQL("INSERT INTO ApiHistory VALUES('"+ apiPath + "')");
            }

            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dbW.isOpen()) dbW.close();
        if (dbR.isOpen()) dbR.close();

        return result;
    }

    private long getMaxDocTime(@NotNull String type) {

        long maxTime = 0;

        String table = getTableName(type);

        if (table != "") {

            dbR = dbHelper.getReadableDatabase();
            Cursor cursor = dbR.rawQuery("select max(updated_at) as updated_at from " + table, null);

            if (cursor.moveToFirst()) {

                maxTime = cursor.getLong(cursor.getColumnIndex("updated_at"));
            }
        }

        if (dbR.isOpen()) {
            dbR.close();
        }

        return maxTime;
    }

    private long getMaxTaskTime() {

        long maxTime = 0;

        dbR = dbHelper.getReadableDatabase();
        Cursor cursor = dbR.rawQuery("select max(updated_at) as updated_at from Task", null);

        if (cursor.moveToFirst()) {

            maxTime = cursor.getLong(cursor.getColumnIndex("updated_at"));
        }

        if (dbR.isOpen()) {
            dbR.close();
        }

        return maxTime;
    }

    private ArrayList<Header> getHeaders() {

        ArrayList<Header> headers = new ArrayList();

        String date = String.valueOf(new Date().getTime());

        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-1");

            hash.update(deviceKey.getBytes("UTF-8"));
            hash.update(date.getBytes("UTF-8"));
            hash.update(passwordHash.getBytes("UTF-8"));

            String token = getHex(hash.digest());

            headers.add(new Header("X-Auth-Key", deviceKey));
            headers.add(new Header("X-Auth-Timestamp", date));
            headers.add(new Header("X-Auth-Token", token));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return headers;

    }

    @Contract(pure = true)
    private String getTableName(@NotNull String type) {

        String tableName = "";

        switch (type) {
            case "inbox":
                tableName = "DocIn";
                break;
            case "outbox":
                tableName = "DocOut";
                break;
            case "internal":
                tableName = "DocInner";
                break;
        }

        return tableName;
    }

    public ArrayList<Action> getActions(String type, String id, String method) {

        ArrayList<Action> actions = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();


        String query = method + "?chapter=" + type + "&iddocument=" + id;

        ArrayList<Header> headers = getHeaders();

        Request request = new Request.Builder()
                .url(apiPath + query)
                .get()
                .addHeader(headers.get(0).name.utf8(), headers.get(0).value.utf8())
                .addHeader(headers.get(1).name.utf8(), headers.get(1).value.utf8())
                .addHeader(headers.get(2).name.utf8(), headers.get(2).value.utf8())
                .build();

        try {
            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();

            JSONArray jsonActions = new JSONObject(jsonData).getJSONArray(method);

            for (int i = 0; i < jsonActions.length(); i++) {

                actions.add(new Action(jsonActions.getJSONObject(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return actions;
    }

    public ArrayList<Action> getActions(@NotNull ActionRequest params) {

        ArrayList<Action> actions = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();


        String query = params.method + "?chapter=" + params.type + "&iddocument=" + params.id;

        if (params.task) {

            query = params.method + "?chapter=" + params.type + "&idtask=" + params.id;
        }

        ArrayList<Header> headers = getHeaders();

        Request request = new Request.Builder()
                .url(apiPath + query)
                .get()
                .addHeader(headers.get(0).name.utf8(), headers.get(0).value.utf8())
                .addHeader(headers.get(1).name.utf8(), headers.get(1).value.utf8())
                .addHeader(headers.get(2).name.utf8(), headers.get(2).value.utf8())
                .build();

        try {
            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();

            JSONArray jsonActions = new JSONObject(jsonData).getJSONArray(params.method);

            for (int i = 0; i < jsonActions.length(); i++) {

                actions.add(new Action(jsonActions.getJSONObject(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return actions;
    }

    public String getFile(@NotNull File file) throws Exception {

        String fileName = file.getId() + "." + file.getType();

        java.io.File jFile = new java.io.File(context.getFilesDir(), fileName);

        //filePath = jFile.getName();

        if (!jFile.exists()) {

            OkHttpClient client = new OkHttpClient();


            String query = "files/?id=" + file.getId();

            ArrayList<Header> headers = getHeaders();

            Request request = new Request.Builder()
                    .url(apiPath + query)
                    .get()
                    .addHeader(headers.get(0).name.utf8(), headers.get(0).value.utf8())
                    .addHeader(headers.get(1).name.utf8(), headers.get(1).value.utf8())
                    .addHeader(headers.get(2).name.utf8(), headers.get(2).value.utf8())
                    .build();

            Response response = client.newCall(request).execute();

            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(response.body().bytes());
            outputStream.close();
        }

        file.setDownloaded(true);

        return fileName;
    }
}
