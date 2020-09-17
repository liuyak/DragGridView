package com.drag.draglibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.Collections;
import java.util.List;

/**
 * 可拖拽的GridView
 */
public abstract class BaseDragGridView<T> extends GridView {
    private List<T> mItems;
    private BaseAdapter mAdapter;

    private OnPositionChangerListener mOnPositionChangerListener;

    private Handler mHandler = new Handler();
    private Runnable mCreateDragRunnable = new Runnable() {
        @Override
        public void run() {
            createDragImage(mCreateX, mCreateY);
        }
    };

    private int mScrollBy = 20;
    private Runnable mScrollDragRunnable = new Runnable() {
        @Override
        public void run() {
            smoothScrollBy(mScrollBy, 300);
            mHandler.postDelayed(mScrollDragRunnable, 300);
        }
    };

    private View mDownView;
    private Bitmap mDragBitmap;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private ImageView mDragImageView;

    //触摸点相对于item的坐标值(自身)
    private int mItemX;
    private int mItemY;

    /**
     * 相对于屏幕的距离
     */
    private int mScreenX;
    private int mScreenY;

    /**
     * 开始创建镜像时,镜像的XY位置
     */
    private int mCreateX;
    private int mCreateY;

    /**
     * 按下的位置
     */
    private int mDownItemPosition;

    private int downX;
    private int downY;

    /**
     * 是否可以拖拽
     */
    private boolean mDragEnabled;

    /**
     * 不可变的位置
     */
    private int mImmutablePosition = 0;

    public BaseDragGridView(Context context) {
        this(context, null);
    }

    public BaseDragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseDragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("value", "Intercept: down");

                //得到按下的位置相对于GridView边缘的距离
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                mDownItemPosition = pointToPosition(downX, downY);

                //得到按下的位置相对于屏幕的距离
                mScreenX = (int) ev.getRawX();
                mScreenY = (int) ev.getRawY();

                //位置无效或者不允许拖拽,拦截手势事件
                if (mDownItemPosition == AdapterView.INVALID_POSITION || !mDragEnabled) {
                    return super.onInterceptTouchEvent(ev);//不拦截事件
                }
                builderDragView();

