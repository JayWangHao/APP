package com.cattsoft.wow.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cattsoft.wow.R;
import com.cattsoft.wow.mudels.Wo;

import java.util.List;

/**
 * 告警适配器
    * Created by lenovo on 2016/2/17.
            */
    public class WoListViewAdapter extends BaseAdapter{

        private Context mcontext = null;
        public List<Wo> list;
        private int num = 0;

        public WoListViewAdapter(Context context, List<Wo> list){

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

        String woid = "";
        ViewHolder holder = null;
        if(view ==null){
            view = LayoutInflater.from(this.mcontext).inflate(R.layout.fragment_wo_detail_item,null);
            holder = new ViewHolder();

            holder.siteTxt = (TextView) view.findViewById(R.id.site_name_dtl);//站点名称
            holder.woTitleTxt = (TextView) view.findViewById(R.id.wo_title_dtl);//工单标题
            holder.woDurationTxt = (TextView) view.findViewById(R.id.wo_duration_dtl);//工单历时
            holder.distributePresonTxt = (TextView) view.findViewById(R.id.distribute_person_dtl);//派发人员
            holder.distributeTimeTxt = (TextView) view.findViewById(R.id.distribute_time_dtl);//派发时间
            holder.noticeTimeTxt = (TextView) view.findViewById(R.id.notic_time_dtl);//通知时间
            holder.woContentTxt = (TextView) view.findViewById(R.id.wo_content_dtl);//工单内容
            holder.emosStepTxt = (TextView) view.findViewById(R.id.emos_step_dtl);//EMOS环节
            holder.acceptPersonTxt = (TextView) view.findViewById(R.id.accept_person_dtl);//待受理人
            holder.emosid = (TextView) view.findViewById(R.id.emosid_dtl);//EmosId
            holder.overTimeTxt = (TextView)view.findViewById(R.id.isovertime_dtl);//是否超时
            holder.npromptTimeTex = (TextView)view.findViewById(R.id.nprompttime_dtl);//催单次数
            holder.acceptTanceTxt = (TextView)view.findViewById(R.id.acceptance_dtl);//受理时间
            holder.finishTimeTxt = (TextView)view.findViewById(R.id.finishtime_dtl);//完成时间
            holder.faultLevelTxt = (TextView)view.findViewById(R.id.faultlevel_dtl);// 故障单等级
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Wo wo = list.get(i);
        holder.siteTxt.setText(wo.getVcLogids());
        holder.woTitleTxt.setText(wo.getVcTitle());
        holder.woDurationTxt.setText(wo.getcTime());
        holder.distributePresonTxt.setText(wo.getAcceptPerson());
        holder.distributeTimeTxt.setText(wo.getDacceptTime());
        holder.noticeTimeTxt.setText(wo.getDsendTime());
        holder.woContentTxt.setText(wo.getVcConnect());
        holder.emosStepTxt.setText(wo.getNcurrlink());
        holder.acceptPersonTxt.setText(wo.getDisPathPerson());
        holder.emosid.setText(wo.geteMosId());
        holder.overTimeTxt.setText(wo.getNiSoutTime());
        holder.npromptTimeTex.setText(wo.getNpromptTimes());
        holder.acceptTanceTxt.setText(wo.getDacceptTime());
        holder.finishTimeTxt.setText(wo.getFinishTime());
        holder.faultLevelTxt.setText(wo.getNfaultLevel());
        woid = wo.getVcAreaCode();
        return view;
    }


    public  class ViewHolder{
        TextView siteTxt;//站点名称
        TextView woTitleTxt;//工单标题
        TextView woDurationTxt;//工单历时
        TextView distributePresonTxt;//派发人员
        TextView distributeTimeTxt;//派发时间
        TextView noticeTimeTxt;//通知时间
        TextView woContentTxt;//工单内容
        TextView emosStepTxt;//EMOS环节
        TextView acceptPersonTxt;//待受理人
        TextView emosid;//EmosId
        TextView overTimeTxt;//是否超时
        TextView npromptTimeTex;//催单次数
        TextView acceptTanceTxt;//受理时间
        TextView finishTimeTxt;//完成时间
        TextView faultLevelTxt;//故障单等级
    }


}
