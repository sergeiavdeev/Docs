package com.avdeev.docs.ui.docOut;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.network.pojo.Document;
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

    }

    public void updateList() {

    }

    public void search(String searchText) {

        wait.setValue(true);
        DocListAdapter adapter = docListAdapter.getValue();
        adapter.getFilter().filter(searchText);
        wait.setValue(false);
    }


}