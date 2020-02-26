package com.avdeev.docs.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class TaskFile {
    public String id;
    @ColumnInfo(name = "task_id")
    public String taskId;
    public String type;
    public long size;
}
