package io.deloop.tools.proxy.specs

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import io.deloop.tools.proxy.ReplayProxyFactory.GENERATED_CLASS_SUFFIX
import io.deloop.tools.proxy.helpers.MethodIdGenerator
import io.deloop.tools.proxy.internal.BaseImpl
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.INTERFACE
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.util.ElementFilter.methodsIn

class TypeSpecGenerator(private val input: Element,
                                 env: ProcessingEnvironment,
                                 private val methodIdGenerator: MethodIdGenerator) {

    private val types = env.typeUtils

    fun createSpec() : TypeSpec {
        val newClassName = input.simpleName.toString() + GENERATED_CLASS_SUFFIX

        val inputTypeName = TypeName.get(input.asType())
        val parentClassName = ClassName.get(BaseImpl::class.java)

        val typeSpec = TypeSpec.classBuilder(newClassName)
                .addModifiers(PUBLIC, FINAL)
                .addSuperinterface(inputTypeName)
                .superclass(ParameterizedTypeName.get(parentClassName, inputTypeName))

        allMethodsToBeImplemented()
                .map { methodSpecFor(it) }
                .forEach{ typeSpec.addMethod(it) }

        return typeSpec.build()
    }

    private fun allMethodsToBeImplemented(): List<ExecutableElement> {
        val enclosedElements = ArrayList<Element>()
        addEnclosedElementsOf(input, enclosedElements)
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

    private fun methodSpecFor(method: ExecutableElement)= MethodSpecGenerator(input, method, methodIdGenerator)
                .createSpec()
}
