plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.lint.api)
    implementation(libs.kotlin.ext.date)
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "com.example.rules.registry.CustomIssueRegistry")
    }

    configurations["runtimeClasspath"].forEach {
        from(zipTree(it.absoluteFile)) {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}
