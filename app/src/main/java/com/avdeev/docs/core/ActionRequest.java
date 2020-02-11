package com.avdeev.docs.core;

public class ActionRequest extends Object {

    String type;
    String method;
    String id;
    boolean task;

    public ActionRequest(String method, String type, String id) {

        this.method = method;
        this.type = type;
        this.id = id;
        this.task = false;
    }

    public ActionRequest(String method, String type, String id, boolean task) {

        this.method = method;
        this.type = type;
        this.id = id;
        this.task = task;
    }


}
