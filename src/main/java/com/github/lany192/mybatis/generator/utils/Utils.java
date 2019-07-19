package com.github.lany192.mybatis.generator.utils;

import java.io.File;

public class Utils {

    /**
     * 包名转成文件路径.例如"com.github.lany192" -> "/com/github/lany192/"
     *
     * @param packageName 包名
     * @return 文件路径
     */
    public static String package2path(String packageName) {
        if (!packageName.contains(".")) {
            return packageName;
        }
        StringBuilder path = new StringBuilder();
        String[] paths = packageName.split("\\.");
        path.append(File.separator);
        for (String item : paths) {
            path.append(File.separator).append(item);
        }
        path.append(File.separator);
        return path.toString();
    }
}
