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
 * 分页查询插件
 */
public class SelectByPagePlugin extends PluginAdapter {
    private final String METHOD_NAME = "selectByPage";
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
        AbstractJavaMapperMethodGenerator methodGenerator = new JavaMapperMethodGenerator();
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.addInterfaceElements(interfaze);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    class XmlElementGenerator extends AbstractXmlElementGenerator {

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

            // 增加pageList
            XmlElement pageList = new XmlElement("select");
            pageList.addAttribute(new Attribute("id", METHOD_NAME));
            pageList.addAttribute(new Attribute("resultMap", "BaseResultMap"));
            pageList.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
            pageList.addElement(selectText);
            pageList.addElement(include);
            parentElement.addElement(pageList);
        }
    }

    class JavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {

        @Override
        public void addInterfaceElements(Interface interfaze) {
            // 先创建import对象
            Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
            // 添加Page的包
            importedTypes.add(new FullyQualifiedJavaType("com.github.pagehelper.Page"));
            // 创建方法对象
            Method method = new Method();
            // 设置该方法为public
            method.setVisibility(JavaVisibility.PUBLIC);
            // 设置返回类型是List
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Page");
            FullyQualifiedJavaType listType;
            // 设置List的类型是实体类的对象
            listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            importedTypes.add(listType);
            // 返回类型对象设置为List
            returnType.addTypeArgument(listType);
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
}
