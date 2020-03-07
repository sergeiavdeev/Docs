package com.avdeev.docs.ui.docIn;

import android.app.Application;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.dao.DocInbox;
import com.avdeev.docs.core.database.entity.DocumentInbox;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.DocumentsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocInViewModel extends DocAppModel {

    private DocInbox docInboxDao;
    public LiveData<PagedList<DocumentInbox>> docList;

    public DocInViewModel(Application app) {
        super(app);
        docInboxDao = DocDatabase.getInstance().inbox();
        docList = new LivePagedListBuilder<>(docInboxDao.documentsByDate(""), 50).build();
    }

    public void search(LifecycleOwner lifecycleOwner, String search) {

        docList.removeObservers(lifecycleOwner);
        docList = new LivePagedListBuilder<>(docInboxDao.documentsByDate(search), 50).build();
    }

    public void updateFromNetwork() {
        setWait(true);
        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(() -> {
            long lastUpdateTime = db.inbox().getLastUpdateTime();
            NetworkService.getInstance().getApi()
                    .getDocumentsInbox(lastUpdateTime)
                    .enqueue(new Callback<DocumentsResponse<DocumentInbox>>() {
                        @Override
                        public void onResponse(Call<DocumentsResponse<DocumentInbox>> call, Response<DocumentsResponse<DocumentInbox>> response) {
                            setWait(false);
                            if (response.isSuccessful()) {
                                addDocsToDatabase(response.body().documents);
                            }
                        }

                        @Override
                        public void onFailure(Call<DocumentsResponse<DocumentInbox>> call, Throwable t) {
                            setWait(false);
                            t.printStackTrace();
                        }
                    });
        });
    }

    private void addDocsToDatabase(List<DocumentInbox> documents) {
        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(()->{
            db.inbox().add(documents);
            if (documents.size() >= NetworkService.API_PAGE_SIZE) {
                updateFromNetwork();
            }
        });
    }
}