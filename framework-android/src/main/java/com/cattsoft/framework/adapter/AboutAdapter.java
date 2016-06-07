package com.cattsoft.framework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cattsoft.framework.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tianli on 2015/7/17.
 */
public class AboutAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    String[] data;

    public AboutAdapter(Context context,String[] data) {
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_about_item, parent, false);
            holder.txtTitle = (TextView) convertView
                    .findViewById(R.id.about_txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTitle.setText(data[position]);
        return convertView;
    }

    private static class ViewHolder {
        TextView txtTitle;
    }
}
