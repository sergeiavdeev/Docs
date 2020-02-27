package com.avdeev.docs.core.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.avdeev.docs.core.database.entity.DocumentInner;
import java.util.List;

@Dao
public interface DocInner {

    @Query("SELECT * FROM DocumentInner")
    List<DocumentInner> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(DocumentInner document);

    @Query("DELETE FROM DocumentInner WHERE id=:id")
    void delete(String id);

    @Query("DELETE FROM DocumentInner")
    void clear();
}
