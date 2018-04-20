package com.carry.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension

class WXSDKTransferPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("------------plugin start----------------")
        def android = project.extensions.getByType(AppExtension)
        def transform = new WXSDKTransform(project)
        android.registerTransform(transform)
        System.out.println("-----------------carry plugin finish----------------")
    }
}
