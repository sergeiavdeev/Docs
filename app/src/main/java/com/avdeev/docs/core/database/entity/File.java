package com.avdeev.docs.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.avdeev.docs.core.network.pojo.AppFile;

import java.io.Serializable;

@Entity
public class File extends Object implements Serializable {
    @PrimaryKey (autoGenerate = true) public long _id;
    public String id;
    @ColumnInfo(name = "task_id") public String taskId;
    public String name;
    public String type;
    public long size;

    public File(String id, String name) {
        //this.taskId = taskId;
        this.id = id;
        this.name = name;
    }

    public static File create(AppFile appFile) {
        File newFile = new File(appFile.getId(), appFile.getName());
        newFile.type = appFile.getType();
        newFile.size = appFile.getSize();
        return newFile;
    }
}
