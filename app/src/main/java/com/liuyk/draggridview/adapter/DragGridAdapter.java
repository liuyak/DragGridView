package com.liuyk.draggridview.adapter;

import android.content.Context;
import android.graphics.Color;
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
public class DragGridAdapter extends AppBaseAdapter<Channel> {

    public DragGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindViewHolder(int position, ViewHolder viewHolder, View convertView, ViewGroup parent) {
        if (position == 0) {
            ((DragViewHolder) viewHolder).channelName.setText(getItem(position).getChannelName());
            ((DragViewHolder) viewHolder).channelName.setTextColor(Color.parseColor("#ffffff"));
            ((DragViewHolder) viewHolder).channelName.setBackgroundResource(R.drawable.subscription_immutable_item_background);
        } else {
            ((DragViewHolder) viewHolder).channelName.setBackgroundColor(Color.parseColor("#ffffff"));
            ((DragViewHolder) viewHolder).channelName.setTextColor(Color.parseColor("#000000"));
            ((DragViewHolder) viewHolder).channelName.setText(getItem(position).getChannelName());
        }
    }

    @Override
    protected ViewHolder createViewHolder(int position, LayoutInflater inflater, ViewGroup parent) {
        return new DragViewHolder(inflater.inflate(R.layout.drag_channel_grid_view, parent, false));
    }

    static final class DragViewHolder extends ViewHolder {
        private TextView channelName;

        public DragViewHolder(View convertView) {
            super(convertView);
            channelName = convertView.findViewById(R.id.channel_name);
        }
    }

}
