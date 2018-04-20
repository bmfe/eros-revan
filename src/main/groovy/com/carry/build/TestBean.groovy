package com.carry.build

class TestBean {
    String apk
    boolean ignoreWarning
    boolean useSign

    TestBean() {
        sourceApk = ''
        ignoreWarning = false
        useSign = true
    }


    @Override
    String toString() {
        return "TestBean{" +
                "apk='" + apk + '\'' +
                ", ignoreWarning=" + ignoreWarning +
                ", useSign=" + useSign +
                '}';
    }
}