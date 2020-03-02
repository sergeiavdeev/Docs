package com.avdeev.docs.core.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.avdeev.docs.core.network.pojo.File;

import java.util.ArrayList;
import java.util.List;

public class TaskWithFiles {
    @Embedded public Task task;
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    public List<TaskFile> files;

    public static TaskWithFiles create(com.avdeev.docs.core.network.pojo.Task task) {
        TaskWithFiles taskWithFiles = new TaskWithFiles();
        taskWithFiles.task = Task.create(task);
        taskWithFiles.files = new ArrayList<>();
        for (File file:task.getFiles()) {
            taskWithFiles.files.add(TaskFile.create(file));
        }
        return taskWithFiles;
    }
}
