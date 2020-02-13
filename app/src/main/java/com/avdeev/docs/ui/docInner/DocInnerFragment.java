package com.avdeev.docs.ui.docInner;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avdeev.docs.R;
import com.avdeev.docs.core.DocFragment;
import com.avdeev.docs.core.Document;
import com.avdeev.docs.ui.docDetail.DocDetailActivity;
import com.avdeev.docs.ui.listAdapters.DocListAdapter;

import java.util.ArrayList;

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

        Application app = getActivity().getApplication();

        docInnerViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(app).create(DocInnerViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_doc_inner, container, false);

        final ListView listView = root.findViewById(R.id.doc_list);
        //final ProgressBar progressBar = root.findViewById(R.id.progress_bar);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        docInnerViewModel.getDocList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Document>>() {
            @Override
            public void onChanged(ArrayList<Document> documents) {

                listAdapter = new DocListAdapter(getContext(), documents);
                listView.setAdapter(listAdapter);
            }
        });

        docInnerViewModel.isWaiting().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Document doc = (Document) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), DocDetailActivity.class);
                intent.putExtra("id", doc);
                intent.putExtra("type", "internal");
                intent.putExtra("caption", "Внутренние");
                startActivity(intent);
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
    }
}
