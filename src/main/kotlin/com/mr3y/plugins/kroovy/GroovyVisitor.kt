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

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Conditions
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.daemon.common.trimQuotes
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.plugins.groovy.lang.psi.GroovyRecursiveElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariable
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrVariableDeclaration
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import org.jetbrains.plugins.groovy.lang.psi.api.types.GrBuiltInTypeElement

class GroovyVisitor(private val project: Project): GroovyRecursiveElementVisitor(){

    private val factory by lazy {
        KtPsiFactory(project)
    }

    override fun visitVariableDeclaration(variableDeclaration: GrVariableDeclaration) {
        super.visitVariableDeclaration(variableDeclaration)
        val varInitializing = PsiTreeUtil.getChildOfType(variableDeclaration, GrVariable::class.java)!!
        if (varInitializing.text.contains("=")) {
            val modifiers = variableDeclaration.modifierList
            if (modifiers.text.contains("def")) {
                val valModifier = factory.createValKeyword().firstChild
                modifiers.replace(valModifier)
                return
            }
            val type = PsiTreeUtil.findChildOfType(variableDeclaration, GrBuiltInTypeElement::class.java)!!
            val typedVariable = factory.createProperty("val x: ${type.text.capitalize()} = 1")
            typedVariable.findElementAt(4)!!.replace(varInitializing.firstChild)
            typedVariable.lastChild!!.replace(varInitializing.lastChild)
            return
        }
        for (variable in variableDeclaration.variables) {
            val abstractDeclarationInKotlin = factory.createValKeyword()
            abstractDeclarationInKotlin.findElementAt(4)!!.replace(variable.nameIdentifier!!)
            val parent = PsiTreeUtil.findFirstParent(variableDeclaration, Conditions.alwaysTrue())
            parent?.addAfter(abstractDeclarationInKotlin, variableDeclaration)
        }
        variableDeclaration.removeStatement() // it can be safely removed now
    }

    override fun visitAssignmentExpression(expression: GrAssignmentExpression) {
        super.visitAssignmentExpression(expression)
        val rOperand = expression.lastChild
        if(isSingleQuotedString(rOperand)) {
            val ktString = factory.createLiteralStringTemplateEntry(rOperand.text.trimQuotes())
            rOperand.replace(ktString)
            return
        }
    }

    private fun isSingleQuotedString(element: PsiElement): Boolean {
        return (element is GrLiteral && element.text.matches(SINGLE_QUOTED_PATTERN.toRegex()))
    }

    companion object {
        private const val SINGLE_QUOTED_PATTERN = """^'.{2,}'$"""
    }
}