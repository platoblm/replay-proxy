package io.deloop.tools.proxy.specs

import com.squareup.javapoet.JavaFile
import io.deloop.tools.proxy.helpers.MethodIdGenerator
import io.deloop.tools.proxy.validation.Validator
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

class FileGenerator(private val env: ProcessingEnvironment,
                    private val idGenerator: MethodIdGenerator) {

    fun createFor(input: Element) {
        if (!Validator(env, input).isValid()) {
            return
        }

        val typeSpec = TypeSpecGenerator(input, env, idGenerator)
                .createSpec()

        JavaFile.builder(packageOf(input), typeSpec)
                .build()
                .writeTo(env.filer)
    }

    private fun packageOf(input: Element) = env
            .elementUtils
            .getPackageOf(input)
            .qualifiedName
            .toString()
}