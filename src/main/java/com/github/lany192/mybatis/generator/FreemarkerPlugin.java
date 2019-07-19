package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.FileBuilder;
import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.Log;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

import java.beans.Introspector;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Freemarker插件
 *
 * @author Lany
 */
public class FreemarkerPlugin extends PluginAdapter {
    private final String TAG = getClass().getSimpleName();
    private Map<String, Object> params = new HashMap<>();

    @Override
    public boolean validate(List<String> warnings) {
        Properties properties = getProperties();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (StringUtility.stringHasValue(value)) {
                params.put(key, value);
            }
        }
        Log.i(TAG, "所有属性:" + JsonUtils.object2json(params));
        if (!StringUtility.stringHasValue(getProperty(Keys.PACKAGE_NAME))) {
            warnings.add(TAG + ":请配置" + Keys.PACKAGE_NAME + "属性");
            return false;
        }
        if (!StringUtility.stringHasValue(getProperty(Keys.TEMPLATE_NAME))) {
            warnings.add(TAG + ":请配置" + Keys.TEMPLATE_NAME + "属性");
            return false;
        }
        if (!StringUtility.stringHasValue(getProperty(Keys.FILE_SUFFIX))) {
            warnings.add(TAG + ":请配置" + Keys.FILE_SUFFIX + "属性");
            return false;
        }
        if (!StringUtility.stringHasValue(getProperty(Keys.FILE_FORMAT))) {
            warnings.add(TAG + ":请配置" + Keys.FILE_FORMAT + "属性");
            return false;
        }
        return true;
    }

    private String getProperty(String key) {
        return (String) params.get(key);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        Map<String, Object> data = new HashMap<>(params);
        data.remove(Keys.TEMPLATE_PATH);
        data.remove(Keys.TEMPLATE_NAME);
        data.remove(Keys.FILE_SUFFIX);
        data.remove(Keys.FILE_FORMAT);
        data.remove(Keys.ROOT_DIR_PATH);
        data.remove(Keys.MODULE_NAME);

        JavaModelGeneratorConfiguration modelConfig = getContext().getJavaModelGeneratorConfiguration();
        String modelType = introspectedTable.getBaseRecordType();
        String modelPackage = modelConfig.getTargetPackage();
        String modelName = modelType.replace(modelPackage + ".", "");


        data.put("modelType", modelType);
        data.put("modelPackage", modelPackage);
        data.put("modelName", modelName);
        data.put("modelNameLower", Introspector.decapitalize(modelName));


        String templatePath = getProperty(Keys.TEMPLATE_PATH);
        if (!StringUtility.stringHasValue(templatePath)) {
            templatePath = System.getProperty("user.dir")
                    + File.separator + "src"
                    + File.separator + "main"
                    + File.separator + "resources"
                    + File.separator + "templates"
                    + File.separator + "autocode";
        } else {
            templatePath = System.getProperty("user.dir") + templatePath;
        }

        String moduleName = getProperty(Keys.MODULE_NAME);
        if (!StringUtility.stringHasValue(moduleName)) {
            moduleName = "";
        }
        String rootDirPath = getProperty(Keys.ROOT_DIR_PATH);
        if (!StringUtility.stringHasValue(rootDirPath)) {
            rootDirPath = "";
        }
        new FileBuilder()
                .rootPath(rootDirPath)
                .module(moduleName)
                .path("src/main/java")
                .packageName(getProperty(Keys.PACKAGE_NAME))
                .modelName(modelName)
                .suffix(getProperty(Keys.FILE_SUFFIX))
                .format(getProperty(Keys.FILE_FORMAT))
                .setTemplatePath(templatePath)
                .setTemplateName(getProperty(Keys.TEMPLATE_NAME))
                .setData(data)
                .build();
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }

}
