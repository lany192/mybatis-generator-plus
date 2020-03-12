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

        interfaze.addMethod(findAllMethod(info));
        interfaze.addMethod(selectByIds(info));
        interfaze.addMethod(deleteByIds(info));
        interfaze.addMethod(selectByPage(info));
        interfaze.addMethod(selectByPageAndSize(info));
        return super.clientGenerated(interfaze, introspectedTable);
    }

    private Method findAllMethod(TableInfo info) {
        Method method = new Method("findAll");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 查看所有记录");
        method.addJavaDocLine(" */");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        String returnType = "List<" + info.getFullType() + ">";
        method.setReturnType(new FullyQualifiedJavaType(returnType));
        method.addBodyLine("return select(SelectDSLCompleter.allRows());");
        return method;
    }

    private Method selectByIds(TableInfo info) {
        Method method = new Method("selectByIds");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 根据多个id，查询记录");
        method.addJavaDocLine(" */");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<" + info.getFullType() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<Long>"), "ids"));
        method.addBodyLine("return select(dsl -> dsl.where(id, isIn(ids)));");
        return method;
    }

    private Method deleteByIds(TableInfo info) {
        Method method = new Method("deleteByIds");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 根据多个id，删除记录");
        method.addJavaDocLine(" */");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<Long>"), "ids"));
        method.addBodyLine("return delete(dsl -> dsl.where(id, isIn(ids)));");
        return method;
    }

    private Method selectByPage(TableInfo info) {
        Method method = new Method("selectByPage");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 分页查询记录,指定页码");
        method.addJavaDocLine(" */");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo<" + info.getName() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "page"));
        method.addBodyLine("return selectByPage(page, 30, SelectDSLCompleter.allRows());");
        return method;
    }

    private Method selectByPageAndSize(TableInfo info) {
        Method method = new Method("selectByPage");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 分页查询记录,指定页码");
        method.addJavaDocLine(" */");
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
