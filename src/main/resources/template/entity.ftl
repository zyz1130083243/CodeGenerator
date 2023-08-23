package ${packagePath};

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;
import org.zyz.mybatis.annotion.Table;
import org.zyz.mybatis.annotion.Id;
import org.zyz.mybatis.annotion.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import ${packagePath}.dto.${entityName}DTO;
/**
  * @author auto create
  * ${tableComment}
  */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Table("${tableName}")
public class ${entityName} {
<#-- 遍历表字段 -->
<#list propertyList! as property>

    /**
     * ${property.columnComment!}
     */
    <#if primaryColumn == property.columnName>
    @Id
    </#if>
    @Column("${property.columnName!}")
    private ${property.propertyType!} ${property.propertyName!};
</#list>

}
