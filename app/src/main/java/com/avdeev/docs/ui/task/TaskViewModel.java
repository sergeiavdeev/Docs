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
        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(() -> {
            long lastUpdateTime = db.task().getLastUpdateTime();
            NetworkService.getInstance()
                    .getApi()
                    .getTasks(lastUpdateTime)
                    .enqueue(new Callback<TasksResponse>() {

                        @Override
                        public void onResponse(Call<TasksResponse> call, Response<TasksResponse> response) {
                            setWait(false);
                            List<Task> tasks = response.body().tasks;
                            if (tasks != null) {
                                addTasksToDatabase(tasks);
                            }
                        }

                        @Override
                        public void onFailure(Call<TasksResponse> call, Throwable t) {
                            setWait(false);
                        }
                    });
        });
    }

    private void addTasksToDatabase(List<Task> tasks) {

        DocDatabase db = DocDatabase.getInstance();
        for (Task task:tasks) {
            TaskWithFiles taskWithFiles = TaskWithFiles.create(task);
            DocDatabase.executor.execute(() -> {
                db.task().add(taskWithFiles);
            });
        }

        if (tasks.size() >= NetworkService.API_PAGE_SIZE) {
            updateTasksFromNetwork();
        }
    }
}