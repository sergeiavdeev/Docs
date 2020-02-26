package com.avdeev.docs.ui.task.action;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Task;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.CommonResponse;
import com.avdeev.docs.core.network.pojo.TaskActionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActionViewModel extends DocAppModel {

    private MutableLiveData<Boolean> actionComplete;
    private Task task;
    private String comment;

    public TaskActionViewModel(Application app) {
        super(app);
        actionComplete = new MutableLiveData<>(false);
        comment = "";
    }

    public LiveData<Boolean> getActionComplete() {
        return actionComplete;
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

        NetworkService.getInstance("https://sed.rudn.ru/BGU_DEMO/hs/DGU_APP_Mobile_Client/")
                .getApi()
                .postTaskAction(new TaskActionRequest(task.getId(), action, comment))
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        setWait(false);
                        CommonResponse r = response.body();

                        if (r.getSuccess() == 1) {
                            actionComplete.setValue(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        setWait(false);
                    }
                });

        actionComplete.setValue(true);
    }

    private void deleteTask() {

    }
}
