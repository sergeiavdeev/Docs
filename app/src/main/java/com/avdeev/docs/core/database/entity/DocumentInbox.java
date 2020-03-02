package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.avdeev.docs.core.network.pojo.Document;

@Entity
public class DocumentInbox {
    @PrimaryKey @NonNull public String id;
    public String title;
    public String author;
    public String type;
    public String number;
    @ColumnInfo(name = "updated_at") public long updatedAt;
    public long date;

    public DocumentInbox(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public DocumentInbox(Document document) {
        id = document.getId();
        title = document.getTitle();
        author = document.getAuthor();
        type = document.getType();
        number = document.getNumber();
        updatedAt = document.getUpdated_at();
        date = document.getDate();
    }
}
