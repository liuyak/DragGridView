package com.liuyk.draggridview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuyk.draggridview.activity.R;
import com.liuyk.draggridview.model.Channel;

/**
 * 拖拽GridView适配器
 * <p>
 *
 * @author liuyk
 */
public class DragListAdapter extends AppBaseAdapter<Channel> {

    public DragListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindViewHolder(int position, ViewHolder viewHolder, View convertView, ViewGroup parent) {
        ((DragViewHolder) viewHolder).channelName.setText(getItem(position).getChannelName());
    }

    @Override
    protected ViewHolder createViewHolder(int position, LayoutInflater inflater, ViewGroup parent) {
        return new DragViewHolder(inflater.inflate(R.layout.drag_channel_list_view, parent, false));
    }

    static final class DragViewHolder extends ViewHolder {
        private TextView channelName;

        public DragViewHolder(View convertView) {
            super(convertView);
            channelName = convertView.findViewById(R.id.channel_name);
        }
    }

}
