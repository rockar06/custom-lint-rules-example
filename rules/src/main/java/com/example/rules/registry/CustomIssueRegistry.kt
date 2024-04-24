package com.example.rules.registry

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.example.rules.detectors.AndroidLogDetector

class CustomIssueRegistry : IssueRegistry() {

    override val issues: List<Issue> = listOf(
        AndroidLogDetector.ISSUE
    )

    override val api: Int = CURRENT_API

    override val vendor: Vendor = Vendor("Armando Ruiz")
}
