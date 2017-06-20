package io.deloop.tools.proxy.specs

import com.google.auto.common.MoreElements.isAnnotationPresent
import com.squareup.javapoet.*
import com.squareup.javapoet.MethodSpec.Builder
import io.deloop.tools.proxy.ReplayAlways
import io.deloop.tools.proxy.helpers.MethodIdGenerator
import io.deloop.tools.proxy.internal.Invocation
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.TypeElement

internal class MethodSpecGenerator(inputInterface: TypeElement,
                                   private val method: ExecutableElement,
                                   private val idGenerator: MethodIdGenerator) {

    private val inputInterface = ClassName.get(inputInterface)
    private val parameters = findListOfParameters(method)
    private val replayAlways = isAnnotationPresent(method, ReplayAlways::class.java)

    fun createSpec(): MethodSpec = forwardToPaentSpec()
                .apply { addRecordBlock(this) }
                .build()

    private fun forwardToPaentSpec(): Builder {
        val returnIfForwarded = !replayAlways

        return MethodSpec
                .methodBuilder(method.simpleName.toString())
                .addAnnotation(Override::class.java)
                .addModifiers(PUBLIC)
                .returns(TypeName.VOID)
                .addParameters(parameters)
                .addStatement("\$T target = targetRef.get()", inputInterface)
                .beginControlFlow("if (target != null)")
                .addStatement("target.\$L(\$L)", method.simpleName, argumentsList())
                .addCode(if (returnIfForwarded) "return;\n" else "")
                .endControlFlow()
    }

    private fun addRecordBlock(methodSpec: Builder) {
        val invocationName = ClassName.get(Invocation::class.java)
        val invocation = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(invocationName, inputInterface))
                .addMethod(MethodSpec.methodBuilder("replayOn")
                        .addAnnotation(Override::class.java)
                        .addModifiers(PUBLIC)
                        .addParameter(inputInterface, "futureTarget")
                        .addStatement("futureTarget.\$L(\$L)", method.simpleName, argumentsList())
                        .build())
                .build()

        with(methodSpec) {
            addCode("\n")

            if (replayAlways) {
                addStatement("recordForAlways(\$L)", invocation)
            } else {
                addStatement("final String methodId = \$S", uniqueMethodId())
                addStatement("recordForOnce(methodId, \$L)", invocation)
            }
        }
    }

    private fun uniqueMethodId(): String {
        val methodName = method.simpleName.toString()
        return idGenerator.uniqueId(methodName)
    }

    private fun argumentsList() = parameters.map { it.name }.joinToString()

    private fun findListOfParameters(method: ExecutableElement) = method.parameters
            .map {
                val paramType = TypeName.get(it.asType())
                val paramName = it.simpleName.toString()
                ParameterSpec.builder(paramType, paramName, FINAL).build()
            }.toList()
}
