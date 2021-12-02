package pl.osmalek.bartek.composeinject

import com.google.auto.service.AutoService
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.lang.model.AnnotatedConstruct
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class ComposeInjectProcessor : AbstractProcessor() {
  private var userModuleName: ClassName? = null

  override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()
  override fun getSupportedAnnotationTypes() = setOf(
    ComposeInject::class.java.canonicalName,
    ComposeInjectModule::class.java.canonicalName,
  )

  override fun process(
    annotations: MutableSet<out TypeElement>?,
    roundEnv: RoundEnvironment
  ): Boolean {
    val injectedTypes = getInjectedTypes(roundEnv) ?: return false
    val moduleClassName = getModuleClassName(roundEnv) ?: return false
    userModuleName = moduleClassName
    val fileName = moduleClassName.toComposeInjectModuleName()
    val composeModuleClassName = ClassName.get(moduleClassName.packageName(), fileName)

    val type = buildModule(composeModuleClassName, injectedTypes)
    val fileSpec = JavaFile.builder(moduleClassName.packageName(), type)
      .build()
    fileSpec.writeTo(processingEnv.filer)

    if (roundEnv.processingOver() && userModuleName != null) {
      val userModuleName = userModuleName!!
      val composeModuleClassName = ClassName.get(
        userModuleName.packageName(),
        userModuleName.toComposeInjectModuleName()
      )
      val userModule =
        processingEnv.elementUtils.getTypeElement(userModuleName.canonicalName())
      val annotationValue = userModule.getAnnotation("dagger.Module")!!
        .elementValues
        .entries
        .firstOrNull { it.key.simpleName.contentEquals("includes") }
        ?.value
        ?.toMirrorValue() as? MirrorValue.Array
      val referencesGeneratedModule = annotationValue?.filterIsInstance<MirrorValue.Type>()
        ?.map { TypeName.get(it) }
        ?.any { it == composeModuleClassName } ?: false
      if (!referencesGeneratedModule) {
        error(
          "@ComposeInjectModule's @Module must include ${composeModuleClassName.simpleName()}",
          userModule
        )
        return false
      }
    }
    return true
  }

  private fun buildModule(
    composeModuleClassName: ClassName?,
    injectedTypes: List<TypeElement>
  ) = TypeSpec.classBuilder(composeModuleClassName)
    .addAnnotation(Module::class.java)
    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
    .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
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

  private fun getModuleClassName(roundEnv: RoundEnvironment): ClassName? {
    val modules = roundEnv.getElementsAnnotatedWith(ComposeInjectModule::class.java)
    if (modules.size > 1) {
      error("@ComposeInjectModule can only be applied to single Module", null)
      return null
    }
    val module = modules.firstOrNull() ?: return null
    if (module.kind != ElementKind.CLASS) {
      error("Can only be applied to class", module)
      return null
    }
    if (module.getAnnotation("dagger.Module") == null) {
      error("Can only be applied to Dagger Module", module)
      return null
    }
    return ClassName.get(module as TypeElement)
  }

  private fun getInjectedTypes(roundEnv: RoundEnvironment): List<TypeElement>? {
    return roundEnv.getElementsAnnotatedWith(ComposeInject::class.java)
      .map { element ->
        if (element.kind != ElementKind.CLASS) {
          error("Can only be applied to class", element)
          return null
        }
        if (
          element.enclosedElements.none {
            it.kind == ElementKind.CONSTRUCTOR &&
                it.getAnnotation(Inject::class.java) != null
          }
        ) {
          error("Can only be applied to class with @Inject constructor", element)
          return null
        }
        element as TypeElement
      }
  }

  private fun ClassName.toComposeInjectModuleName() =
    "ComposeInject_${simpleNames().joinToString(separator = ".")}"

  private fun error(message: String, element: Element?) {
    processingEnv.messager.printMessage(
      Diagnostic.Kind.ERROR,
      message,
      element
    )
  }
}

private fun AnnotatedConstruct.getAnnotation(qualifiedName: String): AnnotationMirror? {
  return annotationMirrors.firstOrNull {
    (it.annotationType.asElement() as? TypeElement)?.qualifiedName.contentEquals(qualifiedName)
  }
}
