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
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "com.example.rules.registry.CustomIssueRegistry")
    }
}
