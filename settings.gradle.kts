pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}

rootProject.name = "AS-Academy-C"
include(":app", ":core", ":course")

// Core به صورت Repository مرکزی و مجزا نگهداری می شود؛ برای توسعه محلی دو repo را کنار هم clone کنید.
project(":core").projectDir = file("../AS-Academy-Core/core")
project(":course").projectDir = file("../AS-Academy-Core/course")
