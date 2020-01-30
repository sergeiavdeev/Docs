package com.avdeev.docs.ui.ext;

import android.widget.Filter;

import com.avdeev.docs.core.Document;

import java.util.ArrayList;

public class DocFilter extends Filter {

    private Document[] documents;

    public DocFilter(Document[] documents) {

        this.documents = documents;
    }

    @Override
    protected FilterResults performFiltering(CharSequence search) {

        FilterResults results = new FilterResults();

        if (search != null && search.length() > 0) {

            search = search.toString().toUpperCase();

            ArrayList<Document> filters = new ArrayList<Document>();

            for (int i = 0; i < documents.length; i++) {

                if (documents[i].getName().toUpperCase().contains(search) || documents[i].getAuthor().toUpperCase().contains(search)) {
                    filters.add(new Document(documents[i]));
                }
            }

            results.count = filters.size();
            results.values = filters;

        } else {

            results.count = documents.length;
            results.values = documents;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        documents = (Document[]) filterResults.values;

    }
}
