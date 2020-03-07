package com.avdeev.docs.core.database.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.avdeev.docs.core.database.entity.DocumentOutbox;
import java.util.List;

@Dao
public interface DocOutbox {

    @Query("SELECT * FROM DocumentOutbox")
    List<DocumentOutbox> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(DocumentOutbox document);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<DocumentOutbox> documents);

    @Query("DELETE FROM DocumentOutbox WHERE id=:id")
    void delete(String id);

    @Query("DELETE FROM DocumentOutbox")
    void clear();

    @Query("SELECT * FROM DocumentOutbox " +
            "WHERE " +
            "title LIKE '%' || :search || '%' OR " +
            "author LIKE '%' || :search || '%' OR " +
            "number LIKE '%' || :search || '%' " +
            "ORDER BY updated_at DESC")
    DataSource.Factory<Integer, DocumentOutbox>documentsByDate(String search);

    @Query("SELECT COUNT(id) FROM DocumentOutbox")
    long getCount();

    @Query("SELECT MAX(updated_at) FROM DocumentOutbox")
    long getLastUpdateTime();
}
