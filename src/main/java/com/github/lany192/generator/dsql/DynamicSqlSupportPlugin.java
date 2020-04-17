package com.github.lany192.generator.dsql;

import com.github.lany192.generator.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * 修改DynamicSqlSupport类的目标地址
 */
public class DynamicSqlSupportPlugin extends BasePlugin {
    /**
     * 目标包
     */
    private String targetPackage;

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(getContext().getTargetRuntime())
                && !"MyBatis3DynamicSQL".equalsIgnoreCase(getContext().getTargetRuntime())) {
            warnings.add(this.getClass().getTypeName() + "插件要求运行targetRuntime必须为MyBatis3DynamicSQL！");
            return false;
        }
        if (!check("target_package")) {
            warnings.add("请配置" + this.getClass().getName() + "插件的目标包名属性：" + "target_package");
            return false;
        }
        this.targetPackage = getProperty("target_package", "");
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        String oldType = introspectedTable.getMyBatisDynamicSqlSupportType();
        //修改包名
        String newType = oldType.replace(getContext().getJavaClientGeneratorConfiguration().getTargetPackage(), this.targetPackage);
        introspectedTable.setMyBatisDynamicSqlSupportType(newType);
    }
}
