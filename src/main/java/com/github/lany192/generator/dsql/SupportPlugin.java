package com.github.lany192.generator.dsql;

import com.github.lany192.generator.BasePlugin;
import com.github.lany192.generator.utils.OtherUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 修改DynamicSqlSupport类的目标地址
 */
public class SupportPlugin extends BasePlugin {
    private String targetPackage;
    private String replaceString;
    private Pattern pattern;

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(context.getTargetRuntime())
                && !"MyBatis3DynamicSQL".equalsIgnoreCase(context.getTargetRuntime())) {
            warnings.add(this.getClass().getTypeName() + "插件要求运行targetRuntime必须为MyBatis3DynamicSQL！");
            return false;
        }
        this.targetPackage = getProperty("targetPackage", "");
        String searchString = getProperty("searchString", "");
        this.replaceString = getProperty("replaceString", "");
        if (StringUtility.stringHasValue(searchString)) {
            if (!StringUtility.stringHasValue(this.replaceString)) {
                warnings.add(this.getClass().getTypeName() + "插件:请配置replaceString参数！");
                return false;
            } else {
                this.pattern = Pattern.compile(searchString);
            }
        } else {
            if (StringUtility.stringHasValue(this.replaceString)) {
                warnings.add(this.getClass().getTypeName() + "插件:请配置searchString参数！");
                return false;
            }
        }
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        String supportType = introspectedTable.getMyBatisDynamicSqlSupportType();
        //修改包名
        if (!OtherUtils.isEmpty(this.targetPackage)) {
            supportType = supportType.replace(context.getJavaClientGeneratorConfiguration().getTargetPackage(), this.targetPackage);
        }
        //修改类名
        if (pattern != null) {
            Matcher matcher = pattern.matcher(supportType);
            supportType = matcher.replaceAll(this.replaceString);
        }
        introspectedTable.setMyBatisDynamicSqlSupportType(supportType);
    }
}
