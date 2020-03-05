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

        NetworkService.getInstance().getApi()
                .getDocumentsInbox( 0)
                .enqueue(new Callback<DocumentsResponse<DocumentInbox>>() {
                    @Override
                    public void onResponse(Call<DocumentsResponse<DocumentInbox>> call, Response<DocumentsResponse<DocumentInbox>> response) {
                        setWait(false);
                        if (response.isSuccessful()) {
                            DocDatabase.executor.execute(()->{
                                DocDatabase.getInstance().inbox().add(response.body().documents);
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<DocumentsResponse<DocumentInbox>> call, Throwable t) {
                        setWait(false);
                        t.printStackTrace();
                    }
                });
    }
}