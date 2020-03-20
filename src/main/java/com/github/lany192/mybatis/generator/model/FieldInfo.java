package com.github.lany192.mybatis.generator.model;

import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 自定义的字段类型
     */
    private boolean isCustomType;

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
        isCustomType = isJdbcType(info.getJdbcType());
    }

    /**
     * 是否是自定义类型
     *
     * @param jdbcType
     * @return
     */
    private boolean isJdbcType(int jdbcType) {
        Map<Integer, JavaTypeResolverDefaultImpl.JdbcTypeInformation> jdbcTypeMap = new HashMap<>();
        jdbcTypeMap.put(2003, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("ARRAY", new FullyQualifiedJavaType(Object.class.getName())));
        jdbcTypeMap.put(-5, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("BIGINT", new FullyQualifiedJavaType(Long.class.getName())));
        jdbcTypeMap.put(-2, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("BINARY", new FullyQualifiedJavaType("byte[]")));
        jdbcTypeMap.put(-7, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("BIT", new FullyQualifiedJavaType(Boolean.class.getName())));
        jdbcTypeMap.put(2004, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("BLOB", new FullyQualifiedJavaType("byte[]")));
        jdbcTypeMap.put(16, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("BOOLEAN", new FullyQualifiedJavaType(Boolean.class.getName())));
        jdbcTypeMap.put(1, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("CHAR", new FullyQualifiedJavaType(String.class.getName())));
        jdbcTypeMap.put(2005, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("CLOB", new FullyQualifiedJavaType(String.class.getName())));
        jdbcTypeMap.put(70, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("DATALINK", new FullyQualifiedJavaType(Object.class.getName())));
        jdbcTypeMap.put(91, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("DATE", new FullyQualifiedJavaType(java.util.Date.class.getName())));
        jdbcTypeMap.put(3, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("DECIMAL", new FullyQualifiedJavaType(BigDecimal.class.getName())));
        jdbcTypeMap.put(2001, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("DISTINCT", new FullyQualifiedJavaType(Object.class.getName())));
        jdbcTypeMap.put(8, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("DOUBLE", new FullyQualifiedJavaType(Double.class.getName())));
        jdbcTypeMap.put(6, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("FLOAT", new FullyQualifiedJavaType(Double.class.getName())));
        jdbcTypeMap.put(4, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("INTEGER", new FullyQualifiedJavaType(Integer.class.getName())));
        jdbcTypeMap.put(2000, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("JAVA_OBJECT", new FullyQualifiedJavaType(Object.class.getName())));
        jdbcTypeMap.put(-16, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("LONGNVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        jdbcTypeMap.put(-4, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("LONGVARBINARY", new FullyQualifiedJavaType("byte[]")));
        jdbcTypeMap.put(-1, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("LONGVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        jdbcTypeMap.put(-15, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("NCHAR", new FullyQualifiedJavaType(String.class.getName())));
        jdbcTypeMap.put(2011, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("NCLOB", new FullyQualifiedJavaType(String.class.getName())));
        jdbcTypeMap.put(-9, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("NVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        jdbcTypeMap.put(0, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("NULL", new FullyQualifiedJavaType(Object.class.getName())));
        jdbcTypeMap.put(2, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("NUMERIC", new FullyQualifiedJavaType(BigDecimal.class.getName())));
        jdbcTypeMap.put(1111, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("OTHER", new FullyQualifiedJavaType(Object.class.getName())));
        jdbcTypeMap.put(7, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("REAL", new FullyQualifiedJavaType(Float.class.getName())));
        jdbcTypeMap.put(2006, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("REF", new FullyQualifiedJavaType(Object.class.getName())));
        jdbcTypeMap.put(5, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("SMALLINT", new FullyQualifiedJavaType(Short.class.getName())));
        jdbcTypeMap.put(2002, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("STRUCT", new FullyQualifiedJavaType(Object.class.getName())));
        jdbcTypeMap.put(92, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("TIME", new FullyQualifiedJavaType(java.util.Date.class.getName())));
        jdbcTypeMap.put(93, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("TIMESTAMP", new FullyQualifiedJavaType(java.util.Date.class.getName())));
        jdbcTypeMap.put(-6, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Byte.class.getName())));
        jdbcTypeMap.put(-3, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("VARBINARY", new FullyQualifiedJavaType("byte[]")));
        jdbcTypeMap.put(12, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("VARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        jdbcTypeMap.put(2013, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("TIME_WITH_TIMEZONE", new FullyQualifiedJavaType("java.time.OffsetTime")));
        jdbcTypeMap.put(2014, new JavaTypeResolverDefaultImpl.JdbcTypeInformation("TIMESTAMP_WITH_TIMEZONE", new FullyQualifiedJavaType("java.time.OffsetDateTime")));

        return jdbcTypeMap.containsKey(jdbcType);
    }
}
