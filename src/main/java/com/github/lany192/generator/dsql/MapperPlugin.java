package com.github.lany192.generator.dsql;

import com.github.lany192.generator.BasePlugin;
import com.github.lany192.generator.model.TableModel;
import com.github.lany192.generator.utils.JsonUtils;
import com.github.lany192.generator.utils.Log;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * 拓展Mapper类的功能
 */
public class MapperPlugin extends BasePlugin {

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(getContext().getTargetRuntime())
                && !"MyBatis3DynamicSQL".equalsIgnoreCase(getContext().getTargetRuntime())) {
            warnings.add(this.getClass().getTypeName() + "插件要求运行targetRuntime必须为MyBatis3DynamicSQL！");
            return false;
        }
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        TableModel info = new TableModel(introspectedTable, System.getProperty("user.name"));
        Log.i(info.getName() + "信息:" + JsonUtils.object2json(info));

        interfaze.addImportedType(new FullyQualifiedJavaType(info.getFullType()));

        interfaze.addImportedType(new FullyQualifiedJavaType(PageHelper.class.getTypeName()));
        interfaze.addImportedType(new FullyQualifiedJavaType(PageInfo.class.getTypeName()));
        interfaze.addImportedType(new FullyQualifiedJavaType(SqlBuilder.class.getTypeName()));

        interfaze.addMethod(findAllMethod(info));
        interfaze.addMethod(selectByIds(info));
        interfaze.addMethod(deleteByIds(info));
        interfaze.addMethod(selectByPage(info));
        interfaze.addMethod(selectByPageAndSize(info));
        interfaze.addMethod(findByPage(info));
        interfaze.addMethod(findByPageAndSize(info));
        interfaze.addMethod(insertMultiple(info));
        return super.clientGenerated(interfaze, introspectedTable);
    }

    private Method findAllMethod(TableModel info) {
        Method method = new Method("findAll");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 查看所有记录");
        method.addJavaDocLine(" */");
        method.addAnnotation("@Generated(value = \"org.mybatis.generator.api.MyBatisGenerator\", comments = \"Source Table: " + info.getTableName() + "\")");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        String returnType = "List<" + info.getFullType() + ">";
        method.setReturnType(new FullyQualifiedJavaType(returnType));
        method.addBodyLine("return select(SelectDSLCompleter.allRows());");
        return method;
    }

    private Method selectByIds(TableModel info) {
        Method method = new Method("selectByIds");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 根据多个id，查询记录");
        method.addJavaDocLine(" */");
        method.addAnnotation("@Generated(value = \"org.mybatis.generator.api.MyBatisGenerator\", comments = \"Source Table: " + info.getTableName() + "\")");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<" + info.getFullType() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + info.getPrimaryKeyType() + ">"), "ids"));
        method.addBodyLine("return select(c -> c.where(id, SqlBuilder.isIn(ids)));");
        return method;
    }

    private Method deleteByIds(TableModel info) {
        Method method = new Method("deleteByIds");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 根据多个id，删除记录");
        method.addJavaDocLine(" */");
        method.addAnnotation("@Generated(value = \"org.mybatis.generator.api.MyBatisGenerator\", comments = \"Source Table: " + info.getTableName() + "\")");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + info.getPrimaryKeyType() +">"), "ids"));
        method.addBodyLine("return delete(c -> c.where(id, SqlBuilder.isIn(ids)));");
        return method;
    }

    private Method selectByPage(TableModel info) {
        Method method = new Method("selectByPage");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 分页查询记录,指定页码");
        method.addJavaDocLine(" * @return 记录PageInfo集");
        method.addJavaDocLine(" */");
        method.addAnnotation("@Generated(value = \"org.mybatis.generator.api.MyBatisGenerator\", comments = \"Source Table: " + info.getTableName() + "\")");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo<" + info.getName() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "pageNum"));
        method.addBodyLine("return selectByPage(pageNum, 30, SelectDSLCompleter.allRows());");
        return method;
    }

    private Method selectByPageAndSize(TableModel info) {
        Method method = new Method("selectByPage");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 分页查询记录,指定页码");
        method.addJavaDocLine(" * @return 记录PageInfo集");
        method.addJavaDocLine(" */");
        method.addAnnotation("@Generated(value = \"org.mybatis.generator.api.MyBatisGenerator\", comments = \"Source Table: " + info.getTableName() + "\")");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo<" + info.getName() + ">"));
        method.addParameter(0, new Parameter(new FullyQualifiedJavaType("int"), "pageNum"));
        method.addParameter(1, new Parameter(new FullyQualifiedJavaType("int"), "pageSize"));
        method.addParameter(2, new Parameter(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSLCompleter"), "completer"));
        method.addBodyLine("PageHelper.startPage(pageNum, pageSize);");
        method.addBodyLine("List<" + info.getName() + "> records = select(completer);");
        method.addBodyLine("return new PageInfo<>(records);");
        return method;
    }


    private Method findByPage(TableModel info) {
        Method method = new Method("findByPage");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 分页查询记录,指定页码");
        method.addJavaDocLine(" * @return 记录List集");
        method.addJavaDocLine(" */");
        method.addAnnotation("@Generated(value = \"org.mybatis.generator.api.MyBatisGenerator\", comments = \"Source Table: " + info.getTableName() + "\")");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<" + info.getName() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "pageNum"));
        method.addBodyLine("return findByPage(pageNum, 30, SelectDSLCompleter.allRows());");
        return method;
    }

    private Method findByPageAndSize(TableModel info) {
        Method method = new Method("findByPage");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 分页查询记录,指定页码");
        method.addJavaDocLine(" * @return 记录List集");
        method.addJavaDocLine(" */");
        method.addAnnotation("@Generated(value = \"org.mybatis.generator.api.MyBatisGenerator\", comments = \"Source Table: " + info.getTableName() + "\")");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<" + info.getName() + ">"));
        method.addParameter(0, new Parameter(new FullyQualifiedJavaType("int"), "pageNum"));
        method.addParameter(1, new Parameter(new FullyQualifiedJavaType("int"), "pageSize"));
        method.addParameter(2, new Parameter(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSLCompleter"), "completer"));
        method.addBodyLine("PageHelper.startPage(pageNum, pageSize);");
        method.addBodyLine("return select(completer);");
        return method;
    }

    private Method insertMultiple(TableModel info) {
        Method method = new Method("insertMultiple");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 批量插入");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 插入数量");
        method.addJavaDocLine(" */");
        method.addAnnotation("@Generated(value = \"org.mybatis.generator.api.MyBatisGenerator\", comments = \"Source Table: " + info.getTableName() + "\")");
        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + info.getName() + ">"), "records"));
        method.addBodyLine("if (records != null && records.size() > 0) {");
        method.addBodyLine("for (" + info.getName() + " record : records) {");
        method.addBodyLine("insertSelective(record);");
        method.addBodyLine("}");
        method.addBodyLine("return records.size();");
        method.addBodyLine("}");
        method.addBodyLine("return 0;");
        return method;
    }

}