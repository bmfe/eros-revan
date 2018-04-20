package com.carry.build

import com.carry.build.utils.BaseUtils

import com.carry.build.utils.JarZipUtil
import com.carry.build.utils.JsCallbackTransfer
import com.carry.build.utils.WXTypeFaceTransfer
import javassist.ClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.JarClassPath
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.util.jar.JarFile

class Injector {
    static def classPathList = new ArrayList<JarClassPath>()


    public static void injectJar(String path, Project project) {
        ClassPool pool = ClassPool.getDefault()
//        def classPath = new JarClassPath(path)
//        classPathList.add(classPath)
//        pool.appendClassPath(classPath)
        //project.android.bootClasspath 加入android.jar，否则找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        if (BaseUtils.isTargetJar(path)) {
            //sdk jar
            project.logger.error("--------找到WEEXSDK------------")
            File jarFile = new File(path);
            if (!jarFile.exists()) return
            String jarZipDir = jarFile.getParent() + File.separator + jarFile.getName().replace(".jar", "")
            List classNameList = JarZipUtil.unzipJar(path, jarZipDir);
            jarFile.delete()

            pool.appendClassPath(jarZipDir)
            BaseUtils.importBaseClass(pool)
            for (String className : classNameList) {
                if (className.endsWith(".class") && !className.contains('R$') && !className.contains('$')//代理类
                        && !className.contains('R.class') && !className.contains("BuildConfig.class")) {
                    if (className.contains(WXTypeFaceTransfer.CLASS_NAME)) {
                        WXTypeFaceTransfer typeFaceTransfer = new WXTypeFaceTransfer();
                        typeFaceTransfer.transfer(pool, jarZipDir)
                    }
                    if (className.contains(JsCallbackTransfer.CLASS_NAME)) {
                        JsCallbackTransfer jsCallbackTransfer = new JsCallbackTransfer();
                        jsCallbackTransfer.transfer(pool, jarZipDir)
                    }
                }
            }
            JarZipUtil.zipJar(jarZipDir, path)

            //delete temp dir
            FileUtils.deleteDirectory(new File(jarZipDir))
        }
    }


    public static void injectDir(String path, String packageName, Project project) {

    }


    static void removeClassPath(Project project) {
        if (classPathList != null) {
            def pool = ClassPool.getDefault()
            classPathList.each {
                try {

                    pool.removeClassPath(it)
                } catch (Exception e) {
                    project.logger.error(e.getMessage())
                }
            }
            classPathList.clear()
        }
    }


}
