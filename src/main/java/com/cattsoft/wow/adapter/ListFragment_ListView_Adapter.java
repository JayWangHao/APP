package com.cattsoft.wow.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cattsoft.wow.R;
import com.cattsoft.wow.activity.ListItemActivity;
import com.cattsoft.wow.activity.MapItemActivity;
import com.cattsoft.wow.fragment.ListItemFragment;
import com.cattsoft.wow.fragment.MapWoFragment;
import com.cattsoft.wow.fragment.MapWoFragmentlist;
import com.cattsoft.wow.mudels.ListFragmentJavaBean;

import java.util.List;

/**
 * 告警适配器
 */
public class ListFragment_ListView_Adapter extends BaseAdapter {

    private Context mcontext = null;
    public List<ListFragmentJavaBean> list;
    private int num = 0;
    private SpannableString ss;
    private SpannableString ss1;
    private FragmentManager fm;

    public ListFragment_ListView_Adapter(Context context, List<ListFragmentJavaBean> list, FragmentManager fm) {

        this.mcontext = context;
        this.list = list;
        this.fm = fm;
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
//            holder.noBrokenStationNumber = (TextView) view.findViewById(R.id.noBrokenStationNumber);
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

        String a = listFragmentBean.getUNBREAK1();
//        String a = "55";
        String b = listFragmentBean.getBREAK1();
        ss = new SpannableString(a +"/"+ b );
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#DC143C")), 0, a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.LTGRAY), a.length(), a.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#4876FF")), a.length()+1, a.length()+1+b.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String a1 = listFragmentBean.getUNTIYSN();
//        String a1 = "65";
        String b1 = listFragmentBean.getTIYSN();
        ss1 = new SpannableString(a1 +"/"+ b1 );
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#DC143C")), 0, a1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new ForegroundColorSpan(Color.LTGRAY), a1.length(), a1.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#4876FF")), a1.length() + 1, a1.length() + 1 + b1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.areaName.setText(listFragmentBean.getNGEONAME());
//        holder.noBrokenStationNumber.setText(listFragmentBean.getBREAK2());
        holder.workingNumber.setText(ss1);
        holder.brokenStationNumber.setText(ss);
//        holder.brokenStationNumber.setText(listFragmentBean.getBREAK1());

        holder.workingNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction4 = fm.beginTransaction();
//                MapWoFragmentlist mapWoFragment = new MapWoFragmentlist();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("areacode", list.get(i).getNGEOID());
//                bundle.putString("areaname", list.get(i).getNGEONAME());
//                mapWoFragment.setArguments(bundle);
//                transaction4.replace(R.id.layout, mapWoFragment);
//
//                transaction4.commit();

                Intent intent = new Intent(mcontext, MapItemActivity.class);
                intent.putExtra("areacode",list.get(i).getNGEOID());
                intent.putExtra("areaname",list.get(i).getNGEONAME());
                mcontext.startActivity(intent);
            }
        });

        holder.brokenStationNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction4 = fm.beginTransaction();
//                ListItemFragment listItemFragment = new ListItemFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("areacode", list.get(i).getNGEOID());
//                bundle.putString("areaname", list.get(i).getNGEONAME());
//                listItemFragment.setArguments(bundle);
//                transaction4.replace(R.id.layout, listItemFragment);
//
//                transaction4.commit();

                Intent intent = new Intent(mcontext, ListItemActivity.class);
                intent.putExtra("areacode",list.get(i).getNGEOID());
                intent.putExtra("areaname",list.get(i).getNGEONAME());
                mcontext.startActivity(intent);
            }
        });

        return view;
    }


    public class ViewHolder {

        LinearLayout ll_item;//根布局
        TextView areaName;//区域名
//        TextView noBrokenStationNumber;//非断站数
        TextView workingNumber;//工单数
        TextView brokenStationNumber;//断站数

    }


}
