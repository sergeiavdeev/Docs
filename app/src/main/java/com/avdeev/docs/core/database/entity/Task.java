package com.avdeev.docs.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey
    public String id;
    public String title;
    public String author;
    public String priority;
    public String type;
    public String number;
    public String assignee;
    public String document_type;
    public String description;
    @ColumnInfo(name = "date_due")
    public long dateDue;
    public long date;
    @ColumnInfo(name = "updated_at")
    public long updatedAt;
}

/*
tle TEXT, " +
                "author TEXT, " +
                "priority NUMERIC, " +
                "type TEXT, " +
                "number TEXT, " +
                "assignee TEXT, " +
                "document_type TEXT, " +
                "description TEXT, " +
                "date_due NUMERIC, " +
                "date NUMERIC, " +
                "updated_at NUMERIC)");
 */
