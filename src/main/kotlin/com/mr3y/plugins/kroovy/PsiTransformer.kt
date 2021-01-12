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

import com.intellij.psi.PsiFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.jetbrains.plugins.groovy.lang.psi.GroovyRecursiveElementVisitor

// TODO: consider if it should be object
class PsiTransformer {

    // investigate if there are action edge cases to be handled(i.e avoid send the event again)
    private val _transformResult = MutableStateFlow(ConvertResult.Initial)
    val transformResult: StateFlow<ConvertResult>
        get() = _transformResult

    /**
     * transform the given groovy gradle file PsiElements to .kts gradle file syntax
     *
     * @return [Finished.Succeeded] or [Finished.Failed] on failure
     */
    fun transform(file: PsiFile): Finished {
        require(file is GroovyFile && file.isScript) {
            return@transform Finished.Failed("Transformation Failure, the file isn't a valid groovy script file")
        }
        file.accept(object : GroovyRecursiveElementVisitor() {

        })
        return Finished.Succeeded
    }
}