package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DocumentInner {
    @PrimaryKey @NonNull public String id;
    public String title;
    public String author;
    public String type;
    public String number;
    public long updated_at;
    public long date;

    public DocumentInner(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
