package com.cattsoft.wow.mudels;

import java.io.Serializable;

/**
 * Created by xumin on 2016/3/9.
 */
public class RelevanceWarning implements Serializable {
    private String vcnecname;//站点名称
    private String alarmlevel;//告警等级
    private String dfirsteventtime;//发生时间
    private String ctime1;//告警时长
    private String vcspecificproblems;//告警内容
    private String cnastate;//当前状态
    private String vcmoiname;//网元内定位
    private String objecttype;//告警对象

    public String getVcnecname() {
        return vcnecname;
    }

    public void setVcnecname(String vcnecname) {
        this.vcnecname = vcnecname;
    }

    public String getAlarmlevel() {
        return alarmlevel;
    }

    public void setAlarmlevel(String alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    public String getDfirsteventtime() {
        return dfirsteventtime;
    }

    public void setDfirsteventtime(String dfirsteventtime) {
        this.dfirsteventtime = dfirsteventtime;
    }

    public String getCtime1() {
        return ctime1;
    }

    public void setCtime1(String ctime1) {
        this.ctime1 = ctime1;
    }

    public String getVcspecificproblems() {
        return vcspecificproblems;
    }

    public void setVcspecificproblems(String vcspecificproblems) {
        this.vcspecificproblems = vcspecificproblems;
    }

    public String getCnastate() {
        return cnastate;
    }

    public void setCnastate(String cnastate) {
        this.cnastate = cnastate;
    }

    public String getVcmoiname() {
        return vcmoiname;
    }

    public void setVcmoiname(String vcmoiname) {
        this.vcmoiname = vcmoiname;
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }
}
