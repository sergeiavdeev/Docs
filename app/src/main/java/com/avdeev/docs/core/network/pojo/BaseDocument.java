package com.avdeev.docs.core.network.pojo;

import android.text.format.DateFormat;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;

public class BaseDocument extends Object implements Serializable {
    protected String id;
    protected String title;
    protected String description;

    public BaseDocument() {
        id = "";
        title = "";
        description = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
