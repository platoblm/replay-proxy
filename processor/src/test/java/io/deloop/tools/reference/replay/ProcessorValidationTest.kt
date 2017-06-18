package io.deloop.tools.reference.replay

import com.google.testing.compile.CompilationSubject
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler.javac
import com.google.testing.compile.JavaFileObjects
import org.junit.Test

class ProcessorValidationTest {

    @Test fun shouldSucceedForValidInterface() {
        assertThatFor("ValidInterface.java").succeededWithoutWarnings()
    }

    @Test fun shouldFailWhenTooManyMethodsAnnotatedAsReplayAlways() {
        assertThatFor("TooManyReplayAlways.java")
                .hadErrorContaining("Interface has too many methods annotated with")
    }

    @Test fun shouldFailWhenClassAnnotated() {
        assertThatFor("InvalidAnnotatedType.java").hadErrorContaining("Is not an interface.")
    }

    @Test fun shouldFailWhenReturnTypeIsInvalid() {
        assertThatFor("InvalidReturnType.java").hadErrorContaining("Has non-void method")
    }

    @Test fun shouldFailWhenInheritedMethodReturnTypeIsInvalid() {
        assertThatFor("InvalidInheritedMethodA.java", "InvalidInheritedMethodB.java")
                .hadErrorContaining("Has non-void method")
    }

    private fun assertThatFor(vararg files: String): CompilationSubject {
        val javaObjects = files
                .map { "validation/$it" }
                .map { JavaFileObjects.forResource(it) }

        return assertThat(javac()
                .withProcessors(ReplayReferenceProcessor())
                .compile(javaObjects))
    }
}
