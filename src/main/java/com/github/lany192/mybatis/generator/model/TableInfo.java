package com.github.lany192.mybatis.generator.model;

import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TableInfo {
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

    /**
     * 包含BLOBColumns字段
     */
    private boolean hasBlob;

    public TableInfo(IntrospectedTable info) {
        //是否包含BLOBColumns字段
        if (info.getBLOBColumns() != null && info.getBLOBColumns().size() > 0) {
            hasBlob = true;
        }
        FullyQualifiedJavaType type;
        if (hasBlob) {
            type = new FullyQualifiedJavaType(info.getRecordWithBLOBsType());
        } else {
            type = new FullyQualifiedJavaType(info.getBaseRecordType());
        }
        name = type.getShortName();
        fullTypeName = type.getFullyQualifiedName();
        lowerName = Introspector.decapitalize(type.getShortName());

        remark = info.getRemarks();
        tableName = info.getFullyQualifiedTable().getIntrospectedTableName();
        List<FieldInfo> fields = new ArrayList<>();
        for (IntrospectedColumn item : info.getAllColumns()) {
            fields.add(new FieldInfo(item));
        }
        this.fields = fields;
    }
}
