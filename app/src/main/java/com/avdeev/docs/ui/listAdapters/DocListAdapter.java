package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.network.pojo.Document;

import java.util.ArrayList;

public class DocListAdapter extends BaseAdapter<Document> {

    public DocListAdapter(Context context, ArrayList<Document> list) {
        super(context, list);
    }

    public static DocListAdapter create(Context context, ArrayList<Document> list) {

        return new DocListAdapter(context, list);
    }

    @Override
    protected BaseHolder createHolder(View view) {
        return new DocHolder(view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.doc_list_row;
    }

    @Override
    protected Document copyObject(Document object) {
        return new Document(object);
    }

    @Override
    protected boolean findText(Document document, CharSequence text) {

        boolean result = false;



        if (document.getTitle().toUpperCase().contains(text) ||
            document.getAuthor().toUpperCase().contains(text) ||
            document.getNumber().toUpperCase().contains(text)) {

            result = true;
        }

        return result;
    }

    protected class DocHolder extends BaseHolder {

        private TextView nameView;
        private TextView authorView;

        @Override
        protected void bind(Document document) {

            nameView.setText(document.getTitle());
            authorView.setText(document.getAuthor());
        }

        public DocHolder(View view) {
            super(view);

            nameView = view.findViewById(R.id.title);
            authorView = view.findViewById(R.id.author);
        }
    }
}
