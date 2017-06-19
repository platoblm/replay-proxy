package io.deloop.tools.reference.replay

import com.google.auto.common.MoreElements.asExecutable
import com.google.auto.common.MoreElements.isAnnotationPresent
import com.google.auto.common.SuperficialValidation
import io.deloop.tools.references.replay.ReplayAlways
import io.deloop.tools.references.replay.ReplayReference
import java.lang.Exception
import java.util.*
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ElementKind.INTERFACE
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.type.TypeKind.VOID
import javax.lang.model.util.Types
import javax.tools.Diagnostic.Kind

internal class Validator(private val element: Element, private val types: Types, private val messager: Messager) {

    fun validate() = try {
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
            warningCompilationErrors(element, messager)
            fail()
        }
    }

    private fun checkIfInterface() {
        if (element.kind != INTERFACE) {
            val annotation = ReplayReference::class.java.simpleName
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
        val result = ArrayList<Element>()
        result.addAll(element.enclosedElements)

        // add methods of extended interfaces too
        for (tm in types.directSupertypes(element.asType())) {
            val el = types.asElement(tm)
            if (el.kind == ElementKind.INTERFACE) {
                result.addAll(el.enclosedElements)
            }
        }

        return result
    }

    private fun methodReturnsVoid(e: Element) = asExecutable(e).returnType.kind == VOID

    private fun warningCompilationErrors(element: Element, messager: Messager) {
        messager.printMessage(Kind.WARNING, "Had compilation errors", element)
    }

    private fun fail(): Nothing = throw InvalidUsageException()

    private class InvalidUsageException : Exception()
}
