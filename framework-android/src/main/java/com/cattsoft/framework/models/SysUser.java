package com.cattsoft.framework.models;

import android.content.Context;

import com.cattsoft.framework.cache.CacheProcess;

import java.io.Serializable;

/**
 * Created by yylc on 15/6/1.
 */
public class SysUser implements Serializable{

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 员工名称
     */
    private String name;

    /**
     * 员工编号
     */
    private String staffId;

    /**
     * 用户Id
     */
    private String sysUserId;

    /**
     * 联系电话
     */
    private String telNbr;

    /**
     * 登录的手机号码
     */
    private String mobile;

    /**
     * 组织机构
     */
    private String orgName;

    /**
     * 组织结构
     */
    private String orgId;

    /**
     * 电子邮箱
     */
    private String mailBox;

    private SysUser(){}


    public static SysUser parse(Context context) {

        SysUser sysUser = new SysUser();

        sysUser.name = CacheProcess.getCacheValueInSharedPreferences(context, "name");
        sysUser.orgId = CacheProcess.getCacheValueInSharedPreferences(context, "orgId");
        sysUser.telNbr = CacheProcess.getCacheValueInSharedPreferences(context, "telNbr");
        sysUser.mobile = CacheProcess.getCacheValueInSharedPreferences(context, "mobile");
        sysUser.staffId = CacheProcess.getCacheValueInSharedPreferences(context, "staffId");
        sysUser.mailBox = CacheProcess.getCacheValueInSharedPreferences(context, "mailBox");
        sysUser.orgName = CacheProcess.getCacheValueInSharedPreferences(context, "orgName");
        sysUser.sysUserId = CacheProcess.getCacheValueInSharedPreferences(context, "sysUserId");
        sysUser.loginName = CacheProcess.getCacheValueInSharedPreferences(context, "loginName");

        return sysUser;
    }


    public String getLoginName() {
        return loginName;
    }

    public String getName() {
        return name;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public String getTelNbr() {
        return telNbr;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getMailBox() {
        return mailBox;
    }
}
