package com.avdeev.docs.core;

import androidx.annotation.NonNull;

public class Document extends Object {

    private String name;
    private String author;
    private String id;
    private String number;
    private String updated_at;
    private String type;

    public Document(String id, String name, String author) {

        this.id = id;
        this.name = name;
        this.author = author;
        number = "";
        updated_at = "";
        type = "";
    }

    public Document(@NonNull Document document) {

        this.id = document.getId();
        this.name = document.getName();
        this.author = document.getAuthor();
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }
}
