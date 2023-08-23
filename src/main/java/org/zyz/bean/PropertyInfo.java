package org.zyz.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 映射实体类信息
 */
@Setter
@Getter
@NoArgsConstructor
public class PropertyInfo {

    private Integer index;

    private String columnName;

    private String propertyName;

    private String propertyType;

    private String propertyComment;

}
