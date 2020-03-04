package com.avdeev.docs.core.network.pojo;


import android.database.Cursor;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AppTask extends BaseDocument {

    private String author;
    private long priority;
    private String type;
    private String number;
    private String assignee;
    private String document_type;
    private long date_due;
    private long date;
    private long updated_at;
    private List<AppFile> files;

    public AppTask() {

        author = "";
        priority = 0;
        type = "";
        number = "";
        assignee = "";
        document_type = "";
        date_due = 0;
        date = 0;
        updated_at = 0;
        files = new ArrayList<AppFile>();
    }

    public AppTask(AppTask task) {

        id = task.getId();
        title = task.getTitle();
        author = task.getAuthor();
        priority = task.getPriority();
        type = task.getType();
        number = task.getNumber();
        assignee = task.getAssignee();
        document_type = task.getDocument_type();
        description = task.getDescription();
        date_due = task.getDate_due();
        date = task.getDate();
        updated_at = task.getUpdated_at();
        files = new ArrayList<AppFile>();

        List<AppFile> f = task.getAppFiles();
        for (int i = 0; i < f.size(); i++) {
            files.add(new AppFile(f.get(i)));
        }
    }

    public AppTask(@NotNull JSONObject task) throws Exception {

        id = task.getString("id");
        title = task.getString("title");
        author = task.getString("author");
        priority = task.getLong("priority");
        type = task.getString("type");
        number = task.getString("number");
        assignee = task.getString("assignee");
        document_type = task.getString("document_type");
        description = task.getString("description");
        date_due = task.getLong("date_due");
        date = task.getLong("date");
        updated_at = task.getLong("updated_at");

        JSONArray jFiles = task.getJSONArray("files");

        files = new ArrayList<>();

        for (int i = 0; i < jFiles.length(); i++) {

            files.add(new AppFile(jFiles.getJSONObject(i)));
        }
    }

    public AppTask(@NotNull Cursor cursor, @NotNull Cursor cFiles) throws Exception {

        id = cursor.getString(cursor.getColumnIndex("id"));
        title = cursor.getString(cursor.getColumnIndex("title"));
        author = cursor.getString(cursor.getColumnIndex("author"));
        priority = cursor.getLong(cursor.getColumnIndex("priority"));
        type = cursor.getString(cursor.getColumnIndex("type"));
        number = cursor.getString(cursor.getColumnIndex("number"));
        assignee = cursor.getString(cursor.getColumnIndex("assignee"));
        document_type = cursor.getString(cursor.getColumnIndex("document_type"));
        description = cursor.getString(cursor.getColumnIndex("description"));
        date_due = cursor.getLong(cursor.getColumnIndex("date_due"));
        date = cursor.getLong(cursor.getColumnIndex("date"));
        updated_at = cursor.getLong(cursor.getColumnIndex("updated_at"));

        files = new ArrayList<>();

        if (cFiles.moveToFirst()) {

            do {
                files.add(new AppFile(cFiles));

            }
            while (cFiles.moveToNext());
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public long getDate_due() {
        return date_due;
    }

    public void setDate_due(long date_due) {
        this.date_due = date_due;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public List<AppFile> getAppFiles() {
        return files;
    }

    public void setAppFiles(List<AppFile> appFiles) {
        this.files = appFiles;
    }

}
