package com.avdeev.docs.core.network.pojo;

import android.database.Cursor;

import com.avdeev.docs.core.database.entity.DocumentInbox;
import com.avdeev.docs.core.database.entity.DocumentInner;
import com.avdeev.docs.core.database.entity.DocumentOutbox;
import com.avdeev.docs.core.database.entity.File;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Document extends BaseDocument {

    public long created_at;
    public String description;
    public String addressee;
    public String status;
    public String signee;
    public String department;

    public List<File> files;

    public Document() {
        super();
        created_at = 0;
        addressee = "";
        signee = "";
        department = "";
        files = new ArrayList<>();
    }

    public static Document create(DocumentInbox doc) {
        Document newDoc = new Document();
        newDoc.id = doc.id;
        newDoc.title = doc.title;
        newDoc.author = doc.author;
        newDoc.type = doc.type;
        newDoc.number = doc.number;
        newDoc.updated_at = doc.updated_at;
        newDoc.date = doc.date;
        return newDoc;
    }

    public static Document create(DocumentOutbox doc) {
        Document newDoc = new Document();
        newDoc.id = doc.id;
        newDoc.title = doc.title;
        newDoc.author = doc.author;
        newDoc.type = doc.type;
        newDoc.number = doc.number;
        newDoc.updated_at = doc.updated_at;
        newDoc.date = doc.date;
        return newDoc;
    }

    public static Document create(DocumentInner doc) {
        Document newDoc = new Document();
        newDoc.id = doc.id;
        newDoc.title = doc.title;
        newDoc.author = doc.author;
        newDoc.type = doc.type;
        newDoc.number = doc.number;
        newDoc.updated_at = doc.updated_at;
        newDoc.date = doc.date;
        return newDoc;
    }

}
