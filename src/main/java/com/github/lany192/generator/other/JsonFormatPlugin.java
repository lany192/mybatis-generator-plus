package com.github.lany192.generator.other;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class JsonFormatPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addAnnotations(topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addAnnotations(topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addAnnotations(topLevelClass);
        return true;
    }

    private void addAnnotations(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonInclude"));
        topLevelClass.addAnnotation("@JsonInclude(value = JsonInclude.Include.NON_NULL)");
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName().equals("java.time.LocalDateTime")) {
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonFormat");
            field.addAnnotation("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
        }
        if (introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName().equals("java.time.LocalTime")) {
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonFormat");
            field.addAnnotation("@JsonFormat(pattern = \"HH:mm:ss\")");
        }
        if (introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName().equals("java.time.LocalDate")) {
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonFormat");
            field.addAnnotation("@JsonFormat(pattern = \"yyyy-MM-dd\")");
        }
        if (introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName().equals("java.util.Date")) {
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonFormat");
            field.addAnnotation("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
