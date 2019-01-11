package com.github.lany192.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author Lany
 */
public class SelectExpandXmlElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        // 增加base_query
        XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id", "base_query"));
        //在这里添加where条件
        //设置trim标签
        XmlElement selectTrimElement = new XmlElement("trim");
        selectTrimElement.addAttribute(new Attribute("prefix", "WHERE"));
        //添加where和and
        selectTrimElement.addAttribute(new Attribute("prefixOverrides", "AND | OR"));
        StringBuilder sb = new StringBuilder();
        for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            //$NON-NLS-1$
            XmlElement selectNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append("null != ");
            sb.append(introspectedColumn.getJavaProperty());
            selectNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            sb.setLength(0);
            // 添加and
            sb.append(" and ");
            // 添加别名t
            sb.append("t.");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            // 添加等号
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            selectNotNullElement.addElement(new TextElement(sb.toString()));
            selectTrimElement.addElement(selectNotNullElement);
        }
        sql.addElement(selectTrimElement);
        parentElement.addElement(sql);

        // 公用select
        sb.setLength(0);
        sb.append("select ");
        sb.append("t.* ");
        sb.append("from ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        sb.append(" t");
        TextElement selectText = new TextElement(sb.toString());

        // 公用include
        XmlElement include = new XmlElement("include");
        include.addAttribute(new Attribute("refid", "base_query"));

//        // 增加find
//        XmlElement find = new XmlElement("select");
//        find.addAttribute(new Attribute("id", "selectOne"));
//        find.addAttribute(new Attribute("resultMap", "BaseResultMap"));
//        find.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
//        find.addElement(selectText);
//        find.addElement(include);
//        parentElement.addElement(find);

        // 增加list
        XmlElement list = new XmlElement("select");
        list.addAttribute(new Attribute("id", "selectByEntity"));
        list.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        list.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        list.addElement(selectText);
        list.addElement(include);
        parentElement.addElement(list);

        // 增加pageList
        XmlElement pageList = new XmlElement("select");
        pageList.addAttribute(new Attribute("id", "selectByPage"));
        pageList.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        pageList.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        pageList.addElement(selectText);
        pageList.addElement(include);
        parentElement.addElement(pageList);
    }
}
