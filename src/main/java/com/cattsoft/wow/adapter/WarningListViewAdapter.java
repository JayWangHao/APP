package com.cattsoft.wow.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.wow.R;
import com.cattsoft.wow.activity.WarningHistoryActivity;
import com.cattsoft.wow.activity.WoDetailActivity;
import com.cattsoft.wow.mudels.Warning;

import java.util.List;

/**
 * 告警适配器
 * Created by lenovo on 2016/2/17.
 */
public class WarningListViewAdapter extends BaseAdapter{

    private Context mcontext = null;
    public List<Warning> list;
    private int num = 0;

    public WarningListViewAdapter(Context context, List<Warning> list){

        this.mcontext = context;
        this.list =list;
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

        ViewHolder viewHolder = null;
        int length = list.size();
        if(view == null){
            if(length == 1){
                view = LayoutInflater.from(this.mcontext).inflate(R.layout.fragment_warning_item_single,null);

                TextView warningLevel = (TextView) view.findViewById(R.id.warning_level2);
                TextView warningAddress = (TextView) view.findViewById(R.id.warning_address2);
                TextView warningContext = (TextView) view.findViewById(R.id.warning_context2);
                TextView businessType = (TextView) view.findViewById(R.id.business_type2);
                TextView warningTime = (TextView) view.findViewById(R.id.warning_time2);
                TextView happenTime = (TextView) view.findViewById(R.id.happen_time2);
                TextView warningCode = (TextView) view.findViewById(R.id.warning_code2);
                TextView handlePerson = (TextView) view.findViewById(R.id.handle_person2);
                TextView netLocation = (TextView) view.findViewById(R.id.net_location2);
                TextView longitude = (TextView) view.findViewById(R.id.longitude2);
                TextView addText = (TextView) view.findViewById(R.id.add_text2);
                final TextView historyWarning2 = (TextView) view.findViewById(R.id.history_warning22);
                final TextView flowWarning2 = (TextView) view.findViewById(R.id.flow_warning);

                Warning warning = list.get(i);
                warningLevel.setText(warning.getWarningLevel());
                warningAddress.setText(warning.getWarningAddress());
                warningContext.setText(warning.getWarningContext());
                businessType.setText(warning.getBusinessType());
                warningTime.setText(warning.getWarningTime());
                happenTime.setText(warning.getHappenTime());
                warningCode.setText(warning.getWarningCode());
                handlePerson.setText(warning.getHandlePerson());
                netLocation.setText(warning.getNetLocation());
                longitude.setText(warning.getLongitude());
                addText.setText(warning.getAddText());

                historyWarning2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mcontext, WarningHistoryActivity.class);
                        mcontext.startActivity(intent);
                    }
                });
                //判断工单按钮是否可以展示
                final  String tisyn = warning.getTisyn();
                if(!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
                    flowWarning2.setVisibility(view.VISIBLE);
                    flowWarning2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Warning warnvo = list.get(i);
                            Intent intent = new Intent(mcontext, WoDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("marnInfo", warnvo);
                            intent.putExtras(bundle);
                            mcontext.startActivity(intent);
                        }
                    });

                }

            }else {
                view = LayoutInflater.from(this.mcontext).inflate(R.layout.fragment_warning_item,null);

                viewHolder = new ViewHolder();
                viewHolder.warningLevel = (TextView) view.findViewById(R.id.warning_level);
                viewHolder.warningAddress = (TextView) view.findViewById(R.id.warning_address);
                viewHolder.warningContext = (TextView) view.findViewById(R.id.warning_context);
                viewHolder.businessType = (TextView) view.findViewById(R.id.business_type);
                viewHolder.warningTime = (TextView) view.findViewById(R.id.warning_time);
                viewHolder.happenTime = (TextView) view.findViewById(R.id.happen_time);
                viewHolder.warningCode = (TextView) view.findViewById(R.id.warning_code);
                viewHolder.handlePerson = (TextView) view.findViewById(R.id.handle_person);
                viewHolder.netLocation = (TextView) view.findViewById(R.id.net_location);
                viewHolder.longitude = (TextView) view.findViewById(R.id.longitude);
                viewHolder.addText = (TextView) view.findViewById(R.id.add_text);
                final ImageView downImg = (ImageView) view.findViewById(R.id.down_img);
                final ImageView upImg = (ImageView) view.findViewById(R.id.up_img);
                final LinearLayout hideLayout = (LinearLayout) view.findViewById(R.id.hide_layout);
                final TextView historyWarning = (TextView) view.findViewById(R.id.history_warning);
                final TextView historyWarning2 = (TextView) view.findViewById(R.id.history_warning2);
                final TextView flowWarning = (TextView) view.findViewById(R.id.flow_warning);
                final TextView flowWarning2 = (TextView) view.findViewById(R.id.flow_warning2);



                Warning warning = list.get(i);
                viewHolder.warningLevel.setText(warning.getWarningLevel());
                viewHolder.warningAddress.setText(warning.getWarningAddress());
                viewHolder.warningContext.setText(warning.getWarningContext());
                viewHolder.businessType.setText(warning.getBusinessType());
                viewHolder.warningTime.setText(warning.getWarningTime());
                viewHolder.happenTime.setText(warning.getHappenTime());
                viewHolder.warningCode.setText(warning.getWarningCode());
                viewHolder.handlePerson.setText(warning.getHandlePerson());
                viewHolder.netLocation.setText(warning.getNetLocation());
                viewHolder.longitude.setText(warning.getLongitude());
                viewHolder.addText.setText(warning.getAddText());

                //判断工单按钮是否可以展示
               final  String tisyn = warning.getTisyn();
                if(!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
                    flowWarning.setVisibility(view.VISIBLE);
                    flowWarning2.setVisibility(view.VISIBLE);
                    View.OnClickListener  flowOnClick=new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Warning warnvo = list.get(i);
                                Intent intent = new Intent(mcontext, WoDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("marnInfo", warnvo);
                                intent.putExtras(bundle);
                                mcontext.startActivity(intent);
                            }
                    };
                    flowWarning.setOnClickListener(flowOnClick);
                    flowWarning2.setOnClickListener(flowOnClick);

                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(num % 2 == 0){
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            historyWarning.setVisibility(View.GONE);
                            historyWarning2.setVisibility(View.VISIBLE);

                            if(!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
                                flowWarning.setVisibility(View.GONE);
                                flowWarning2.setVisibility(View.VISIBLE);
                            }

                        }else{
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            historyWarning.setVisibility(View.VISIBLE);
                            historyWarning2.setVisibility(View.GONE);
                            if(!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
                                flowWarning.setVisibility(View.VISIBLE);
                                flowWarning2.setVisibility(View.GONE);
                            }

                        }
                        num++;
                    }
                });

                downImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideLayout.setVisibility(View.VISIBLE);
                        downImg.setVisibility(View.GONE);
                        upImg.setVisibility(View.VISIBLE);
                        historyWarning.setVisibility(View.GONE);
                        historyWarning2.setVisibility(View.VISIBLE);
                    }
                });
                upImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideLayout.setVisibility(View.GONE);
                        downImg.setVisibility(View.VISIBLE);
                        upImg.setVisibility(View.GONE);
                        historyWarning.setVisibility(View.VISIBLE);
                        historyWarning2.setVisibility(View.GONE);
                    }
                });
                historyWarning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mcontext, WarningHistoryActivity.class);
                        mcontext.startActivity(intent);
                    }
                });
                historyWarning2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mcontext, WarningHistoryActivity.class);
                        mcontext.startActivity(intent);
                    }
                });
            }
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    class ViewHolder {
        TextView warningLevel;
        TextView warningAddress;
        TextView warningContext;
        TextView businessType;
        TextView warningTime;
        TextView happenTime;
        TextView warningCode;
        TextView handlePerson;
        TextView netLocation;
        TextView longitude;
        TextView addText;
    }


}
