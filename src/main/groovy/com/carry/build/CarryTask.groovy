package com.carry.build

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CarryTask extends DefaultTask {

    CarryTask() {
        super()
        group = 'patcher'
    }

    @TaskAction
    public void test() {
        println("this is carry task>>>>>>>>>>>>>>")
    }
}