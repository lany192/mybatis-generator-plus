package com.github.lany192.mybatis.generator.model;

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
     * 如果是自定义枚举类，所有值得几个
     */
    private List<Map<String, Object>> enums;

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
            enums = getEnumValues(fullTypeName);
        }
    }

    /**
     * 获取枚举的所有值
     */
    private static List<Map<String, Object>> getEnumValues(String className) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isEnum()) {
                System.out.println(clazz.getSimpleName() + "是枚举");
                //得到enum的所有实例
                Object[] objects = clazz.getEnumConstants();
                for (Object item : objects) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("index", clazz.getMethod("ordinal").invoke(item));
                    map.put("name", clazz.getMethod("name").invoke(item));
                    list.add(map);
                }
            } else {
                System.out.println(clazz.getSimpleName() + "不是枚举");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
