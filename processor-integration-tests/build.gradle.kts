apply {
    plugin("kotlin")
    plugin("kotlin-kapt")
}

dependencies {
    compile(project(":runtime"))
    kapt(project(":processor"))

    listOf(extra["dependencies.junit"],
            extra["dependencies.mockito"],
            extra["dependencies.assertj"],
            extra["dependencies.kotlinStdLib"])
            .forEach { testCompile(it) }
}

