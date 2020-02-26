package com.avdeev.docs;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.entity.ApiPathHistory;
import com.avdeev.docs.core.database.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        db = Room.databaseBuilder(appContext, DocDatabase.class, "docs").build();
    }

    @Test
    public void dbCreate() {

        assertEquals(false, db==null);
    }

    @Test
    public void userAddAndClear() {

        db.users().add(new User("abcde", "https://"));
        User user = db.users().getOne();
        assertEquals(user.token, "abcde");
        assertEquals(user.apiPath, "https://");
        db.users().clear();
        user = db.users().getOne();
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

    @After
    public void closeDb() {
        if (db.isOpen()) {
            db.close();
        }
    }
}
