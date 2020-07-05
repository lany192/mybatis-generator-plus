package com.github.lany192.generator.other;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class JsonFormatPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (introspectedColumn.getJdbcTypeName().equals("java.time.LocalDateTime")) {
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonFormat");
            field.addAnnotation("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
        }
        if (introspectedColumn.getJdbcTypeName().equals("java.time.LocalTime")) {
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonFormat");
            field.addAnnotation("@JsonFormat(pattern = \"HH:mm:ss\")");
        }
        if (introspectedColumn.getJdbcTypeName().equals("java.time.LocalDate")) {
            topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonFormat");
            field.addAnnotation("@JsonFormat(pattern = \"yyyy-MM-dd\")");
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
