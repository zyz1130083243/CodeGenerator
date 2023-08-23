package org.zyz;

import org.zyz.config.CodeGeneratorConfig;

import java.util.Arrays;

public class CodeGenMain {

    public static void main(String[] args) throws ClassNotFoundException {
        CodeGeneratorConfig config = new CodeGeneratorConfig();

        // 1、配置数据库连接
        config.setDbUrl("jdbc:mysql://localhost:3306/information_schema?Unicode=true&characterEncoding=utf8&useSSL=false");
        config.setDbName("yshop");
        config.setDbUserName("root");
        config.setDbUserPassword("E=Kbk3i8r6pw");

        // 2、配置要生成的表
        String[] tables = {
                "gen_test",
                "email_config",
        };
        config.setTableList(Arrays.asList(tables));

        // 3、配置代码生成路径，注释掉相应路径则不会生成

        // 3.1 配置后端代码路径
        config.setCodeGenPath("/codegen/backend/src/main/java");
        // 配置实体类基础包名，后面会自动拼接对应包名(entity, mapper, service, controller)
        config.setPackagePath("org.zyz");

        // 3.2 生成Admin前端代码
        config.setAdminVueGenPath("/codegen/admin-vue");

        // 4、其他配置
        config.setRewriteFile(true);    // 是否覆盖文件
        config.setFilterPrefix(null);   // 表名前缀过滤

        // 5、执行生成
        config.generator();
    }

}
