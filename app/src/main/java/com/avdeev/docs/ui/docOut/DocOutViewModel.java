package com.avdeev.docs.ui.docOut;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;

import java.util.ArrayList;

public class DocOutViewModel extends DocAppModel {

    private MutableLiveData<ArrayList<Document>> mDocList;

    public DocOutViewModel(Application app) {
        super(app);

        mDocList = new MutableLiveData<>();
        mDocList.setValue(new ArrayList<Document>());
    }

    public LiveData<ArrayList<Document>> getDocList() {
        return mDocList;
    }

    public void getList() {

        wait.setValue(true);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                ArrayList<Document> documents = user.getDocOutList();
                if (documents.size() == 0) {
                    user.updateDocList("outbox");
                    documents = user.getDocOutList();
                }

                return documents;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                wait.setValue(false);
                mDocList.setValue((ArrayList<Document>) result);
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
                mDocList.setValue((ArrayList<Document>) result);
            }

        }.execute();
    }
}