package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.Log;
import com.github.lany192.mybatis.generator.utils.StringsUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Freemarker插件
 *
 * @author Lany
 */
public class TemplatePlugin extends PluginAdapter {
    private final String TAG = getClass().getSimpleName();
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
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }
}
