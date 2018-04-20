package com.carry.build.utils;

import javassist.ClassPool;
import javassist.CtClass
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException

import java.lang.reflect.Modifier;

/**
 * Created by Carry on 2018/4/16.
 * TypefaceUtil转化类
 */

public class WXTypeFaceTransfer {
    static String CLASS_NAME = "com.taobao.weex.utils.TypefaceUtil";
    static String METHOD_NAME = "loadTypeface";
    static String FIELD_NAME = "EROS";
    static String TEMPLATE = "  if (\$1 != null && \$1.getTypeface() == null &&\n" +
            "                (\$1.getState() != FontDO.STATE_SUCCESS )) {\n" +
            "            if(\$1.getType()==FontDO.TYPE_UNKNOWN){\n" +
            "                final String url = \$1.getUrl();\n" +
            "                final String fontFamily = \$1.getFontFamilyName();\n" +
            "                final String fileName = WXFileUtils.md5(url);\n" +
            "                File dir = new File(getFontCacheDir());\n" +
            "                if(!dir.exists()){\n" +
            "                    dir.mkdirs();\n" +
            "                }\n" +
            "                final String fullPath =  dir.getAbsolutePath()+ File.separator " +
            "+fileName;\n" +
            "                downloadFontByNetwork(url, fullPath, fontFamily);\n" +
            "            }\n" +
            "        }";

    void transfer(ClassPool classPool, String path) {
        CtClass ctClass = null;
        System.out.println("--------loadTypeface----------");
        ctClass = classPool.getCtClass(CLASS_NAME);
        BaseUtils.defrost(ctClass);
        try {
            CtField mark = ctClass.getDeclaredField(FIELD_NAME);
        } catch (NotFoundException e) {
            //field not found inject
            if (ctClass != null) {
                CtMethod ctMethod = ctClass.getDeclaredMethod(WXTypeFaceTransfer.METHOD_NAME);
                ctMethod.insertAfter(TEMPLATE);
                CtClass stringType = classPool.get("java.lang.String");
                CtField mark = new CtField(stringType, FIELD_NAME, ctClass)
                mark.setModifiers(Modifier.STATIC)
                ctClass.addField(mark)
                ctClass.writeFile(path);
            }
        } finally {
            ctClass.detach();
        }
    }


}
