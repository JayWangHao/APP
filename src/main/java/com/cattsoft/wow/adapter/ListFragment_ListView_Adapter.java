package com.cattsoft.wow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cattsoft.wow.R;
import com.cattsoft.wow.mudels.ListFragmentJavaBean;

import java.util.List;

/**
 * 告警适配器
 */
public class ListFragment_ListView_Adapter extends BaseAdapter {

    private Context mcontext = null;
    public List<ListFragmentJavaBean> list;
    private int num = 0;

    public ListFragment_ListView_Adapter(Context context, List<ListFragmentJavaBean> list) {

        this.mcontext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(this.mcontext).inflate(R.layout.listfragment_listview_item, null);
            holder = new ViewHolder();

            holder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            holder.areaName = (TextView) view.findViewById(R.id.areaName);
            holder.noBrokenStationNumber = (TextView) view.findViewById(R.id.noBrokenStationNumber);
            holder.workingNumber = (TextView) view.findViewById(R.id.workingNumber);
            holder.brokenStationNumber = (TextView) view.findViewById(R.id.brokenStationNumber);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder. ll_item.setBackgroundResource(R.color.white);
        if (i % 2 == 0) {
            //偶数，把背景设置成白色
            holder. ll_item.setBackgroundResource(R.color.white);
        } else {
            //奇数，把背景设置成灰色
            holder. ll_item.setBackgroundResource(R.color.default_background);

        }


        ListFragmentJavaBean listFragmentBean = list.get(i);
        holder.areaName.setText(listFragmentBean.getNGEONAME());
        holder.noBrokenStationNumber.setText(listFragmentBean.getBREAK2());
        holder.workingNumber.setText(listFragmentBean.getTIYSN());
        holder.brokenStationNumber.setText(listFragmentBean.getBREAK1());

        return view;
    }


    public class ViewHolder {

        LinearLayout ll_item;//根布局
        TextView areaName;//区域名
        TextView noBrokenStationNumber;//非断站数
        TextView workingNumber;//工单数
        TextView brokenStationNumber;//断站数

    }


}
