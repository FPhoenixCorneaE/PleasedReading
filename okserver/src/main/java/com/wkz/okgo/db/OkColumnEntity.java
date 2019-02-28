package com.wkz.okgo.db;

import java.io.Serializable;

/**
 * 表字段列的属性
 */
public class OkColumnEntity implements Serializable {

    private static final long serialVersionUID = -8523562261507244685L;

    public String columnName;               //列的名字
    public String columnType;               //列的类型
    public String[] compositePrimaryKey;    //复合主键
    public boolean isPrimary;               //是否是主键
    public boolean isNotNull;               //是否不能为空
    public boolean isAutoincrement;         //AUTOINCREMENT 是否自增

    /**
     * @param compositePrimaryKey 复合主键
     */
    public OkColumnEntity(String... compositePrimaryKey) {
        this.compositePrimaryKey = compositePrimaryKey;
    }

    /**
     * @param columnName 列名
     * @param columnType 列的数据类型
     */
    public OkColumnEntity(String columnName, String columnType) {
        this(columnName, columnType, false, false, false);
    }

    /**
     * @param columnName 列名
     * @param columnType 列的数据类型
     * @param isPrimary  是否为主键
     * @param isNotNull  是否不能为空
     */
    public OkColumnEntity(String columnName, String columnType, boolean isPrimary, boolean isNotNull) {
        this(columnName, columnType, isPrimary, isNotNull, false);
    }

    /**
     * @param columnName      列名
     * @param columnType      列的数据类型
     * @param isPrimary       是否为主键
     * @param isNotNull       是否不能为空
     * @param isAutoincrement 是否自增
     */
    public OkColumnEntity(String columnName, String columnType, boolean isPrimary, boolean isNotNull, boolean isAutoincrement) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.isPrimary = isPrimary;
        this.isNotNull = isNotNull;
        this.isAutoincrement = isAutoincrement;
    }
}
