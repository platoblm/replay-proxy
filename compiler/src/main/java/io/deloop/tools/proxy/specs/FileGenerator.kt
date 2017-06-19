package io.deloop.tools.proxy.specs

import com.squareup.javapoet.JavaFile
import io.deloop.tools.proxy.helpers.MethodIdGenerator
import io.deloop.tools.proxy.validation.Validator
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

class FileGenerator(private val env: ProcessingEnvironment,
                    private val methodIdGenerator: MethodIdGenerator) {

    fun createFor(input: Element) {
        val isValid = Validator(env, input).validate()
        if (!isValid) {
            return
        }

        val typeSpec = TypeSpecGenerator(input, env, methodIdGenerator)
                .createSpec()

        JavaFile.builder(generatedPackage(input), typeSpec)
                .build()
                .writeTo(env.filer)
    }

    private fun generatedPackage(input: Element) = env
            .elementUtils
            .getPackageOf(input)
            .qualifiedName
            .toString()
}