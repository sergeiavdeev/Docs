package com.avdeev.docs.ui.docInner;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avdeev.docs.R;
import com.avdeev.docs.core.DocFragment;
import com.avdeev.docs.core.database.entity.DocumentInner;
import com.avdeev.docs.core.network.pojo.Document;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.ui.docDetail.DocDetailActivity;
import com.avdeev.docs.ui.listAdapters.DocInnerListAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DocInnerFragment extends DocFragment {

    private DocInnerViewModel docInnerViewModel;
    private DocInnerListAdapter listAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        Application app = getActivity().getApplication();

        docInnerViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(app).create(DocInnerViewModel.class);
        final View root = inflater.inflate(R.layout.common_list_layout, container, false);
        final RecyclerView listView = root.findViewById(R.id.view_list);
        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        listAdapter = new DocInnerListAdapter(getContext());
        listAdapter.setOnItemClickListener(createClickListener());

        listView.setAdapter(listAdapter);

        docInnerViewModel.isWaiting().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {

                refreshLayout.setRefreshing(wait);
            }
        });

        docInnerViewModel.docList.observe(getViewLifecycleOwner(), (pagedList) -> {
            listAdapter.submitList(pagedList);
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                docInnerViewModel.updateFromNetwork();
            }
        });

        docInnerViewModel.updateFromNetwork();

        return root;
    }

    @Override
    public void onSearch(String searchText) {
        docInnerViewModel.search(getViewLifecycleOwner(), searchText);
        docInnerViewModel.docList.observe(getViewLifecycleOwner(), (pagedList) -> {
            listAdapter.submitList(pagedList);
        });
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    private ItemClickListener createClickListener() {

        return new ItemClickListener<DocumentInner>() {
            @Override
            public void onItemClick(DocumentInner documentInner) {

                Intent intent = new Intent(getActivity(), DocDetailActivity.class);
                intent.putExtra("document", Document.create(documentInner));
                intent.putExtra("type", "internal");
                intent.putExtra("caption", "Внутренние");
                startActivity(intent);
            }
        };
    }
}
