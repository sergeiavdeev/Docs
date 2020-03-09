package com.avdeev.docs.ui.task.action;

import android.app.Application;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.entity.Task;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.CommonResponse;
import com.avdeev.docs.core.network.pojo.TaskActionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActionViewModel extends DocAppModel {

    private Task task;
    private String comment;

    public TaskActionViewModel(Application app) {
        super(app);
        comment = "";
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TaskActionViewModel setTask(Task task) {
        this.task = task;
        return this;
    }

    public void postTaskAction(String action) {

        setWait(true);

        NetworkService.getInstance()
                .getApi()
                .postTaskAction(new TaskActionRequest(task.id, action, comment))
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        setWait(false);
                        CommonResponse r = response.body();

                        if (response.isSuccessful() && r.success == 1) {
                            deleteTask();
                        } else {
                            error.setValue(true);
                            errorMessage.setValue("Ошибка работы с интернет");
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        setWait(false);
                    }
                });
    }

    private void deleteTask() {
        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(() -> {
            db.task().delete(task.id);
            complete.postValue(true);
        });
    }
}
