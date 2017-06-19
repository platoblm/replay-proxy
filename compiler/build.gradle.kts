buildscript {
    repositories { jcenter() }
    dependencies { classpath(extra["dependencies.bintray"]) }
}

project.extra["publishing.artifact"] = "replay-reference-compiler"
project.extra["publishing.description"] = "The annotation processor for replay-reference. " +
        "You need to add the 'replay-reference' runtime dependency too."

apply {
    plugin("java-library")
    plugin("kotlin")
    from("../gradle/publishing.gradle")
}

dependencies {
    compile(project(":runtime"))

    listOf("com.google.auto.service:auto-service:1.0-rc3",
            "com.squareup:javapoet:1.9.0",
            extra["dependencies.kotlinStdLib"])
            .forEach { compile(it) }

    listOf(extra["dependencies.junit"],
            extra["dependencies.mockito"],
            extra["dependencies.assertj"],
            "com.google.testing.compile:compile-testing:0.10",
            "org.checkerframework:compiler:2.1.5" /* workaround for ClassNotFoundException: com.sun.source.util.TreeScanner */)
            .forEach { testCompile(it) }
}
