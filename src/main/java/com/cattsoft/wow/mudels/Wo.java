package com.cattsoft.wow.mudels;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 工单
 * Created by lenovo on 2016/2/17.
 */
public class Wo implements Serializable {

    private String acceptPerson; //受理人
    private String cTime;//告警时长
    private String dacceptTime;//告警时长
    private String finishTime;//完成时间
    private String disPathPerson;//故障单派单人员
    private String  dnoiifyTime;//故障单通知时间;
    private String dsendTime;//数据发送接口时间；
    private String currtStep;//故障当前环节
    private String cattSoftStep;//大唐环节
    private String vcTitle;//工单标题
    private String niSoutTime;//未知
    private String npromptTimes;//未知
    private String vcConnect;
    private boolean expend;//额外添加的属性，默认都是false

    public void seteMosId(String eMosId) {
        this.eMosId = eMosId;
    }

    private String ncurrlink;//归档状态
    private String vcdeallink;//

    public String geteMosId() {
        return eMosId;
    }

    public void setNfaultLevel(String nfaultLevel) {
        this.nfaultLevel = nfaultLevel;
    }

    private String vip;
    private String needAlarm;
    private String vcLogids;//站点名称

    public String getNfaultLevel() {
        return nfaultLevel;
    }

    private String  vcAccperiPersonList;
    private String eMosId;

    public void setVcMajor(String vcMajor) {
        this.vcMajor = vcMajor;
    }

    public String getVcMajor() {

        return vcMajor;
    }

    private String nfaultLevel;
    private String  vcMajor;
    private String vcAreaCode;

    public String getVcAreaCode() {
        return vcAreaCode;
    }

    public void setVcAreaCode(String vcAreaCode) {
        this.vcAreaCode = vcAreaCode;
    }

    public void setVcFlowid(String vcFlowid) {
        this.vcFlowid = vcFlowid;
    }

    public String getVcAccperiPersonList() {
        return vcAccperiPersonList;
    }

    public String getVcFlowid() {
        return vcFlowid;

    }

    public void setVcAccperiPersonList(String vcAccperiPersonList) {
        this.vcAccperiPersonList = vcAccperiPersonList;
    }

    private String vcConsumtime;
    private String  vcFlowid;

    public void setAcceptPerson(String acceptPerson) {
        this.acceptPerson = acceptPerson;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public void setDacceptTime(String dacceptTime) {
        this.dacceptTime = dacceptTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void setDisPathPerson(String disPathPerson) {
        this.disPathPerson = disPathPerson;
    }

    public void setDnoiifyTime(String dnoiifyTime) {
        this.dnoiifyTime = dnoiifyTime;
    }

    public void setDsendTime(String dsendTime) {
        this.dsendTime = dsendTime;
    }

    public void setCurrtStep(String currtStep) {
        this.currtStep = currtStep;
    }

    public void setCattSoftStep(String cattSoftStep) {
        this.cattSoftStep = cattSoftStep;
    }

    public void setVcTitle(String vcTitle) {
        this.vcTitle = vcTitle;
    }

    public void setNiSoutTime(String niSoutTime) {
        this.niSoutTime = niSoutTime;
    }

    public void setNpromptTimes(String npromptTimes) {
        this.npromptTimes = npromptTimes;
    }

    public void setVcConnect(String vcConnect) {
        this.vcConnect = vcConnect;
    }

    public void setNcurrlink(String ncurrlink) {
        this.ncurrlink = ncurrlink;
    }

    public void setVcdeallink(String vcdeallink) {
        this.vcdeallink = vcdeallink;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public void setNeedAlarm(String needAlarm) {
        this.needAlarm = needAlarm;
    }

    public void setVcLogids(String vcLogids) {
        this.vcLogids = vcLogids;
    }

    public void setVcConsumtime(String vcConsumtime) {
        this.vcConsumtime = vcConsumtime;
    }

    public void setNdTcurrlink(String ndTcurrlink) {
        this.ndTcurrlink = ndTcurrlink;
    }

    private String ndTcurrlink;//受理状态

    public String getAcceptPerson() {
        return acceptPerson;
    }

    public String getcTime() {
        return cTime;
    }

    public String getDacceptTime() {
        return dacceptTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getDisPathPerson() {
        return disPathPerson;
    }

    public String getDnoiifyTime() {
        return dnoiifyTime;
    }

    public String getDsendTime() {
        return dsendTime;
    }

    public String getCurrtStep() {
        return currtStep;
    }

    public String getCattSoftStep() {
        return cattSoftStep;
    }

    public String getVcTitle() {
        return vcTitle;
    }

    public String getNiSoutTime() {
        return niSoutTime;
    }

    public String getNpromptTimes() {
        return npromptTimes;
    }

    public String getVcConnect() {
        return vcConnect;
    }

    public String getNcurrlink() {
        return ncurrlink;
    }

    public String getVcdeallink() {
        return vcdeallink;
    }

    public String getVip() {
        return vip;
    }

    public String getNeedAlarm() {
        return needAlarm;
    }

    public String getVcLogids() {
        return vcLogids;
    }

    public String getVcConsumtime() {
        return vcConsumtime;
    }

    public String getNdTcurrlink() {
        return ndTcurrlink;
    }

    public boolean isExpend() {
        return expend;
    }

    public void setExpend(boolean expend) {
        this.expend = expend;
    }

    public static Wo parse(JSONObject obj){

        if(obj == null){
            return null;
        }
        Wo wo = new Wo();
        wo.setVcFlowid(obj.getString("VCFLOWID"));
        wo.setVcAccperiPersonList(obj.getString("VCACCEPTPERSONLIST"));
        wo.seteMosId(obj.getString("VCEMOSID"));
        wo.setNfaultLevel(obj.getString("NFAULTLEVEL"));
        wo.setVcMajor(obj.getString("VCMAJOR"));
        wo.setVcAreaCode(obj.getString("VCAREACODE"));
        wo.setDsendTime(obj.getString("DSENDTIME"));
        wo.setDisPathPerson(obj.getString("DISPATHPERSON"));
        wo.setDnoiifyTime(obj.getString("DNOTIFYTIME"));
        wo.setDacceptTime(obj.getString("DACCEPTTIME"));
        wo.setFinishTime(obj.getString("DFINISHTIME"));
        wo.setVcTitle(obj.getString("VCTITLE"));
        wo.setNiSoutTime(obj.getString("NISOUTTIME"));
        wo.setNpromptTimes(obj.getString("NPROMPTTIMES"));
        wo.setVcConnect(obj.getString("VCCONTENT"));
        wo.setNcurrlink(obj.getString("NCURRLINK"));
        wo.setVcdeallink(obj.getString("VCDEALLINK"));
        wo.setAcceptPerson(obj.getString("ACCEPTPERSON"));
        wo.setVip(obj.getString("VIP"));
        wo.setNeedAlarm(obj.getString("NEEDALARM"));
        wo.setVcLogids(obj.getString("VCLOGIDS"));
        wo.setVcConsumtime(obj.getString("VCCONSUMTIME"));
        wo.setcTime(obj.getString("CTIME"));
        wo.setNdTcurrlink(obj.getString("NDTCURRLINK"));
        wo.setExpend(false);

        return  wo;
    }
}
