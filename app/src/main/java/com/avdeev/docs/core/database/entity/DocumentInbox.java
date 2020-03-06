package com.avdeev.docs.core.database.entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
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
    public long updated_at;
    public long date;

    public DocumentInbox(String id, String title) {
        this.id = id;
        this.title = title;
    }

    protected boolean equals(DocumentInbox documentInbox) {
        return id.equals(documentInbox.id)&&
                title.equals(documentInbox.title)&&
                author.equals(documentInbox.author)&&
                type.equals(documentInbox.type)&&
                number.equals(documentInbox.number)&&
                updated_at == documentInbox.updated_at&&
                date == documentInbox.date;
    }

    public static DiffUtil.ItemCallback<DocumentInbox> DIFF_UTIL = new DiffUtil.ItemCallback<DocumentInbox>() {

        @Override
        public boolean areItemsTheSame(DocumentInbox oldDOc, DocumentInbox newDoc) {
            return oldDOc.id.equals(newDoc.id);
        }

        @Override
        public boolean areContentsTheSame(DocumentInbox oldDOc, DocumentInbox newDoc) {
            return oldDOc.equals(newDoc);
        }
    };
}
