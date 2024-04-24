package com.example.rules.utils

import com.android.tools.lint.detector.api.Severity
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

interface DetectorExpiration {

    val implementationDate: LocalDate

    val expirationDate: LocalDate

    fun getDetectorExplanation(explanation: String): String

    fun detectorSeverity(): Severity
}

class DetectorExpirationImpl(
    override val implementationDate: LocalDate,
    override val expirationDate: LocalDate
) : DetectorExpiration {

    override fun getDetectorExplanation(explanation: String): String {
        return """
            $explanation
            
            Implementation Date: $implementationDate
            Expiration Date: $expirationDate
        """.trimIndent()
    }

    override fun detectorSeverity(): Severity {
        val now = Clock.System.now()
        return if ((now - expirationDate.atStartOfDayIn(TimeZone.UTC)).inWholeDays > 0) {
            Severity.ERROR
        } else {
            Severity.WARNING
        }
    }
}
