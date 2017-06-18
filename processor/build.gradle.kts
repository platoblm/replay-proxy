apply {
    plugin("java-library")
    plugin("kotlin")
}

dependencies {
    compile(project(":runtime"))

    listOf("com.google.auto.service:auto-service:1.0-rc3",
            "com.squareup:javapoet:1.9.0",
            extra["dependencies.kotlinStdLib"]

    ).forEach { compile(it) }

    listOf(extra["dependencies.junit"],
            extra["dependencies.mockito"],
            extra["dependencies.assertj"],
            "com.google.testing.compile:compile-testing:0.10",
            "org.checkerframework:compiler:2.1.5" /* workaround for ClassNotFoundException: com.sun.source.util.TreeScanner */

    ).forEach { testCompile(it) }
}

