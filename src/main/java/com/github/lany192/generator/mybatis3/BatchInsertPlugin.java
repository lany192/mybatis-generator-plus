package com.github.lany192.generator.mybatis3;

import com.github.lany192.generator.BasePlugin;
import com.github.lany192.generator.utils.Log;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * 批量插入插件
 * <plugin type="com.github.lany192.generator.mybatis3.BatchInsertPlugin">
 * <property name="ignoreColumns" value="id,create_time"/>
 * </plugin>
 */
public class BatchInsertPlugin extends BasePlugin {
    public static final String METHOD_BATCH_INSERT = "batchInsert";  // 方法名
    public static final String KEY_IGNORE_COLUMNS = "ignoreColumns";  // 要忽略的字段，多个字段用逗号隔开

    /**
     * 要忽略的字段
     */
    private List<String> ignoreColumns;

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(context.getTargetRuntime())
                && !"MyBatis3".equalsIgnoreCase(context.getTargetRuntime())) {
            warnings.add(this.getClass().getTypeName() + "插件要求运行targetRuntime必须为MyBatis3！");
            return false;
        }
        String columns = getProperty(KEY_IGNORE_COLUMNS, "");
        this.ignoreColumns = Arrays.asList(columns.split(","));
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(introspectedTable.getRules().calculateAllFieldsClass());

        Method method = new Method(METHOD_BATCH_INSERT);
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 批量插入");
        method.addJavaDocLine(" */");
        method.setAbstract(true);
        method.setVisibility(JavaVisibility.DEFAULT);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(listType, "list", "@Param(\"list\")"));

        interfaze.addMethod(method);
        return super.clientGenerated(interfaze, introspectedTable);
    }

    /**
     * <insert id="batchInsert" parameterType="java.util.List">
     * insert into user_table (name, logID,url)
     * values
     * <foreach collection="list" item="item" index="index" separator=",">
     * (#{item.name,jdbcType=VARCHAR}, #{item.logid,jdbcType=INTEGER},#{item.url,jdbcType=LONGVARCHAR})
     * </foreach>
     * </insert>
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement batchInsertEle = new XmlElement("insert");
        batchInsertEle.addAttribute(new Attribute("id", METHOD_BATCH_INSERT));
        batchInsertEle.addAttribute(new Attribute("parameterType", "java.util.List"));

        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        StringJoiner joiner = new StringJoiner(",");
        for (IntrospectedColumn column : columns) {
            //排除字段
            if (ignoreColumns.contains(column.getActualColumnName())) {
                Log.i("忽略字段:" + column.getActualColumnName());
            } else {
                joiner.add(column.getActualColumnName());
            }
        }
        batchInsertEle.addElement(new TextElement("insert into " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " (" + joiner.toString() + ") values"));

        // 添加foreach节点
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addAttribute(new Attribute("index", "index"));
//        foreachElement.addAttribute(new Attribute("open", "("));
//        foreachElement.addAttribute(new Attribute("close", ")"));

        StringJoiner joiner2 = new StringJoiner(",");
        for (IntrospectedColumn column : columns) {
            //排除字段
            if (ignoreColumns.contains(column.getActualColumnName())) {
                Log.i("忽略字段:" + column.getActualColumnName());
            } else {
                joiner2.add("#{item." + column.getJavaProperty() + ",jdbcType=" + column.getJdbcTypeName() + "}");
            }
        }
        foreachElement.addElement(new TextElement("(" + joiner2.toString() + ")"));

        batchInsertEle.addElement(foreachElement);
        document.getRootElement().addElement(batchInsertEle);
        return true;
    }
}