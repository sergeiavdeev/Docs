package com.avdeev.docs.core.network.pojo;

import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import com.avdeev.docs.core.database.entity.File;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppFile extends Object implements Serializable {

    private String name;
    private String type;
    private String id;
    private long size;
    private boolean downloading;
    private boolean exist;

    public AppFile() {

        name = "";
        type = "";
        id = "";
        size = 0;
        downloading = false;
        exist = false;
    }

    public static AppFile create(File file) {
        AppFile newFile = new AppFile();
        newFile.id = file.id;
        newFile.name = file.name;
        newFile.type = file.type;
        newFile.size = file.size;
        newFile.downloading = false;
        newFile.exist = false;
        return newFile;
    }

    public static AppFile create(AppFile file) {
        AppFile newFile = new AppFile();
        newFile.id = file.id;
        newFile.name = file.name;
        newFile.type = file.type;
        newFile.size = file.size;
        newFile.downloading = false;
        newFile.exist = false;
        return newFile;
    }

    public static List<AppFile> createList(List<File> files) {
        List<AppFile> newList = new ArrayList<>();
        for (File file : files) {
            newList.add(AppFile.create(file));
        }
        return newList;
    }

    public AppFile(@NotNull AppFile appFile) {

        name = appFile.getName();
        type = appFile.getType();
        id = appFile.getId();
        size = appFile.getSize();

        downloading = appFile.isDownloading();
        exist = appFile.isExitst();
    }

    public AppFile(@NotNull JSONObject object) throws Exception {

        id = object.getString("id");
        name = object.getString("name");
        type = object.getString("type");
        size = object.getLong("size");

        downloading = false;
        exist = false;
    }

    public AppFile(@NotNull Cursor cursor) {

        id = cursor.getString(cursor.getColumnIndex("id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        type = cursor.getString(cursor.getColumnIndex("type"));
        size = cursor.getLong(cursor.getColumnIndex("size"));

        downloading = false;
        exist = false;
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

    public boolean isDownloading() {
        return downloading;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public boolean isExitst() {
        return exist;
    }
}
