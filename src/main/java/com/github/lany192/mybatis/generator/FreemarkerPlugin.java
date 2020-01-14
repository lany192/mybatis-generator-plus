package com.github.lany192.mybatis.generator;

import com.alibaba.fastjson.JSON;
import com.github.lany192.mybatis.generator.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.Log;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;

import java.beans.Introspector;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreemarkerPlugin extends BasePlugin {
    //模板文件相对路径
    private final String TEMPLATE_FILE_RELATIVE_PATH = "template_file_relative_path";
    //目标文件格式
    private final String TARGET_FILE_FORMAT = "target_file_format";
    //生成目标文件所在目录
    private final String TARGET_FILE_RELATIVE_OUT_DIR = "target_file_relative_out_dir";
    //目标文件名称前缀
    private final String TARGET_FILE_NAME_PREFIX = "target_file_name_prefix";
    //目标文件名称后缀
    private final String TARGET_FILE_NAME_SUFFIX = "target_file_name_suffix";

    @Override
    public boolean validate(List<String> warnings) {
        if (isEmpty(TEMPLATE_FILE_RELATIVE_PATH)) {
            warnings.add(TAG + ":请配置模板文件相对路径" + TEMPLATE_FILE_RELATIVE_PATH + "属性");
            return false;
        }
        if (isEmpty(TARGET_FILE_RELATIVE_OUT_DIR)) {
            warnings.add(TAG + ":请配置目标文件输出目录" + TARGET_FILE_RELATIVE_OUT_DIR + "属性");
            return false;
        }
        if (isEmpty(TARGET_FILE_FORMAT)) {
            warnings.add(TAG + ":请配置目标文件的文件格式" + TARGET_FILE_FORMAT + "属性");
            return false;
        }
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        //前缀
        String filePrefix = getProperty(TARGET_FILE_NAME_PREFIX);
        //后缀
        String fileSuffix = getProperty(TARGET_FILE_NAME_SUFFIX);
        //文件格式
        String fileFormat = getProperty(TARGET_FILE_FORMAT);
        //输出文件路径
        String targetRelativeOutDirPath = getProperty(TARGET_FILE_RELATIVE_OUT_DIR);
        //模板文件完整路径
        String templateFilePath = getProperty(TEMPLATE_FILE_RELATIVE_PATH);

        targetRelativeOutDirPath = path2path(targetRelativeOutDirPath);
        templateFilePath = path2path(templateFilePath);

        Map<String, Object> data = new HashMap<>(getParams());
        data.remove(TARGET_FILE_NAME_PREFIX);
        data.remove(TARGET_FILE_NAME_SUFFIX);
        data.remove(TARGET_FILE_FORMAT);
        data.remove(TARGET_FILE_RELATIVE_OUT_DIR);
        data.remove(TEMPLATE_FILE_RELATIVE_PATH);

        TableInfo info = new TableInfo(getContext(), introspectedTable);
        Log.i(info.getName() + "信息:" + JSON.toJSONString(info));
        //项目的根目录，相对多模块而言
        String rootPath = new File(System.getProperty("user.dir")).getParent();
        //模板文件
        File templateFile = new File(rootPath + templateFilePath);
        Log.i("模板文件:" + templateFile.getPath());
        if (templateFile.exists()) {
            File outDirFile = new File(rootPath + targetRelativeOutDirPath);
            Log.i("目标文件输出目录:" + outDirFile.getPath());
            String targetFileName = filePrefix + info.getName() + fileSuffix;
            File outFile = new File(outDirFile.getPath() + targetFileName + "." + fileFormat);
            Log.i("目标文件:" + outFile.getPath());
            data.put("target_file_name", targetFileName);
            data.put("author", System.getProperty("user.name"));
            //中文名称，从备注中获取
            data.put("model_zname", info.getRemark());
            data.put("model_type", info.getFullType());
            data.put("model_name", info.getName());
            data.put("model_name_lower", Introspector.decapitalize(info.getName()));
            data.put("model_has_blob", info.isHasBlob());
            if (info.isHasBlob()) {
                data.put("model_blob_type", info.getFullBlobType());
                data.put("model_blob_name", info.getBlobName());
                data.put("model_blob_name_lower", Introspector.decapitalize(info.getBlobName()));
            }
            data.put("model_fields", info.getFields());
            //生成文件
            buildFile(templateFile, outFile, data);
        } else {
            Log.i("模板文件:" + templateFile.getPath() + "不存在！");
        }
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }

    /**
     * 将路径中的路径分隔符转换成当前系统的路径分隔符
     */
    private String path2path(String path) {
        path = path.replace("\\", File.separator);
        path = path.replace("/", File.separator);
        return path;
    }

    /**
     * 根据模板文件生成新文件
     *
     * @param templateFile 模板
     * @param outFile      新文件
     * @param params       参数
     */
    private void buildFile(File templateFile, File outFile, Map<String, Object> params) {
        if (!templateFile.exists()) {
            Log.i("找不到对应的模板文件" + templateFile.getPath());
            return;
        }
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        Log.i("定义的属性:" + JSON.toJSONString(params));
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDirectoryForTemplateLoading(templateFile.getParentFile());
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            Template template = cfg.getTemplate(templateFile.getName());
            Writer writer = new FileWriter(outFile);
            template.process(params, writer);
            Log.i(outFile.getPath() + "文件生成成功");
        } catch (Exception e) {
            throw new RuntimeException(outFile.getPath() + "文件生成失败", e);
        }
    }
}
