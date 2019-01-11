package com.github.lany192.mybatis.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.config.Context;

/**
 * @author Administrator
 */
public class SelectOneByExampleXmlElementGenerator extends AbstractXmlElementGenerator {
    private final String METHOD_NAME = "selectOneByExample";

    public SelectOneByExampleXmlElementGenerator(Context context, Document document, IntrospectedTable introspectedTable) {
        setContext(context);
        setIntrospectedTable(introspectedTable);
        addElements(document.getRootElement());
    }

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