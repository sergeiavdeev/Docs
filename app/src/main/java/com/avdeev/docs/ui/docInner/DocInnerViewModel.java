package com.avdeev.docs.ui.docInner;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.dao.DocInner;
import com.avdeev.docs.core.database.entity.DocumentInner;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.DocumentsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocInnerViewModel extends DocAppModel {

    private DocInner docInnerDao;
    public LiveData<PagedList<DocumentInner>> docList;

    public DocInnerViewModel(Application app) {
        super(app);
        docInnerDao = DocDatabase.getInstance().inner();
        docList = new LivePagedListBuilder<>(docInnerDao.documentsByDate(""), 50).build();
    }

    public void search(LifecycleOwner lifecycleOwner, String search) {

        docList.removeObservers(lifecycleOwner);
        docList = new LivePagedListBuilder<>(docInnerDao.documentsByDate(search), 50).build();
    }

    public void updateFromNetwork() {
        setWait(true);
        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(() -> {
            long lastUpdateTime = db.inner().getLastUpdateTime();
            NetworkService.getInstance().getApi()
                    .getDocumentsInternal(lastUpdateTime)
                    .enqueue(new Callback<DocumentsResponse<DocumentInner>>() {
                        @Override
                        public void onResponse(Call<DocumentsResponse<DocumentInner>> call, Response<DocumentsResponse<DocumentInner>> response) {
                            setWait(false);
                            if (response.isSuccessful()) {
                                addDocsToDatabase(response.body().documents);
                            }
                        }

                        @Override
                        public void onFailure(Call<DocumentsResponse<DocumentInner>> call, Throwable t) {
                            setWait(false);
                            t.printStackTrace();
                        }
                    });
        });
    }

    private void addDocsToDatabase(List<DocumentInner> documents) {
        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(()->{
            db.inner().add(documents);
            if (documents.size() >= NetworkService.API_PAGE_SIZE) {
                updateFromNetwork();
            }
        });
    }
}
