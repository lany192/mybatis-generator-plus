package com.github.lany192.mybatis.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.config.Context;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Administrator
 */
public class SelectOneByExampleMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {
    private final String METHOD_NAME = "selectOneByExample";

    public SelectOneByExampleMapperMethodGenerator(Context context, Interface interfaze, IntrospectedTable introspectedTable) {
        super();
        setContext(context);
        setIntrospectedTable(introspectedTable);
        addInterfaceElements(interfaze);
    }

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