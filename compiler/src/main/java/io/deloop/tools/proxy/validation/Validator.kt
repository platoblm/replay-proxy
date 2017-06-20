package io.deloop.tools.proxy.validation

import com.google.auto.common.MoreElements.asExecutable
import com.google.auto.common.MoreElements.isAnnotationPresent
import com.google.auto.common.SuperficialValidation
import io.deloop.tools.proxy.CreateReplayProxy
import io.deloop.tools.proxy.ReplayAlways
import java.lang.Exception
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.INTERFACE
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.type.TypeKind.VOID
import javax.tools.Diagnostic.Kind

internal class Validator(private val env: ProcessingEnvironment,
                         private val element: Element) {

    private val messager = env.messager

    fun isValid() = try {
        checkIfCompiles()
        checkIfInterface()
        checkIfItHasOnlyVoidMethods()
        allowMaxOneReplayAlwaysMethod()

        true
    } catch (ignore: InvalidUsageException) {
        false
    }

    private fun checkIfCompiles() {
        if (!SuperficialValidation.validateElement(element)) {
            warningCompilationErrors(element)
            fail()
        }
    }

    private fun checkIfInterface() {
        if (element.kind != INTERFACE) {
            val annotation = CreateReplayProxy::class.java.simpleName
            error(element, "$annotation can only be applied to interfaces")
            fail()
        }
    }

    private fun checkIfItHasOnlyVoidMethods() {
        enclosedElements()
                .filter { isMethod(it) }
                .filter { !methodReturnsVoid(it) }
                .forEach {
                    error(element, "Has non-void method ${it.simpleName}")
                    fail()
                }
    }

    private fun allowMaxOneReplayAlwaysMethod() {
        enclosedElements()
                .filter { isMethod(it) }
                .filter { isAnnotationPresent(it, ReplayAlways::class.java) }
                .count()
                .apply {
                    if (this > 1) {
                        errorTooManyReplayAlwaysMethods()
                        fail()
                    }
                }
    }

    private fun errorTooManyReplayAlwaysMethods() {
        error(element, "Interface has too many methods annotated with" +
                ReplayAlways::class.java.simpleName + ". Max is 1.")
    }

    private fun error(e: Element, message: String) {
        messager.printMessage(Kind.ERROR, message, e)
    }

    private fun isMethod(e: Element) = e.kind == METHOD

    private fun enclosedElements(): List<Element> {
        val types = env.typeUtils
        val result = ArrayList<Element>()
        result.addAll(element.enclosedElements)

        // add methods of extended interfaces too
        for (tm in types.directSupertypes(element.asType())) {
            val el = types.asElement(tm)
            if (el.kind == INTERFACE) {
                result.addAll(el.enclosedElements)
            }
        }

        return result
    }

    private fun methodReturnsVoid(e: Element) = asExecutable(e).returnType.kind == VOID

    private fun warningCompilationErrors(element: Element) {
        messager.printMessage(Kind.WARNING, "Had compilation errors", element)
    }

    private fun fail(): Nothing = throw InvalidUsageException()

    private class InvalidUsageException : Exception()
}
