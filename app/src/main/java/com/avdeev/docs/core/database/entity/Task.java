package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey @NonNull public String id;
    public String title;
    public String author;
    public long priority;
    public String type;
    public String number;
    public String assignee;
    public String document_type;
    public String description;
    @ColumnInfo(name = "date_due") public long dateDue;
    public long date;
    @ColumnInfo(name = "updated_at") public long updatedAt;

    public Task(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Task create(com.avdeev.docs.core.network.pojo.Task task) {
        Task newTask = new Task(task.getId(), task.getTitle());
        newTask.author = task.getAuthor();
        newTask.priority = task.getPriority();
        newTask.type = task.getType();
        newTask.number = task.getNumber();
        newTask.assignee = task.getAssignee();
        newTask.document_type = task.getDocument_type();
        newTask.description = task.getDescription();
        newTask.dateDue = task.getDate_due();
        newTask.date = task.getDate();
        newTask.updatedAt = task.getUpdated_at();

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
                this.dateDue == task.dateDue &&
                this.date == task.date &&
                this.updatedAt == task.updatedAt;

    }
}

