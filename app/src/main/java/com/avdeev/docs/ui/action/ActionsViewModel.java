package com.avdeev.docs.ui.action;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.Action;
import com.avdeev.docs.core.ActionRequest;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.network.pojo.HistoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionsViewModel extends DocAppModel {

    private MutableLiveData<List<Action>> actions;
    private String actionType;
    private String ownerId;
    private String ownerType;

    public ActionsViewModel(Application app) {
        super(app);

        actions = new MutableLiveData<>();
    }

    public ActionsViewModel init(String actionType, String ownerId, String ownerType) {
        this.actionType = actionType;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        return this;
    }

    public LiveData<List<Action>> getActions() {
        return actions;
    }

    public void updateActions(final ActionRequest request) {

        wait.setValue(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                return appUser.getActions(request);
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                actions.setValue((ArrayList<Action>)result);
            }

        }.execute();
    }

    public void updateActions() {
        if (ownerType.equals("task")) {
            getTaskHistory(ownerId);
        } else {
            getDocumentAction(actionType, ownerType, ownerId);
        }
    }

    private void getTaskHistory(String id) {
        setWait(true);
        NetworkService.getInstance().getApi()
        .getHistory(id)
        .enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                onReciveActions(response);
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                setWait(false);
                t.printStackTrace();
            }
        });
    }

    private void getDocumentAction(String actionType, String ownerType, String ownerId) {
        if (actionType.equals("history")) {
            getDocumentHistory(ownerType, ownerId);
        }
    }

    private void getDocumentHistory(String type, String id) {

    }

    private void onReciveActions(Response<HistoryResponse> response) {
        setWait(false);
        if (response.isSuccessful()) {
            actions.setValue(response.body().history);
        }
    }
}
