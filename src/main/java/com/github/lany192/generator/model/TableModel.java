package com.github.lany192.generator.model;

import com.github.lany192.generator.utils.OtherUtils;
import lombok.Getter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.beans.Introspector;
import java.util.*;

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
    /**
     * 主键类型
     */
    private String primaryKeyType;
    /**
     * 驼峰命名的名称转成的路径
     */
    private String modelNamePath;

    public TableModel(IntrospectedTable info, String author) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(info.getBaseRecordType());
        name = type.getShortName();
        tableName = info.getFullyQualifiedTable().getIntrospectedTableName();
        fullType = type.getFullyQualifiedName();
        if (info.getPrimaryKeyColumns() != null && info.getPrimaryKeyColumns().size() > 0) {
            primaryKeyType = info.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().getFullyQualifiedName();
        } else {
            primaryKeyType = "Long";
        }
        List<ColumnModel> columns = new ArrayList<>();
        for (IntrospectedColumn item : info.getAllColumns()) {
            columns.add(new ColumnModel(item));
        }
        boolean hasBlob = info.getBLOBColumns() != null && info.getBLOBColumns().size() > 1;
        modelNamePath = OtherUtils.hump2path(name);
        //运行时是否是动态sql
        map.put("is_dynamic_sql", info.getTargetRuntime().equals(IntrospectedTable.TargetRuntime.MYBATIS3_DSQL));
        //表名
        map.put("table_name", tableName);
        //备注
        map.put("table_remark", info.getRemarks());
        //主键类型
        map.put("primary_key_type", info.getPrimaryKeyType());
        //实体名称
        map.put("model_name", name);
        //实体完整名称
        map.put("model_full_type", fullType);
        //首字母小写名称
        map.put("model_lower_name", Introspector.decapitalize(type.getShortName()));
        //实体名称转路径
        map.put("model_name_path", modelNamePath);
        //年份
        map.put("year", Calendar.getInstance().get(Calendar.YEAR));
        //作者
        map.put("author", author);
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

    /**
     * 是否是关联表
     * @return
     */
    public boolean isRelationTable(){
        //根据表名是否包含relation作判断
        return tableName.contains("relation");
    }
}
