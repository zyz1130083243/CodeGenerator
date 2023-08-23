package org.zyz.utils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 模板引擎工具类
 */
public class FreeMarkerUtil {

    private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

    static{
        // 设置模板文件存放路径
        configuration.setTemplateLoader(new ClassTemplateLoader(FreeMarkerUtil.class, "/template"));
        // 指定字符集
        configuration.setDefaultEncoding("UTF-8");
        // 生成模板异常处理器
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setCacheStorage(NullCacheStorage.INSTANCE);
    }

    /**
     *
     * @param path 生成文件路径
     * @param templateName 模板名称
     * @param dataModel 数据模板
     * @param overwrite 是否覆盖
     */
    public static void genTemplateCode(String path, String templateName, Object dataModel, boolean overwrite) {
        if (null == path || null == templateName) {
            return;
        }

        File file = new File(path);
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            try (
                    FileWriter fileWriter = new FileWriter(file, !overwrite);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            ){
                Template template = configuration.getTemplate(templateName);
                template.process(dataModel, bufferedWriter);
            } catch (TemplateException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
