package com.github.lany192.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 查询单个记录
 */
public class SelectOneByExamplePlugin extends PluginAdapter {
    private final String METHOD_NAME = "selectOneByExample";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        AbstractXmlElementGenerator elementGenerator = new XmlElementGenerator();
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.addElements(document.getRootElement());
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        AbstractJavaMapperMethodGenerator methodGenerator = new MapperMethodGenerator();
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.addInterfaceElements(interfaze);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    class MapperMethodGenerator extends AbstractJavaMapperMethodGenerator {

        @Override
        public void addInterfaceElements(Interface interfaze) {
            // 设置返回类型是List
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            // 设置参数类型是对象
            FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getExampleType());


            Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(returnType);
            // 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
            method.setName(METHOD_NAME);


            // 先创建import对象
            Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
            // 添加Lsit的包
            importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
            // import参数类型对象
            importedTypes.add(parameterType);
            // 为方法添加参数，变量名称record
            method.addParameter(new Parameter(parameterType, "example"));


            context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
            if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
                interfaze.addImportedTypes(importedTypes);
                interfaze.addMethod(method);
            }
        }
    }

    class XmlElementGenerator extends AbstractXmlElementGenerator {

        @Override
        public void addElements(XmlElement parentElement) {
            XmlElement answer = new XmlElement("select");
            answer.addAttribute(new Attribute("id", METHOD_NAME));
            answer.addAttribute(new Attribute("resultMap", "BaseResultMap"));
            answer.addAttribute(new Attribute("parameterType", introspectedTable.getExampleType()));
            context.getCommentGenerator().addComment(answer);

            answer.addElement(new TextElement("select"));

            answer.addElement(getBaseColumnListElement());

            StringBuilder sb = new StringBuilder();
            sb.setLength(0);
            sb.append("from ");
            sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
            answer.addElement(new TextElement(sb.toString()));
            answer.addElement(getExampleIncludeElement());

            XmlElement ifElement = new XmlElement("if");
            ifElement.addAttribute(new Attribute("test", "orderByClause != null"));
            ifElement.addElement(new TextElement("order by ${orderByClause}"));
            answer.addElement(ifElement);
            parentElement.addElement(answer);
        }
    }
}
