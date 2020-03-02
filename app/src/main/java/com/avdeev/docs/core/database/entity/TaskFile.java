package com.avdeev.docs.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.avdeev.docs.core.network.pojo.File;

@Entity
public class TaskFile {
    @PrimaryKey (autoGenerate = true) public long _id;
    public String id;
    @ColumnInfo(name = "task_id") public String taskId;
    public String name;
    public String type;
    public long size;

    public TaskFile(String id, String name) {
        //this.taskId = taskId;
        this.id = id;
        this.name = name;
    }

    public static TaskFile create(File file) {
        TaskFile newFile = new TaskFile(file.getId(), file.getName());
        newFile.type = file.getType();
        newFile.size = file.getSize();
        return newFile;
    }
}
