package com.liuyk.draggridview.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.drag.draglibrary.BaseDragListView;
import com.liuyk.draggridview.model.Channel;

/**
 * DESC
 * <p>
 * Created by liuyakui on 2020/9/17.
 */
public class MyListView extends BaseDragListView<Channel> {
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}