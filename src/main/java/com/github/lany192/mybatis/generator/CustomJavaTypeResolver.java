package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.StringsUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 指定某些字段为某种自定义类型
 *
 *
 * 用法:
 *
 *         <javaTypeResolver type="com.github.lany192.mybatis.generator.CustomJavaTypeResolver">
 *             <property name="sex" value="xxx.xxx.xxx.enums.SexEnum"/>
 *         </javaTypeResolver>
 *
 * @author Administrator
 */
public class CustomJavaTypeResolver extends JavaTypeResolverDefaultImpl {
    private Map<String, String> mFieldsMap = new HashMap<>();

    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (!StringsUtils.isEmpty(value)) {
                mFieldsMap.put(key, value);
            }
        }
        mFieldsMap.remove(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS);
        mFieldsMap.remove(PropertyRegistry.TYPE_RESOLVER_USE_JSR310_TYPES);
        System.out.println("自定义Java类型的属性映射关系:" + JsonUtils.object2json(mFieldsMap));
    }

    @Override
    public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        String tableFiledName = introspectedColumn.getActualColumnName();
        //判断是否是自定义字段
        if (mFieldsMap.containsKey(tableFiledName)) {
            String customJavaType = mFieldsMap.get(tableFiledName);
            System.out.println("数据库字段:" + tableFiledName + "，使用自定义的Java类型:" + customJavaType);
            return new FullyQualifiedJavaType(customJavaType);
        }
        return super.calculateJavaType(introspectedColumn);
    }
}