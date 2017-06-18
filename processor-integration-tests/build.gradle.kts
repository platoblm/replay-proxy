apply {
    plugin("java-library")
    plugin("kotlin")
    plugin("kotlin-kapt")

}

dependencies {
    kapt(project(":processor"))

    compile(project(":runtime"))

    listOf(extra["dependencies.junit"],
            extra["dependencies.mockito"],
            extra["dependencies.assertj"],
            extra["dependencies.kotlinStdLib"]

    ).forEach { testCompile(it) }
}