                return true;
            case MotionEvent.ACTION_MOVE:
                Log.i("value", "Intercept: move");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("value", "Intercept: action_up");
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!mDragEnabled) {//切切要在这里做拦截，否则会导致onItemClick()不可用
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return super.onTouchEvent(ev);

            case MotionEvent.ACTION_MOVE:
                Log.i("value", "Touch: move");
                if (mDragEnabled && mDragImageView != null) {
                    onMoveDrag((int) ev.getRawX(), (int) ev.getRawY());
                    int moveX = (int) ev.getX();
                    int moveY = (int) ev.getY();
                    swapItemView(moveX, moveY);
                }

                break;
            case MotionEvent.ACTION_UP:
                removeDragImage();
                mHandler.removeCallbacks(mCreateDragRunnable);
                mHandler.removeCallbacks(mScrollDragRunnable);
                int toPositionX = (int) ev.getX();
                int toPositionY = (int) ev.getY();
                stopDrag(toPositionX, toPositionY);
                break;
            default:
                return super.onTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 构建镜像View
     */
    public void builderDragView() {
        mDownView = getChildAt(mDownItemPosition - getFirstVisiblePosition());
        if (mDownView != null) {
            //开启绘图机制
            mDownView.setDrawingCacheEnabled(true);
            //获取downView在缓存中的Bitmap对象
            mDragBitmap = Bitmap.createBitmap(mDownView.getDrawingCache());
            //释放缓存,避免重复出现镜像
            mDownView.destroyDrawingCache();

            //计算出触摸点相对于,自己本身的坐标值 getLeft()ItemView边缘相对于GridView边缘的距离
            mItemX = downX - mDownView.getLeft();
            mItemY = downY - mDownView.getTop();

            //计算出开始创建镜像时,实际镜像的位置
            mCreateX = mScreenX - mItemX;
            mCreateY = mScreenY - mItemY - DeviceUtils.getStatusHeight(getContext());

            mHandler.postDelayed(mCreateDragRunnable, 10);//小小延迟下，缓解压力

            //隐藏要拖动的Item
            mDownView.setVisibility(View.INVISIBLE);
        }
    }

    private void onMoveDrag(int moveX, int moveY) {//移动镜像
        if (mDownView != null && mDragImageView != null) {

            int mOffsetY = moveY - mItemY - DeviceUtils.getStatusHeight(getContext());
            mWindowLayoutParams.x = moveX - mItemX;
            mWindowLayoutParams.y = mOffsetY;
            mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams);

            //GridView自动滚动
            if (mOffsetY > (DeviceUtils.getWindowHeight(getContext()) * 0.5)) {
                mScrollBy = 20;
                mHandler.postDelayed(mScrollDragRunnable, 300);
            } else if (mOffsetY < (DeviceUtils.getWindowHeight(getContext()) / 4)) {
                mScrollBy = -20;
                mHandler.postDelayed(mScrollDragRunnable, 300);
            }
        }
    }

    private void swapItemView(int toPositionX, int toPositionY) {
        int toItemPosition = pointToPosition(toPositionX, toPositionY);
        final View toItemView = getChildAt(toItemPosition - getFirstVisiblePosition());

        if (toItemPosition == mDownItemPosition || toItemPosition == AdapterView.INVALID_POSITION ||
                toItemView == null || mDownView == null ||
                toItemPosition == mImmutablePosition) {
            return;
        }
        if (mOnPositionChangerListener != null) {
            mOnPositionChangerListener.onChange(mDownItemPosition, toItemPosition);
        }
        swapItems(mDownItemPosition, toItemPosition);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.VISIBLE);
        }
        toItemView.setVisibility(View.INVISIBLE);
        //关键的一步,每次到达一个新位置,一定要重新将开始位置和结束位置,致为一直
        //否则就会位置错乱
        mDownItemPosition = toItemPosition;
    }

    private void stopDrag(int toPositionX, int toPositionY) {
        int toItemPosition = pointToPosition(toPositionX, toPositionY);
        final View toItemView = getChildAt(toItemPosition - getFirstVisiblePosition());

        //交换位置
        if (mDownView != null && toItemView != null) {
            if (mOnPositionChangerListener != null) {
                mOnPositionChangerListener.onChange(mDownItemPosition, toItemPosition);
            }
            swapItems(mDownItemPosition, toItemPosition);
        }
        //如果toItemView为null,也要把所有的child显示,否则会出现child丢失的现象
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.VISIBLE);
        }

    }

    private void createDragImage(int createX, int createY) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT;//图片之外的区域透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.START;//center?
        mWindowLayoutParams.alpha = 0.6f;
        mWindowLayoutParams.x = createX;
        mWindowLayoutParams.y = createY;
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(mDragBitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    private void removeDragImage() {//移除镜像
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * 设置是否可以拖拽
     *
     * @param isDrag 是否拖拽
     */
    public void setDragEnabled(boolean isDrag) {
        this.mDragEnabled = isDrag;
    }

    /**
     * 得到mDragEnabled
     *
     * @return mDragEnabled
     */
    public boolean getDragEnabled() {
        return mDragEnabled;
    }

    /**
     * 设置不可变的位置
     *
     * @param position 位置
     */
    public void setImmutablePosition(int position) {
        mImmutablePosition = position;
    }

    public void setItems(List<T> mItems) {
        this.mItems = mItems;
    }

    private void swapItems(int fromPosition, int toPosition) {
        if (mItems == null || mItems.isEmpty()) {
            return;
        }
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= mItems.size() || toPosition >= mItems.size()) {
            return;
        }
        if (fromPosition > toPosition) {
            for (int i = toPosition; i < fromPosition; i++) {
                Collections.swap(mItems, fromPosition, i);

            }
        } else if (fromPosition < toPosition) {
            for (int i = toPosition; i > fromPosition; i--) {
                Collections.swap(mItems, fromPosition, i);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (BaseAdapter) adapter;
    }

    public void setOnPositionChangerListener(OnPositionChangerListener onPositionChangerListener) {
        this.mOnPositionChangerListener = onPositionChangerListener;
    }

    public interface OnPositionChangerListener {
        void onChange(int fromPosition, int toPosition);
    }

}
