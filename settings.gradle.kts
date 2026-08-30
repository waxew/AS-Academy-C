pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }

    // ماژول های Core از aliasهای libs استفاده می کنند؛ Course App همان catalog مرکزی را import می کند.
    versionCatalogs {
        create("libs") {
            from(files("../AS-Academy-Core/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "AS-Academy-C"
include(":app", ":core", ":course", ":engine")

// Core به صورت Repository مرکزی و مجزا نگهداری می شود؛ برای توسعه محلی دو repo را کنار هم clone کنید.
project(":core").projectDir = file("../AS-Academy-Core/core")
project(":course").projectDir = file("../AS-Academy-Core/course")
project(":engine").projectDir = file("../AS-Academy-Core/engine")
