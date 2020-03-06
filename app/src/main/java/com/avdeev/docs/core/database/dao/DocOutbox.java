package com.avdeev.docs.core.database.dao;

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

    @Query("DELETE FROM DocumentOutbox WHERE id=:id")
    void delete(String id);

    @Query("DELETE FROM DocumentOutbox")
    void clear();

    @Query("SELECT COUNT(id) FROM DocumentOutbox")
    long getCount();

    @Query("SELECT MAX(updated_at) FROM DocumentOutbox")
    long getLastUpdateTime();
}
