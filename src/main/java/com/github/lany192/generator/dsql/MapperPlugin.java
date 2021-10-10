package com.github.lany192.generator.dsql;

import com.github.lany192.generator.BasePlugin;
import com.github.lany192.generator.model.ColumnModel;
import com.github.lany192.generator.model.TableModel;
import com.github.lany192.generator.utils.JsonUtils;
import com.github.lany192.generator.utils.Log;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.mybatis.dynamic.sql.Constant;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.util.Buildable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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
        interfaze.addImportedType(new FullyQualifiedJavaType(Constant.class.getTypeName()));
        interfaze.addImportedType(new FullyQualifiedJavaType(SelectModel.class.getTypeName()));
        interfaze.addImportedType(new FullyQualifiedJavaType(Buildable.class.getTypeName()));
        interfaze.addImportedType(new FullyQualifiedJavaType(RenderingStrategies.class.getTypeName()));
        interfaze.addImportedType(new FullyQualifiedJavaType(QueryExpressionDSL.class.getTypeName()));
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.commons.lang3.StringUtils"));
        interfaze.addImportedType(new FullyQualifiedJavaType(ArrayList.class.getTypeName()));

        interfaze.addMethod(selectAllMethod(info));
        interfaze.addMethod(selectByIds(info));
        interfaze.addMethod(deleteByIds(info));
        interfaze.addMethod(selectByPage(info));
        interfaze.addMethod(selectByPageAndSize(info));
        interfaze.addMethod(insertMultiple(info));
        interfaze.addMethod(selectByEntity(info));
        interfaze.addMethod(search(info));
        interfaze.addMethod(exist(info));
        interfaze.addMethod(existById(info));
        interfaze.addMethod(existByEntity(info));
        return super.clientGenerated(interfaze, introspectedTable);
    }

    private Method existByEntity(TableModel info) {
        Method method = new Method("existByEntity");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 根据条件查看是否有记录");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 是否存在");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("boolean"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(info.getFullType()), "record"));
        List<ColumnModel> columns = info.getColumns();
        StringJoiner joiner = new StringJoiner(
                ")\n                .and(",
                "return exist(c -> c.where(",
                "));");
        for (ColumnModel column : columns) {
            joiner.add(column.getName() + ", isEqualToWhenPresent(record::get" + column.getFirstUpperName() + ")");
        }
        method.addBodyLine(joiner.toString());
        return method;
    }

    private Method existById(TableModel info) {
        Method method = new Method("exist");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 是否存在目标id");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 是否存在");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("boolean"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Long"), "id"));
        method.addBodyLine("return selectByPrimaryKey(id).isPresent();");
        return method;
    }

    private Method exist(TableModel info) {
        Method method = new Method("exist");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 是否存在满足条件的记录");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 是否存在");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("boolean"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.SelectDSLCompleter"), "completer"));
        method.addBodyLine("QueryExpressionDSL<SelectModel> dsl = SqlBuilder.select(BasicColumn.columnList(Constant.of(\"count(*)\"))).from(" + info.getFirstLowerTableName() + ");");
        method.addBodyLine("dsl.limit(1);");
        method.addBodyLine("SelectStatementProvider selectStatement = completer.apply(dsl).build().render(RenderingStrategies.MYBATIS3);");
        method.addBodyLine("return count(selectStatement) > 0;");
        return method;
    }

    private Method search(TableModel info) {
        Method method = new Method("search");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 搜索(目前仅支持文本）");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 记录集");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);

        method.setReturnType(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo<" + info.getName() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "searchKeyword"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "pageNum"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "pageSize"));
        method.addBodyLine("return selectByPage(pageNum, pageSize, c -> {");
        StringJoiner joiner = new StringJoiner(
                ")\n                    .or",
                "c\n                    .where",
                ");");
        List<ColumnModel> columns = info.getColumns();
        boolean enable = false;
        for (ColumnModel column : columns) {
            //目前仅支持文本
            if (column.getFullTypeName().equals(String.class.getTypeName())) {
                joiner.add("(" + column.getName() + ", isLike(\"%\" + searchKeyword + \"%\").filter(obj -> !StringUtils.isEmpty(searchKeyword))");
                enable = true;
            }
        }
        if (enable) {
            method.addBodyLine("return " + joiner);
        } else {
            method.addBodyLine("return c;");
        }
        method.addBodyLine("});");
        return method;
    }

    private Method selectByEntity(TableModel info) {
        Method method = new Method("selectByEntity");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 根据条件查看记录");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 记录集");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        String returnType = "List<" + info.getFullType() + ">";
        method.setReturnType(new FullyQualifiedJavaType(returnType));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(info.getFullType()), "record"));
        List<ColumnModel> columns = info.getColumns();
        StringJoiner joiner = new StringJoiner(
                ")\n                .and(",
                "return select(c -> c.where(",
                "));");
        for (ColumnModel column : columns) {
            joiner.add(column.getName() + ", isEqualToWhenPresent(record::get" + column.getFirstUpperName() + ")");
        }
        method.addBodyLine(joiner.toString());
        return method;
    }

    private Method selectAllMethod(TableModel info) {
        Method method = new Method("selectAll");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 查看所有记录");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 记录集");
        method.addJavaDocLine(" */");

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
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 记录集");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<" + info.getFullType() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + info.getPrimaryKeyType() + ">"), "ids"));
        method.addBodyLine("return select(c -> c.where(id, isIn(ids)));");
        return method;
    }

    private Method deleteByIds(TableModel info) {
        Method method = new Method("deleteByIds");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 根据多个id，删除记录");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + info.getPrimaryKeyType() + ">"), "ids"));
        method.addBodyLine("return delete(c -> c.where(id, isIn(ids)));");
        return method;
    }

    private Method selectByPage(TableModel info) {
        Method method = new Method("selectByPage");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 分页查询记录");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 记录PageInfo集");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo<" + info.getName() + ">"));
        method.addParameter(0, new Parameter(new FullyQualifiedJavaType("int"), "pageNum"));
        method.addParameter(1, new Parameter(new FullyQualifiedJavaType("int"), "pageSize"));
        method.addBodyLine("return selectByPage(pageNum, pageSize, SelectDSLCompleter.allRows());");
        return method;
    }

    private Method selectByPageAndSize(TableModel info) {
        Method method = new Method("selectByPage");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 分页查询记录");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @return 记录PageInfo集");
        method.addJavaDocLine(" */");

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

    private Method insertMultiple(TableModel info) {
        Method method = new Method("insertMultiple");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 批量插入");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 生成记录的id集");
        method.addJavaDocLine(" */");

        method.setDefault(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<Long>"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + info.getName() + ">"), "records"));
        method.addBodyLine("List<Long> ids = new ArrayList<>();");
        method.addBodyLine("if (records != null && records.size() > 0) {");
        method.addBodyLine("for (" + info.getName() + " record : records) {");
        method.addBodyLine("insertSelective(record);");
        method.addBodyLine("ids.add(record.getId());");
        method.addBodyLine("}");
        method.addBodyLine("}");
        method.addBodyLine("return ids;");
        return method;
    }
}
