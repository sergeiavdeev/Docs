package com.avdeev.docs.core.network.pojo;

import java.util.List;

public class DocumentsResponse<T> {
    public List<T> documents;
    public List<String> delete_ids;
}
