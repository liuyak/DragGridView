package com.liuyk.draggridview.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.liuyk.draggridview.adapter.DragAdapter;
import com.liuyk.draggridview.model.Channel;
import com.liuyk.draggridview.widget.DragGridView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements DragGridView.OnPositionChangerListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    private Vibrator mVibrator;
    private DragGridView mDragGridView;
    private ArrayList<Channel> mChannels;
    private DragAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initView();
    }

    private void initView() {
        findViewById(R.id.sure).setOnClickListener(this);
        mDragGridView = (DragGridView) findViewById(R.id.drag_grid);
        mAdapter = new DragAdapter(this);
        mChannels = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final Channel channel = new Channel();
            channel.setChannelName("频道" + i);
            mChannels.add(channel);
        }
        mAdapter.setItems(mChannels);
        mDragGridView.setAdapter(mAdapter);
        mDragGridView.setOnPositionChangerListener(this);
        mDragGridView.setOnItemClickListener(this);
        mDragGridView.setOnItemLongClickListener(this);
    }

    private void unedited() {
        mDragGridView.setDragEnabled(false);
    }

    private void editing() {
        mDragGridView.setDragEnabled(true);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "您点击了" + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        mVibrator.vibrate(100);
        mDragGridView.builderDragView();
        editing();
        return true;
    }

    @Override
    public void onClick(View view) {
        unedited();
        Toast.makeText(this, "长按拖拽", Toast.LENGTH_SHORT).show();
    }
}
