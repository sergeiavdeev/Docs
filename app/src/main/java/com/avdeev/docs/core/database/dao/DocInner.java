package com.avdeev.docs.core.database.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.avdeev.docs.core.database.entity.DocumentInner;
import java.util.List;

@Dao
public interface DocInner {

    @Query("SELECT * FROM DocumentInner")
    List<DocumentInner> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(DocumentInner document);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<DocumentInner> documents);

    @Query("DELETE FROM DocumentInner WHERE id=:id")
    void delete(String id);

    @Query("DELETE FROM DocumentInner")
    void clear();

    @Query("SELECT * FROM DocumentInner " +
            "WHERE " +
            "title LIKE '%' || :search || '%' OR " +
            "author LIKE '%' || :search || '%' OR " +
            "number LIKE '%' || :search || '%' " +
            "ORDER BY updated_at DESC")
    DataSource.Factory<Integer, DocumentInner>documentsByDate(String search);

    @Query("SELECT COUNT(id) FROM DocumentInner")
    long getCount();

    @Query("SELECT MAX(updated_at) FROM DocumentInner")
    long getLastUpdateTime();
}
