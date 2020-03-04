package com.avdeev.docs.core.network.pojo;

import android.database.Cursor;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Document extends BaseDocument {

    private String author;
    private String number;
    private String type;
    private String status;
    private String recipient;
    private String signer;
    private String department;
    private long updated_at;
    private long date;

    private List<AppFile> appFiles;

    public Document(String id, String title, String author) {

        this.id = id;
        this.title = title;
        this.author = author;
        number = "";
        type = "";
        updated_at = 0;
        date = 0;
        appFiles = new ArrayList<>();
    }

    public Document(@NotNull Document document) {

        id = document.getId();
        title = document.getTitle();
        author = document.getAuthor();
        type = document.getType();
        number = document.getNumber();
        date = document.getDate();
        updated_at = document.getUpdated_at();
        appFiles = new ArrayList<>();
    }

    public Document(@NotNull Cursor cursor) {

        id = cursor.getString(cursor.getColumnIndex("id"));
        title = cursor.getString(cursor.getColumnIndex("title"));
        author = cursor.getString(cursor.getColumnIndex("author"));
        number = cursor.getString(cursor.getColumnIndex("number"));
        type = cursor.getString(cursor.getColumnIndex("type"));
        updated_at = cursor.getLong(cursor.getColumnIndex("updated_at"));
        date = cursor.getLong(cursor.getColumnIndex("date"));
        appFiles = new ArrayList<>();
    }

    public String getAuthor() {
        return author;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public long getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSigner() {
        return signer;
    }

    public String getDepartment() {
        return department;
    }

    public List<AppFile> getAppFiles() {
        return appFiles;
    }

    public void setAppFiles(ArrayList<AppFile> appFiles) {
        this.appFiles = appFiles;
    }

    public void fromJson(JSONObject object) {

        try {
            date = object.getLong("date");
            description = object.getString("description");
            recipient = object.getString("addressee");
            status = object.getString("status");
            signer = object.getString("signee");
            department = object.getString("department");

            JSONArray jsonFiles = object.getJSONArray("files");

            for (int i = 0; i < jsonFiles.length(); i++) {
                JSONObject file = jsonFiles.getJSONObject(i);

                appFiles.add(new AppFile(file));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
