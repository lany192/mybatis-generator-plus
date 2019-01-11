package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.CodeBuilder;
import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.MapBuilder;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * 生成Controller插件
 */
public class ControllerPlugin extends PluginAdapter {
    private final String TAG = getClass().getSimpleName();
    /**
     * 目标包
     */
    private String controllerPackage;
    /**
     * 基础包名
     */
    private String basePackage;

    /**
     * service包
     */
    private String servicePackage;

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        JavaModelGeneratorConfiguration modelConfig = getContext().getJavaModelGeneratorConfiguration();
        String modelType = introspectedTable.getBaseRecordType();
        String modelPackage = modelConfig.getTargetPackage();
        String modelName = modelType.replace(modelPackage + ".", "");
        String templatePath = System.getProperty("user.dir") + File.separator + "src"
                + File.separator + "main" + File.separator + "resources"
                + File.separator + "templates" + File.separator + "autocode";
        System.out.println("Controller包名==" + controllerPackage);
        System.out.println("model的名称:" + modelName);
        System.out.println("model的类型:" + modelType);
        System.out.println("model的包名:" + modelPackage);
        System.out.println("模板路径:" + templatePath);


        new CodeBuilder()
                .module("")
                .path("src/main/java")
                .packageName(controllerPackage)
                .modelName(modelName)
                .suffix("Controller")
                .format("java")
                .setTemplatePath(templatePath)
                .setTemplateName("api-controller.ftl")
                .setData(MapBuilder.of()
                        .put("basePackage", basePackage)
                        .put("modelPackage", modelPackage)
                        .put("apiControllerPackage", controllerPackage)
                        .put("servicePackage", servicePackage)
                        .put("nameUpper", modelName)
                        //.put("nameLower", item.getLowerName())
                        //.put("nameZh", modelName)
                        .build())
                .build();

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean validate(List<String> warnings) {
        Properties properties = getProperties();
        System.out.println("所有属性:" + JsonUtils.object2json(properties));

        controllerPackage = properties.getProperty("controllerPackage");
        if (controllerPackage == null) {
            warnings.add("请配置" + TAG + "插件的（controllerPackage）属性");
            return false;
        }

        servicePackage = properties.getProperty("servicePackage");
        if (servicePackage == null) {
            warnings.add("请配置" + TAG + "插件的（servicePackage）属性");
            return false;
        }

        basePackage = properties.getProperty("basePackage");
        if (basePackage == null) {
            warnings.add("请配置" + TAG + "插件的（basePackage）属性");
            return false;
        }
        return true;
    }
}
