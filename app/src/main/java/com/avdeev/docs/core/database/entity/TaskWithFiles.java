package com.avdeev.docs.core.database.entity;

import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.avdeev.docs.core.network.pojo.File;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskWithFiles extends Object implements Serializable {
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

    public boolean equals(TaskWithFiles task) {
        return this.task.id.equals(task.task.id)&&
                this.task.title.equals(task.task.title)&&
                this.task.author.equals(task.task.author)&&
                this.task.priority == task.task.priority &&
                this.task.type.equals(task.task.type)&&
                this.task.number.equals(task.task.number)&&
                this.task.assignee.equals(task.task.assignee)&&
                this.task.document_type.equals(task.task.document_type)&&
                this.task.description.equals(task.task.description)&&
                this.task.dateDue == task.task.dateDue &&
                this.task.date == task.task.date &&
                this.task.updatedAt == task.task.updatedAt;

    }

    public static DiffUtil.ItemCallback<TaskWithFiles> DIFF_UTIL = new DiffUtil.ItemCallback<TaskWithFiles>() {

        @Override
        public boolean areItemsTheSame(TaskWithFiles oldTask, TaskWithFiles newTask) {
            return oldTask.task.id.equals(newTask.task.id);
        }

        @Override
        public boolean areContentsTheSame(TaskWithFiles oldTask, TaskWithFiles newTask) {
            return oldTask.equals(newTask);
        }
    };
}
