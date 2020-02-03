package com.avdeev.docs.core;

import org.json.JSONObject;

public class File extends Object {

    private String name;
    private String type;
    private String id;
    private long size;

    public File() {

        name = "";
        type = "";
        id = "";
        size = 0;
    }

    public File(JSONObject object) throws Exception {

        id = object.getString("id");
        name = object.getString("name");
        type = object.getString("type");
        size = object.getLong("size");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
