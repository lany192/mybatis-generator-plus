package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.utils.Log;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 将mapper文件继承通用BaseMapper
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
        //输出文件路径
        String rootMapperClass = getProperty(ROOT_MAPPER_CLASS);
        Log.i(TAG, "属性值:" + rootMapperClass);
        //例如:"my.mabatis.example.base.BaseMapper"
        FullyQualifiedJavaType imp = new FullyQualifiedJavaType(rootMapperClass);
        Log.i(TAG, "属性1:" + imp.getShortName());
        Log.i(TAG, "属性1:" + imp.getFullyQualifiedName());
        Log.i(TAG, "属性1:" + imp.getPackageName());

        //主键默认采用java.lang.Long
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(imp.getShortName() + "<"
                + introspectedTable.getBaseRecordType() + ","
                + introspectedTable.getExampleType() + ","
                + "java.lang.Long" + ">");

        //添加 extends MybatisBaseMapper
        interfaze.addSuperInterface(fqjt);
        //添加import my.mabatis.example.base.MybatisBaseMapper;
        interfaze.addImportedType(imp);
        //方法不需要
        interfaze.getMethods().clear();
        interfaze.getAnnotations().clear();
        return true;
    }
}
