package com.avdeev.docs.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class File implements Serializable {
    @PrimaryKey (autoGenerate = true) public long _id;
    public String id;
    @ColumnInfo(name = "task_id") public String taskId;
    public String name;
    public String type;
    public long size;

    @Ignore
    private boolean downloading;

    @Ignore
    private boolean exist;

    public File(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static File create(File appFile) {
        File newFile = new File(appFile.id, appFile.name);
        newFile.type = appFile.type;
        newFile.size = appFile.size;
        return newFile;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public boolean isExist() {
        return exist;
    }
}
