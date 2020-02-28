package com.avdeev.docs.ui.start;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avdeev.docs.core.AppUser;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.entity.User;

public class StartViewModel extends DocAppModel {

    private MutableLiveData<Boolean> complete;

    public StartViewModel(Application app) {
        super(app);
        complete = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> isComplete() {
        return complete;
    }

    public void init() {

        DocDatabase db = DocDatabase.getInstance();
        AppUser.getInstance();
        DocDatabase.executor.execute(() -> {
            User user = db.user().getOne();
            if (user == null) {
                db.user().add(new User("", ""));
                user = db.user().getOne();
            }
            AppUser.setKey(user.key);
            AppUser.setApiUrl(user.apiUrl);
            AppUser.setHash(user.hash);
            complete.postValue(true);
        });
    }

}
