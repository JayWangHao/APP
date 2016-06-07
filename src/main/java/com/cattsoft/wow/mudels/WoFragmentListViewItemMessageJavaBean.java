package com.cattsoft.wow.mudels;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 工单
 * Created by lenovo on 2016/2/17.
 */
public class WoFragmentListViewItemMessageJavaBean implements Serializable {

    private String DCOMMENTTIME; //时间
    private String NCHECKFLAG;//
    private String VCCOMMENTS;//显示内容
    private String VCEMOSID;//
    private String VCREALNAME;//名字
    private String  VCUSERID;//

    public static WoFragmentListViewItemMessageJavaBean parse(JSONObject obj){

        if(obj == null){
            return null;
        }
        WoFragmentListViewItemMessageJavaBean messageJavaBean = new WoFragmentListViewItemMessageJavaBean();
        messageJavaBean.setDCOMMENTTIME(obj.getString("DCOMMENTTIME"));
        messageJavaBean.setNCHECKFLAG(obj.getString("NCHECKFLAG"));
        messageJavaBean.setVCCOMMENTS(obj.getString("VCCOMMENTS"));
        messageJavaBean.setVCEMOSID(obj.getString("VCEMOSID"));
        messageJavaBean.setVCREALNAME(obj.getString("VCREALNAME"));
        messageJavaBean.setVCUSERID(obj.getString("VCUSERID"));

        return  messageJavaBean;
    }

    public String getDCOMMENTTIME() {
        return DCOMMENTTIME;
    }

    public void setDCOMMENTTIME(String DCOMMENTTIME) {
        this.DCOMMENTTIME = DCOMMENTTIME;
    }

    public String getNCHECKFLAG() {
        return NCHECKFLAG;
    }

    public void setNCHECKFLAG(String NCHECKFLAG) {
        this.NCHECKFLAG = NCHECKFLAG;
    }

    public String getVCCOMMENTS() {
        return VCCOMMENTS;
    }

    public void setVCCOMMENTS(String VCCOMMENTS) {
        this.VCCOMMENTS = VCCOMMENTS;
    }

    public String getVCEMOSID() {
        return VCEMOSID;
    }

    public void setVCEMOSID(String VCEMOSID) {
        this.VCEMOSID = VCEMOSID;
    }

    public String getVCREALNAME() {
        return VCREALNAME;
    }

    public void setVCREALNAME(String VCREALNAME) {
        this.VCREALNAME = VCREALNAME;
    }

    public String getVCUSERID() {
        return VCUSERID;
    }

    public void setVCUSERID(String VCUSERID) {
        this.VCUSERID = VCUSERID;
    }
}
