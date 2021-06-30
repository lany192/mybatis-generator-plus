package com.github.lany192.generator.other;

import com.github.lany192.generator.utils.OtherUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 实体增加api文档注解
 */
public class SwaggerPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");

        String classAnnotation = "@ApiModel(value=\"" + topLevelClass.getType().getShortName() + "\", description = \"" + introspectedTable.getRemarks() + "\")";
        if (!topLevelClass.getAnnotations().contains(classAnnotation)) {
            topLevelClass.addAnnotation(classAnnotation);
        }
        //如果有默认值，打印默认值
        if (OtherUtils.isEmpty(introspectedColumn.getDefaultValue())) {
            field.addAnnotation("@ApiModelProperty(value=\"" + introspectedColumn.getRemarks() + "\")");
        } else {
            field.addAnnotation("@ApiModelProperty(value=\"" + introspectedColumn.getRemarks() + "\", example = \"" + introspectedColumn.getDefaultValue() + "\")");
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
