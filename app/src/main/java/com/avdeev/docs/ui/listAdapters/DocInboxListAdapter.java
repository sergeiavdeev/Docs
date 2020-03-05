package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;

import com.avdeev.docs.R;
import com.avdeev.docs.core.database.entity.DocumentInbox;

public class DocInboxListAdapter extends BasePagedAdapter<DocumentInbox> {

    public DocInboxListAdapter(Context context) {
        super(context, DocumentInbox.DIFF_UTIL);
    }

    @Override
    protected BaseHolder createHolder(View view) {
        return new DocViewHolder(view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.doc_list_row;
    }

    protected class DocViewHolder extends BaseHolder {

        private TextView nameView;
        private TextView authorView;

        public DocViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.title);
            authorView = view.findViewById(R.id.author);
        }

        @Override
        protected void bind(DocumentInbox document) {
            nameView.setText(document.title);
            authorView.setText(document.author);
        }
    }
}
