package com.cattsoft.wow.bean;

import java.util.ArrayList;

/**
 * Created by sjzb on 2016/7/21.
 */
public class MapBean {
    public ArrayList<CityBean> mapLists;

    public class CityBean{
        public int ONCEALARM;//断站总数
        public int ALARMUNFIN;//未恢复总数
        public int ONCEALARM2G;
        public int ALARMUNFIN2G;
        public int ONCEALARM3G;
        public int ALARMUNFIN3G;
        public int ONCEALARM4G;
        public int ALARMUNFIN4G;
    }
}
