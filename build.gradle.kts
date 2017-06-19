val commonDependencies = mapOf(
        "kotlinStdLib" to "org.jetbrains.kotlin:kotlin-stdlib:1.1.2-5",
        "mockito" to "org.mockito:mockito-core:2.3.7",
        "assertj" to "org.assertj:assertj-core:3.6.1",
        "junit" to "junit:junit:4.12",
        "bintray" to "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.3")
        .mapKeys { "dependencies.${it.key}" }

buildscript {
    repositories { gradleScriptKotlin() }
    dependencies { classpath(kotlinModule("gradle-plugin")) }
}

allprojects {
    group = "io.deloop.tools.references.replay"
    version = "0.2"

    commonDependencies.forEach { extra[it.key] = it.value }

    repositories { jcenter() }
}


// to deploy run  ./gradlew clean build bintrayPublishAll
tasks {
    "bintrayPublishAll" {
        listOf("compiler", "runtime").forEach {
            dependsOn(":$it:bintrayUpload")
        }
    }
}
