package io.deloop.tools.reference.replay;

import com.squareup.javapoet.*;
import io.deloop.tools.reference.replay.helpers.MethodIdGenerator;
import io.deloop.tools.references.replay.internal.BaseReplayReferenceImpl;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.deloop.tools.references.replay.ReplayReferenceFactory.GENERATED_CLASS_SUFFIX;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.util.ElementFilter.methodsIn;

class TypeGenerator {

    private final TypeElement input; // the annotated interface
    private final Filer filer;
    private final Types types;
    private final Elements elements;
    private final MethodIdGenerator methodIdGenerator;

    TypeGenerator(Element input, Elements elements, Types types,
                  Filer filer, MethodIdGenerator methodIdGenerator) {
        this.input = (TypeElement) input;
        this.filer = filer;
        this.elements = elements;
        this.types = types;
        this.methodIdGenerator = methodIdGenerator;
    }

    void generateFile() {
        String newClassName = input.getSimpleName() + GENERATED_CLASS_SUFFIX;

        TypeName inputTypeName = TypeName.get(input.asType());
        ClassName parentClassName = ClassName.get(BaseReplayReferenceImpl.class);

        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(newClassName)
                .addModifiers(PUBLIC, FINAL)
                .addSuperinterface(inputTypeName)
                .superclass(ParameterizedTypeName.get(parentClassName, inputTypeName));

        for (ExecutableElement method : allMethodsToBeImplemented()) {
            typeSpec.addMethod(methodSpecFor(method));
        }

        JavaFile javaFile = JavaFile
                .builder(generatedPackage(), typeSpec.build())
                .build();

        writeFile(javaFile);
    }

    private String generatedPackage() {
        return elements
                .getPackageOf(input)
                .getQualifiedName()
                .toString();
    }

    private List<ExecutableElement> allMethodsToBeImplemented() {
        List<Element> enclosedElements = new ArrayList<>();
        addEnclosedElementsOf(input, enclosedElements);
        return methodsIn(enclosedElements);
    }

    private void addEnclosedElementsOf(Element el, List<Element> to) {
        to.addAll(el.getEnclosedElements());

        // add methods of extended interfaces too
        for (TypeMirror superType : types.directSupertypes(el.asType())) {
            Element superTypeElement = types.asElement(superType);
            if (isInterface(superTypeElement)) {
                addEnclosedElementsOf(superTypeElement, to);
            }
        }
    }

    private boolean isInterface(Element el) {
        return el.getKind().equals(INTERFACE);
    }

    private MethodSpec methodSpecFor(ExecutableElement method) {
        return new MethodGenerator(input, method, methodIdGenerator)
                .generate();
    }

    private void writeFile(JavaFile javaFile) {
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
