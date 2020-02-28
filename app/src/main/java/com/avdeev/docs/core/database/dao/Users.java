package com.avdeev.docs.core.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.avdeev.docs.core.database.entity.User;

import java.util.UUID;

@Dao
public abstract class Users {

    @Query("SELECT * FROM user LIMIT 1")
    public abstract User getOne();

    @Query("DELETE FROM user")
    public abstract void clear();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void addUser(User user);

    @Transaction
    public void add(User user) {
        User existUser = getOne();
        if (existUser == null) {
            existUser = new User(user.hash, user.apiUrl);
            existUser.key = UUID.randomUUID().toString();
        }
        existUser.hash = user.hash;
        existUser.apiUrl = user.apiUrl;
        addUser(existUser);
    }
}
