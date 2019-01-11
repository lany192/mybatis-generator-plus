package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.CodeBuilder;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 生成Controller插件
 */
public class ControllerPlugin extends PluginAdapter {
    /**
     * 目标包
     */
    private String targetPackage;
    /**
     * 基础包名
     */
    private String basePackage;

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        JavaModelGeneratorConfiguration modelConfig = getContext().getJavaModelGeneratorConfiguration();
        String modelType = introspectedTable.getBaseRecordType();
        String modelPackage = modelConfig.getTargetPackage();
        String modelName = modelType.replace(modelPackage + ".", "");
        String templatePath = System.getProperty("user.dir") + File.separator + "src"
                + File.separator + "main" + File.separator + "resources"
                + File.separator + "templates" + File.separator + "autocode";
        System.out.println("Controller包名==" + targetPackage);
        System.out.println("model的名称:" + modelName);
        System.out.println("model的类型:" + modelType);
        System.out.println("model的包名:" + modelPackage);
        System.out.println("模板路径:" + templatePath);

        Map<String, Object> data1 = new HashMap<>();
        data1.put("servicePackage", targetPackage);
        data1.put("basePackage", basePackage);
        data1.put("nameUpper", modelName);


        new CodeBuilder()
                .module("")
                .path("src/main/java")
                .packageName(targetPackage)
                .modelName(modelName)
                .suffix("Service")
                .format("java")
                .setTemplatePath(templatePath)
                .setTemplateName("api-controller.ftl")
                .setData(data1)
                .build();
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean validate(List<String> warnings) {
        // 获取配置的目标package
        Properties properties = getProperties();
        this.targetPackage = properties.getProperty("targetPackage");
        if (this.targetPackage == null) {
            warnings.add("请配置ServicePlugin插件的目标包名(targetPackage)！");
            return false;
        }
        this.basePackage = properties.getProperty("basePackage");
        if (this.basePackage == null) {
            warnings.add("请配置ServicePlugin插件的基础包名(basePackage)！");
            return false;
        }
        return true;
    }
}
