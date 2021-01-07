package com.github.mr3ytheprogrammer.kroovy.services

import com.intellij.openapi.project.Project
import com.github.mr3ytheprogrammer.kroovy.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
