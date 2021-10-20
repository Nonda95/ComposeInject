package pl.osmalek.bartek.composeinject

import com.google.auto.service.AutoService
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class ComposeInjectProcessor : AbstractProcessor() {
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()
    override fun getSupportedAnnotationTypes() = setOf(
        ComposeInject::class.java.canonicalName,
        ComposeInjectModule::class.java.canonicalName,
    )

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val injectedTypes = roundEnv.getElementsAnnotatedWith(ComposeInject::class.java)
            .map { element ->
                if (element.kind != ElementKind.CLASS) {
                    error("Can only be applied to class", element)
                    return false
                }
                if (
                    element.enclosedElements.none {
                        it.kind == ElementKind.CONSTRUCTOR &&
                                it.getAnnotation(Inject::class.java) != null
                    }
                ) {
                    error("Can only be applied to class with @Inject constructor", element)
                    return false
                }
                element as TypeElement
            }
        roundEnv.getElementsAnnotatedWith(ComposeInjectModule::class.java)
            .forEach { element ->
                if (element.kind != ElementKind.CLASS) {
                    error("Can only be applied to class", element)
                    return false
                }
//                if ((element as TypeElement).(Module::class.java)) {
//                    error("Can only be applied to Dagger Module", element)
//                    return false
//                }
                val moduleClassName = ClassName.get(element as TypeElement)
                val fileName =
                    "ComposeInject_${moduleClassName.simpleNames().joinToString(separator = ".")}"
                val composeModuleClassName = ClassName.get(moduleClassName.packageName(), fileName)

                val type = TypeSpec.classBuilder(composeModuleClassName)
                    .addAnnotation(Module::class.java)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addMethod(
                        MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build()
                    )
                    .addMethods(
                        injectedTypes.map {
                            val typeClassName = ClassName.get(it)
                            MethodSpec.methodBuilder(
                                "bind_${
                                    Regex("\\.").replace(
                                        it.qualifiedName,
                                        "_"
                                    )
                                }"
                            )
                                .addAnnotation(Binds::class.java)
                                .addAnnotation(IntoMap::class.java)
                                .addAnnotation(ComposeInjectMap::class.java)
                                .addAnnotation(
                                    AnnotationSpec.builder(ClassKey::class.java)
                                        .addMember("value", "\$T.class", typeClassName)
                                        .build()
                                )
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .addParameter(typeClassName, "element")
                                .returns(Any::class.java)
                                .build()
                        }
                    )
                    .build()
                val fileSpec = JavaFile.builder(moduleClassName.packageName(), type)
                    .build()
                fileSpec.writeTo(processingEnv.filer)
            }
        return true
    }

    private fun error(message: String, element: Element?) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.ERROR,
            message,
            element
        )
    }
}
