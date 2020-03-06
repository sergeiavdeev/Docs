package com.avdeev.docs.core.network;

import com.avdeev.docs.core.database.entity.DocumentInbox;
import com.avdeev.docs.core.database.entity.DocumentInner;
import com.avdeev.docs.core.database.entity.DocumentOutbox;
import com.avdeev.docs.core.network.pojo.CommonResponse;
import com.avdeev.docs.core.network.pojo.DocumentResponse;
import com.avdeev.docs.core.network.pojo.DocumentsResponse;
import com.avdeev.docs.core.network.pojo.ActionsResponse;
import com.avdeev.docs.core.network.pojo.Login;
import com.avdeev.docs.core.network.pojo.TaskActionRequest;
import com.avdeev.docs.core.network.pojo.TasksResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonDocApi {

    @POST("auth")
    Call<CommonResponse> auth(@Body Login login);

    @GET("documents?chapter=inbox")
    Call<DocumentsResponse<DocumentInbox>> getDocumentsInbox(@Query("timestamp") long timestamp);

    @GET("documents?chapter=outbox")
    Call<DocumentsResponse<DocumentOutbox>> getDocumentsOutbox(@Query("timestamp") long timestamp);

    @GET("documents?chapter=internal")
    Call<DocumentsResponse<DocumentInner>> getDocumentsInternal(@Query("timestamp") long timestamp);

    @GET("documents/details")
    Call<DocumentResponse> getDocument(@Query("chapter") String chapter, @Query("id") String id);

    @POST("tasks")
    Call<CommonResponse> postTaskAction(@Body TaskActionRequest request);

    @GET("tasks")
    Call<TasksResponse> getTasks();

    @GET("files")
    Call<ResponseBody>getFile(@Query("id") String id);

    @GET("history")
    Call<ActionsResponse>getHistory(@Query("idtask") String taskId);

    @GET("history")
    Call<ActionsResponse> getHistory(@Query("iddocument") String id, @Query("chapter") String chapter);

    @GET("visas")
    Call<ActionsResponse> getVisas(@Query("iddocument") String id, @Query("chapter") String chapter);

    @GET("resolutions")
    Call<ActionsResponse> getResolutions(@Query("iddocument") String id, @Query("chapter") String chapter);

    @GET("mailing")
    Call<ActionsResponse> getMailing(@Query("iddocument") String id, @Query("chapter") String chapter);
}
