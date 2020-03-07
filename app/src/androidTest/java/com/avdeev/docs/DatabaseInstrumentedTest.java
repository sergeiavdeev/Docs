package com.avdeev.docs;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.entity.ApiPathHistory;
import com.avdeev.docs.core.database.entity.DocumentInbox;
import com.avdeev.docs.core.database.entity.DocumentInner;
import com.avdeev.docs.core.database.entity.DocumentOutbox;
import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.database.entity.File;
import com.avdeev.docs.core.database.entity.TaskWithFiles;
import com.avdeev.docs.core.database.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {

    private DocDatabase db;

    @Before
    public void init() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(appContext, DocDatabase.class).build();

    }

    @Test
    public void dbCreate() {

        assertEquals(false, db==null);
    }

    @Test
    public void userFull() {

        db.user().add(new User("abcde", "https://"));
        User user = db.user().getOne();
        assertEquals(user.hash, "abcde");
        assertEquals(user.apiUrl, "https://");
        assertEquals(user.key.length() > 0, true);

        db.user().add(new User("effff", "https://d"));
        user = db.user().getOne();
        assertEquals(user.hash, "effff");
        assertEquals(user.apiUrl, "https://d");

        db.user().clear();
        user = db.user().getOne();
        assertEquals(user, null);
    }

    @Test
    public void apiPathAddAndGet() {

        db.apiPath().add(new ApiPathHistory("https://abc"));
        db.apiPath().add(new ApiPathHistory("https://def"));

        List<ApiPathHistory> history = db.apiPath().getAll();
        assertEquals(history.size(), 2);
        assertEquals(history.get(0).apiPath, "https://abc");

        db.apiPath().clear();
        history = db.apiPath().getAll();
        assertEquals(history.size(), 0);
    }

    @Test
    public void docInFull() {


    }

    @Test
    public void docOutFull() {

        db.outbox().add(new DocumentOutbox("1", "Жопа"));
        db.outbox().add(new DocumentOutbox("2", "Ручка"));

        assertEquals(db.outbox().getAll().size(), 2);
        assertEquals(db.outbox().getAll().get(0).title, "Жопа");
        assertEquals(db.outbox().getAll().get(1).title, "Ручка");

        db.outbox().add(new DocumentOutbox("2", "Жопа с ручкой"));
        assertEquals(db.outbox().getAll().size(), 2);
        assertEquals(db.outbox().getAll().get(1).title, "Жопа с ручкой");

        db.outbox().delete("1");
        assertEquals(db.outbox().getAll().get(0).id, "2");

        db.outbox().clear();
        assertEquals(db.outbox().getAll().size(), 0);
    }

    @Test
    public void docInnerFull() {

        db.inner().add(new DocumentInner("1", "Жопа"));
        db.inner().add(new DocumentInner("2", "Ручка"));

        assertEquals(db.inner().getAll().size(), 2);
        assertEquals(db.inner().getAll().get(0).title, "Жопа");
        assertEquals(db.inner().getAll().get(1).title, "Ручка");

        db.inner().add(new DocumentInner("2", "Жопа с ручкой"));
        assertEquals(db.inner().getAll().size(), 2);
        assertEquals(db.inner().getAll().get(1).title, "Жопа с ручкой");

        db.inner().delete("1");
        assertEquals(db.inner().getAll().get(0).id, "2");

        db.inner().clear();
        assertEquals(db.inner().getAll().size(), 0);
    }

    @Test
    public void taskFull() {

        Task task = new Task("1", "task 1");
        List<File> files = new ArrayList<>();
        files.add(new File("1", "file 1"));
        files.add(new File("2", "file 2"));
        TaskWithFiles taskWithFiles = new TaskWithFiles();
        taskWithFiles.task = task;
        taskWithFiles.files = files;

        db.task().add(taskWithFiles);

        List<TaskWithFiles> tasks = db.task().getAll();

        assertEquals(tasks.get(0).files.size(), 2);

        task.title = "Task2";
        files.add(new File("3", "file 3"));
        db.task().add(taskWithFiles);
        tasks = db.task().getAll();

        assertEquals(tasks.get(0).task.title, "Task2");
        assertEquals(tasks.get(0).files.size(), 3);

        long count = db.task().getLastUpdateTime();
        assertTrue(count > 0);


        db.task().delete("1");
        tasks = db.task().getAll();
        assertEquals(tasks.size(), 0);

        db.task().clear();
        assertEquals(db.task().getAll().size(), 0);

        count = db.task().getLastUpdateTime();
        assertEquals(count, 0);
    }

    @After
    public void closeDb() {

        if (db.isOpen()) {
            db.close();
        }
    }
}
