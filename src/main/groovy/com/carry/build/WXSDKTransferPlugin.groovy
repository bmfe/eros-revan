package com.carry.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension

class WXSDKTransferPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("eros-revan:1.0.0")
        def android = project.extensions.getByType(AppExtension)
        def transform = new WXSDKTransform(project)
        android.registerTransform(transform)
    }
}
