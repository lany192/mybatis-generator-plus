package com.github.lany192.mybatis.generator.model;

import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.Log;
import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;

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
     * 首字母小写名称
     */
    private String lowerName;
    /**
     * 实体完整名称
     */
    private String fullType;

    /**
     * 实体字段
     */
    private List<FieldInfo> fields;

    /**
     * Blob实体名称
     */
    private String blobName;
    /**
     * Blob实体完整名称
     */
    private String fullBlobType;
    /**
     * 首字母小写Blob名称
     */
    private String lowerBlobName;

    /**
     * 包含BLOBColumns字段
     */
    private boolean hasBlob;

    /**
     * 运行时是否是动态sql
     */
    private boolean isDynamicSql;

    public TableInfo(IntrospectedTable info) {
        isDynamicSql = info.getTargetRuntime().equals(IntrospectedTable.TargetRuntime.MYBATIS3_DSQL);

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(info.getBaseRecordType());
        name = type.getShortName();
        fullType = type.getFullyQualifiedName();
        lowerName = Introspector.decapitalize(type.getShortName());
        remark = info.getRemarks();
        tableName = info.getFullyQualifiedTable().getIntrospectedTableName();
        //是否包含BLOBColumns字段,注意：数量大于2个才会生成BLOB实体类
        if (info.getBLOBColumns() != null && info.getBLOBColumns().size() > 1) {
            hasBlob = true;
            FullyQualifiedJavaType blobType = new FullyQualifiedJavaType(info.getRecordWithBLOBsType());
            blobName = blobType.getShortName();
            fullBlobType = blobType.getFullyQualifiedName();
            lowerBlobName = Introspector.decapitalize(blobType.getShortName());
        } else {
            hasBlob = false;
        }
        List<FieldInfo> fields = new ArrayList<>();
        for (IntrospectedColumn item : info.getAllColumns()) {
            fields.add(new FieldInfo(item));
        }
        this.fields = fields;
    }
}
