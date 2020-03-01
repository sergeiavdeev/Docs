package com.avdeev.docs.ui.settings;

import android.app.Application;
import android.view.View;

import com.avdeev.docs.core.AppUser;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.entity.User;

public class SettingsViewModel extends DocAppModel {

    private String apiUrl;

    public SettingsViewModel(Application app) {
        super(app);
        apiUrl = AppUser.getApiUrl();

        if (AppUser.getApiUrl().length() == 0) {
            apiUrl = "https://sed.rudn.ru/BGU_DEMO/hs/DGU_APP_Mobile_Client/";
        }
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void onSaveClick(View view) {

        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(() -> {
            db.user().add(new User("", apiUrl));
            AppUser.setApiUrl(apiUrl);
            complete.postValue(true);
        });

    }
}
