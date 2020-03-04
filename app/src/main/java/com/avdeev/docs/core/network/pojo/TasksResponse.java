package com.avdeev.docs.core.network.pojo;

import com.avdeev.docs.core.database.entity.Task;

import java.util.List;

public class TasksResponse {

    public List<Task> tasks;
    public List<String> delete_ids;
}
