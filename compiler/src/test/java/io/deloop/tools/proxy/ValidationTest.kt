package io.deloop.tools.proxy

import com.google.testing.compile.CompilationSubject
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler.javac
import com.google.testing.compile.JavaFileObjects
import org.junit.Test

class ValidationTest {

    @Test fun shouldSucceedForValidInterface() {
        compiling("ValidInterface.java").succeededWithoutWarnings()
    }

    @Test fun shouldFailWhenTooManyMethodsAnnotatedAsReplayAlways() {
        compiling("TooManyReplayAlways.java")
                .hadErrorContaining("Interface has too many methods annotated with")
    }

    @Test fun shouldFailWhenClassAnnotated() {
        compiling("InvalidAnnotatedType.java")
                .hadErrorContaining("can only be applied to interfaces")
    }

    @Test fun shouldFailWhenReturnTypeIsInvalid() {
        compiling("InvalidReturnType.java")
                .hadErrorContaining("Has non-void method")
    }

    @Test fun shouldFailWhenInheritedMethodReturnTypeIsInvalid() {
        compiling("InvalidInheritedMethodA.java", "InvalidInheritedMethodB.java")
                .hadErrorContaining("Has non-void method")
    }

    private fun compiling(vararg files: String): CompilationSubject {
        val javaObjects = files
                .map { "validation/$it" }
                .map { JavaFileObjects.forResource(it) }

        return assertThat(javac()
                .withProcessors(ReplayProxyProcessor())
                .compile(javaObjects))
    }
}
