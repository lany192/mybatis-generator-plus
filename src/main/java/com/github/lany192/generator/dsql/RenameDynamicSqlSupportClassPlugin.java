package com.github.lany192.generator.dsql;

import com.github.lany192.generator.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 修改DynamicSqlSupport的名称
 */
public class RenameDynamicSqlSupportClassPlugin extends BasePlugin {
    private String replaceString;
    private Pattern pattern;

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(getContext().getTargetRuntime())
                && !"MyBatis3DynamicSQL".equalsIgnoreCase(getContext().getTargetRuntime())) {
            warnings.add(this.getClass().getTypeName() + "插件要求运行targetRuntime必须为MyBatis3DynamicSQL！");
            return false;
        }
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
        Matcher matcher = this.pattern.matcher(oldType);
        oldType = matcher.replaceAll(this.replaceString);
        introspectedTable.setMyBatisDynamicSqlSupportType(oldType);
    }
}
