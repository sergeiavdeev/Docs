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
    public String priority;
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
}

