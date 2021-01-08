/*
 *    Copyright [2021] [MR3Y]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mr3y.plugins.kroovy

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class ConvertAction: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        TODO("Not yet implemented")
    }

    override fun update(e: AnActionEvent) {
        val focusedFile = e.getData(CommonDataKeys.PSI_FILE)
        val actionPresentation = e.presentation
        if (focusedFile != null) {
            when (focusedFile.fileType.defaultExtension) {
                "gradle" -> actionPresentation.isEnabledAndVisible = true
                "gradle.kts" -> {
                    actionPresentation.isVisible = true
                    actionPresentation.isEnabled = false
                }
                else -> actionPresentation.isEnabledAndVisible = false
            }
            return
        }
        actionPresentation.isVisible = false
        return
    }
}