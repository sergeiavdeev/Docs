package com.avdeev.docs.core.network.pojo;

public class TaskActionRequest {
    public String id;
    public String action;
    public String comment;

    public final static String ACTION_NO = "no";
    public final static String ACTION_COMMENTS = "comment";

    public TaskActionRequest(String id, String action, String comment) {
        this.id = id;
        this.action = action;
        this.comment = comment;
    }
}
