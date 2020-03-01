package com.avdeev.docs.ui.login;

import android.app.Application;
import com.avdeev.docs.core.AppUser;
import com.avdeev.docs.core.DocAppModel;
import com.avdeev.docs.core.database.DocDatabase;
import com.avdeev.docs.core.database.entity.User;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.CommonResponse;
import com.avdeev.docs.core.network.pojo.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends DocAppModel {

    private String login;
    private String password;

    public LoginViewModel(Application app) {
        super(app);
        login = "Игнатьев Олег Владимирович";
        password = "gbrn03121965sed";
    }

    public void auth() {
        setWait(true);
        NetworkService.getInstance(AppUser.getApiUrl())
                .setAuthKey(AppUser.getKey())
                .setPasswordHashFromPassword(password)
                .getApi()
                .auth(new Login(login))
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        setWait(false);
                        CommonResponse r = response.body();
                        if (r.getSuccess() == 1) {
                            appUser.setAuth(true);
                            saveUserToDatabase();
                        } else {
                            error.setValue(true);
                            errorMessage.setValue("Не верное имя пользователя или пароль");
                            setWait(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        setWait(false);
                        error.setValue(true);
                        errorMessage.setValue("Ошибка работы с интернет");
                    }
                });
    }

    private void saveUserToDatabase() {

        DocDatabase db = DocDatabase.getInstance();
        DocDatabase.executor.execute(() -> {
            db.user().add(new User(AppUser.getHash(), AppUser.getApiUrl()));
            complete.postValue(true);
            auth.postValue(true);
        });
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
