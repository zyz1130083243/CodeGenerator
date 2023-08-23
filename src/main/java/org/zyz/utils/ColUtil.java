package org.zyz.utils;

import org.zyz.bean.ColumnInfo;
import org.zyz.bean.PropertyInfo;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 表字段工具类
 */
public class ColUtil {

    private static Properties properties = null;

    /**
     * 驼峰转换(首字母大写)
     * @param str 待转换的字符串
     * @return String
     */
    public static String strToHumpU(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str1 : str.split("_")) {
            stringBuilder.append(str1.substring(0,1).toUpperCase())
                    .append(str1.substring(1));
        }
        return stringBuilder.toString();
    }



    /**
     * 驼峰转换(首字母小写)
     * @param str 待转换的字符串
     * @param replacePrefix 过滤前缀
     * @return String
     */
    public static String strToHumpL(String str) {
        str = strToHumpU(str);
        if (str == null || str.isEmpty()) {
            return null;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 表字段名转换为实体类属性名
     * @param columnInfoList
     * @return
     */
    public static List<PropertyInfo> col2Property(List<ColumnInfo> columnInfoList) {
        if (columnInfoList.isEmpty()) {
            return null;
        }
        List<PropertyInfo> list = new ArrayList<>();
        int i = 1;
        PropertyInfo propertyInfo;
        for (ColumnInfo columnInfo : columnInfoList) {
            propertyInfo = new PropertyInfo();
            propertyInfo.setIndex(i++);
            propertyInfo.setColumnName(columnInfo.getColumnName());
            propertyInfo.setPropertyComment(columnInfo.getColumnComment());
            propertyInfo.setPropertyName(strToHumpL(columnInfo.getColumnName()));
            propertyInfo.setPropertyType(colType2JavaType(columnInfo.getColumnType()));
            list.add(propertyInfo);
        }
        return list;
    }

    /**
     * SQL字段类型转换JAVA数据类型
     * @param colType SQL字段类型
     * @return String Java字段类型
     */
    public static String colType2JavaType(String colType) {
        if (null == properties) {
            properties = new Properties();
            try (FileReader fileReader = new FileReader("src\\main\\resources\\fieldTypeMapping.properties");)
            {
                properties.load(fileReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return (String) properties.get(colType);
    }

}
