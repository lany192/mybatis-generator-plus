package com.github.lany192.mybatis.generator.model;

import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.beans.Introspector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TableInfo implements Serializable {
    /**
     * 备注
     */
    private String remark;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 实体名称
     */
    private String name;
    /**
     * 实体完整名称
     */
    private String fullTypeName;
    /**
     * 首字母小写名称
     */
    private String lowerName;
    /**
     * 实体字段
     */
    private List<FieldInfo> fields;

    public TableInfo(IntrospectedTable table) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(table.getBaseRecordType());
        remark = table.getRemarks();
        tableName = table.getFullyQualifiedTable().getIntrospectedTableName();
        name = type.getShortName();
        fullTypeName = type.getFullyQualifiedName();
        lowerName = Introspector.decapitalize(type.getShortName());
        List<FieldInfo> fields = new ArrayList<>();
        for (IntrospectedColumn item : table.getAllColumns()) {
            fields.add(new FieldInfo(item));
        }
        this.fields = fields;
    }
}
