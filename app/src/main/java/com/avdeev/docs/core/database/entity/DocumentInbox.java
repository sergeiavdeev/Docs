package com.avdeev.docs.core.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DocumentInbox {
    @PrimaryKey
    public String id;

    public String title;
    public String type;
    public String number;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public long date;
}
