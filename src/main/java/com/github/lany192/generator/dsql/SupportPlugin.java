package com.github.lany192.generator.dsql;

import com.github.lany192.generator.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 修改DynamicSqlSupport类的目标地址
 */
public class SupportPlugin extends BasePlugin {
    /**
     * 目标包
     */
    private String targetPackage;
    private String replaceString;
    private Pattern pattern;

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
        String searchString = getProperty("searchString", "");
        this.replaceString = getProperty("replaceString", "");
        boolean valid = StringUtility.stringHasValue(searchString) && StringUtility.stringHasValue(this.replaceString);
        if (valid) {
            this.pattern = Pattern.compile(searchString);
        } else {
            if (!StringUtility.stringHasValue(searchString)) {
                warnings.add(Messages.getString("ValidationError.18", "RenameDynamicSqlSupportClassPlugin", "searchString"));
            }

            if (!StringUtility.stringHasValue(this.replaceString)) {
                warnings.add(Messages.getString("ValidationError.18", "RenameDynamicSqlSupportClassPlugin", "replaceString"));
            }
        }
        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        String oldType = introspectedTable.getMyBatisDynamicSqlSupportType();
        //修改包名
        String newType = oldType.replace(getContext().getJavaClientGeneratorConfiguration().getTargetPackage(), this.targetPackage);
        //修改类名
        Matcher matcher = this.pattern.matcher(newType);
        newType = matcher.replaceAll(this.replaceString);
        introspectedTable.setMyBatisDynamicSqlSupportType(newType);
    }
}
