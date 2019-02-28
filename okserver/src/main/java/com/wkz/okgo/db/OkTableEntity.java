package com.wkz.okgo.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表的属性
 */
public class OkTableEntity implements Serializable {

    private static final long serialVersionUID = 2796283406560659021L;

    public String tableName;            //表名
    private List<OkColumnEntity> list;    //所有的表字段

    public OkTableEntity(String tableName) {
        this.tableName = tableName;
        list = new ArrayList<>();
    }

    public OkTableEntity addColumn(OkColumnEntity columnEntity) {
        list.add(columnEntity);
        return this;
    }

    /**
     * 建表语句
     */
    public String buildTableString() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName).append('(');
        for (OkColumnEntity entity : list) {
            if (entity.compositePrimaryKey != null) {
                sb.append("PRIMARY KEY (");
                for (String primaryKey : entity.compositePrimaryKey) {
                    sb.append(primaryKey).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
            } else {
                sb.append(entity.columnName).append(" ").append(entity.columnType);
                if (entity.isNotNull) {
                    sb.append(" NOT NULL");
                }
                if (entity.isPrimary) {
                    sb.append(" PRIMARY KEY");
                }
                if (entity.isAutoincrement) {
                    sb.append(" AUTOINCREMENT");
                }
                sb.append(",");
            }
        }
        if (sb.toString().endsWith(",")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * 获取数据库表中列的名字
     *
     * @param columnIndex 列在表中的序号
     * @return 返回列的名字
     */
    public String getColumnName(int columnIndex) {
        return list.get(columnIndex).columnName;
    }

    /**
     * 获取数据库表中列的个数
     */
    public int getColumnCount() {
        return list.size();
    }

    public int getColumnIndex(String columnName) {
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            if (list.get(i).columnName.equals(columnName)) return i;
        }
        return -1;
    }
}
