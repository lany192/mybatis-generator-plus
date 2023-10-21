package com.github.lany192.generator.other;

import com.github.lany192.generator.BasePlugin;
import com.github.lany192.generator.Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 检查校验字段
 */
public class ValidationPlugin extends BasePlugin {
    private List<String> columns;

    @Override
    public boolean validate(List<String> warnings) {
        if (!containsKey(Constants.IGNORE_COLUMNS)) {
            warnings.add(TAG + ":请配置忽略字段" + Constants.IGNORE_COLUMNS + "属性");
            return false;
        }
        String value = getProperty(Constants.IGNORE_COLUMNS, "");
        columns = Arrays.asList(value.split(","));
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (!introspectedColumn.isNullable()) {
            if (ObjectUtils.isEmpty(columns) || !columns.contains(introspectedColumn.getActualColumnName())) {
                if (Objects.equals(introspectedColumn.getFullyQualifiedJavaType(), new FullyQualifiedJavaType(String.class.getTypeName()))) {
                    topLevelClass.addImportedType("jakarta.validation.constraints.NotBlank");
                    field.addAnnotation("@NotBlank(message = \"" + introspectedColumn.getRemarks() + "不能为空\")");
                } else {
                    topLevelClass.addImportedType("jakarta.validation.constraints.NotNull");
                    field.addAnnotation("@NotNull(message = \"" + introspectedColumn.getRemarks() + "不能为null\")");
                }
            }
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
