package com.github.lany192.generator.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;

public class FreemarkerUtils {

    /**
     * 根据模板文件生成新文件
     *
     * @param templateFile 模板
     * @param outFile      新文件
     * @param params       参数
     */
    public static void buildFile(File templateFile, File outFile, Map<String, Object> params) {
        if (!templateFile.exists()) {
            Log.i("找不到对应的模板文件：" + templateFile.getPath());
            return;
        }
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        Log.i("定义的属性:" + JsonUtils.object2json(params));
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setDirectoryForTemplateLoading(templateFile.getParentFile());
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            Template template = cfg.getTemplate(templateFile.getName());
            Writer writer = new FileWriter(outFile);
            template.process(params, writer);
            Log.i("目标文件生成成功" + outFile.getPath());
        } catch (Exception e) {
            throw new RuntimeException("目标文件生成失败!" + outFile.getPath(), e);
        }
    }
}
