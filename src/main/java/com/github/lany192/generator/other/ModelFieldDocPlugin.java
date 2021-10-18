package com.github.lany192.generator.other;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 实体增加api文档注解
 */
public class ModelFieldDocPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
//        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
//        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
//
//        String classAnnotation = "@ApiModel(value=\"" + topLevelClass.getType().getShortName() + "\", description = \"" + introspectedTable.getRemarks() + "\")";
//        if (!topLevelClass.getAnnotations().contains(classAnnotation)) {
//            topLevelClass.addAnnotation(classAnnotation);
//        }
        String comment = introspectedTable.getFullyQualifiedTable().toString() + "." + introspectedColumn.getActualColumnName();
//        field.addAnnotation("@ApiModelProperty(value=\"" + introspectedColumn.getRemarks() + "\", notes = \"" + comment + "\", dataType = \"" + introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName() + "\")");

        field.addJavaDocLine(introspectedColumn.getRemarks() + "\", notes = \"" + comment + "\", dataType = \"" + introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName());
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
