package com.liuyk.draggridview.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.drag.draglibrary.BaseDragGridView;
import com.liuyk.draggridview.model.Channel;

/**
 * DESC
 * <p>
 * Created by liuyakui on 2020/9/17.
 */
public class MyGridView extends BaseDragGridView<Channel> {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}