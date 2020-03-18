package com.github.lany192.mybatis.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;

import java.util.List;

/**
 * 修改Example类的目标地址
 */
public class RenameExamplePackagePlugin extends BasePlugin {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        String exampleType = introspectedTable.getExampleType();
        JavaModelGeneratorConfiguration config = getContext().getJavaModelGeneratorConfiguration();
        String targetPackage = config.getTargetPackage();
        String newPackage = targetPackage + ".example";
        String newExampleType = exampleType.replace(targetPackage, newPackage);
        // 修改包名
        introspectedTable.setExampleType(newExampleType);
    }
}
