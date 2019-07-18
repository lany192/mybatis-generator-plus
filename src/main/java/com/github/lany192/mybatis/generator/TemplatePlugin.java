package com.github.lany192.mybatis.generator;


import com.github.lany192.mybatis.generator.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.Log;
import com.github.lany192.mybatis.generator.utils.StringsUtils;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Freemarker插件
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
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        TableInfo info = new TableInfo(introspectedTable);
        Log.i(TAG, fullyQualifiedTable.getIntrospectedTableName() + "表信息:" + JsonUtils.object2json(info));
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }
}