package io.deloop.tools.proxy.specs

import com.squareup.javapoet.JavaFile
import io.deloop.tools.proxy.helpers.MethodIdGenerator
import io.deloop.tools.proxy.validation.Validator
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class FileGenerator(private val elements: Elements,
                    private val types: Types,
                    private val filer: Filer,
                    private val messager : Messager,
                    private val methodIdGenerator: MethodIdGenerator) {

    fun createFor(input: Element) {
        val isValid = Validator(input, types, messager).validate()
        if (!isValid) {
            return
        }

        val typeSpec = TypeSpecGenerator(input, elements, types, filer, methodIdGenerator)
                .create()

        JavaFile.builder(generatedPackage(input), typeSpec)
                .build()
                .writeTo(filer)
    }

    private fun generatedPackage(input: Element) = elements
            .getPackageOf(input)
            .qualifiedName
            .toString()
}