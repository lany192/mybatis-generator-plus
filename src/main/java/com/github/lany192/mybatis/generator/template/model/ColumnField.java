package com.github.lany192.mybatis.generator.template.model;

import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.io.Serializable;

@Getter
public class ColumnField implements Serializable {
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

    public ColumnField(IntrospectedColumn info) {
        defaultValue = info.getDefaultValue();
        FullyQualifiedJavaType type = info.getFullyQualifiedJavaType();
        column = info.getActualColumnName();
        name = info.getJavaProperty();
        remark = info.getRemarks();
        length = info.getLength();
        nullable = info.isNullable();
        typeName = type.getShortName();
        fullTypeName = type.getFullyQualifiedName();
    }
}
