package com.github.lany192.generator.mybatis3;

import com.github.lany192.generator.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * 修改Example类的目标地址
 */
public class ExamplePlugin extends BasePlugin {
    private final String TARGET_PACKAGE = "target_package";
    /**
     * 目标包
     */
    private String targetPackage;

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(getContext().getTargetRuntime())
                && !"MyBatis3".equalsIgnoreCase(getContext().getTargetRuntime())) {
            warnings.add(this.getClass().getTypeName() + "插件要求运行targetRuntime必须为MyBatis3！");
            return false;
        }
        this.targetPackage = getProperty(TARGET_PACKAGE);
        if (this.targetPackage == null) {
            warnings.add("请配置" + this.getClass().getName() + "插件的目标包名属性：" + TARGET_PACKAGE);
            return false;
        }
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        JavaModelGeneratorConfiguration config = getContext().getJavaModelGeneratorConfiguration();
        String newExampleType = introspectedTable.getExampleType().replace(config.getTargetPackage(), this.targetPackage);
        // 修改包名
        introspectedTable.setExampleType(newExampleType);
    }
}
