package pl.osmalek.bartek.composeinject

import javax.lang.model.element.AnnotationValue
import javax.lang.model.type.ErrorType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleAnnotationValueVisitor8
import javax.lang.model.util.SimpleTypeVisitor8

fun AnnotationValue.toMirrorValue(): MirrorValue = accept(MirrorValueVisitor, null)

sealed class MirrorValue {
  data class Type(private val value: TypeMirror) : MirrorValue(), TypeMirror by value
  data class Array(private val values: List<MirrorValue>) : MirrorValue(),
    List<MirrorValue> by values

  object Unmapped : MirrorValue()
  object Error : MirrorValue()
}

private object MirrorValueVisitor : SimpleAnnotationValueVisitor8<MirrorValue, Nothing?>() {
  override fun defaultAction(o: Any, ignored: Nothing?) = MirrorValue.Unmapped

  override fun visitType(mirror: TypeMirror, ignored: Nothing?) = mirror.accept(TypeVisitor, null)

  override fun visitArray(values: List<AnnotationValue>, ignored: Nothing?) =
    MirrorValue.Array(values.map { it.accept(this, null) })
}

private object TypeVisitor : SimpleTypeVisitor8<MirrorValue, Nothing?>() {
  override fun visitError(type: ErrorType, ignored: Nothing?) = MirrorValue.Error
  override fun defaultAction(type: TypeMirror, ignored: Nothing?) = MirrorValue.Type(type)
}
