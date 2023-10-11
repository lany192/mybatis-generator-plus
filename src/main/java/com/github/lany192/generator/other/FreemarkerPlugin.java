package com.github.lany192.generator.other;

import com.github.lany192.generator.BasePlugin;
import com.github.lany192.generator.Constants;
import com.github.lany192.generator.model.TableModel;
import com.github.lany192.generator.utils.FreemarkerUtils;
import com.github.lany192.generator.utils.JsonUtils;
import com.github.lany192.generator.utils.Log;
import com.github.lany192.generator.utils.OtherUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FreemarkerPlugin extends BasePlugin {
    /**
     * 项目的根目录
     */
    private final String rootPath = System.getProperty("user.dir");

    @Override
    public boolean validate(List<String> warnings) {
        if (!containsKey(Constants.TEMPLATE_FILE_PATH)) {
            warnings.add(TAG + ":请配置模板文件相对路径" + Constants.TEMPLATE_FILE_PATH + "属性");
            return false;
        }
        String templatePath = rootPath + getPath(Constants.TEMPLATE_FILE_PATH, "");
        if (!new File(templatePath).exists()) {
            warnings.add(TAG + ":模板文件不存在:" + templatePath);
            return false;
        }
        warnings.add(TAG + ":模板文件正常:" + templatePath);


        if (!containsKey(Constants.TARGET_OUT_PATH)) {
            warnings.add(TAG + ":请配置目标文件输出目录" + Constants.TARGET_OUT_PATH + "属性");
            return false;
        }
        if (!containsKey(Constants.TARGET_FILE_FORMAT)) {
            warnings.add(TAG + ":请配置目标文件的文件格式" + Constants.TARGET_FILE_FORMAT + "属性");
            return false;
        }
        warnings.add(TAG + ":配置信息：" + JsonUtils.object2json(getParams()));
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        //固定名称
        String fixationName = getProperty(Constants.TARGET_FIXATION_NAME, "");
        //前缀
        String filePrefix = getProperty(Constants.TARGET_NAME_PREFIX, "");
        //后缀
        String fileSuffix = getProperty(Constants.TARGET_NAME_SUFFIX, "");
        //文件格式
        String fileFormat = getProperty(Constants.TARGET_FILE_FORMAT, "");
        //输出文件路径
        String targetPath = getPath(Constants.TARGET_OUT_PATH, "");
        //模板文件路径
        String templatePath = getPath(Constants.TEMPLATE_FILE_PATH, "");
        //是否删除旧的目标文件
        boolean overwrite = getProperty(Constants.OVERWRITE, false);
        //作者
        String author = getProperty(Constants.AUTHOR, System.getProperty("user.name"));
        //需要忽略的表名
        String ignoreTableName = getProperty(Constants.IGNORE_TABLE_NAME, "");

        String tableName = introspectedTable.getFullyQualifiedTable().toString();

        if (!StringUtils.isEmpty(ignoreTableName) && Pattern.compile(ignoreTableName).matcher(tableName).matches()) {
            Log.i("忽略该表：" + tableName);
            return super.contextGenerateAdditionalJavaFiles(introspectedTable);
        }

        TableModel info = new TableModel(introspectedTable, author);

        Map<String, Object> data = new HashMap<>(getParams());
        data.remove(Constants.TARGET_NAME_PREFIX);
        data.remove(Constants.TARGET_NAME_SUFFIX);
        data.remove(Constants.TARGET_FILE_FORMAT);
        data.remove(Constants.TARGET_OUT_PATH);
        data.remove(Constants.TEMPLATE_FILE_PATH);
        data.putAll(info.getMap());

        //模板文件
        File templateFile = new File(rootPath + templatePath);
        if (templateFile.exists()) {
            Log.i("模板文件:" + templateFile.getPath());
            String targetFileName = filePrefix + info.getName() + fileSuffix;
            //如果有配置固定名字，使用固定名称。必须配置自定义文件路径，不然生成的文件路径和名称冲突
            if (!OtherUtils.isEmpty(fixationName)) {
                targetFileName = fixationName;
            }

            File outFile = new File(rootPath + File.separator + targetPath + File.separator + targetFileName + "." + fileFormat);
            Log.i("目标文件:" + outFile.getPath());
            data.put("target_file_name", targetFileName);

            if (outFile.exists() && !overwrite) {
                Log.i("已存在,忽略:" + outFile.getPath());
            } else {
                //生成文件
                FreemarkerUtils.buildFile(templateFile, outFile, data);
            }
        } else {
            Log.i("不存在！模板文件:" + templateFile.getPath());
        }
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }
}
