package com.avdeev.docs.ui.docInner;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;

public class DocInnerViewModel extends DocAppModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<Document[]> mDocList;

    public DocInnerViewModel(Application app) {
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

                Document[] documents = user.getDocInnerList();
                if (documents.length == 0) {
                    int count = 5000;
                    while(count == 5000) {
                        count = user.updateDocList("internal");
                    }
                    documents = user.getDocInnerList();
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

                user.updateDocList("internal");
                return user.getDocInnerList();
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
