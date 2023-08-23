package org.zyz.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * table字段信息
 */
@Setter
@Getter
@AllArgsConstructor
public class ColumnInfo {
    // 数据库名称
    private String dbName;
    // 表名称
    private String tableName;
    // 表字段名
    private String columnName;
    // 表字段类型
    private String columnType;
    // 表字段键
    private String columnKey;
    // 表字段注释
    private String columnComment;

}
