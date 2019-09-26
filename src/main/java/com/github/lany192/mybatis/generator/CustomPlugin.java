package com.github.lany192.mybatis.generator;

import com.github.lany192.mybatis.generator.model.TableInfo;
import com.github.lany192.mybatis.generator.utils.JsonUtils;
import com.github.lany192.mybatis.generator.utils.Log;
import com.github.lany192.mybatis.generator.utils.Utils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;

import java.beans.Introspector;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPlugin extends BasePlugin {
    //模板文件绝对路径
    private final String TEMPLATE_FILE_ABSOLUTE_PATH = "template_file_absolute_path";
    //目标文件格式
    private final String TARGET_FILE_FORMAT = "target_file_Format";
    //生成目标文件所在目录
    private final String TARGET_FILE_OUT_DIR = "target_file_out_dir";
    //目标文件名称前缀
    private final String TARGET_FILE_NAME_PREFIX = "target_file_name_prefix";
    //目标文件名称后缀
    private final String TARGET_FILE_NAME_SUFFIX = "target_file_name_suffix";

    @Override
    public boolean validate(List<String> warnings) {
        if (isEmpty(TEMPLATE_FILE_ABSOLUTE_PATH)) {
            warnings.add(TAG + ":请配置模板文件绝对路径" + TEMPLATE_FILE_ABSOLUTE_PATH + "属性");
            return false;
        }
        if (isEmpty(TARGET_FILE_OUT_DIR)) {
            warnings.add(TAG + ":请配置目标文件输出目录" + TARGET_FILE_OUT_DIR + "属性");
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
        String targetOutDirPath = getProperty(TARGET_FILE_OUT_DIR);
        //模板文件完整路径
        String templateFilePath = getProperty(TEMPLATE_FILE_ABSOLUTE_PATH);

        Map<String, Object> data = new HashMap<>(getParams());
        data.remove(TARGET_FILE_NAME_PREFIX);
        data.remove(TARGET_FILE_NAME_SUFFIX);
        data.remove(TARGET_FILE_FORMAT);
        data.remove(TARGET_FILE_OUT_DIR);
        data.remove(TEMPLATE_FILE_ABSOLUTE_PATH);

        TableInfo info = new TableInfo(getContext(), introspectedTable);
        Log.i(info.getName() + "信息:" + JsonUtils.object2json(info));
        data.put("model_type", info.getFullType());
        data.put("model_name", info.getName());
        data.put("model_name_lower", Introspector.decapitalize(info.getName()));
        data.put("model_has_blob", info.isHasBlob());
        if (info.isHasBlob()) {
            data.put("model_blob_type", info.getFullBlobType());
            data.put("model_blob_name", info.getBlobName());
            data.put("model_blob_name_lower", Introspector.decapitalize(info.getBlobName()));
        }
        File templateFile = new File(templateFilePath);
        Log.i("模板文件:" + templateFile.getPath());

        String targetFileName = filePrefix + info.getName() + fileSuffix;
        //完整路径
        String outFileFullPath = targetOutDirPath + targetFileName + "." + fileFormat;
        File outFile = new File(outFileFullPath);
        Log.i("目标文件:" + outFile.getPath());
        outFile.deleteOnExit();
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("目标文件生成失败:" + e.getMessage());
        }
        data.put("target_file_name", targetFileName);
        //生成文件
        Utils.buildFile(templateFile, outFile, data);
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }
}
