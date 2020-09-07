package com.liuyk.draggridview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liuyk.draggridview.adapter.DragListAdapter;
import com.liuyk.draggridview.model.Channel;
import com.liuyk.draggridview.widget.DragListView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * DESC
 * <p>
 * Created by liuyakui on 2020/9/7.
 */
public class DragListViewActivity extends AppCompatActivity implements View.OnClickListener, DragListView.OnPositionChangerListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private DragListView mDragListView;
    private ArrayList<Channel> mChannels;
    private DragListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_listview);

        initView();
    }

    private void initView() {
        findViewById(R.id.sure).setOnClickListener(this);
        mDragListView = findViewById(R.id.drag_list);
        mAdapter = new DragListAdapter(this);
        mChannels = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            final Channel channel = new Channel();
            channel.setChannelName("频道" + i);
            mChannels.add(channel);
        }
        mAdapter.setItems(mChannels);
        mDragListView.setAdapter(mAdapter);
        mDragListView.setOnPositionChangerListener(this);
        mDragListView.setOnItemClickListener(this);
        mDragListView.setOnItemLongClickListener(this);
    }

    private void unedited() {
        mDragListView.setDragEnabled(false);
    }

    private void editing() {
        mDragListView.setDragEnabled(true);
    }

    @Override
    public void onClick(View v) {
        unedited();
        Toast.makeText(this, "长按拖拽", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChange(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= mChannels.size() || toPosition >= mChannels.size()) {
            return;
        }
        if (fromPosition > toPosition) {
            for (int i = toPosition; i < fromPosition; i++) {
                Collections.swap(mChannels, fromPosition, i);

            }
        } else if (fromPosition < toPosition) {
            for (int i = toPosition; i > fromPosition; i--) {
                Collections.swap(mChannels, fromPosition, i);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mDragListView.builderDragView();
        editing();
        return true;
    }
}