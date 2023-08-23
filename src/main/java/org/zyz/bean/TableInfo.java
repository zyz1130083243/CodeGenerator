package org.zyz.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 表数据，示例:user_info
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class TableInfo {

    /**
     * 表名称，示例:user_info
     */
    private String tableName;

    /**
     * 表注释，示例:用户详情表
     */
    private String tableComment;

    /**
     * 主键，示例:user_id
     */
    private String primaryColumn;

    /**
     * 主键属性，示例：userId
     */
    private String primaryProperty;

    /**
     * 实体类名称，驼峰转换，示例: UserInfo
     */
    private String entityName;

    /**
     * 包名基础路径
     */
    private String packagePath;

    /**
     * 表字段信息
     */
    private List<ColumnInfo> columnInfoList;

    /**
     *  表字段映射的实体类信息
     */
    private List<PropertyInfo> propertyList;

    /**
     * 添加表字段信息
     * @param columnInfo
     */
    public void addColumnInfo(ColumnInfo columnInfo) {
        if (this.getColumnInfoList() == null) {
            this.setColumnInfoList(new ArrayList<>());
        }
        this.getColumnInfoList().add(columnInfo);
    }

}
