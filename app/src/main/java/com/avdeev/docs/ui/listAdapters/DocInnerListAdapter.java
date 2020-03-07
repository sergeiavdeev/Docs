package com.avdeev.docs.ui.listAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.database.entity.DocumentInner;

public class DocInnerListAdapter extends BasePagedAdapter<DocumentInner> {

    public DocInnerListAdapter(Context context) {
        super(context, DocumentInner.DIFF_UTIL);
    }

    @Override
    protected BaseHolder createHolder(View view) {
        return new DocInnerListAdapter.DocViewHolder(view);
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
        protected void bind(DocumentInner document) {
            nameView.setText(document.title);
            authorView.setText(document.author);
        }
    }
}
