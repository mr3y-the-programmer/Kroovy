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
import com.intellij.openapi.progress.ProgressIndicatorProvider
import org.jetbrains.kotlin.com.intellij.openapi.diagnostic.Logger

class ConvertAction: AnAction() {

    private val logger by lazy {
        Logger.getInstance("#com.intellij.openapi.actionSystem.ConvertAction")
    }

    override fun actionPerformed(e: AnActionEvent) {
        val progressIndicator = ProgressIndicatorProvider.getGlobalProgressIndicator()
        val gradleFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        if (progressIndicator != null) {
            PsiTransformer.transform(gradleFile)
            when(val result = PsiTransformer.transformResult.value) {
                is ConvertResult.Initial -> {
                    logger.info("Psi Transformation is about to start!")
                    progressIndicator.apply {
                        isIndeterminate = false
                        fraction = 0.0
                        text = "Processing the file..."
                    }
                }
                is ConvertResult.WorkInProgress -> {
                    val progress = result.percentage
                    logger.info("Psi Transformation has started!, Progress: $progress")
                    progressIndicator.fraction = progress.toDouble().div(100).coerceIn(0.0, 1.0)
                    progressIndicator.text = "Work In Progress..., Go walk around Or get yourself a Coffee"
                }
                is Finished.Succeeded -> {
                    logger.info("Transformation Finished successfully!")
                    progressIndicator.text = "We're Done, Take a look at your file in its new syntax"
                }
                is Finished.Failed -> {
                    val reason = result.getMessage()
                    logger.error(reason, result.exception ?: RuntimeException())
                    progressIndicator.text = "Unfortunately, Transformation Failed:\nreason: $reason"
                }
            }
        }
        // TODO: represent the data in other visual way if progressIndicator isn't available
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