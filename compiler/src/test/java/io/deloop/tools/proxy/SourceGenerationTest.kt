package io.deloop.tools.proxy

import com.google.testing.compile.CompilationSubject
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler
import com.google.testing.compile.JavaFileObjects
import io.deloop.tools.proxy.helpers.MethodIdGenerator
import org.junit.Test

class SourceGenerationTest {

    @Test fun shouldWordForSimpleCase() {
        compiling("SimpleInput.java") {
            succeededWithoutWarnings()
            and()
            generatedSourceFile("com/example/Sample_ReplayProxy")
                    .hasSourceEquivalentTo(sourceFrom("SimpleOutput.java"))
        }
    }

    @Test fun shouldWorkForNestedInterfaces() {
        compiling("NestedInterfaceInput.java") {
            succeededWithoutWarnings()
            and()
            generatedSourceFile("com/example/SampleNestedInterface_ReplayProxy")
                    .hasSourceEquivalentTo(sourceFrom("NestedInterfaceOutput.java"))
        }
    }

    @Test fun shouldGenerateClassForNestedInterface() {
        compiling("ExtendsInputA.java", "ExtendsInputB.java", "ExtendsInputC.java") {
            succeededWithoutWarnings()
            and()
            generatedSourceFile("com/example/SampleTop_ReplayProxy")
                    .hasSourceEquivalentTo(sourceFrom("ExtendsOutput.java"))
        }
    }

    private fun compiling(vararg files: String, block: CompilationSubject.() -> Unit) {
        assertThat(Compiler.javac()
                .withProcessors(ReplayProxyProcessor.forTests(EmptyGenerator()))
                .compile(files.map { sourceFrom(it) }))
                .block()
    }

    private class EmptyGenerator : MethodIdGenerator {
        override fun uniqueId(methodName: String) = methodName
    }

    private fun sourceFrom(resourceName: String) = JavaFileObjects.forResource("generated/$resourceName")

    private fun CompilationSubject.and() {}
}
