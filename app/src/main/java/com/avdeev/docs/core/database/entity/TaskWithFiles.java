package com.avdeev.docs.core.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TaskWithFiles {
    @Embedded public Task task;
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    public List<TaskFile> files;
}
