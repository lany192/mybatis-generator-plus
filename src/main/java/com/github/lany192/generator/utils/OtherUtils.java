package com.github.lany192.generator.utils;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class OtherUtils {

    /***
     * 驼峰命名转成路径
     *
     * @param str  驼峰命名的字符串
     */
    public static String hump2path(String str) {
        StringBuilder builder = new StringBuilder(str);
        int temp = 0;//定位
        //判断是否有路径符号/或者\
        if (!(str.contains("/") || str.contains("\\"))) {
            for (int i = 0; i < str.length(); i++) {
                if (Character.isUpperCase(str.charAt(i))) {
                    builder.insert(i + temp, File.separator);
                    temp += 1;
                }
            }
        }
        return builder.toString().toLowerCase();
    }

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para 下划线命名的字符串
     */
    public static String underline2hump(String para) {
        final String UNDERLINE = "_";
        StringBuilder result = new StringBuilder();
        String[] a = para.split(UNDERLINE);
        for (String s : a) {
            if (!para.contains(UNDERLINE)) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para 驼峰命名的字符串
     */
    public static String hump2underline(String para) {
        final String UNDERLINE = "_";
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        if (!para.contains(UNDERLINE)) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 是否是自定义类型
     *
     * @param jdbcType
     * @return
     */
    public static boolean isJdbcType(int jdbcType) {
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
