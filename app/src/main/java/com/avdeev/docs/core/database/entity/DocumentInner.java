package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
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

    protected boolean equals(DocumentInner documentInbox) {
        return id.equals(documentInbox.id)&&
                title.equals(documentInbox.title)&&
                author.equals(documentInbox.author)&&
                type.equals(documentInbox.type)&&
                number.equals(documentInbox.number)&&
                updated_at == documentInbox.updated_at&&
                date == documentInbox.date;
    }

    public static DiffUtil.ItemCallback<DocumentInner> DIFF_UTIL = new DiffUtil.ItemCallback<DocumentInner>() {

        @Override
        public boolean areItemsTheSame(DocumentInner oldDOc, DocumentInner newDoc) {
            return oldDOc.id.equals(newDoc.id);
        }

        @Override
        public boolean areContentsTheSame(DocumentInner oldDOc, DocumentInner newDoc) {
            return oldDOc.equals(newDoc);
        }
    };
}
