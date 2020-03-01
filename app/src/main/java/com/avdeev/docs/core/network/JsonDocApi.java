package com.avdeev.docs.core.network;

import com.avdeev.docs.core.network.pojo.CommonResponse;
import com.avdeev.docs.core.network.pojo.DocumentResponse;
import com.avdeev.docs.core.network.pojo.DocumentsResponse;
import com.avdeev.docs.core.network.pojo.Login;
import com.avdeev.docs.core.network.pojo.TaskActionRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonDocApi {

    @POST("auth")
    Call<CommonResponse> auth(@Body Login login);

    @GET("documents")
    Call<DocumentsResponse> getDocuments(@Query("chapter") String chapter, @Query("timestamp") long timestamp);

    @GET("documents/details")
    Call<DocumentResponse> getDocument(@Query("chapter") String chapter, @Query("id") String id);

    @POST("tasks")
    Call<CommonResponse> postTaskAction(@Body TaskActionRequest request);
}
