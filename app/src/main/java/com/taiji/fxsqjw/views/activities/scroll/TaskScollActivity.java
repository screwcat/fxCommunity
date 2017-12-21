package com.taiji.fxsqjw.views.activities.scroll;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.DividerItemDecoration;
import com.taiji.fxsqjw.views.adapter.PagingScrollHelper;
import com.taiji.fxsqjw.views.adapter.TaskScrollAdapter;

public class TaskScollActivity extends Activity implements PagingScrollHelper.onPageChangeListener {
    RecyclerView recyclerView;
    private RecyclerView.ItemDecoration lastItemDecoration = null;
    private DividerItemDecoration vDividerItemDecoration = null;
    private LinearLayoutManager vLinearLayoutManager = null;
    private TaskScrollAdapter taskScrollAdapter;
    PagingScrollHelper scrollHelper = new PagingScrollHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_scoll);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        taskScrollAdapter = new TaskScrollAdapter();
        recyclerView.setAdapter(taskScrollAdapter);
        scrollHelper.setUpRecycleView(recyclerView);
        scrollHelper.setOnPageChangeListener(this);
        init();
    }

    private void init() {

        vDividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        vLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration itemDecoration = vDividerItemDecoration;
        recyclerView.setLayoutManager(vLinearLayoutManager);
        recyclerView.removeItemDecoration(lastItemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        scrollHelper.updateLayoutManger();
        lastItemDecoration = itemDecoration;
    }

    @Override
    public void onPageChange(int index) {
        Toast.makeText(this,"第"+index+"页",Toast.LENGTH_SHORT).show();
    }
}
