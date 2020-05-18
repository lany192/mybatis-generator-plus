package com.github.lany192.generator.other;

import com.github.lany192.generator.BasePlugin;
import com.github.lany192.generator.model.TableModel;
import com.github.lany192.generator.utils.FreemarkerUtils;
import com.github.lany192.generator.utils.Log;
import com.github.lany192.generator.utils.OtherUtils;
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

    //固定目标名
    private final String TARGET_FIXATION_NAME = "target_fixation_name";

    //是否删除旧的目标文件
    private final String DELETE_OLD_FILE = "delete_old_file";
    //作者
    private final String KEY_AUTHOR = "author";
    //是否要添加自定义文件夹路径（将包名的一部分转成文件夹路径）
    private final String KEY_NEED_CUSTOM_PATH = "need_custom_path";

    @Override
    public boolean validate(List<String> warnings) {
        if (!check(TEMPLATE_FILE_PATH)) {
            warnings.add(TAG + ":请配置模板文件相对路径" + TEMPLATE_FILE_PATH + "属性");
            return false;
        }
        if (!check(TARGET_OUT_PATH)) {
            warnings.add(TAG + ":请配置目标文件输出目录" + TARGET_OUT_PATH + "属性");
            return false;
        }
        if (!check(TARGET_FILE_FORMAT)) {
            warnings.add(TAG + ":请配置目标文件的文件格式" + TARGET_FILE_FORMAT + "属性");
            return false;
        }
        //是否删除旧的目标文件
        boolean clearOldFile = getProperty(DELETE_OLD_FILE, false);
        if (clearOldFile) {
            //模板文件完整路径
            String templateFilePath = getProperty(TEMPLATE_FILE_PATH, "");
            File file = new File(templateFilePath);
            //删除整个目录
            if (file.isDirectory()) {
                file.deleteOnExit();
            }
        }
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        //固定名称
        String fixationName = getProperty(TARGET_FIXATION_NAME, "");
        //前缀
        String filePrefix = getProperty(TARGET_NAME_PREFIX, "");
        //后缀
        String fileSuffix = getProperty(TARGET_NAME_SUFFIX, "");
        //文件格式
        String fileFormat = getProperty(TARGET_FILE_FORMAT, "");
        //输出文件路径
        String targetRelativeOutDirPath = getProperty(TARGET_OUT_PATH, "");
        //模板文件完整路径
        String templateFilePath = getProperty(TEMPLATE_FILE_PATH, "");
        //是否删除旧的目标文件
        boolean clearOldFile = getProperty(DELETE_OLD_FILE, false);
        //获取作者
        String author = getProperty(KEY_AUTHOR, "");
        //是否需要自定义文件路径
        boolean needCustomPath = getProperty(KEY_NEED_CUSTOM_PATH, false);

        if (OtherUtils.isEmpty(author)) {
            author = System.getProperty("user.name");
        }
        TableModel info = new TableModel(introspectedTable, author);
//        //判断是否是关联表
//        if (!info.isRelationTable()) {
            if (needCustomPath) {
                targetRelativeOutDirPath = targetRelativeOutDirPath + File.separator + info.getModelNamePath();
            }

            targetRelativeOutDirPath = path2path(targetRelativeOutDirPath);

            templateFilePath = path2path(templateFilePath);

            Map<String, Object> data = new HashMap<>(getParams());
            data.remove(TARGET_NAME_PREFIX);
            data.remove(TARGET_NAME_SUFFIX);
            data.remove(TARGET_FILE_FORMAT);
            data.remove(TARGET_OUT_PATH);
            data.remove(TEMPLATE_FILE_PATH);


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
                //如果有配置固定名字，使用固定名称。必须配置自定义文件路径，不然生成的文件路径和名称冲突
                if (!OtherUtils.isEmpty(fixationName) && needCustomPath) {
                    targetFileName = fixationName;
                }

                File outFile = new File(outDirFile.getPath() + File.separator + targetFileName + "." + fileFormat);
                Log.i("目标文件:" + outFile.getPath());
                data.put("target_file_name", targetFileName);

                if (outFile.exists() && !clearOldFile) {
                    Log.i("已存在,忽略:" + outFile.getPath());
                } else {
                    //生成文件
                    FreemarkerUtils.buildFile(templateFile, outFile, data);
                }
            } else {
                Log.i("不存在！模板文件:" + templateFile.getPath());
            }
//        } else {
//            Log.i("忽略关联表:" + info.getTableName());
//        }
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
