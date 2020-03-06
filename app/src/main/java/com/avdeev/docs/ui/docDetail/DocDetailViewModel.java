package com.avdeev.docs.ui.docDetail;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.Document;
import com.avdeev.docs.core.network.pojo.DocumentResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocDetailViewModel extends DocAppModel {

    protected MutableLiveData<Document> document;
    private MutableLiveData<Boolean> filesVisible;
    private MutableLiveData<Boolean> moreVisible;

    public DocDetailViewModel(Application app) {
        super(app);

        document = new MutableLiveData<>();
        document.setValue(new Document());

        filesVisible = new MutableLiveData<>();
        filesVisible.setValue(false);

        moreVisible = new MutableLiveData<>();
        moreVisible.setValue(false);
    }

    public void setDocument(Document document) {
        this.document.setValue(document);
    }

    public LiveData<Document>getDocument() {

        return document;
    }

    public LiveData<Boolean> getFileVisible() {
        return filesVisible;
    }

    public LiveData<Boolean> getMoreVisible() {
        return moreVisible;
    }

    public void updateDocument(final String type) {

        setWait(true);
        NetworkService.getInstance().getApi()
                .getDocument(type, document.getValue().id)
                .enqueue(new Callback<DocumentResponse>() {
                    @Override
                    public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
                        setWait(false);
                        if (response.isSuccessful()) {
                            document.setValue(response.body().document);
                        }
                    }

                    @Override
                    public void onFailure(Call<DocumentResponse> call, Throwable t) {
                        setWait(false);
                    }
                });
    }

    public void changeFileVisible() {

        boolean visible = filesVisible.getValue();
        filesVisible.setValue(!visible);
    }

    public void changeMoreVisible() {

        boolean visible = moreVisible.getValue();
        moreVisible.setValue(!visible);
    }
}
