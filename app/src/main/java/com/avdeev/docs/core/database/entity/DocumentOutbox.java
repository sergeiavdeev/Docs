package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DocumentOutbox {
    @PrimaryKey @NonNull public String id;
    public String title;
    public String author;
    public String type;
    public String number;
    public long updated_at;
    public long date;

    public DocumentOutbox(String id, String title) {
        this.id = id;
        this.title = title;
    }

    protected boolean equals(DocumentOutbox documentInbox) {
        return id.equals(documentInbox.id)&&
                title.equals(documentInbox.title)&&
                author.equals(documentInbox.author)&&
                type.equals(documentInbox.type)&&
                number.equals(documentInbox.number)&&
                updated_at == documentInbox.updated_at&&
                date == documentInbox.date;
    }

    public static DiffUtil.ItemCallback<DocumentOutbox> DIFF_UTIL = new DiffUtil.ItemCallback<DocumentOutbox>() {

        @Override
        public boolean areItemsTheSame(DocumentOutbox oldDOc, DocumentOutbox newDoc) {
            return oldDOc.id.equals(newDoc.id);
        }

        @Override
        public boolean areContentsTheSame(DocumentOutbox oldDOc, DocumentOutbox newDoc) {
            return oldDOc.equals(newDoc);
        }
    };
}
