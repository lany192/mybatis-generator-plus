package com.github.lany192.mybatis.generator.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;

public class Utils {

    /**
     * 包名转成文件路径.例如"com.github.lany192" -> "/com/github/lany192/"
     *
     * @param packageName 包名
     * @return 文件路径
     */
    public static String package2path(String packageName) {
        if (!packageName.contains(".")) {
            return packageName;
        }
        StringBuilder path = new StringBuilder();
        String[] paths = packageName.split("\\.");
        path.append(File.separator);
        for (String item : paths) {
            path.append(File.separator).append(item);
        }
        path.append(File.separator);
        return path.toString();
    }

    public static String getRootPath(boolean check) {
        String rootPath = System.getProperty("user.dir");
        if (!check || !rootPath.contains("dao")) {
            return rootPath;
        }
        File rootFile = new File(rootPath);
        return rootFile.getParentFile().getPath();
    }

    /**
     * 根据模板文件生成新文件
     *
     * @param templateFile 模板
     * @param outFile      新文件
     * @param params       参数
     */
    public static void buildFile(File templateFile, File outFile, Map<String, Object> params) {
        if (!templateFile.exists()) {
            Log.i("找不到对应的模板文件" + templateFile.getPath());
            return;
        }
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        Log.i("定义的属性:" + JsonUtils.object2json(params));
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDirectoryForTemplateLoading(templateFile.getParentFile());
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            Template template = cfg.getTemplate(templateFile.getName());
            Writer writer = new FileWriter(outFile);
            template.process(params, writer);
            Log.i(outFile.getPath() + "文件生成成功");
        } catch (Exception e) {
            throw new RuntimeException(outFile.getPath() + "文件生成失败", e);
        }
    }

    public static String getAuthor(String author) {
        if (author == null || author.length() == 0) {
            return System.getProperty("user.name");
        }
        return author;
    }
}
