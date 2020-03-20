package com.github.lany192.mybatis.generator.model;

import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TableInfo {
    private Map<String, Object> map = new HashMap<>();
    private String name;
    /**
     * 实体完整名称
     */
    private String fullType;
    /**
     * 表名
     */
    private String tableName;

    public TableInfo(IntrospectedTable info) {
        //运行时是否是动态sql
        map.put("is_dynamic_sql", info.getTargetRuntime().equals(IntrospectedTable.TargetRuntime.MYBATIS3_DSQL));
        //备注
        map.put("remark", info.getRemarks());
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(info.getBaseRecordType());
        name = type.getShortName();
        //实体名称
        map.put("name", name);
        //表名
        tableName = info.getFullyQualifiedTable().getIntrospectedTableName();
        map.put("table_name", tableName);
        //实体完整名称
        fullType = type.getFullyQualifiedName();
        map.put("full_model_type", fullType);
        //首字母小写名称
        map.put("lower_model_name", Introspector.decapitalize(type.getShortName()));
        //作者
        map.put("author", System.getProperty("user.name"));

        List<FieldInfo> fields = new ArrayList<>();
        for (IntrospectedColumn item : info.getAllColumns()) {
            fields.add(new FieldInfo(item));
        }
        //实体字段
        map.put("fields", fields);
        //包含BLOBColumns字段
        boolean hasBlob = info.getBLOBColumns() != null && info.getBLOBColumns().size() > 1;
        map.put("has_blob_column", hasBlob);
        //是否包含BLOBColumns字段,注意：数量大于2个才会生成BLOB实体类
        if (hasBlob) {
            FullyQualifiedJavaType blobType = new FullyQualifiedJavaType(info.getRecordWithBLOBsType());
            //Blob实体名称
            map.put("blob_model_name", blobType.getShortName());
            //Blob实体完整名称
            map.put("full_blob_model_type", blobType.getFullyQualifiedName());
            //首字母小写Blob名称
            map.put("lower_blob_model_name", Introspector.decapitalize(blobType.getShortName()));
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
