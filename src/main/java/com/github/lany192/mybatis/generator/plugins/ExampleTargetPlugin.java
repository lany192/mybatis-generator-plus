package com.github.lany192.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;

import java.util.List;
import java.util.Properties;

/**
 * 修改Example类的目标地址
 *
 *         <!-- 修改Example包名插件 -->
 *         <plugin type="com.github.lany192.mybatis.generator.plugins.ExampleTargetPlugin">
 *             <property name="targetPackage" value="cn.taoduorou.server.entity.example"/>
 *         </plugin>
 */
public class ExampleTargetPlugin extends PluginAdapter {
    /**
     * 目标包
     */
    private String targetPackage;

    @Override
    public boolean validate(List<String> warnings) {
        // 获取配置的目标package
        Properties properties = getProperties();
        this.targetPackage = properties.getProperty("targetPackage");
        if (this.targetPackage == null) {
            warnings.add("请配置com.itfsw.mybatis.generator.plugins.ExampleTargetPlugin插件的目标包名(targetPackage)！");
            return false;
        }
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        String exampleType = introspectedTable.getExampleType();
        // 修改包名
        JavaModelGeneratorConfiguration configuration = getContext().getJavaModelGeneratorConfiguration();
        String targetPackage = configuration.getTargetPackage();
        String newExampleType = exampleType.replace(targetPackage, this.targetPackage);
        introspectedTable.setExampleType(newExampleType);
    }
}
