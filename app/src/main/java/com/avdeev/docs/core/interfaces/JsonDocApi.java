package com.avdeev.docs.core.interfaces;

import com.avdeev.docs.core.Document;
import com.avdeev.docs.core.network.pojo.DocumentResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonDocApi {

    @GET("/BGU_DEMO/hs/DGU_APP_Mobile_Client/documents")
    Call<DocumentResponse> getDocuments(@Query("chapter") String chapter, @Query("timestamp") long timestamp);
}
