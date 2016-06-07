package com.cattsoft.framework.template;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by admin_local on 15/8/30.
 *
 * 测试ORMLite的表对应的类
 *
 * 注解说明：http://blog.sina.com.cn/s/blog_8c804f6d0100wsfn.html
 */
@DatabaseTable(tableName = "TestORMLiteBean")
public class TestORMLiteBean {

    /**
     * ORMLite需要一个空的构造方法
     */
    public TestORMLiteBean(){

    }
    /**
     * 为了保存到本地数据库，而设置的自增长ID
     */
    @DatabaseField(generatedId=true)
    private int generatedId;

    /**
     * 表的字段label
     */
    @DatabaseField
    private String label;

    /**
     * 表的字段value
     */
    @DatabaseField
    private String value;

    public int getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(int generatedId) {
        this.generatedId = generatedId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
