package com.example.rules.detectors

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class AndroidLogDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> =
        listOf("tag", "format", "v", "d", "i", "w", "e", "wtf")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, "android.util.Log")) {
            reportUsage(context, node)
        }
    }

    private fun reportUsage(context: JavaContext, node: UCallExpression) {
        context.report(
            issue = ISSUE,
            scope = node,
            location = context.getCallLocation(
                call = node,
                includeReceiver = true,
                includeArguments = true
            ),
            message = "android.util.Log usage is forbidden in the project."
        )
    }

    companion object {

        private val IMPLEMENTATION = Implementation(
            AndroidLogDetector::class.java,
            Scope.JAVA_FILE_SCOPE
        )

        val ISSUE: Issue = Issue
            .create(
                id = "AndroidLogDetector",
                briefDescription = "The default android Log should not be used in the project",
                explanation = """
                    Leaving android Log statements in production can expose sensitive 
                    information like passwords, API keys, or debug data. This can be a 
                    security risk if the app is tampered with.
                """.trimIndent(),
                category = Category.SECURITY,
                priority = 8,
                severity = Severity.WARNING,
                androidSpecific = true,
                implementation = IMPLEMENTATION
            )
    }
}
