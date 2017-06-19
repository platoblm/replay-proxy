package io.deloop.tools.proxy;

import com.google.auto.service.AutoService;
import io.deloop.tools.proxy.helpers.MethodIdGenerator;
import io.deloop.tools.proxy.specs.TypeSpecGenerator;
import io.deloop.tools.proxy.validation.Validator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;

@AutoService(Processor.class)
public class ReplayProxyProcessor extends AbstractProcessor {

    public static ReplayProxyProcessor forTests(MethodIdGenerator methodIdGenerator) {
        return new ReplayProxyProcessor(methodIdGenerator);
    }

    private Types types;
    private Elements elements;
    private Filer filer;
    private Messager messager;
    private final MethodIdGenerator idGenerator;

    public ReplayProxyProcessor() {
        this(MethodIdGenerator.Companion.getReal());
    }

    private ReplayProxyProcessor(MethodIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        types = env.getTypeUtils();
        elements = env.getElementUtils();
        filer = env.getFiler();
        messager = env.getMessager();
    }

    @Override public boolean process(Set<? extends TypeElement> set, RoundEnvironment environment) {
        for (Element el : findRelevantInterfaces(environment)) {
            new TypeSpecGenerator(el, elements, types, filer, idGenerator)
                    .generateFile();
        }
        return true;
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(singletonList(HasReplayProxy.class.getCanonicalName()));
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private List<Element> findRelevantInterfaces(RoundEnvironment env) {
        List<Element> result = new ArrayList<>();

        for (Element element : env.getElementsAnnotatedWith(HasReplayProxy.class)) {
            if (new Validator(element, types, messager).validate()) {
                result.add(element);
            }
        }
        return result;
    }
}
