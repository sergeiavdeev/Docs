package com.avdeev.docs.core.database.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.avdeev.docs.core.database.entity.DocumentInbox;
import com.avdeev.docs.core.database.entity.TaskWithFiles;

import java.util.List;

@Dao
public abstract class DocInbox {

    @Query("SELECT * FROM DocumentInbox")
    public abstract List<DocumentInbox> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void add(List<DocumentInbox> documents);

    @Query("DELETE FROM DocumentInbox WHERE id=:id")
    public abstract void delete(String id);

    @Query("DELETE FROM DocumentInbox")
    public abstract void clear();

    @Query("SELECT * FROM DocumentInbox " +
            "WHERE " +
            "title LIKE '%' || :search || '%' OR " +
            "author LIKE '%' || :search || '%' OR " +
            "number LIKE '%' || :search || '%' " +
            "ORDER BY updated_at DESC")
    public abstract DataSource.Factory<Integer, DocumentInbox>documentsByDate(String search);
}
