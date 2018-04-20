package com.carry.build.utils

import javassist.ClassPool
import javassist.CtClass

class BaseUtils {
    //目标包名
    static String TARGET_PACKAGE_NAME = "com.taobao.weex.WXSDKEngine";

    static void importBaseClass(ClassPool classPool) {
        classPool.importPackage("android.util.Log")
        classPool.importPackage("java.util.Map")
        classPool.importPackage("java.lang.reflect.Method")
        classPool.importPackage("com.taobao.weex.utils.FontDO")
        classPool.importPackage("com.taobao.weex.utils.WXFileUtils")
        classPool.importPackage("java.io.File")
        classPool.importPackage("java.io.Serializable")
    }


    static String getClassName(int index, String filePath) {
        System.out.println("filePath>>>>>>>>>" + filePath);
        int end = filePath.length() - 6 // .class = 6
        return filePath.substring(index, end).replace('\\', '.').replace('/', '.')
    }

    static boolean isTargetJar(String path) {
        return JarZipUtil.findClass(path, TARGET_PACKAGE_NAME);
    }

    static void defrost(CtClass ctClass) {
        if (ctClass.isFrozen()) ctClass.defrost()
    }
}
