package com.liuyk.draggridview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.liuyk.draggridview.adapter.DragGridAdapter;
import com.liuyk.draggridview.model.Channel;
import com.liuyk.draggridview.widget.MyGridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    private Vibrator mVibrator;
    private MyGridView mDragGridView;
    private ArrayList<Channel> mChannels;
    private DragGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initView();
    }

    private void initView() {
        findViewById(R.id.sure).setOnClickListener(this);
        mDragGridView = findViewById(R.id.drag_grid);
        mAdapter = new DragGridAdapter(this);
        mChannels = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final Channel channel = new Channel();
            channel.setChannelName("频道" + i);
            mChannels.add(channel);
        }
        mAdapter.setItems(mChannels);
        mDragGridView.setAdapter(mAdapter);
        mDragGridView.setOnItemClickListener(this);
        mDragGridView.setOnItemLongClickListener(this);
        mDragGridView.setItems(mChannels);
    }

    private void unedited() {
        mDragGridView.setDragEnabled(false);
    }

    private void editing() {
        mDragGridView.setDragEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Intent intent = new Intent(this, DragListViewActivity.class);
        startActivity(intent);
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
