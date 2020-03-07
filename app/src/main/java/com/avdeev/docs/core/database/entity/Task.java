package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class Task implements Serializable {
    @PrimaryKey @NonNull
    public String id;

    public String title;
    public String author;
    public long priority;
    public String type;
    public String number;
    public String assignee;
    public String document_type;
    public String description;
    public long date_due;
    public long date;
    public long updated_at;

    @Ignore
    public List<File> files;

    public Task(String id, String title) {
        this.id = id;
        this.title = title;
    }


    public static Task create(Task task) {
        Task newTask = new Task(task.id, task.title);
        newTask.author = task.author;
        newTask.priority = task.priority;
        newTask.type = task.type;
        newTask.number = task.number;
        newTask.assignee = task.assignee;
        newTask.document_type = task.document_type;
        newTask.description = task.description;
        newTask.date_due = task.date_due;
        newTask.date = task.date;
        newTask.updated_at = task.updated_at;

        return newTask;
    }

    public boolean equals(Task task) {
        return this.id.equals(task.id)&&
                this.title.equals(task.title)&&
                this.author.equals(task.author)&&
                this.priority == task.priority &&
                this.type.equals(task.type)&&
                this.number.equals(task.number)&&
                this.assignee.equals(task.assignee)&&
                this.document_type.equals(task.document_type)&&
                this.description.equals(task.description)&&
                this.date_due == task.date_due &&
                this.date == task.date &&
                this.updated_at == task.updated_at;

    }

    public static DiffUtil.ItemCallback<Task> DIFF_UTIL = new DiffUtil.ItemCallback<Task>() {

        @Override
        public boolean areItemsTheSame(Task oldTask, Task newTask) {
            return oldTask.id.equals(newTask.id);
        }

        @Override
        public boolean areContentsTheSame(Task oldTask, Task newTask) {
            return oldTask.equals(newTask);
        }
    };
}

