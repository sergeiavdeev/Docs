package com.avdeev.docs.ui.docInner;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.avdeev.docs.R;
import com.avdeev.docs.core.DocFragment;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.ui.docInner.DocInnerViewModel;
import com.avdeev.docs.ui.ext.DocListAdapter;

public class DocInnerFragment extends DocFragment {

    private DocInnerViewModel docInnerViewModel;
    private DocListAdapter listAdapter;

    public static DocInnerFragment newInstance() {
        return new DocInnerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        docInnerViewModel =
                ViewModelProviders.of(this).get(DocInnerViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_doc_inner, container, false);

        final ListView listView = root.findViewById(R.id.doc_list);
        //final ProgressBar progressBar = root.findViewById(R.id.progress_bar);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.resresh);

        docInnerViewModel.getDocList().observe(this, new Observer<Document[]>() {
            @Override
            public void onChanged(Document[] documents) {

                listAdapter = new DocListAdapter(getContext(), documents);
                listView.setAdapter(listAdapter);
            }
        });

        docInnerViewModel.isWaiting().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                //int visible = (wait ? View.VISIBLE : View.GONE);

                //progressBar.setVisibility(visible);
                refreshLayout.setRefreshing(wait);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                docInnerViewModel.updateList();
            }
        });

        docInnerViewModel.getList();

        return root;
    }

    @Override
    public void onSearch(String searchText) {

        if (listAdapter != null) {
            listAdapter.getFilter().filter(searchText);
        }
        //Toast.makeText(getActivity(), "Doc In Search: " + searchText, Toast.LENGTH_LONG).show();
    }
}
