package io.deloop.tools.proxy;

import com.google.auto.service.AutoService;
import io.deloop.tools.proxy.helpers.MethodIdGenerator;
import io.deloop.tools.proxy.specs.FileGenerator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonList;

@AutoService(Processor.class)
public class ReplayProxyProcessor extends AbstractProcessor {

    public static ReplayProxyProcessor forTests(MethodIdGenerator methodIdGenerator) {
        return new ReplayProxyProcessor(methodIdGenerator);
    }

    private ProcessingEnvironment processingEnv;
    private final MethodIdGenerator idGenerator;

    public ReplayProxyProcessor() {
        this(MethodIdGenerator.Companion.getReal());
    }

    private ReplayProxyProcessor(MethodIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        processingEnv = env;
    }

    @Override public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        FileGenerator fileGenerator = new FileGenerator(processingEnv, idGenerator);

        for (Element element : env.getElementsAnnotatedWith(CreateReplayProxy.class)) {
            fileGenerator.createFor(element);
        }
        return true;
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(singletonList(CreateReplayProxy.class.getCanonicalName()));
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
