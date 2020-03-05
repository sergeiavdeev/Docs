package com.avdeev.docs;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.avdeev.docs.core.AppUser;
import com.avdeev.docs.core.database.entity.DocumentInbox;
import com.avdeev.docs.core.network.NetworkService;
import com.avdeev.docs.core.network.pojo.DocumentsResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Response;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class JsonApiTest {

    @Before
    public void init() {

        AppUser.getInstance();
        AppUser.setApiUrl("https://sed.rudn.ru/BGU_DEMO/hs/DGU_APP_Mobile_Client/");
        //AppUser.setHash("5b2eb333802480cff356377f725f7b76b9dfc1f9");
        AppUser.setHash("erter");
        AppUser.setKey("7533ea90-6538-4f3b-8542-8130a193ad66");
        AppUser.setAuth(true);

        NetworkService.getInstance(AppUser.getApiUrl())
                .setAuthKey(AppUser.getKey())
                .setPasswordHash(AppUser.getHash());

    }

    @Test
    public void getDocList() {

        Response<DocumentsResponse<DocumentInbox>> r;
        try {
            r = NetworkService.getInstance()
                    .getApi()
                    .getDocumentsInbox(0)
                    .execute();
        } catch (Exception e) {
            r = null;
            e.printStackTrace();
        }
        if (r!=null && r.isSuccessful()) {
            assertTrue(r.body().documents != null);
        } else {
            assertTrue(false);
        }
    }

    @After
    public void after() {


    }
}
