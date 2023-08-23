package org.zyz.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zyz.bean.ColumnInfo;
import org.zyz.bean.PropertyInfo;
import org.zyz.bean.TableInfo;
import org.zyz.utils.ColUtil;
import org.zyz.utils.FreeMarkerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CodeGeneratorConfig {

    /**
     * 数据库连接信息
     */
    private String dbUrl = null;

    private String dbName = null;

    private String dbUserName = null;

    private String dbUserPassword = null;

    /**
     * 等待生成的表
     */
    private List<String> tableList = new ArrayList<>();

    /**
     * 生成路径、条件
     */
    private String codeGenPath = null;

    private String adminVueGenPath = null;

    private String packagePath = null;

    private Boolean rewriteFile = false;

    private String filterPrefix = null;


    public void generator() throws ClassNotFoundException {
        if (dbUrl == null || dbName == null || dbUserName == null || dbUserPassword == null) {
            System.out.println("DB connection params cannot be empty!");
            return;
        }

        if (tableList == null || tableList.isEmpty()) {
            System.out.println("[tableIncludes] cannot be empty!");
            return;
        }

        // 从DB获取表信息
        List<TableInfo> tableInfoList = getTableInfoByDB();

        // 遍历表数据生成代码
        for (TableInfo tableInfo : tableInfoList) {
            this.dataInit(tableInfo);

            genBackendCode(tableInfo);
            genAdminVueCode(tableInfo);
        }
    }

    /**
     * 连接数据库，获取表字段信息
     *
     * @return
     */
    public List<TableInfo> getTableInfoByDB() throws ClassNotFoundException {
        List<TableInfo> list = new ArrayList<>();

        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "select table_schema, table_name, column_name, data_type, column_key, column_comment from information_schema.columns where table_schema = ? and table_name = ? order by ordinal_position;";

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        TableInfo tableInfo;
        ColumnInfo columnInfo;
        for (String tableName : tableList) {
            try {
                con = DriverManager.getConnection(dbUrl, dbUserName, dbUserPassword);
                statement = con.prepareStatement(sql);
                statement.setString(1, dbName);
                statement.setString(2, tableName);
                resultSet = statement.executeQuery();

                tableInfo = new TableInfo();
                while (resultSet.next()) {
                    tableInfo.setTableName(tableName);
                    tableInfo.setTableComment(tableName); // todo

                    if ("PRI".equals(resultSet.getString("COLUMN_KEY"))) {
                        tableInfo.setPrimaryColumn(resultSet.getString("COLUMN_NAME"));
                    }

                    // 保存表的列字段
                    columnInfo = new ColumnInfo(
                            dbName,
                            tableName,
                            resultSet.getString("COLUMN_NAME"),
                            resultSet.getString("DATA_TYPE"),
                            resultSet.getString("COLUMN_KEY"),
                            resultSet.getString("COLUMN_COMMENT")
                    );
                    tableInfo.addColumnInfo(columnInfo);

                }
                list.add(tableInfo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (statement != null) statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (con != null) con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list;
    }

    /**
     * 封装生成FreeMarker需要的数据
     *
     * @param tableInfo
     * @return
     */
    private void dataInit(TableInfo tableInfo) {
        if (tableInfo == null || tableInfo.getColumnInfoList() == null) {
            return;
        }

        String tableName = tableInfo.getTableName();
        if (filterPrefix != null) {
            tableName = tableName.replaceFirst(filterPrefix, "");
        }

        tableInfo.setEntityName(ColUtil.strToHumpU(tableName));
        tableInfo.setPackagePath(packagePath);

        tableInfo.setPrimaryProperty(ColUtil.strToHumpL(tableInfo.getPrimaryColumn()));


        List<PropertyInfo> propertyInfos = ColUtil.col2Property(tableInfo.getColumnInfoList());
        tableInfo.setPropertyList(propertyInfos);
    }

    /**
     * 生成后端代码
     * @param tableInfo
     */
    private void genBackendCode(TableInfo tableInfo) {
        if (codeGenPath == null || packagePath == null) {
            return;
        }
        String basePath = codeGenPath + "/" + packagePath.replace(".", "/");

        // 生成Mapper
        String mapperPath = basePath + "/mapper/" + tableInfo.getEntityName() + "Mapper.java";
        FreeMarkerUtil.genTemplateCode(mapperPath, "mapper.ftl", tableInfo, rewriteFile);

        // 生成SqlBuilder
        String sqlBuilderPath = basePath + "/mapper/" + tableInfo.getEntityName() + "SqlBuilder.java";
        FreeMarkerUtil.genTemplateCode(sqlBuilderPath, "sqlbuilder.ftl", tableInfo, rewriteFile);

        // 生成Entity
        String entityPath = basePath + "/entity/" + tableInfo.getEntityName() + ".java";
        FreeMarkerUtil.genTemplateCode(entityPath, "entity.ftl", tableInfo, rewriteFile);

        // 生成DTO
        String dtoPath = basePath + "/dto/" + tableInfo.getEntityName() + "DTO.java";
        FreeMarkerUtil.genTemplateCode(dtoPath, "dto.ftl", tableInfo, rewriteFile);


    }

    /**
     * 生成前端代码
     * @param tableInfo
     */
    private void genAdminVueCode(TableInfo tableInfo) {
        if (adminVueGenPath == null) {
            return;
        }

        String apiJsPath = adminVueGenPath + "/src/api/" + tableInfo.getEntityName().toLowerCase() + "/" + tableInfo.getEntityName() + ".js";
        FreeMarkerUtil.genTemplateCode(apiJsPath, "api.ftl", tableInfo, rewriteFile);

        String viewPath = adminVueGenPath + "/src/views/" + tableInfo.getEntityName().toLowerCase() + "/index.vue";
        FreeMarkerUtil.genTemplateCode(viewPath, "index.ftl", tableInfo, rewriteFile);

    }

}
