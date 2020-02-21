package com.avdeev.docs.core.network.pojo;

import com.avdeev.docs.core.Document;

import java.util.List;

public class DocumentsResponse {
    public List<Document> documents;
    public List<String> delete_ids;


    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<String> getDelete_ids() {
        return delete_ids;
    }

    public void setDelete_ids(List<String> delete_ids) {
        this.delete_ids = delete_ids;
    }
}
