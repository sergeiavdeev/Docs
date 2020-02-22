package com.avdeev.docs.ui.docIn;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.ui.listAdapters.DocListAdapter;

import java.util.ArrayList;

public class DocInViewModel extends DocAppModel {

    private MutableLiveData<DocListAdapter> docListAdapter;

    public DocInViewModel(Application app) {
        super(app);
        docListAdapter = new MutableLiveData<>();
        docListAdapter.setValue(new DocListAdapter(getContext(), new ArrayList<Document>()));
    }

    public LiveData<DocListAdapter> getDocListAdapter() {
        return docListAdapter;
    }

    public void loadList() {

        new DocListLoader().execute();
    }

    public void updateList() {

        new DocListUpdater().execute();
    }

    public void search(String searchText) {
        //wait.setValue(true);
        setWait(true);
        DocListAdapter adapter = docListAdapter.getValue();
        adapter.getFilter().filter(searchText);
        //wait.setValue(false);
        setWait(false);
    }

    private class DocListLoader extends BaseAsyncTask<ArrayList<Document>> {

        @Override
        protected ArrayList<Document> process() {

            ArrayList<Document> documents = user.getDocInList();
            if (documents.size() == 0) {
                user.updateDocList("inbox");
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

            user.updateDocList("inbox");
            return user.getDocInList();
        }

        @Override
        protected void onPostProcess(ArrayList<Document> documentList, Context context) {
            docListAdapter.setValue(DocListAdapter.create(context, documentList));
        }
    }
}