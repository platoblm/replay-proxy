package io.deloop.tools.reference.replay;

import com.squareup.javapoet.*;
import com.squareup.javapoet.MethodSpec.Builder;
import io.deloop.tools.reference.replay.helpers.MethodIdGenerator;
import io.deloop.tools.references.replay.ReplayAlways;
import io.deloop.tools.references.replay.internal.Invocation;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

import static com.google.auto.common.MoreElements.isAnnotationPresent;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

class MethodGenerator {
    private final ClassName inputInterface;
    private final ExecutableElement method;
    private final MethodIdGenerator methodIdGenerator;
    private final List<ParameterSpec> parameters;

    MethodGenerator(TypeElement inputInterface,
                    ExecutableElement method,
                    MethodIdGenerator methodIdGenerator) {
        this.inputInterface = ClassName.get(inputInterface);
        this.method = method;
        this.parameters = findListOfParameters(method);
        this.methodIdGenerator = methodIdGenerator;
    }

    MethodSpec generate() {
        boolean replayAlways = isAnnotationPresent(method, ReplayAlways.class);
        boolean ifCallForwardedDontRecord = !replayAlways;

        MethodSpec.Builder spec = ifTargetPresentForwardAndReturn(ifCallForwardedDontRecord);

        addRecordBlock(spec, replayAlways);

        return spec.build();
    }

    private MethodSpec.Builder ifTargetPresentForwardAndReturn(boolean returnIfCallForwarded) {
        String comment = "If target is present, forward" +
                (returnIfCallForwarded ? " and return" : "");

        return MethodSpec
                .methodBuilder(method.getSimpleName().toString())
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(TypeName.VOID)
                .addParameters(parameters)
                .addComment(comment)
                .addStatement("$T target = targetRef.get()", inputInterface)
                .beginControlFlow("if (target != null)")
                .addStatement("target.$L($L)", method.getSimpleName(), argumentsList())
                .addCode(returnIfCallForwarded ? "return;\n" : "")
                .endControlFlow();
    }

    private void addRecordBlock(Builder methodSpec, boolean replayAlways) {
        ClassName invocationName = ClassName.get(Invocation.class);
        TypeSpec invocation = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(invocationName, inputInterface))
                .addMethod(MethodSpec.methodBuilder("replayOn")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(inputInterface, "futureTarget")
                        .addStatement("futureTarget.$L($L)", method.getSimpleName(), argumentsList())
                        .build())
                .build();

        methodSpec.addCode("\n"); // empty line

        if (replayAlways) {
            methodSpec
                    .addComment("Record invocation - it will always be replayed when a target is set.")
                    .addStatement("recordForAlways($L)", invocation);
        } else {
            methodSpec
                    .addComment("Record invocation to be replayed later, and override previous calls of the same method")
                    .addStatement("final String methodId = $S", uniqueMethodId())
                    .addStatement("recordForOnce(methodId, $L)", invocation);
        }
    }

    private String uniqueMethodId() {
        String methodName = method.getSimpleName().toString();
        return methodIdGenerator.uniqueId(methodName);
    }

    private String argumentsList() {
        StringBuilder args = new StringBuilder();

        for (ParameterSpec parameter : parameters) {
            if (args.length() > 0) {
                args.append(", ");
            }
            args.append(parameter.name);
        }
        return args.toString();
    }

    private static List<ParameterSpec> findListOfParameters(ExecutableElement method) {
        List<ParameterSpec> result = new ArrayList<>();

        for (VariableElement parameter : method.getParameters()) {
            TypeName paramType = TypeName.get(parameter.asType());
            String paramName = parameter.getSimpleName().toString();

            result.add(ParameterSpec.builder(paramType, paramName, FINAL).build());
        }

        return result;
    }
}
