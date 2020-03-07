package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.database.entity.DocumentInbox;
import com.avdeev.docs.core.database.entity.DocumentOutbox;

public class DocOutboxListAdapter extends BasePagedAdapter<DocumentOutbox> {

    public DocOutboxListAdapter(Context context) {
        super(context, DocumentOutbox.DIFF_UTIL);
    }

    @Override
    protected BaseHolder createHolder(View view) {
        return new DocOutboxListAdapter.DocViewHolder(view);
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
        protected void bind(DocumentOutbox document) {
            nameView.setText(document.title);
            authorView.setText(document.author);
        }
    }
}
