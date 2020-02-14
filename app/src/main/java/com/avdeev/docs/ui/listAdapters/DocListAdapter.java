package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.Document;

import java.util.ArrayList;

public class DocListAdapter extends BaseAdapter {

    public DocListAdapter(Context context, ArrayList<Object> list) {
        super(context, list);
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
    protected Object copyObject(Object object) {
        return new Document((Document)object);
    }

    @Override
    protected boolean findText(Object object, CharSequence text) {

        boolean result = false;

        Document document = (Document)object;

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
        void bind(Object object) {

            Document document = (Document)object;
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
