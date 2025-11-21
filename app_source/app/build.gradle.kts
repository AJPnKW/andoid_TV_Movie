/*
 * Project: android_tv_movie
 * Module: app
 * Purpose: Android TV WebView wrapper with whitelist + ad blocking
 * Author: Andrew J. Pearen
 * Created: 2025-11-21
 * Last Updated: 2025-11-21
 */

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mytvmovie.wrapper"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mytvmovie.wrapper"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-parent-safe"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.tvprovider:tvprovider:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
