package com.github.lany192.generator.other;

import com.github.lany192.generator.BasePlugin;
import com.github.lany192.generator.utils.FreemarkerUtils;
import com.github.lany192.generator.utils.Log;
import com.github.lany192.generator.model.TableModel;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreemarkerPlugin extends BasePlugin {
    //模板文件相对路径
    private final String TEMPLATE_FILE_PATH = "template_file_path";
    //目标文件格式
    private final String TARGET_FILE_FORMAT = "target_file_format";
    //生成目标文件所在目录
    private final String TARGET_OUT_PATH = "target_out_path";
    //目标文件名称前缀
    private final String TARGET_NAME_PREFIX = "target_name_prefix";
    //目标文件名称后缀
    private final String TARGET_NAME_SUFFIX = "target_name_suffix";
    //是否删除旧的目标文件
    private final String DELETE_OLD_FILE = "delete_old_file";
    //作者
    private final String KEY_AUTHOR = "author";

    @Override
    public boolean validate(List<String> warnings) {
        if (isEmpty(TEMPLATE_FILE_PATH)) {
            warnings.add(TAG + ":请配置模板文件相对路径" + TEMPLATE_FILE_PATH + "属性");
            return false;
        }
        if (isEmpty(TARGET_OUT_PATH)) {
            warnings.add(TAG + ":请配置目标文件输出目录" + TARGET_OUT_PATH + "属性");
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
        String filePrefix = getProperty(TARGET_NAME_PREFIX);
        //后缀
        String fileSuffix = getProperty(TARGET_NAME_SUFFIX);
        //文件格式
        String fileFormat = getProperty(TARGET_FILE_FORMAT);
        //输出文件路径
        String targetRelativeOutDirPath = getProperty(TARGET_OUT_PATH);
        //模板文件完整路径
        String templateFilePath = getProperty(TEMPLATE_FILE_PATH);
        //是否删除旧的目标文件
        String clearOldFile = getProperty(DELETE_OLD_FILE);
        //获取作者
        String author = getProperty(KEY_AUTHOR);

        if (StringUtils.isEmpty(author)) {
            author = System.getProperty("user.name");
        }

        boolean deleteOldFile = true;
        if (!StringUtils.isEmpty(clearOldFile) && clearOldFile.equals("false")) {
            deleteOldFile = false;
        }

        targetRelativeOutDirPath = path2path(targetRelativeOutDirPath);
        templateFilePath = path2path(templateFilePath);

        Map<String, Object> data = new HashMap<>(getParams());
        data.remove(TARGET_NAME_PREFIX);
        data.remove(TARGET_NAME_SUFFIX);
        data.remove(TARGET_FILE_FORMAT);
        data.remove(TARGET_OUT_PATH);
        data.remove(TEMPLATE_FILE_PATH);

        TableModel info = new TableModel(introspectedTable, author);
        data.putAll(info.getMap());

        //项目的根目录，相对多模块而言
        String rootPath = new File(System.getProperty("user.dir")).getParent();
        //模板文件
        File templateFile = new File(rootPath + templateFilePath);
        if (templateFile.exists()) {
            Log.i("模板文件:" + templateFile.getPath());
            File outDirFile = new File(rootPath + targetRelativeOutDirPath);
            Log.i("目标文件输出目录:" + outDirFile.getPath());
            String targetFileName = filePrefix + info.getName() + fileSuffix;
            File outFile = new File(outDirFile.getPath() + File.separator + targetFileName + "." + fileFormat);
            Log.i("目标文件:" + outFile.getPath());
            data.put("target_file_name", targetFileName);

            if (outFile.exists() && !deleteOldFile) {
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

    /**
     * 将路径中的路径分隔符转换成当前系统的路径分隔符
     */
    private String path2path(String path) {
        path = path.replace("\\", File.separator);
        path = path.replace("/", File.separator);
        return path;
    }

}
