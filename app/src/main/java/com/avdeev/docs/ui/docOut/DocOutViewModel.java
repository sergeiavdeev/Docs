package com.avdeev.docs.ui.docOut;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;

public class DocOutViewModel extends DocAppModel {

    private MutableLiveData<Document[]> mDocList;

    public DocOutViewModel(Application app) {
        super(app);

        mDocList = new MutableLiveData<>();
        mDocList.setValue(new Document[0]);
    }

    public LiveData<Document[]> getDocList() {
        return mDocList;
    }

    public void getList() {

        wait.setValue(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                Document[] documents = user.getDocOutList();
                if (documents.length == 0) {
                    user.updateDocList("outbox");
                    documents = user.getDocOutList();
                }

                return documents;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                mDocList.setValue((Document[])result);
            }

        }.execute();
    }

    public void updateList() {

        wait.setValue(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                user.updateDocList("outbox");
                return user.getDocOutList();
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                mDocList.setValue((Document[])result);
            }

        }.execute();
    }
}