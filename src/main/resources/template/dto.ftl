package ${packagePath}.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
/**
 * @author auto create
 * ${tableComment}DTO
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ${entityName}DTO implements Serializable  {
<#list propertyList! as property>

    /**
     * ${property.columnComment!}
     */
    private ${property.propertyType!} ${property.propertyName!};
</#list>

}
