package io.deloop.tools.reference.replay;

import com.google.auto.common.SuperficialValidation;
import io.deloop.tools.references.replay.ReplayAlways;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import java.util.ArrayList;
import java.util.List;

import static com.google.auto.common.MoreElements.asExecutable;
import static com.google.auto.common.MoreElements.isAnnotationPresent;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.type.TypeKind.VOID;

class Validator {

    private final Element element;
    private final Types types;
    private final Messager messager;

    Validator(Element element, Types types, Messager messager) {
        this.element = element;
        this.types = types;
        this.messager = messager;
    }

    boolean validate() {
        return validateCompilable() &&
               validateInterface() &&
               validateOnlyVoidMethods() &&
               validateMaxOneReplayAlwaysMethod();
    }

    private boolean validateCompilable() {
        // compilation error in source code
        if (!SuperficialValidation.validateElement(element)) {
            warningCompilationErrors(element, messager);
            return false;
        }
        return true;
    }

    private boolean validateInterface() {
        if (!element.getKind().equals(INTERFACE)) {
            errorNotAnInterface();
            return false;
        }
        return true;
    }

    private boolean validateOnlyVoidMethods() {
        for (Element el : enclosedElements()) {
            if (isMethod(el) && !methodReturnsVoid(el)) {
                errorNoVoidMethod(el);
                return false;
            }
        }
        return true;
    }

    private boolean validateMaxOneReplayAlwaysMethod() {
        int count = 0;

        for (Element e : enclosedElements()) {
            if (isMethod(e) && isAnnotationPresent(e, ReplayAlways.class)) {
                count++;
            }
            if (count > 1) {
                errorTooManyReplayAlwaysMethods();
                return false;
            }
        }
        return true;
    }

    private void errorNotAnInterface() {
        String annotationName = ReplayAlways.class.getSimpleName();
        error(element, "Is not an interface. " + annotationName + " can only be applied to interfaces");
    }

    private void errorNoVoidMethod(Element method) {
        error(element, "Has non-void method " + method.getSimpleName());
    }

    private void errorTooManyReplayAlwaysMethods() {
        error(element, "Interface has too many methods annotated with" +
                ReplayAlways.class.getSimpleName() + ". Max is 1.");
    }

    private void error(Element e, String message) {
        messager.printMessage(Kind.ERROR, message, e);
    }

    private boolean isMethod(Element e) {
        return e.getKind().equals(ElementKind.METHOD);
    }

    private List<? extends Element> enclosedElements() {
        List<Element> result = new ArrayList<>();
        result.addAll(element.getEnclosedElements());

        // add methods of extended interfaces too
        for(TypeMirror tm : types.directSupertypes(element.asType())) {
            Element el = types.asElement(tm);
            if (el.getKind().equals(ElementKind.INTERFACE)) {
                result.addAll(el.getEnclosedElements());
            }
        }

        return result;
    }

    private boolean methodReturnsVoid(Element e) {
        return asExecutable(e).getReturnType().getKind().equals(VOID);
    }

    private static void warningCompilationErrors(Element element, Messager messager) {
        messager.printMessage(Kind.WARNING, "Had compilation errors", element);
    }
}
