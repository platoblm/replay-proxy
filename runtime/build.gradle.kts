buildscript {
    repositories { jcenter() }
    dependencies { classpath(extra["dependencies.bintray"]) }
}

project.extra["publishing.artifact"] = "replay-proxy"
project.extra["publishing.description"] = "The runtime dependency for replay-proxy. " +
        "You need to add the 'replay-proxy-compiler' annotation processor too."

apply {
    plugin("java-library")
    from("../gradle/publishing.gradle")
}

dependencies {
    listOf(extra["dependencies.junit"],
            extra["dependencies.mockito"],
            extra["dependencies.assertj"])
            .forEach { testCompile(it) }
}
