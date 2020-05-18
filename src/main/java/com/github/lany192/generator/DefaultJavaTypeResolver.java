package com.github.lany192.generator;

import com.github.lany192.generator.utils.Log;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 指定某些字段为某种自定义类型
 * <p>
 * <p>
 * 用法:
 *
 * <javaTypeResolver type="com.github.lany192.mybatis.generator.DefaultJavaTypeResolver">
 *      <property name="sex" value="xxx.xxx.xxx.enums.SexEnum"/>
 * </javaTypeResolver>
 */
public class DefaultJavaTypeResolver extends JavaTypeResolverDefaultImpl {
    private final String TAG = getClass().getSimpleName();
    private Map<String, String> mFieldsMap = new HashMap<>();

    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (StringUtility.stringHasValue(value)) {
                mFieldsMap.put(key, value);
            }
        }
        mFieldsMap.remove(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS);
        mFieldsMap.remove(PropertyRegistry.TYPE_RESOLVER_USE_JSR310_TYPES);
    }

    @Override
    public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        String tableFiledName = introspectedColumn.getActualColumnName();
        //判断是否是自定义字段
        if (mFieldsMap.containsKey(tableFiledName)) {
            String customJavaType = mFieldsMap.get(tableFiledName);
            Log.i(TAG, tableFiledName + "字段转成自定义类型:" + customJavaType);
            return new FullyQualifiedJavaType(customJavaType);
        }
        return super.calculateJavaType(introspectedColumn);
    }
}