package com.cattsoft.wow.mudels;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by chenlipeng on 2016/5/14.
 */
public class ListFragmentJavaBean implements Serializable {

    private String BREAK1;
    private String BREAK2;
    private String NGEOID;
    private String NGEONAME;
    private String ONCE;
    private String TIYSN;

    public String getBREAK1() {
        return BREAK1;
    }

    public void setBREAK1(String BREAK1) {
        this.BREAK1 = BREAK1;
    }

    public String getBREAK2() {
        return BREAK2;
    }

    public void setBREAK2(String BREAK2) {
        this.BREAK2 = BREAK2;
    }

    public String getNGEOID() {
        return NGEOID;
    }

    public void setNGEOID(String NGEOID) {
        this.NGEOID = NGEOID;
    }

    public String getNGEONAME() {
        return NGEONAME;
    }

    public void setNGEONAME(String NGEONAME) {
        this.NGEONAME = NGEONAME;
    }

    public String getONCE() {
        return ONCE;
    }

    public void setONCE(String ONCE) {
        this.ONCE = ONCE;
    }

    public String getTIYSN() {
        return TIYSN;
    }

    public void setTIYSN(String TIYSN) {
        this.TIYSN = TIYSN;
    }

    public static ListFragmentJavaBean parse(JSONObject obj){

        if(obj == null){
            return null;
        }
        ListFragmentJavaBean listFragmentBean = new ListFragmentJavaBean();

        listFragmentBean.setBREAK1(obj.getString("BREAK1"));
        listFragmentBean.setBREAK2(obj.getString("BREAK2"));
        listFragmentBean.setNGEOID(obj.getString("NGEOID"));

        listFragmentBean.setNGEONAME(obj.getString("NGEONAME"));
        listFragmentBean.setONCE(obj.getString("ONCE"));
        listFragmentBean.setTIYSN(obj.getString("TIYSN"));

        return  listFragmentBean;
    }


}
