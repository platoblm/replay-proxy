import org.gradle.script.lang.kotlin.extra
import org.gradle.script.lang.kotlin.gradleScriptKotlin
import org.gradle.script.lang.kotlin.kotlinModule
import org.gradle.script.lang.kotlin.repositories

val kotlinVersion = "1.1.2-5"

val commonDependencies = mapOf(
        "kotlinStdLib" to "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion",
        "mockito" to "org.mockito:mockito-core:2.3.7",
        "assertj" to "org.assertj:assertj-core:3.6.1",
        "junit" to "junit:junit:4.12"

).mapKeys { "dependencies.${it.key}" }

buildscript {
    repositories { gradleScriptKotlin() }
    dependencies { classpath(kotlinModule("gradle-plugin")) }
}


allprojects {
    group = "io.deloop.tools.references.replay"
    version = "0.2"

    commonDependencies.forEach { extra[it.key] = it.value }

    buildscript {
        repositories { gradleScriptKotlin() }
        dependencies { classpath(kotlinModule("gradle-plugin")) }
    }

    repositories {
        mavenCentral()
        jcenter()
        gradleScriptKotlin()
    }
}