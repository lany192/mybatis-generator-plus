package com.github.lany192.mybatis.generator.utils;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class CodeBuilder {
    private String rootPath = System.getProperty("user.dir");
    private String module;
    private String path;
    private String packageName;
    private String modelName;
    private String format;
    /**
     * 前缀
     */
    private String prefix = "";
    /**
     * 后缀
     */
    private String suffix = "";
    //--------------------------------------
    private String templatePath;
    private String templateName;
    private Map<String, Object> data;

    public void build() {
        String outPath = getPath();
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setDirectoryForTemplateLoading(new File(templatePath));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            cfg.getTemplate(templateName).process(data, new FileWriter(new File(outPath)));
            System.out.println("接口文件" + outPath + "生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成" + outPath + "失败", e);
        }
    }

    private String getPath() {
        String result = rootPath + File.separator;
        if (!isEmpty(module)) {
            result += module + File.separator;
        }
        if (!isEmpty(path)) {
            result += path + File.separator;
        }
        if (!isEmpty(packageName)) {
            result += packagePath(packageName) + File.separator;
        }
        if (!isEmpty(prefix)) {
            result += prefix;
        }
        if (!isEmpty(modelName)) {
            result += modelName;
        }
        if (!isEmpty(suffix)) {
            result += suffix;
        }
        result += ".";
        result += format;

        //输出路径
        File file = new File(result);
        if (file.exists()) {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file.getPath();
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public CodeBuilder setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public CodeBuilder setTemplateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public CodeBuilder setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }


    private String packagePath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    public CodeBuilder rootPath(String rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    public CodeBuilder module(String module) {
        this.module = module;
        return this;
    }

    public CodeBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CodeBuilder packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public CodeBuilder modelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public CodeBuilder format(String format) {
        this.format = format;
        return this;
    }

    public CodeBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CodeBuilder suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }
}
