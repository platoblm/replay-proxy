package io.deloop.tools.proxy.specs

import com.squareup.javapoet.*
import io.deloop.tools.proxy.ReplayProxyFactory.GENERATED_CLASS_SUFFIX
import io.deloop.tools.proxy.helpers.MethodIdGenerator
import io.deloop.tools.proxy.internal.BaseImpl
import java.io.IOException
import java.util.*
import javax.annotation.processing.Filer
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.INTERFACE
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter.methodsIn
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class TypeSpecGenerator(input: Element, private val elements: Elements, private val types: Types,
                                 private val filer: Filer, private val methodIdGenerator: MethodIdGenerator) {

    private val inputInterface = input as TypeElement // the annotated interface

    fun generateFile() {
        val newClassName = inputInterface.simpleName.toString() + GENERATED_CLASS_SUFFIX

        val inputTypeName = TypeName.get(inputInterface.asType())
        val parentClassName = ClassName.get(BaseImpl::class.java)

        val typeSpec = TypeSpec.classBuilder(newClassName)
                .addModifiers(PUBLIC, FINAL)
                .addSuperinterface(inputTypeName)
                .superclass(ParameterizedTypeName.get(parentClassName, inputTypeName))

        allMethodsToBeImplemented()
                .map { methodSpecFor(it) }
                .forEach{ typeSpec.addMethod(it) }

        JavaFile
                .builder(generatedPackage(), typeSpec.build())
                .build()
                .writeFile()
    }

    private fun generatedPackage() = elements
                .getPackageOf(inputInterface)
                .qualifiedName
                .toString()

    private fun allMethodsToBeImplemented(): List<ExecutableElement> {
        val enclosedElements = ArrayList<Element>()
        addEnclosedElementsOf(inputInterface, enclosedElements)
        return methodsIn(enclosedElements)
    }

    private fun addEnclosedElementsOf(el: Element, to: MutableList<Element>) {
        to.addAll(el.enclosedElements)

        // add methods of extended interfaces too
        types.directSupertypes(el.asType())
                .map { types.asElement(it) } // super type
                .filter { isInterface(it) }
                .forEach{ addEnclosedElementsOf(it, to)}
    }

    private fun isInterface(el: Element)=  el.kind == INTERFACE

    private fun methodSpecFor(method: ExecutableElement)= MethodSpecGenerator(inputInterface, method, methodIdGenerator)
                .generate()

    private fun JavaFile.writeFile() {
        try {
            writeTo(filer)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }
}
