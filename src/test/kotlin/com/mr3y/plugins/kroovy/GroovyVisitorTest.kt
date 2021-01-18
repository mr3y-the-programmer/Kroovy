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

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import junit.framework.TestCase
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.junit.Assert
import org.junit.Test

// TODO: need investigation on how to write a test, do your research.
//  one source that might be helpful: https://github.com/JetBrains/intellij-community/tree/master/plugins
class GroovyVisitorTest : LightJavaCodeInsightFixtureTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"

    @Test
    fun `normal declaration with def should return replace def with val`() {
        myFixture.configureByFile("basic_declaration.gradle") // copy and open the file in the editor
        /*val grFile = myFixture.file as GroovyFile
        grFile.accept(GroovyVisitor(grFile.project))*/
        myFixture.testAction(ConvertAction())
        myFixture.checkResultByFile("basic_declaration.gradle.kts")
    }


    public fun doTest() { `normal declaration with def should return replace def with val`() }
}