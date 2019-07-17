package com.github.lany192.mybatis.generator;


import com.github.lany192.mybatis.generator.template.file.TemplateFile;
import com.github.lany192.mybatis.generator.template.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.Log;
import com.github.lany192.mybatis.generator.utils.StringsUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * Freemarker插件
 */
public class TemplatePlugin extends PluginAdapter {
    private final String TAG = getClass().getSimpleName();
    private Map<String, Object> params = new HashMap<>();
    /**
     * 项目路径（目录需要已经存在）
     */
    private String targetProject;
    /**
     * 生成的包（路径不存在则创建）
     */
    private String targetPackage;
    /**
     * 模板路径
     */
    private String templatePath;
    /**
     * 模板内容
     */
    private String templateContent;
    /**
     * 文件名模板，通过模板方式生成文件名，包含后缀
     */
    private String fileName;

    /**
     * 编码
     */
    private String encoding;

    /**
     * 读取文件
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    protected String read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        StringBuffer stringBuffer = new StringBuffer();
        String line = reader.readLine();
        while (line != null) {
            stringBuffer.append(line).append("\n");
            line = reader.readLine();
        }
        return stringBuffer.toString();
    }

    @Override
    public boolean validate(List<String> warnings) {
        Properties properties = getProperties();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (!StringsUtils.isEmpty(value)) {
                params.put(key, value);
            }
        }

        boolean right = true;
        if (!StringUtility.stringHasValue(fileName)) {
            warnings.add("没有配置 \"fileName\" 文件名模板，因此不会生成任何额外代码!");
            right = false;
        }
        if (!StringUtility.stringHasValue(templatePath)) {
            warnings.add("没有配置 \"templatePath\" 模板路径，因此不会生成任何额外代码!");
            right = false;
        } else {
            try {
                URL resourceUrl = null;
                try {
                    resourceUrl = ObjectFactory.getResource(templatePath);
                } catch (Exception e) {
                    warnings.add("本地加载\"templatePath\" 模板路径失败，尝试 URL 方式获取!");
                }
                if (resourceUrl == null) {
                    resourceUrl = new URL(templatePath);
                }
                InputStream inputStream = resourceUrl.openConnection().getInputStream();
                templateContent = read(inputStream);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                warnings.add("读取模板文件出错: " + e.getMessage());
                right = false;
            }
        }
        if (!right) {
            return false;
        }
        int errorCount = 0;
        if (!StringUtility.stringHasValue(targetProject)) {
            errorCount++;
            warnings.add("没有配置 \"targetProject\" 路径!");
        }
        if (!StringUtility.stringHasValue(targetPackage)) {
            errorCount++;
            warnings.add("没有配置 \"targetPackage\" 路径!");
        }
        if (errorCount >= 2) {
            warnings.add("由于没有配置任何有效路径，不会生成任何额外代码!");
            return false;
        }
        return true;
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
        Log.i(TAG, "TableRemarks:" + introspectedTable.getRemarks());
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

        List<GeneratedJavaFile> files = new ArrayList<>();
        files.add(new TemplateFile(new TableInfo(introspectedTable), properties, targetProject, targetPackage, fileName, templateContent));
        return files;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.targetProject = properties.getProperty("targetProject");
        this.targetPackage = properties.getProperty("targetPackage");
        this.templatePath = properties.getProperty("templatePath");
        this.fileName = properties.getProperty("fileName");
        this.encoding = properties.getProperty("encoding", "UTF-8");
    }
}