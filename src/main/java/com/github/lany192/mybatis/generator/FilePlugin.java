package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.Log;
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
        if (!StringUtility.stringHasValue(getProperty(Keys.PACKAGE_NAME))) {
            warnings.add(TAG + ":请配置" + Keys.PACKAGE_NAME + "属性");
            return false;
        }
        if (!StringUtility.stringHasValue(getProperty(Keys.TEMPLATE_NAME))) {
            warnings.add(TAG + ":请配置" + Keys.TEMPLATE_NAME + "属性");
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
        TableInfo info = new TableInfo(introspectedTable);

        Map<String, Object> data = new HashMap<>(params);
        data.remove(Keys.TEMPLATE_PATH);
        data.remove(Keys.TEMPLATE_NAME);
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

        //前缀
        String prefix = getProperty(Keys.FILE_PREFIX);
        //后缀
        String suffix = getProperty(Keys.FILE_SUFFIX);
        //文件格式
        String format = getProperty(Keys.FILE_FORMAT);

        String targetProject = getProperty(Keys.TARGET_PROJECT);
        String targetPackage = getProperty(Keys.TARGET_PACKAGE);

        String outDir = System.getProperty("user.dir") + File.pathSeparator + targetProject + packagePath(targetPackage);
        String outFileName = outDir + prefix + modelName + suffix + "." + format;

        String templateDir = getProperty(Keys.TEMPLATE_PATH);
        String templateName = getProperty(Keys.TEMPLATE_NAME);
        String templatePath = System.getProperty("user.dir") + templateDir + templateName;
        File templateFile = new File(templatePath);
        Log.i(TAG, "模板文件:" + templateFile.getPath());
        File outFile = new File(outFileName);

        //生成文件
        build(templateFile, outFile, data);
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }

    private String packagePath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    public void build(File templateFile, File outFile, Map<String, Object> params) {
        File templateDir = templateFile.getParentFile();
        if (!templateDir.isDirectory()) {
            Log.i(TAG, "模板文件路径异常");
            return;
        }
        if (outFile.exists()) {
            Log.i(TAG, "已经存在旧的文件，删除");
            outFile.delete();
        }
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDirectoryForTemplateLoading(templateFile);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            Template template = cfg.getTemplate(templateFile.getName());
            Writer writer = new FileWriter(outFile);
            template.process(params, writer);
            Log.i(TAG, "接口文件" + outFile.getPath() + "生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成" + outFile.getPath() + "失败", e);
        }
    }
}
