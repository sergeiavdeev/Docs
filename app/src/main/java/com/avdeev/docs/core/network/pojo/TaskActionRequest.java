package com.avdeev.docs.core.network.pojo;

import androidx.annotation.Nullable;
import org.jetbrains.annotations.Contract;

public class TaskActionRequest {
    private String id;
    private String action;
    private String comment;

    public final static String ACTION_NO = "no";
    public final static String ACTION_COMMENTS = "comments";

    public TaskActionRequest(String id, String action, String comment) {
        this.id = id;
        this.action = action;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getComment() {
        return comment;
    }
}
