package com.liuyk.draggridview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class AppBaseAdapter<T> extends BaseAdapter {
    protected Context mContext;
    private LayoutInflater mInflater;
    private List<T> mItems;

    public AppBaseAdapter(Context context) {
        mContext = context;
        mItems = new ArrayList<>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = createViewHolder(position, mInflater, parent);
            convertView = viewHolder.convertView;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindViewHolder(position, viewHolder, convertView, parent);
        return convertView;
    }

    protected abstract void bindViewHolder(int position, ViewHolder viewHolder, View convertView, ViewGroup parent);

    protected abstract ViewHolder createViewHolder(int position, LayoutInflater inflater, ViewGroup parent);

    public static abstract class ViewHolder {
        View convertView;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
            convertView.setTag(this);
        }
    }

    public void setItems(List<T> list){
        this.mItems = list;
    }

    public void addMoreItems(List<T> newItems) {
        this.mItems.addAll(newItems);
    }

    public void addMoreItems(int location, List<T> newItems) {
        this.mItems.addAll(location, newItems);
    }

    public void removeAllItems() {
        this.mItems.clear();
    }

    public List<T> getItems() {
        return mItems;
    }

}