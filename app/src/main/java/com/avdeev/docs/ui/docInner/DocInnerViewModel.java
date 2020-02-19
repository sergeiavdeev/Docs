package com.avdeev.docs.ui.docInner;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.ui.listAdapters.DocListAdapter;

import java.util.ArrayList;

public class DocInnerViewModel extends DocAppModel {

    private MutableLiveData<DocListAdapter> docListAdapter;

    public DocInnerViewModel(Application app) {
        super(app);
        docListAdapter = new MutableLiveData<>();
        docListAdapter.setValue(new DocListAdapter(getContext(), new ArrayList<Document>()));
    }

    public LiveData<DocListAdapter> getDocListAdapter() {
        return docListAdapter;
    }

    public void getList() {
        new DocListLoader().execute();
    }

    public void updateList() {
        new DocListUpdater().execute();
    }

    public void search(String searchText) {
        wait.setValue(true);
        DocListAdapter adapter = docListAdapter.getValue();
        adapter.getFilter().filter(searchText);
        wait.setValue(false);
    }

    private class DocListLoader extends BaseAsyncTask<ArrayList<Document>> {

        @Override
        protected ArrayList<Document> process() {

            ArrayList<Document> documents = user.getDocInnerList();
            if (documents.size() == 0) {
                int count = 5000;
                while(count == 5000) {
                    count = user.updateDocList("internal");
                }
                documents = user.getDocInnerList();
            }
            return documents;
        }

        @Override
        protected void onPostProcess(ArrayList<Document> documentList, Context context) {
            docListAdapter.setValue(DocListAdapter.create(context, documentList));
        }
    }

    private class DocListUpdater extends BaseAsyncTask<ArrayList<Document>> {

        @Override
        protected ArrayList<Document> process() {

            user.updateDocList("internal");
            return user.getDocInnerList();
        }

        @Override
        protected void onPostProcess(ArrayList<Document> documentList, Context context) {
            docListAdapter.setValue(DocListAdapter.create(context, documentList));

        }
    }
}
