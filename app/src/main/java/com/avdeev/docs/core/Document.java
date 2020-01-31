package com.avdeev.docs.core;

import android.database.Cursor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.Serializable;

public class Document extends Object implements Serializable {

    private String title;
    private String author;
    private String id;
    private String number;
    private String type;

    private long updated_at;
    private long date;

    private String status;
    private String description;
    private String recipient;
    private String signer;
    private String department;

    public Document(String id, String title, String author) {

        this.id = id;
        this.title = title;
        this.author = author;
        number = "";
        type = "";
        updated_at = 0;
        date = 0;
    }

    public Document(@NotNull Document document) {

        id = document.getId();
        title = document.getTitle();
        author = document.getAuthor();
        type = document.getType();
        number = document.getNumber();
        date = document.getDate();
        updated_at = document.getUpdated_at();
    }

    public Document(@NotNull Cursor cursor) {

        id = cursor.getString(cursor.getColumnIndex("id"));
        title = cursor.getString(cursor.getColumnIndex("title"));
        author = cursor.getString(cursor.getColumnIndex("author"));
        number = cursor.getString(cursor.getColumnIndex("number"));
        type = cursor.getString(cursor.getColumnIndex("type"));
        updated_at = cursor.getLong(cursor.getColumnIndex("updated_at"));
        date = cursor.getLong(cursor.getColumnIndex("date"));
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
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

    public String getDescription() {
        return description;
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

    public void fromJson(JSONObject object) {

        try {
            date = object.getLong("date");
            description = object.getString("description");
            recipient = object.getString("addressee");
            status = object.getString("status");
            signer = object.getString("signee");
            department = object.getString("department");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
