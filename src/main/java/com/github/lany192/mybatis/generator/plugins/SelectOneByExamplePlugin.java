package com.github.lany192.mybatis.generator.plugins;

import com.github.lany192.mybatis.generator.SelectOneByExampleMapperMethodGenerator;
import com.github.lany192.mybatis.generator.SelectOneByExampleXmlElementGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;

import java.util.List;

/**
 * 查询单个记录
 */
public class SelectOneByExamplePlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        //生成xml方法
        new SelectOneByExampleXmlElementGenerator(context, document, introspectedTable);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //生成mapper方法
        new SelectOneByExampleMapperMethodGenerator(context, interfaze, introspectedTable);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
