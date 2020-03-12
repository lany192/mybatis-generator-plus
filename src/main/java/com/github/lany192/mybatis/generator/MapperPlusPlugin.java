package com.github.lany192.mybatis.generator;

import com.alibaba.fastjson.JSON;
import com.github.lany192.mybatis.generator.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.Log;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * 拓展Mapper类的功能
 */
public class MapperPlusPlugin extends BasePlugin {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        TableInfo info = new TableInfo(getContext(), introspectedTable);
        Log.i(info.getName() + "信息:" + JSON.toJSONString(info));

        interfaze.addImportedType(new FullyQualifiedJavaType(info.getFullType()));

        interfaze.addImportedType(new FullyQualifiedJavaType("com.github.pagehelper.PageHelper"));
        interfaze.addImportedType(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo"));
        interfaze.addImportedType(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.SqlBuilder"));
        interfaze.addImportedType(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSLCompleter"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));

        interfaze.addMethod(findAllMethod(info));
        interfaze.addMethod(selectByIds(info));
        interfaze.addMethod(deleteByIds(info));
        interfaze.addMethod(selectByPage(info));
        interfaze.addMethod(selectByPageAndSize(info));
        return super.clientGenerated(interfaze, introspectedTable);
    }

    /**
     * 查看多有记录
     */
    private Method findAllMethod(TableInfo info) {
        Method method = new Method("findAll");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        String returnType = "List<" + info.getFullType() + ">";
        method.setReturnType(new FullyQualifiedJavaType(returnType));
        method.addBodyLine("return select(SelectDSLCompleter.allRows());");
        return method;
    }

    /**
     * 根据多个id，查询记录
     */
    private Method selectByIds(TableInfo info) {
        Method method = new Method("selectByIds");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<" + info.getFullType() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<Long>"), "ids"));
        method.addBodyLine("return select(dsl -> dsl.where(" + info.getName() + "DynamicSqlSupport.id, SqlBuilder.isIn(ids)));");
        return method;
    }

    /**
     * 根据多个id，删除记录
     */
    private Method deleteByIds(TableInfo info) {
        Method method = new Method("deleteByIds");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<Long>"), "ids"));
        method.addBodyLine("return delete(dsl -> dsl.where(" + info.getName() + "DynamicSqlSupport.id, SqlBuilder.isIn(ids)));");
        return method;
    }

    /**
     * 分页查询记录,指定页码
     */
    private Method selectByPage(TableInfo info) {
        Method method = new Method("selectByPage");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo<" + info.getName() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "page"));
        method.addBodyLine("return selectByPage(page, 30, SelectDSLCompleter.allRows());");
        return method;
    }

    /**
     * 分页查询记录,指定页码
     */
    private Method selectByPageAndSize(TableInfo info) {
        Method method = new Method("selectByPage");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo<" + info.getName() + ">"));
        method.addParameter(0, new Parameter(new FullyQualifiedJavaType("int"), "page"));
        method.addParameter(1, new Parameter(new FullyQualifiedJavaType("int"), "size"));
        method.addParameter(2, new Parameter(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSLCompleter"), "completer"));
        method.addBodyLine("PageHelper.startPage(page, size);");
        method.addBodyLine("List<" + info.getName() + "> records = select(completer);");
        method.addBodyLine("return new PageInfo<>(records);");
        return method;
    }
}
