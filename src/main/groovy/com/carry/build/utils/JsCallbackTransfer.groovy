package com.carry.build.utils

import javassist.ClassPool
import javassist.CtClass
import javassist.NotFoundException

class JsCallbackTransfer {
    public static String CLASS_NAME = "com.taobao.weex.bridge.JSCallback";
    public static String TARGET_SUPERCLASS = "java.io.Serializable"

    void transfer(ClassPool classPool, String path) {
        System.out.println("--------JsCallbackTransfer----------");
        CtClass ctClass = classPool.getCtClass(CLASS_NAME);
        BaseUtils.defrost(ctClass);
        try {
            CtClass[] interfaces = ctClass.getInterfaces()
            System.out.println("--------JsCallbackTransfer----------有接口")
            int index = 0;
            for (CtClass inter : interfaces) {
                System.out.println("--------JsCallbackTransfer----------" + inter.getName())
                if (TARGET_SUPERCLASS.equals(inter.getName())) {
                   index++
                }
            }
            if (index == 0) {
                ctClass.setSuperclass(classPool.getCtClass(TARGET_SUPERCLASS))
                ctClass.writeFile(path)
            }

        } catch (NotFoundException e) {
            e.printStackTrace()
            ctClass.setSuperclass(classPool.getCtClass(TARGET_SUPERCLASS))
            ctClass.writeFile(path)
        } finally {
            ctClass.detach();
        }
    }


}
