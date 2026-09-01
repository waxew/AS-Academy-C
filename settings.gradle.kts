pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }

    // Core remains the central version catalog for every thin Course App.
    versionCatalogs {
        create("libs") {
            from(files("../AS-Academy-Core/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "AS-Academy-C"
include(":app", ":core", ":course", ":engine", ":main-ui")

// Shared runtime/engine.
project(":core").projectDir = file("../AS-Academy-Core/core")
project(":course").projectDir = file("../AS-Academy-Core/course")
project(":engine").projectDir = file("../AS-Academy-Core/engine")

// Shared presentation layer. Clone AS-Academy-MainUi beside this repository for local builds.
project(":main-ui").projectDir = file("../AS-Academy-MainUi/main-ui")
