package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.CodeBuilder;
import com.github.lany192.mybatis.generator.utils.MapBuilder;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * 生成Service插件
 */
public class ServicePlugin extends PluginAdapter {
    private final String TAG = getClass().getSimpleName();
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
        String implPackage = targetPackage + ".impl";
        String templatePath = System.getProperty("user.dir") + File.separator + "src"
                + File.separator + "main" + File.separator + "resources"
                + File.separator + "templates" + File.separator + "autocode";
        System.out.println("service包名==" + targetPackage);
        System.out.println("service impl包名==" + implPackage);
        System.out.println("model的名称:" + modelName);
        System.out.println("model的类型:" + modelType);
        System.out.println("model的包名:" + modelPackage);
        System.out.println("模板路径:" + templatePath);


        new CodeBuilder()
                .module("")
                .path("src/main/java")
                .packageName(targetPackage)
                .modelName(modelName)
                .suffix("Service")
                .format("java")
                .setTemplatePath(templatePath)
                .setTemplateName("service.ftl")
                .setData(MapBuilder.of()
                        .put("servicePackage", targetPackage)
                        .put("basePackage", basePackage)
                        .put("nameUpper", modelName)
                        .build())
                .build();

        new CodeBuilder()
                .module("")
                .path("src/main/java")
                .packageName(implPackage)
                .modelName(modelName)
                .suffix("ServiceImpl")
                .format("java")
                .setTemplatePath(templatePath)
                .setTemplateName("service-impl.ftl")
                .setData(MapBuilder.of()
                        .put("serviceImplPackage", implPackage)
                        .put("servicePackage", targetPackage)
                        .put("basePackage", basePackage)
                        .put("nameUpper", modelName)
                        .build())
                .build();
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean validate(List<String> warnings) {
        Properties properties = getProperties();
        this.targetPackage = properties.getProperty("targetPackage");
        if (this.targetPackage == null) {
            warnings.add("请配置" + TAG + "插件的目标包名(targetPackage)！");
            return false;
        }
        this.basePackage = properties.getProperty("basePackage");
        if (this.basePackage == null) {
            warnings.add("请配置" + TAG + "插件的基础包名(basePackage)！");
            return false;
        }
        return true;
    }
}
