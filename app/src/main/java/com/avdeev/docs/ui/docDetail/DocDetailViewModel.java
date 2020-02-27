package com.avdeev.docs.ui.docDetail;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;

public class DocDetailViewModel extends DocAppModel {

    protected MutableLiveData<Document> document;
    private MutableLiveData<Boolean> filesVisible;
    private MutableLiveData<Boolean> moreVisible;

    public DocDetailViewModel(Application app) {
        super(app);

        document = new MutableLiveData<>();
        document.setValue(new Document("", "", ""));

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

        wait.setValue(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                return appUser.updateDocument(document.getValue(), type);
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                document.setValue((Document)result);
            }

        }.execute();
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
