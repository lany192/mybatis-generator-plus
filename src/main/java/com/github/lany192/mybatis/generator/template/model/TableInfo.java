package com.github.lany192.mybatis.generator.template.model;

import lombok.Data;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.beans.Introspector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TableInfo implements Serializable {
    private IntrospectedTable introspectedTable;
    private FullyQualifiedJavaType type;

    private String tableName;
    private String variableName;
    private String lowerCaseName;
    private String shortClassName;
    private String fullClassName;
    private String packageName;
    private String remarks;

    private List<ColumnField> pkFields;
    private List<ColumnField> baseFields;
    private List<ColumnField> blobFields;
    private List<ColumnField> allFields;

    public TableInfo(IntrospectedTable table) {
        setIntrospectedTable(table);
        setRemarks(table.getRemarks());

        FullyQualifiedTable fullyQualifiedTable = table.getFullyQualifiedTable();
        setTableName(fullyQualifiedTable.getIntrospectedTableName());

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(table.getBaseRecordType());
        setType(type);
        setVariableName(Introspector.decapitalize(type.getShortName()));
        setLowerCaseName(type.getShortName().toLowerCase());
        setShortClassName(type.getShortName());
        setFullClassName(type.getFullyQualifiedName());
        setPackageName(type.getPackageName());

        List<ColumnField> pkFields = new ArrayList<>();
        List<ColumnField> baseFields = new ArrayList<>();
        List<ColumnField> blobFields = new ArrayList<>();
        List<ColumnField> allFields = new ArrayList<>();
        for (IntrospectedColumn column : table.getPrimaryKeyColumns()) {
            ColumnField field = new ColumnField(column);
            field.setTableInfo(this);
            pkFields.add(field);
            allFields.add(field);
        }
        for (IntrospectedColumn column : table.getBaseColumns()) {
            ColumnField field = new ColumnField(column);
            field.setTableInfo(this);
            baseFields.add(field);
            allFields.add(field);
        }
        for (IntrospectedColumn column : table.getBLOBColumns()) {
            ColumnField field = new ColumnField(column);
            field.setTableInfo(this);
            blobFields.add(field);
            allFields.add(field);
        }
        setPkFields(pkFields);
        setBaseFields(baseFields);
        setBlobFields(blobFields);
        setAllFields(allFields);
    }

}
