package com.cattsoft.wow.mudels;

import java.io.Serializable;

/**
 * 告警的详细内容
 * Created by lenovo on 2016/2/17.
 */
public class Warning implements Serializable {

    private String warningLevel;      //级别
    private String warningAddress;   //报警地址
    private String warningContext;   //告警内容
    private String businessType;     //业务类型
    private String warningTime;      //告警时长
    private String happenTime;       //产生时间
    private String warningCode;      //告警码
    private String handlePerson;    //处理人员
    private String netLocation;     //网元定位
    private String longitude;        //经纬度
    private String addText;          //附加文本
    private String tisyn;        //工单按钮是否展示的依据
    private String geoId;        //区域编码
    private String neeName;     //地址id
    private boolean expend;   //额外添加的属性，默认都是false

    public String getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(String warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getWarningAddress() {
        return warningAddress;
    }

    public void setWarningAddress(String warningAddress) {
        this.warningAddress = warningAddress;
    }

    public String getWarningContext() {
        return warningContext;
    }

    public void setWarningContext(String warningContext) {
        this.warningContext = warningContext;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(String warningTime) {
        this.warningTime = warningTime;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(String happenTime) {
        this.happenTime = happenTime;
    }

    public String getWarningCode() {
        return warningCode;
    }

    public void setWarningCode(String warningCode) {
        this.warningCode = warningCode;
    }

    public String getHandlePerson() {
        return handlePerson;
    }

    public void setHandlePerson(String handlePerson) {
        this.handlePerson = handlePerson;
    }

    public String getNetLocation() {
        return netLocation;
    }

    public void setNetLocation(String netLocation) {
        this.netLocation = netLocation;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddText() {
        return addText;
    }

    public void setAddText(String addText) {
        this.addText = addText;
    }

    public String getTisyn() {
        return tisyn;
    }

    public void setTisyn(String tisyn) {
        this.tisyn = tisyn;
    }

    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    public String getNeeName() {
        return neeName;
    }

    public void setNeeName(String neeName) {
        this.neeName = neeName;
    }

    public boolean isExpend() {
        return expend;
    }

    public void setExpend(boolean expend) {
        this.expend = expend;
    }

}
