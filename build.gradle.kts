// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt.kotlin.android) apply false
}

buildscript {
    dependencies {
        classpath(libs.apollo.gradle.plugin)
        classpath(libs.gradle)
    }
}