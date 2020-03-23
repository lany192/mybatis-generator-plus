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
public class TableModel {
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

    public TableModel(IntrospectedTable info) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(info.getBaseRecordType());
        name = type.getShortName();
        tableName = info.getFullyQualifiedTable().getIntrospectedTableName();
        fullType = type.getFullyQualifiedName();
        List<ColumnModel> columns = new ArrayList<>();
        for (IntrospectedColumn item : info.getAllColumns()) {
            columns.add(new ColumnModel(item));
        }
        boolean hasBlob = info.getBLOBColumns() != null && info.getBLOBColumns().size() > 1;

        //运行时是否是动态sql
        map.put("is_dynamic_sql", info.getTargetRuntime().equals(IntrospectedTable.TargetRuntime.MYBATIS3_DSQL));
        //表名
        map.put("table_name", tableName);
        //备注
        map.put("table_remark", info.getRemarks());
        //实体名称
        map.put("model_name", name);
        //实体完整名称
        map.put("model_full_type", fullType);
        //首字母小写名称
        map.put("model_lower_name", Introspector.decapitalize(type.getShortName()));
        //作者
        map.put("author", System.getProperty("user.name"));
        //实体字段
        map.put("columns", columns);
        //包含BLOBColumns字段
        map.put("has_blob_column", hasBlob);
        //是否包含BLOBColumns字段,注意：数量大于2个才会生成BLOB实体类
        if (hasBlob) {
            FullyQualifiedJavaType blobType = new FullyQualifiedJavaType(info.getRecordWithBLOBsType());
            //Blob实体名称
            map.put("blob_model_name", blobType.getShortName());
            //Blob实体完整名称
            map.put("blob_model_full_type", blobType.getFullyQualifiedName());
            //首字母小写Blob名称
            map.put("blob_model_lower_name", Introspector.decapitalize(blobType.getShortName()));
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
