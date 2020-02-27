package com.avdeev.docs.core.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.avdeev.docs.core.database.entity.DocumentInbox;

import java.util.List;

@Dao
public interface DocInbox {

    @Query("SELECT * FROM DocumentInbox")
    List<DocumentInbox> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(DocumentInbox document);

    @Query("DELETE FROM DocumentInbox WHERE id=:id")
    void delete(String id);

    @Query("DELETE FROM DocumentInbox")
    void clear();
}
