package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.CodeBuilder;
import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.Log;
import com.github.lany192.mybatis.generator.utils.StringsUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
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
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        return super.contextGenerateAdditionalJavaFiles();
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        Log.i(TAG, "***********************************************");
        Log.i(TAG, "TableName:" + fullyQualifiedTable.getIntrospectedTableName());
        Log.i(TAG, "DomainObjectName:" + fullyQualifiedTable.getDomainObjectName());

        Log.i(TAG, "DomainObjectSubPackage:" + fullyQualifiedTable.getDomainObjectSubPackage());
        Log.i(TAG, "Alias:" + fullyQualifiedTable.getAlias());
        Log.i(TAG, "Ibatis2SqlMapNamespace:" + fullyQualifiedTable.getIbatis2SqlMapNamespace());
        Log.i(TAG, "Catalog:" + fullyQualifiedTable.getIntrospectedCatalog());
        Log.i(TAG, "Schema:" + fullyQualifiedTable.getIntrospectedSchema());


        Log.i(TAG, "BaseRecordType:" + introspectedTable.getBaseRecordType());
        Log.i(TAG, "RecordWithBLOBsType:" + introspectedTable.getRecordWithBLOBsType());
        Log.i(TAG, "ExampleType:" + introspectedTable.getExampleType());
        Log.i(TAG, "Remarks:" + introspectedTable.getRemarks());
        Log.i(TAG, "DAOImplementationType:" + introspectedTable.getDAOImplementationType());
        Log.i(TAG, "DAOInterfaceType:" + introspectedTable.getDAOInterfaceType());
        Log.i(TAG, "SqlMapFileName:" + introspectedTable.getIbatis2SqlMapFileName());
        Log.i(TAG, "SqlMapPackage:" + introspectedTable.getIbatis2SqlMapPackage());
        Log.i(TAG, "XmlMapperFileName:" + introspectedTable.getMyBatis3XmlMapperFileName());
        Log.i(TAG, "XmlMapperPackage:" + introspectedTable.getMyBatis3XmlMapperPackage());
        Log.i(TAG, "JavaMapperType:" + introspectedTable.getMyBatis3JavaMapperType());


        Log.i(TAG, "以下是表的字段:");
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columns) {
            Log.i(TAG, "ActualColumnName:" + column.getActualColumnName());
            Log.i(TAG, "JavaProperty:" + column.getJavaProperty());
            Log.i(TAG, "Remarks:" + column.getRemarks());
            Log.i(TAG, "JdbcTypeName:" + column.getJdbcTypeName());
            Log.i(TAG, "JdbcType:" + column.getJdbcType());
            Log.i(TAG, "DefaultValue:" + column.getDefaultValue());
            Log.i(TAG, "Length:" + column.getLength());
            Log.i(TAG, "Scale:" + column.getScale());
            Log.i(TAG, "TableAlias:" + column.getTableAlias());
            Log.i(TAG, "TypeHandler:" + column.getTypeHandler());

            FullyQualifiedJavaType javaType = column.getFullyQualifiedJavaType();
            Log.i(TAG, "FullyQualifiedName:" + javaType.getFullyQualifiedName());
            Log.i(TAG, "ShortName:" + javaType.getShortName());
            Log.i(TAG, "PackageName:" + javaType.getPackageName());
            Log.i(TAG, "FullyQualifiedName2:" + javaType.getFullyQualifiedNameWithoutTypeParameters());
            Log.i(TAG, "ShortName2:" + javaType.getShortNameWithoutTypeArguments());
            Log.i(TAG, "-----------------------------------");
        }
        Log.i(TAG, "\n\n\n\n");
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
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
