package com.example.rules.detectors

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.example.rules.utils.DetectorExpiration
import com.example.rules.utils.DetectorExpirationImpl
import com.intellij.psi.PsiMethod
import kotlinx.datetime.LocalDate
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

    companion object : DetectorExpiration by DetectorExpirationImpl(
        implementationDate = LocalDate(2024, 1, 1),
        expirationDate = LocalDate(2024, 4, 30)
    ) {

        private val IMPLEMENTATION = Implementation(
            AndroidLogDetector::class.java,
            Scope.JAVA_FILE_SCOPE
        )

        val ISSUE: Issue = Issue
            .create(
                id = "AndroidLogDetector",
                briefDescription = "The default android Log should not be used in the project",
                explanation = getDetectorExplanation(
                    "Leaving android Log statements in production can expose sensitive " +
                            "information like passwords, API keys, or debug data. This can be a " +
                            "security risk if the app is tampered with."
                ),
                category = Category.SECURITY,
                priority = 8,
                severity = detectorSeverity(),
                androidSpecific = true,
                implementation = IMPLEMENTATION
            )
    }
}
