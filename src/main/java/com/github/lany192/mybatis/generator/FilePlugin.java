package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.Log;
import com.github.lany192.mybatis.generator.utils.Utils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;

import java.beans.Introspector;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Freemarker插件
 *
 * @author Lany
 */
public class FilePlugin extends BasePlugin {

    @Override
    public boolean validate(List<String> warnings) {
        if (isEmpty(Keys.TARGET_PACKAGE)) {
            warnings.add(TAG + ":请配置" + Keys.TARGET_PACKAGE + "属性");
            return false;
        }
        if (isEmpty(Keys.TARGET_PROJECT)) {
            warnings.add(TAG + ":请配置" + Keys.TARGET_PROJECT + "属性");
            return false;
        }
        if (isEmpty(Keys.FILE_FORMAT)) {
            warnings.add(TAG + ":请配置" + Keys.FILE_FORMAT + "属性");
            return false;
        }
        if (isEmpty(Keys.TEMPLATE_PATH)) {
            warnings.add(TAG + ":请配置" + Keys.TEMPLATE_PATH + "属性");
            return false;
        }
        return true;
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

        TableInfo info = new TableInfo(getContext(), introspectedTable);

        Map<String, Object> data = new HashMap<>(getParams());
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

        data.put(Keys.AUTHOR, Utils.getAuthor(author));

        String outDir = Utils.getRootPath(true) + File.separator + targetProject + Utils.package2path(targetPackage);
        String fileName = filePrefix + modelName + fileSuffix;
        //完整路径
        String outFullPath = outDir + fileName + "." + fileFormat;

        File templateFile = new File(Utils.getRootPath(false) + templatePath);
        Log.i(TAG, "模板文件:" + templateFile.getPath());

        File outFile = new File(outFullPath);
        Log.i(TAG, "目标文件:" + outFile.getPath());

        data.put(Keys.FILE_NAME, fileName);
        //生成文件
        Utils.buildFile(templateFile, outFile, data);
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }
}
