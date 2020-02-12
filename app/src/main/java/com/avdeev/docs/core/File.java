package com.avdeev.docs.core;

import android.database.Cursor;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.Serializable;

public class File extends Object implements Serializable {

    private String name;
    private String type;
    private String id;
    private long size;

    private boolean isDownload;
    private boolean wait;

    public File() {

        name = "";
        type = "";
        id = "";
        size = 0;
        isDownload = false;
        wait = false;
    }

    public File(@NotNull File file) {

        name = file.getName();
        type = file.getType();
        id = file.getId();
        size = file.getSize();

        isDownload = file.isDownload();
        wait = file.isWait();
    }

    public File(@NotNull JSONObject object) throws Exception {

        id = object.getString("id");
        name = object.getString("name");
        type = object.getString("type");
        size = object.getLong("size");

        isDownload = false;
        wait = false;
    }

    public File(@NotNull Cursor cursor) {

        id = cursor.getString(cursor.getColumnIndex("id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        type = cursor.getString(cursor.getColumnIndex("type"));
        size = cursor.getLong(cursor.getColumnIndex("size"));

        isDownload = false;
        wait = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }
}
