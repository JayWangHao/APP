package com.cattsoft.wow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cattsoft.wow.R;
import com.cattsoft.wow.mudels.WoFragmentListViewItemMessageJavaBean;

import java.util.List;

/**
 * 留言的适配器
 */
public class ActivityWoItemListViewAdapter extends BaseAdapter {

    private Context mcontext = null;
    public List<WoFragmentListViewItemMessageJavaBean> list;

    public ActivityWoItemListViewAdapter(Context context, List<WoFragmentListViewItemMessageJavaBean> list) {

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
            view = LayoutInflater.from(this.mcontext).inflate(R.layout.activity_wo_item_listview_message_item, null);
            holder = new ViewHolder();

            holder.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            holder.tv_message_time = (TextView) view.findViewById(R.id.tv_message_time);
            holder.tv_message = (TextView) view.findViewById(R.id.tv_message);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }




        WoFragmentListViewItemMessageJavaBean messageJavaBean = list.get(i);

        holder.tv_user_name.setText(messageJavaBean.getVCREALNAME());


        holder.tv_message_time.setText(messageJavaBean.getDCOMMENTTIME());

        if(messageJavaBean.getVCCOMMENTS()==null){
            holder.tv_message.setText("");
        }else {
            holder.tv_message.setText(messageJavaBean.getVCCOMMENTS());
        }


        return view;
    }


    public class ViewHolder {

        TextView tv_user_name;//
        TextView tv_message_time;
        TextView tv_message;

    }


}
