//package com.github.lany192.mybatis.generator;
//
//import org.mybatis.generator.api.IntrospectedTable;
//import org.mybatis.generator.api.PluginAdapter;
//import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
//
//import java.util.List;
//
///**
// * 修改Example类的目标地址
// * <p>
// * <!-- 修改Example包名插件 -->
// * <plugin type="com.github.lany192.mybatis.generator.RenameExamplePackagePlugin"/>
// *
// * @author Lany
// */
//public class RenameExamplePackagePlugin extends PluginAdapter {
//    private final String TAG = getClass().getSimpleName();
//
//    @Override
//    public boolean validate(List<String> warnings) {
////        Properties properties = getProperties();
////        this.targetPackage = properties.getProperty("targetPackage");
////        if (this.targetPackage == null) {
////            warnings.add("请配置" + TAG + "插件的目标包名(targetPackage)！");
////            return false;
////        }
//        return true;
//    }
//
//    @Override
//    public void initialized(IntrospectedTable introspectedTable) {
//        super.initialized(introspectedTable);
//        String exampleType = introspectedTable.getExampleType();
//        JavaModelGeneratorConfiguration config = getContext().getJavaModelGeneratorConfiguration();
//        String targetPackage = config.getTargetPackage();
//        String newPackage = targetPackage + ".example";
//        String newExampleType = exampleType.replace(targetPackage, newPackage);
//        // 修改包名
//        introspectedTable.setExampleType(newExampleType);
//        System.out.println(TAG + ":旧包名:" + targetPackage + ",新包名:" + newPackage);
//    }
//}
