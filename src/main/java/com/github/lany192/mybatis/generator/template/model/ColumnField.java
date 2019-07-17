package com.github.lany192.mybatis.generator.template.model;

import lombok.Data;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.io.Serializable;

@Data
public class ColumnField implements Serializable {
    private TableInfo tableInfo;
    private String columnName;
    private String jdbcType;
    private String fieldName;
    private String remarks;
    private FullyQualifiedJavaType type;
    private String typePackage;
    private String shortTypeName;
    private String fullTypeName;
    private boolean identity;
    private boolean nullable;
    private boolean blobColumn;
    private boolean stringColumn;
    private boolean jdbcCharacterColumn;
    private boolean jdbcDateColumn;
    private boolean jdbcTimeColumn;
    private boolean sequenceColumn;
    private int length;
    private int scale;

    public ColumnField(IntrospectedColumn column) {
        type = column.getFullyQualifiedJavaType();
        typePackage = type.getPackageName();
        shortTypeName = type.getShortName();
        fullTypeName = type.getFullyQualifiedName();
        columnName = column.getActualColumnName();
        jdbcType = column.getJdbcTypeName();
        fieldName = column.getJavaProperty();
        remarks = column.getRemarks();
        identity = column.isIdentity();
        nullable = column.isNullable();
        sequenceColumn = column.isSequenceColumn();
        blobColumn = column.isBLOBColumn();
        stringColumn = column.isStringColumn();
        jdbcCharacterColumn = column.isJdbcCharacterColumn();
        jdbcDateColumn = column.isJDBCDateColumn();
        jdbcTimeColumn = column.isJDBCTimeColumn();
        length = column.getLength();
        scale = column.getScale();
    }
}
