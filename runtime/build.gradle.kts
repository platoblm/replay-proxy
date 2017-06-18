apply {
    plugin("java-library")
}

dependencies {

    listOf(extra["dependencies.junit"],
            extra["dependencies.mockito"],
            extra["dependencies.assertj"])
            .forEach { testCompile(it) }
}

