package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.CodeBuilder;
import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.StringsUtils;
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
 * Freemarker插件
 *
 * @author Lany
 */
public class FreemarkerPlugin extends PluginAdapter {
    private final String TAG = getClass().getSimpleName();
    private final String PACKAGE_NAME = "packageName";
    private final String TEMPLATE_NAME = "templateName";
    private final String FILE_SUFFIX = "fileSuffix";
    private final String FILE_FORMAT = "fileFormat";
    private final String ROOT_DIR_PATH = "rootDirPath";
    private Map<String, Object> params = new HashMap<>();

    @Override
    public boolean validate(List<String> warnings) {
        Properties properties = getProperties();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (!StringsUtils.isEmpty(value)) {
                params.put(key, value);
            }
        }
        if (StringsUtils.isEmpty((String) params.get(PACKAGE_NAME))) {
            warnings.add("请配置" + TAG + "插件的" + PACKAGE_NAME + "属性");
            return false;
        }
        if (StringsUtils.isEmpty((String) params.get(TEMPLATE_NAME))) {
            warnings.add("请配置" + TAG + "插件的" + TEMPLATE_NAME + "属性");
            return false;
        }
        if (StringsUtils.isEmpty((String) params.get(FILE_SUFFIX))) {
            warnings.add("请配置" + TAG + "插件的" + FILE_SUFFIX + "属性");
            return false;
        }
        if (StringsUtils.isEmpty((String) params.get(FILE_FORMAT))) {
            warnings.add("请配置" + TAG + "插件的" + FILE_FORMAT + "属性");
            return false;
        }
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        System.out.println("FreemarkerPlugin属性:" + JsonUtils.object2json(params));
        final String TEMPLATE_PATH = "templatePath";
        final String MODULE_NAME = "moduleName";

        Map<String, Object> data = new HashMap<>(params);
        data.remove(TEMPLATE_PATH);
        data.remove(TEMPLATE_NAME);
        data.remove(FILE_SUFFIX);
        data.remove(FILE_FORMAT);
        data.remove(ROOT_DIR_PATH);
        data.remove(MODULE_NAME);

        JavaModelGeneratorConfiguration modelConfig = getContext().getJavaModelGeneratorConfiguration();
        String modelType = introspectedTable.getBaseRecordType();
        String modelPackage = modelConfig.getTargetPackage();
        String modelName = modelType.replace(modelPackage + ".", "");


        data.put("modelType", modelType);
        data.put("modelPackage", modelPackage);
        data.put("modelNameUpper", modelName);
        data.put("modelNameLower", toLower(modelName));


        String templatePath = (String) params.get(TEMPLATE_PATH);
        if (StringsUtils.isEmpty(templatePath)) {
            templatePath = System.getProperty("user.dir")
                    + File.separator + "src"
                    + File.separator + "main"
                    + File.separator + "resources"
                    + File.separator + "templates"
                    + File.separator + "autocode";
        } else {
            templatePath = System.getProperty("user.dir") + templatePath;
        }
        System.out.println("Freemarker模板属性:" + JsonUtils.object2json(data));

        String moduleName = (String) params.get(MODULE_NAME);
        if (StringsUtils.isEmpty(moduleName)) {
            moduleName = "";
        }
        String rootDirPath = (String) params.get(ROOT_DIR_PATH);
        if (StringsUtils.isEmpty(rootDirPath)) {
            rootDirPath = "";
        }
        new CodeBuilder()
                .rootPath(rootDirPath)
                .module(moduleName)
                .path("src/main/java")
                .packageName((String) params.get(PACKAGE_NAME))
                .modelName(modelName)
                .suffix((String) params.get(FILE_SUFFIX))
                .format((String) params.get(FILE_FORMAT))
                .setTemplatePath(templatePath)
                .setTemplateName((String) params.get(TEMPLATE_NAME))
                .setData(data)
                .build();
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 首字母转小写
     *
     * @param name 单词
     */
    private String toLower(String name) {
        if (Character.isLowerCase(name.charAt(0))) {
            return name;
        } else {
            return Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
    }

}
