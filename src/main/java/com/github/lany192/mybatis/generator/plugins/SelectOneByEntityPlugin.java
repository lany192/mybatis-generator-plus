package com.github.lany192.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 查询单个记录
 */
public class SelectOneByEntityPlugin extends PluginAdapter {
    private final String METHOD_NAME = "selectOneByEntity";

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
            // 先创建import对象
            Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
            // 添加Lsit的包
            importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
            // 创建方法对象
            Method method = new Method();
            // 设置该方法为public
            method.setVisibility(JavaVisibility.PUBLIC);
            // 设置返回类型是List
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            // 方法对象设置返回类型对象
            method.setReturnType(returnType);
            // 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
            method.setName(METHOD_NAME);

            // 设置参数类型是对象
            FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            // import参数类型对象
            importedTypes.add(parameterType);
            // 为方法添加参数，变量名称record
            method.addParameter(new Parameter(parameterType, "record"));
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
            answer.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
            context.getCommentGenerator().addComment(answer);

            answer.addElement(new TextElement("select"));
            answer.addElement(getBaseColumnListElement());
            answer.addElement(new TextElement(" from "));
            answer.addElement(new TextElement(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
            answer.addElement(getRecordWhereXmlElement());

            parentElement.addElement(answer);
        }

        private XmlElement getRecordWhereXmlElement() {
            //在这里添加where条件
            //设置trim标签
            XmlElement selectTrimElement = new XmlElement("trim");
            selectTrimElement.addAttribute(new Attribute("prefix", "WHERE"));
            //添加where和and
            selectTrimElement.addAttribute(new Attribute("prefixOverrides", "AND | OR"));
            StringBuilder sb2 = new StringBuilder();
            for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                XmlElement selectNotNullElement = new XmlElement("if");
                sb2.setLength(0);
                sb2.append("null != ");
                sb2.append(introspectedColumn.getJavaProperty());
                selectNotNullElement.addAttribute(new Attribute("test", sb2.toString()));
                sb2.setLength(0);
                // 添加and
                sb2.append(" and ");
                sb2.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
                // 添加等号
                sb2.append(" = ");
                sb2.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                selectNotNullElement.addElement(new TextElement(sb2.toString()));
                selectTrimElement.addElement(selectNotNullElement);
            }
            return selectTrimElement;
        }
    }
}
