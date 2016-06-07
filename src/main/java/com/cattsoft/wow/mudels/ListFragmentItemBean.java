package com.cattsoft.wow.mudels;

import java.io.Serializable;

/**
 * 列表的listView，进入告警界面的数据
 * chenlipeng
 */
public class ListFragmentItemBean implements Serializable {



    private String CNstate;
    private String ROWNUMBER;
    private String SEVERITY;
    private String VCEMOSID;
    private String almStdLevel;
    private String almStdName;
    private String ctime1;
    private String firstEventTime;
    private String geoId;
    private String moiName;
    private String neCName;
    private String neEName;
    private String needalarm ;
    private String nsiteclass;
    private String objecttype;
    private String person;
    private String specificProblems;
    private String state;
    private String tisyn;

    public String getCNstate() {
        return CNstate;
    }

    public void setCNstate(String CNstate) {
        this.CNstate = CNstate;
    }

    public String getROWNUMBER() {
        return ROWNUMBER;
    }

    public void setROWNUMBER(String ROWNUMBER) {
        this.ROWNUMBER = ROWNUMBER;
    }

    public String getSEVERITY() {
        return SEVERITY;
    }

    public void setSEVERITY(String SEVERITY) {
        this.SEVERITY = SEVERITY;
    }

    public String getVCEMOSID() {
        return VCEMOSID;
    }

    public void setVCEMOSID(String VCEMOSID) {
        this.VCEMOSID = VCEMOSID;
    }

    public String getAlmStdLevel() {
        return almStdLevel;
    }

    public void setAlmStdLevel(String almStdLevel) {
        this.almStdLevel = almStdLevel;
    }

    public String getAlmStdName() {
        return almStdName;
    }

    public void setAlmStdName(String almStdName) {
        this.almStdName = almStdName;
    }

    public String getCtime1() {
        return ctime1;
    }

    public void setCtime1(String ctime1) {
        this.ctime1 = ctime1;
    }

    public String getFirstEventTime() {
        return firstEventTime;
    }

    public void setFirstEventTime(String firstEventTime) {
        this.firstEventTime = firstEventTime;
    }

    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    public String getMoiName() {
        return moiName;
    }

    public void setMoiName(String moiName) {
        this.moiName = moiName;
    }

    public String getNeCName() {
        return neCName;
    }

    public void setNeCName(String neCName) {
        this.neCName = neCName;
    }

    public String getNeEName() {
        return neEName;
    }

    public void setNeEName(String neEName) {
        this.neEName = neEName;
    }

    public String getNeedalarm() {
        return needalarm;
    }

    public void setNeedalarm(String needalarm) {
        this.needalarm = needalarm;
    }

    public String getNsiteclass() {
        return nsiteclass;
    }

    public void setNsiteclass(String nsiteclass) {
        this.nsiteclass = nsiteclass;
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getSpecificProblems() {
        return specificProblems;
    }

    public void setSpecificProblems(String specificProblems) {
        this.specificProblems = specificProblems;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTisyn() {
        return tisyn;
    }

    public void setTisyn(String tisyn) {
        this.tisyn = tisyn;
    }
}
