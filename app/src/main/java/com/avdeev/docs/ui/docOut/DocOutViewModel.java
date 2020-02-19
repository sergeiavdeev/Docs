package com.avdeev.docs.ui.docOut;

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

public class DocOutViewModel extends DocAppModel {

    private MutableLiveData<DocListAdapter> docListAdapter;

    public DocOutViewModel(Application app) {
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

            ArrayList<Document> documents = user.getDocOutList();
            if (documents.size() == 0) {
                user.updateDocList("outbox");
                documents = user.getDocInList();
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

            user.updateDocList("outbox");
            return user.getDocOutList();
        }

        @Override
        protected void onPostProcess(ArrayList<Document> documentList, Context context) {
            docListAdapter.setValue(DocListAdapter.create(context, documentList));

        }
    }
}