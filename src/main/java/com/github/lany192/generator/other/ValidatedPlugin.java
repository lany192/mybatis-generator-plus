package com.github.lany192.generator.other;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Objects;

/**
 * 检查校验字段
 */
public class ValidatedPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (!introspectedColumn.isNullable()) {
            if (Objects.equals(introspectedColumn.getFullyQualifiedJavaType(), new FullyQualifiedJavaType(String.class.getTypeName()))) {
                topLevelClass.addImportedType("jakarta.validation.constraints.NotBlank");
                field.addAnnotation("@NotBlank(message = \"" + introspectedTable.getRemarks() + "不能为空\")");
            } else {
                topLevelClass.addImportedType("jakarta.validation.constraints.NotNull");
                field.addAnnotation("@NotNull(message = \"" + introspectedTable.getRemarks() + "不能为null\")");
            }
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
