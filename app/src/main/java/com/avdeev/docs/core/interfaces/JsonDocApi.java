package com.avdeev.docs.core.interfaces;

import com.avdeev.docs.core.network.pojo.AuthRequest;
import com.avdeev.docs.core.network.pojo.DocumentResponse;
import com.avdeev.docs.core.network.pojo.DocumentsResponse;
import com.avdeev.docs.core.network.pojo.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonDocApi {

    @POST("/BGU_DEMO/hs/DGU_APP_Mobile_Client/auth")
    Call<AuthRequest> auth(@Body Login login);

    @GET("/BGU_DEMO/hs/DGU_APP_Mobile_Client/documents")
    Call<DocumentsResponse> getDocuments(@Query("chapter") String chapter, @Query("timestamp") long timestamp);

    @GET("/BGU_DEMO/hs/DGU_APP_Mobile_Client/documents/details")
    Call<DocumentResponse> getDocument(@Query("chapter") String chapter, @Query("id") String id);
}
