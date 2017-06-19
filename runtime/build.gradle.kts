buildscript {
    repositories { jcenter() }
    dependencies { classpath(extra["dependencies.bintray"]) }
}

project.extra["publishing.artifact"] = "replay-reference"
project.extra["publishing.description"] = "The runtime dependency for replay-reference. " +
        "You need to add the 'replay-reference-compiler' annotation processor too."

apply {
    plugin("java-library")
    from("../publishing.gradle")
}

dependencies {
    listOf(extra["dependencies.junit"],
            extra["dependencies.mockito"],
            extra["dependencies.assertj"])
            .forEach { testCompile(it) }
}
