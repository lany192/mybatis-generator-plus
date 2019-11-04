package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.Log;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 将mapper文件继承自定义基类Mapper
 * https://my.oschina.net/wangmengjun/blog/784697?p=2
 *
 * @author Lany
 */
public class BaseMapperPlugin extends BasePlugin {
    //目标文件名称后缀
    private final String ROOT_MAPPER_CLASS = "root_mapper_class";

    @Override
    public boolean validate(List<String> warnings) {
        if (isEmpty(ROOT_MAPPER_CLASS)) {
            warnings.add(TAG + ":请配置通用Mapper基类" + ROOT_MAPPER_CLASS + "属性");
            return false;
        }
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String rootMapperClass = getProperty(ROOT_MAPPER_CLASS);
        Log.i(TAG, "属性值:" + rootMapperClass);
        //例如:"my.mabatis.example.base.BaseMapper"
        FullyQualifiedJavaType superJavaType = new FullyQualifiedJavaType(rootMapperClass);

        //主键默认采用java.lang.Long
        FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(superJavaType.getShortName() + "<"
                + introspectedTable.getBaseRecordType() + ","
                + introspectedTable.getExampleType() + ","
                + "java.lang.Long" + ">");
        //添加 extends BaseMapper
        interfaze.addSuperInterface(superInterface);
        //添加import my.mabatis.example.base.BaseMapper;
        interfaze.addImportedType(superJavaType);
        interfaze.getMethods().clear();









//        TableInfo info = new TableInfo(getContext(), introspectedTable);
//
//        FullyQualifiedJavaType superInterface;
//        if (info.isHasBlob()) {
//            //主键默认采用java.lang.Long
//            superInterface = new FullyQualifiedJavaType(superJavaType.getShortName() + "<"
//                    + introspectedTable.getBaseRecordType() + ","
//                    + introspectedTable.getRecordWithBLOBsType() + ","
//                    + introspectedTable.getExampleType() + ","
//                    + "java.lang.Long" + ">");
//        } else {
//            //主键默认采用java.lang.Long
//            superInterface = new FullyQualifiedJavaType(superJavaType.getShortName() + "<"
//                    + introspectedTable.getBaseRecordType() + ","
//                    + introspectedTable.getBaseRecordType() + ","
//                    + introspectedTable.getExampleType() + ","
//                    + "java.lang.Long" + ">");
//        }
//        //添加 extends BaseMapper
//        interfaze.addSuperInterface(superInterface);
//        //添加import my.mabatis.example.base.BaseMapper;
//        interfaze.addImportedType(superJavaType);
//        //清除不要的方法
//        if (info.isHasBlob()) {
//            List<Method> methods = interfaze.getMethods();
//            List<Method> clearMethods = new ArrayList<>();
//            for (Method method : methods) {
//                if (!method.getName().contains("WithBLOBs")) {
//                    clearMethods.add(method);
//                }
//            }
//            interfaze.getMethods().removeAll(clearMethods);
//        } else {
//            interfaze.getMethods().clear();
//        }
//        //interfaze.getAnnotations().clear();
        return true;
    }
}
