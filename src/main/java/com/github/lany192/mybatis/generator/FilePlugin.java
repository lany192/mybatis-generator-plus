package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.Log;
import com.github.lany192.mybatis.generator.utils.Utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

import java.beans.Introspector;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Freemarker插件
 *
 * @author Lany
 */
public class FilePlugin extends PluginAdapter {
    private final String TAG = getClass().getSimpleName();
    private Map<String, Object> params = new HashMap<>();

    @Override
    public boolean validate(List<String> warnings) {
        Properties properties = getProperties();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (StringUtility.stringHasValue(value)) {
                params.put(key, value);
            }
        }
        Log.i(TAG, "所有属性:" + JsonUtils.object2json(params));
        if (!StringUtility.stringHasValue(getProperty(Keys.TARGET_PACKAGE))) {
            warnings.add(TAG + ":请配置" + Keys.TARGET_PACKAGE + "属性");
            return false;
        }
        if (!StringUtility.stringHasValue(getProperty(Keys.FILE_SUFFIX))) {
            warnings.add(TAG + ":请配置" + Keys.FILE_SUFFIX + "属性");
            return false;
        }
        if (!StringUtility.stringHasValue(getProperty(Keys.FILE_FORMAT))) {
            warnings.add(TAG + ":请配置" + Keys.FILE_FORMAT + "属性");
            return false;
        }
        return true;
    }

    private String getProperty(String key) {
        String value = (String) params.get(key);
        if (value == null) {
            return "";
        }
        return value;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        //前缀
        String filePrefix = getProperty(Keys.FILE_PREFIX);
        //后缀
        String fileSuffix = getProperty(Keys.FILE_SUFFIX);
        //文件格式
        String fileFormat = getProperty(Keys.FILE_FORMAT);
        //输出文件相对路径
        String targetProject = getProperty(Keys.TARGET_PROJECT);
        //包名
        String targetPackage = getProperty(Keys.TARGET_PACKAGE);
        //作者
        String author = getProperty(Keys.AUTHOR);
        //模板文件，相对完整路径。例如"/src/main/resources/templates/service.ftl"
        String templatePath = getProperty(Keys.TEMPLATE_PATH);

        TableInfo info = new TableInfo(introspectedTable);

        Map<String, Object> data = new HashMap<>(params);
        data.remove(Keys.TEMPLATE_PATH);
        data.remove(Keys.FILE_SUFFIX);
        data.remove(Keys.FILE_FORMAT);

        JavaModelGeneratorConfiguration modelConfig = getContext().getJavaModelGeneratorConfiguration();
        String modelType = introspectedTable.getBaseRecordType();
        String modelPackage = modelConfig.getTargetPackage();
        String modelName = modelType.replace(modelPackage + ".", "");


        data.put("modelType", modelType);
        data.put("modelPackage", modelPackage);
        data.put("modelName", modelName);
        data.put("modelNameLower", Introspector.decapitalize(modelName));

        data.put(Keys.AUTHOR, author.equals("") ? System.getProperty("user.name") : author);

        String outDir = getRootPath(true) + File.separator + targetProject + Utils.package2path(targetPackage);
        String fileName = filePrefix + modelName + fileSuffix;
        String outFilePath = outDir + fileName + "." + fileFormat;

        File templateFile = new File(getRootPath(false) + templatePath);
        Log.i(TAG, "模板文件:" + templateFile.getPath());

        File outFile = new File(outFilePath);
        Log.i(TAG, "目标文件:" + outFile.getPath());

        data.put(Keys.FILE_NAME, fileName);
        //生成文件
        build(templateFile, outFile, data);
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }

    private String getRootPath(boolean check) {
        String rootPath = System.getProperty("user.dir");
        if (!check || !rootPath.contains("dao")) {
            return rootPath;
        }
        File rootFile = new File(rootPath);
        return rootFile.getParentFile().getPath();
    }

    public void build(File templateFile, File outFile, Map<String, Object> params) {
        if (!templateFile.exists()) {
            Log.i(TAG, "找不到对应的模板文件");
            return;
        }
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDirectoryForTemplateLoading(templateFile.getParentFile());
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            Template template = cfg.getTemplate(templateFile.getName());
            Writer writer = new FileWriter(outFile);
            template.process(params, writer);
            Log.i(TAG, outFile.getPath() + "文件生成成功");
        } catch (Exception e) {
            throw new RuntimeException(outFile.getPath() + "文件生成失败", e);
        }
    }
}
