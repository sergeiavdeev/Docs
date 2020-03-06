package com.avdeev.docs.core.network.pojo;

import android.text.format.DateFormat;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;

public class BaseDocument extends Object implements Serializable {

    public String id;
    public String title;
    public String author;
    public String type;
    public String number;
    public long updated_at;
    public long date;

    public BaseDocument() {

    }

    @NotNull
    public static String dateFromLong(long l) {

        String date = "(не указано)";
        if (l > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(l * 1000L);
            date = DateFormat.format("dd.MM.yyyy", calendar).toString();
        }
        return  date;
    }
}
