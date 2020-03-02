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
import com.avdeev.docs.core.network.pojo.Document;
import com.avdeev.docs.core.interfaces.ItemClickListener;
import com.avdeev.docs.ui.docDetail.DocDetailActivity;
import com.avdeev.docs.ui.listAdapters.DocListAdapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DocInnerFragment extends DocFragment {

    private DocInnerViewModel docInnerViewModel;


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
        final View root = inflater.inflate(R.layout.common_list_layout, container, false);

        final RecyclerView listView = root.findViewById(R.id.view_list);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        final SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh);

        docInnerViewModel.getDocListAdapter().observe(getViewLifecycleOwner(), new Observer<DocListAdapter>() {
            @Override
            public void onChanged(DocListAdapter docListAdapter) {
                docListAdapter.setOnItemClickListener(createClickListener());
                listView.setAdapter(docListAdapter);
            }
        });

        docInnerViewModel.isWaiting().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wait) {
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

        docInnerViewModel.search(searchText);
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    private ItemClickListener createClickListener() {

        return new ItemClickListener() {
            @Override
            public void onItemClick(Object object) {

                Intent intent = new Intent(getActivity(), DocDetailActivity.class);
                intent.putExtra("id", (Document)object);
                intent.putExtra("type", "internal");
                intent.putExtra("caption", "Внутренние");
                startActivity(intent);

            }
        };
    }
}
