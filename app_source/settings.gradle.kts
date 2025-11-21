/*
 * Project: android_tv_movie
 * Purpose: Android TV wrapper for my_TV_Movie website
 * Author: Andrew J. Pearen
 * Created: 2025-11-21
 * Last Updated: 2025-11-21
 */

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "android_tv_movie"
include(":app")
