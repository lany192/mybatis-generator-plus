package com.github.lany192.mybatis.generator.model;

import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.Log;
import com.github.lany192.mybatis.generator.utils.OtherUtils;
import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ColumnModel implements Serializable {
    /**
     * 表中字段名称
     */
    private String column;
    /**
     * 字段名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 长度
     */
    private int length;
    /**
     * 是否能置空
     */
    private boolean nullable;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型全称
     */
    private String fullTypeName;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 自定义的字段类型
     */
    private boolean isCustomType;
    /**
     * 是否是枚举类型
     */
    private boolean isEnum;

    /**
     * 自定义类的值
     */
    private String customValue;

    private boolean identity;
    private boolean isColumnNameDelimited;
    private boolean isSequenceColumn;
    private boolean isAutoIncrement;
    private boolean isGeneratedColumn;
    private boolean isGeneratedAlways;

    public ColumnModel(IntrospectedColumn info) {
        identity = info.isIdentity();
        isColumnNameDelimited = info.isColumnNameDelimited();
        isSequenceColumn = info.isSequenceColumn();
        isAutoIncrement = info.isAutoIncrement();
        isGeneratedColumn = info.isGeneratedColumn();
        isGeneratedAlways = info.isGeneratedAlways();
        tableAlias = info.getTableAlias();
        defaultValue = info.getDefaultValue();
        column = info.getActualColumnName();
        name = info.getJavaProperty();
        remark = info.getRemarks();
        length = info.getLength();
        nullable = info.isNullable();
        typeName = info.getFullyQualifiedJavaType().getShortName();
        fullTypeName = info.getFullyQualifiedJavaType().getFullyQualifiedName();
        isCustomType = OtherUtils.isJdbcType(info.getJdbcType());
        if (isCustomType) {
            customValue = getCustomValue(fullTypeName);
        }
    }

    /**
     * 获取枚举的所有值
     */
    private String getCustomValue(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            isEnum = clazz.isEnum();
            if (isEnum) {
                //得到enum的所有实例
                Object[] objects = clazz.getEnumConstants();
                List<Map<String, Object>> list = new ArrayList<>();
                for (Object item : objects) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("value", clazz.getMethod("ordinal").invoke(item));
                    map.put("label", clazz.getMethod("name").invoke(item));
                    list.add(map);
                }
                return JsonUtils.object2json(list);
            } else if (clazz.isArray()) {
                Log.i("数组的元素类型是:" + clazz.getComponentType().getName());
                return "[]";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
