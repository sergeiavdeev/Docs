package com.avdeev.docs.ui.docIn;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;

public class DocInViewModel extends DocAppModel {

    private MutableLiveData<Document[]> mDocList;

    public DocInViewModel(Application app) {
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

                Document[] documents = user.getDocInList();
                if (documents.length == 0) {
                    user.updateDocList("inbox");
                    documents = user.getDocInList();
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

                user.updateDocList("inbox");
                return user.getDocInList();
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