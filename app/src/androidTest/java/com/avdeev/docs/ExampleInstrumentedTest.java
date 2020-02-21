package com.avdeev.docs;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.avdeev.docs.core.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.avdeev.docs", appContext.getPackageName());
    }

    @Test
    public void dateFromLong() {

        long date = 1569583741;
        String dateString = User.dateFromLong(date);
        assertEquals(dateString, "27.09.2019");

        date = 0;
        dateString = User.dateFromLong(date);
        assertEquals(dateString, "(не указано)");
    }
}
