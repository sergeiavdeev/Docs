package com.avdeev.docs.ui.task;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.dao.Tasks;
import com.avdeev.docs.core.database.entity.TaskWithFiles;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.TasksResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskViewModel extends DocAppModel {

    private Tasks tasksDao;
    public LiveData<PagedList<TaskWithFiles>> taskList;

    public TaskViewModel(Application app) {
        super(app);

        this.tasksDao = DocDatabase.getInstance().task();
        taskList = new LivePagedListBuilder<>(tasksDao.taskByDate(""), 50).build();
    }

    public void search(LifecycleOwner lifecycleOwner, String search) {
        taskList.removeObservers(lifecycleOwner);
        taskList = new LivePagedListBuilder<>(tasksDao.taskByDate(search), 50).build();
    }

    public void updateTasksFromNetwork() {

        setWait(true);

        NetworkService.getInstance()
                .getApi()
                .getTasks()
                .enqueue(new Callback<TasksResponse>() {
                    @Override
                    public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                        setWait(false);
                        List<Task> tasks = response.body().tasks;

                        if (tasks != null) {

                            for (Task task:tasks) {

                                TaskWithFiles taskWithFiles = TaskWithFiles.create(task);

                                DocDatabase db = DocDatabase.getInstance();
                                DocDatabase.executor.execute(() -> {
                                    db.task().add(taskWithFiles);
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TasksResponse> call, Throwable t) {
                        setWait(false);
                    }
                });
    }
}