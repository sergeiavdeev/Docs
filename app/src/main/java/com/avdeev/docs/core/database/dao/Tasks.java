package com.avdeev.docs.core.database.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.database.entity.File;
import com.avdeev.docs.core.database.entity.TaskWithFiles;

import java.util.List;

@Dao
public abstract class Tasks {

    @Transaction
    @Query("SELECT * FROM Task")
    public abstract List<TaskWithFiles> getAll();

    @Transaction
    public void clear() {
        clearTasks();
        clearFiles();
    }

    @Query("DELETE FROM File")
    protected abstract void clearFiles();

    @Query("DELETE FROM Task")
    protected abstract void clearTasks();

    @Transaction
    public void add(TaskWithFiles task) {
        addTask(task.task);
        deleteFiles(task.task.id);
        for (File file : task.files) {
            file.taskId = task.task.id;
        }
        addFiles(task.files);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract void addTask(Task task);

    @Query("DELETE FROM File WHERE task_id=:owner")
    protected abstract void deleteFiles(String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract void addFiles(List<File> files);

    @Transaction
    public void delete(String id) {
        deleteTask(id);
        deleteFiles(id);
    }

    @Query("DELETE FROM Task WHERE id=:id")
    protected abstract void deleteTask(String id);

    @Query("SELECT * FROM Task ORDER BY updated_at DESC")
    public abstract DataSource.Factory<Integer, Task>taskByDate();

    @Transaction
    @Query("SELECT * FROM Task " +
            "WHERE " +
            "title LIKE '%' || :search || '%' OR " +
            "author LIKE '%' || :search || '%' OR " +
            "number LIKE '%' || :search || '%' " +
            "ORDER BY updated_at DESC")
    public abstract DataSource.Factory<Integer, TaskWithFiles>taskByDate(String search);
}
