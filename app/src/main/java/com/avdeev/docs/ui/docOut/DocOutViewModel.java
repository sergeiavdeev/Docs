package com.avdeev.docs.ui.docOut;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.dao.DocOutbox;
import com.avdeev.docs.core.database.entity.DocumentOutbox;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.DocumentsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocOutViewModel extends DocAppModel {

    private DocOutbox docOutboxDao;
    public LiveData<PagedList<DocumentOutbox>> docList;

    public DocOutViewModel(Application app) {
        super(app);
        docOutboxDao = DocDatabase.getInstance().outbox();
        docList = new LivePagedListBuilder<>(docOutboxDao.documentsByDate(""), 50).build();
    }

    public void search(LifecycleOwner lifecycleOwner, String search) {

        docList.removeObservers(lifecycleOwner);
        docList = new LivePagedListBuilder<>(docOutboxDao.documentsByDate(search), 50).build();
    }

    public void updateFromNetwork() {
        setWait(true);
        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(() -> {
            long lastUpdateTime = db.outbox().getLastUpdateTime();
            NetworkService.getInstance().getApi()
                    .getDocumentsOutbox(lastUpdateTime)
                    .enqueue(new Callback<DocumentsResponse<DocumentOutbox>>() {
                        @Override
                        public void onResponse(Call<DocumentsResponse<DocumentOutbox>> call, Response<DocumentsResponse<DocumentOutbox>> response) {
                            setWait(false);
                            if (response.isSuccessful()) {
                                addDocsToDatabase(response.body().documents);
                            }
                        }

                        @Override
                        public void onFailure(Call<DocumentsResponse<DocumentOutbox>> call, Throwable t) {
                            setWait(false);
                            t.printStackTrace();
                        }
                    });
        });
    }

    private void addDocsToDatabase(List<DocumentOutbox> documents) {
        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(()->{
            db.outbox().add(documents);
            if (documents.size() >= NetworkService.API_PAGE_SIZE) {
                updateFromNetwork();
            }
        });
    }
}