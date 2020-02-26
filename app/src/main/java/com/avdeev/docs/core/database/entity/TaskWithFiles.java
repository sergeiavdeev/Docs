package com.avdeev.docs.core.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.database.entity.TaskFile;

import java.util.List;

public class TaskWithFiles {
    @Embedded public Task task;
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    public List<TaskFile> files;
}
