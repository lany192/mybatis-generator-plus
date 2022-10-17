package com.github.lany192.generator.mybatis3;

import com.github.lany192.generator.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * 根据ids查询记录
 */
public class SelectByIdsPlugin extends BasePlugin {
    public static final String METHOD_NAME = "selectByIds";  // 方法名

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(context.getTargetRuntime())
                && !"MyBatis3".equalsIgnoreCase(context.getTargetRuntime())) {
            warnings.add(this.getClass().getTypeName() + "插件要求运行targetRuntime必须为MyBatis3！");
            return false;
        }
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(introspectedTable.getRules().calculateAllFieldsClass());

        Method method = new Method(METHOD_NAME);
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 根据ids查询记录");
        method.addJavaDocLine(" */");
        method.setAbstract(true);
        method.setVisibility(JavaVisibility.DEFAULT);
        method.setReturnType(returnType);

        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(new FullyQualifiedJavaType("Long"));
        method.addParameter(new Parameter(listType, "ids", "@Param(\"ids\")"));

        interfaze.addMethod(method);
        return super.clientGenerated(interfaze, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement selectElement = new XmlElement("select");
        selectElement.addAttribute(new Attribute("id", METHOD_NAME));
        selectElement.addAttribute(new Attribute("parameterType", "java.util.List"));
        selectElement.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectElement.addElement(new TextElement("    select\n" +
                "    <include refid=\"Base_Column_List\" />\n" +
                "    from " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " WHERE id IN"));
        // 添加foreach节点
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "ids"));
        foreachElement.addAttribute(new Attribute("item", "id"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addAttribute(new Attribute("index", "index"));
        foreachElement.addAttribute(new Attribute("open", "("));
        foreachElement.addAttribute(new Attribute("close", ")"));

        foreachElement.addElement(new TextElement("#{id}"));

        selectElement.addElement(foreachElement);
        document.getRootElement().addElement(selectElement);
        return true;
    }
}