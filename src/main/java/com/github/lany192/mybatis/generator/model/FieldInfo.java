package com.github.lany192.mybatis.generator.model;

import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;

import java.io.Serializable;

@Getter
public class FieldInfo implements Serializable {
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

    private boolean identity;
    private boolean isColumnNameDelimited;
    private boolean isSequenceColumn;
    private boolean isAutoIncrement;
    private boolean isGeneratedColumn;
    private boolean isGeneratedAlways;

    public FieldInfo(IntrospectedColumn info) {
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
    }
}
